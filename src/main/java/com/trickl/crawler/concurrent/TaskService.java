/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trickl.crawler.concurrent;

import com.trickl.crawler.api.*;
import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.droids.api.DelayTimer;
import org.apache.droids.api.TaskExceptionHandler;
import org.apache.droids.api.TaskExceptionResult;
import org.apache.droids.exception.DroidsException;

public class TaskService<T extends Task> implements TaskMaster<T> {

   private class ServiceThread implements Callable<Integer> {

      final Queue<T> queue;
      final Droid<T> droid;

      public ServiceThread(final Queue<T> queue, final Droid<T> droid) {
         this.queue = queue;
         this.droid = droid;
      }

      @Override
      public Integer call() {
         logger.info("TaskMaster service thread started.");
         try {
            while (state == ExecutionState.RUNNING) {
               logger.finest("Task master checking if work available...");

               if (!queue.isEmpty() && workerQueue.remainingCapacity() > 0) {
                  logger.finest("TaskMaster running task.");

                  T task = queue.poll();

                  long delay = (delayTimer != null) ? delayTimer.getDelayMillis() : 0;
                  if (delay > 0) {
                     logger.log(Level.INFO, "Task given delay of {0}ms.", delay);
                  }

                  Callable<Boolean> workerThread = new WorkerThread(task, droid.getNewWorker(), delay);
                  if (task instanceof TimedTask) {
                     TimedTask timedTask = (TimedTask) task;
                     workerThread = new TimeoutCallable<Boolean>(workerThread,
                             timedTask.getTimeOut(),
                             timedTask.getTimeOutUnit(), executorService);
                  }

                  workerQueue.add(completionService.submit(workerThread));
               } else if (!queue.isEmpty() && workerQueue.remainingCapacity() == 0) {
                  // Wait until a worker has completed
                  logger.fine("Task master running with maximum workers, waiting for task to finish...");
                  waitForTask();
               } else if (queue.isEmpty() && !workerQueue.isEmpty()) {
                  logger.fine("Task master has no more work, waiting for task to finish...");
                  waitForTask();
               } else {
                  logger.info("Task master is idle, shutting down.");
                  shutdown();
               }
            }
         } catch (InterruptedException e) {
            logger.log(Level.WARNING, "TaskMaster service interrupted while waiting.", e);
         } finally {
            finishedWorking = new Date();
            state = ExecutionState.COMPLETE;
            logger.info("TaskMaster service shutdown.");
         }

         return completedCount.intValue();
      }
   }

   private class WorkerThread implements Callable<Boolean> {

      private T task;
      private Worker<T> worker;
      private long delay;

      public WorkerThread(T task, Worker<T> worker, long delay) {
         this.task = task;
         this.worker = worker;
         this.delay = delay;
      }

      @Override
      public Boolean call() {
         boolean success = true;

         try {
            Exception ex = null;
            try {
               if (monitor != null) {
                  monitor.beforeExecute(task, worker);
               }
               if (!task.isAborted()) {
                  if (delay > 0) {
                     Thread.sleep(delay);
                  }
                  worker.execute(task);
               }
               lastCompletedTask = task;
            } catch (DroidsException e) {
               ex = e;
            } catch (IOException e) {
               ex = e;
            } catch (Throwable e) {
               ex = new Exception("Unexpected error while executing droid worker.", e);
            } finally {
               if (ex != null) {
                  try {
                     TaskExceptionResult result = TaskExceptionResult.WARN;
                     if (taskExceptionHandler != null) {
                        result = taskExceptionHandler.handleException(ex);
                     }
                     switch (result) {
                        case WARN:
                           if (logger.isLoggable(Level.FINER)) {
                              // Only show stacktrace for warnings when debugging
                              logger.log(Level.WARNING, ex.getMessage(), ex);
                           }
                           else {
                              logger.log(Level.WARNING, ex.getMessage());    
                           }
                           
                           break;
                        case FATAL:
                           logger.log(Level.SEVERE, ex.getMessage(), ex);
                           success = false;
                           break;
                        case IGNORE:
                           break; // nothing
                     }
                  } catch (Exception e2) {
                     logger.log(Level.SEVERE, e2.getMessage(), e2);
                  }
               }

               if (monitor != null) {
                  monitor.afterExecute(task, worker, ex);
               }
            }

         } catch (Throwable e) {
            logger.log(Level.SEVERE, "Unexpected error in task manager.", e);
            success = false;
         }

         return success;
      }
   };
   private static final Logger logger = Logger.getLogger(TaskService.class.getCanonicalName());
   private ExecutorService executorService;
   private CompletionService<Boolean> completionService;
   private WorkMonitor<T> monitor = null;
   private Date startedWorking = null;
   private Date finishedWorking = null;
   private T lastCompletedTask = null;
   private volatile ExecutionState state = ExecutionState.INITALIZED;
   private AtomicLong completedCount = new AtomicLong();
   private Future<?> serviceThread;
   private LinkedBlockingQueue<Future<Boolean>> workerQueue = new LinkedBlockingQueue<Future<Boolean>>(4);
   private TaskExceptionHandler taskExceptionHandler;
   private DelayTimer delayTimer;

   public TaskService(final ExecutorService executorService) {
      if (executorService == null) {
         throw new NullPointerException();
      }
      this.executorService = executorService;

      // Dummy thread
      this.serviceThread = executorService.submit(new Runnable() {

         @Override
         public void run() {
         }
      });
   }

   @Override
   public synchronized void start(final Queue<T> queue, final Droid<T> droid) {
      if (this.state == ExecutionState.RUNNING) {
         logger.info("TaskMaster service still running.");
         return;
      }

      this.completionService = new ExecutorCompletionService<Boolean>(executorService);
      this.startedWorking = new Date();
      this.finishedWorking = null;
      this.state = ExecutionState.RUNNING;

      // process tasks in a new thread
      logger.info("Starting TaskMaster main thread.");
      serviceThread = executorService.submit(new ServiceThread(queue, droid));
   }

   private void waitForTask() throws InterruptedException {
      logger.finest("Waiting for task...");
      Future<Boolean> task = completionService.take();
      if (task.isDone()) {
         try {
            // Task failed, this should only happen for severe errors
            if (!task.get()) {
               shutdown();
            }

            completedCount.incrementAndGet();
         } catch (RejectedExecutionException e) {
            logger.warning(e.getMessage());
            logger.warning("Task was rejected, waiting for 10 seconds before trying again.");
            Thread.sleep(10000);
         } catch (ExecutionException e) {
            if (e.getCause() instanceof TimeoutException) {
               logger.warning(e.getMessage());
            } else {
               logger.log(Level.SEVERE, "An unexpected exception during task execution", e);
               shutdown();
            }
         }

         logger.finest("Task completed.");
      }

      workerQueue.remove(task);
   }

   public ExecutionState getExecutionState() {
      return state;
   }

   @Override
   public long getCompletedTasks() {
      return completedCount.get();
   }

   @Override
   public Date getFinishedWorking() {
      return finishedWorking;
   }

   @Override
   public T getLastCompletedTask() {
      return lastCompletedTask;
   }

   @Override
   public Date getStartTime() {
      return startedWorking;
   }

   public WorkMonitor<T> getMonitor() {
      return monitor;
   }

   public final void setExceptionHandler(TaskExceptionHandler taskExceptionHandler) {
      this.taskExceptionHandler = taskExceptionHandler;
   }

   public void setMonitor(WorkMonitor<T> monitor) {
      this.monitor = monitor;
   }

   public void setMaxWorkers(int maxWorkers) {
      LinkedBlockingQueue<Future<Boolean>> newQueue = new LinkedBlockingQueue<Future<Boolean>>(maxWorkers);
      workerQueue.drainTo(newQueue); // This will block if no space
      workerQueue = newQueue;
   }

   @Override
   public void shutdown() {
      // Stop anymore tasks being processed
      if (this.state == ExecutionState.RUNNING) {
         this.state = ExecutionState.SHUTTING_DOWN;
      }
   }

   @Override
   public synchronized boolean isShutdown() {
      return (state == ExecutionState.SHUTTING_DOWN ||
              state == ExecutionState.COMPLETE);
   }

   @Override
   public synchronized boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      if (state == ExecutionState.COMPLETE) {
         return true;
      }

      long start = System.currentTimeMillis();
      long originalTimeoutMs = TimeUnit.MILLISECONDS.convert(timeout, unit);
      long timeoutMs = originalTimeoutMs;

      for (Future<Boolean> future : workerQueue) {
         logger.log(Level.INFO, "Waiting for termination, current active workers = {0}", workerQueue.size());

         try {
            future.get(timeoutMs, TimeUnit.MILLISECONDS);
         } catch (ExecutionException e) {
            if (e.getCause() instanceof TimeoutException) {
               logger.warning(e.getMessage());
            } else {
               logger.log(Level.SEVERE, "An unexpected exception occured awaiting termination", e);
            }
         } catch (TimeoutException e) {
            future.cancel(true);
         }

         workerQueue.remove(future);
         timeoutMs = originalTimeoutMs - (System.currentTimeMillis() - start);
      }

      try {
         serviceThread.get(timeoutMs, TimeUnit.MILLISECONDS);
      }
      catch (ExecutionException ex) {
         logger.log(Level.SEVERE, "An unexpected exception occured awaiting termination", ex);
      }
      catch (TimeoutException ex) {
         logger.log(Level.SEVERE, "An unexpected exception occured awaiting termination", ex);
      }


      finishedWorking = new Date();
      state = ExecutionState.COMPLETE;

      if (timeoutMs < 0) {
         return false;
      } else {
         return true;
      }
   }

   @Override
   public synchronized boolean isTerminated() {
      return (state == ExecutionState.COMPLETE);
   }

   public DelayTimer getDelayTimer() {
      return delayTimer;
   }

   public void setDelayTimer(DelayTimer delayTimer) {
      this.delayTimer = delayTimer;
   }
}
