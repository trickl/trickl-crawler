/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trickl.crawler.concurrent;

import com.trickl.crawler.api.TimedTask;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.droids.exception.DroidsException;
import org.junit.Assert;
import java.util.LinkedList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ExecutorService;
import com.trickl.crawler.api.Droid;
import com.trickl.crawler.api.Worker;
import java.net.URI;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import org.apache.droids.api.Protocol;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author tgee
 */
public class TaskServiceTest {

   static class CounterTask implements TimedTask {

      private int maxCounter = 10;
      private int delayMs = 100;
      private int timeoutSecs = 30;
      private String id;
      private Date startDate;
      boolean abort = false;

      public CounterTask(String id, int maxCounter, int delayMs, int timeoutSecs) {
         this.id = id;
         this.maxCounter = maxCounter;
         this.delayMs = delayMs;
         this.timeoutSecs = timeoutSecs;
         this.startDate = new Date();
      }

      @Override
      public String getId() {
         return id;
      }
      
      @Override
      public Date getTaskDate() {
         return startDate;
      }

      @Override
      public void abort() {
         abort = true;
      }

      @Override
      public boolean isAborted() {
         return abort;
      }

      @Override
      public int getTimeOut() {
         return timeoutSecs;
      }

      @Override
      public TimeUnit getTimeOutUnit() {
         return TimeUnit.SECONDS;
      }

      public int getDelayMs() {
         return delayMs;
      }

      public int getMaxCounter() {
         return maxCounter;
      }

      @Override
      public URI getURI() {
         return null;
      }

      @Override
      public Protocol getProtocol() {
         return null;
      }
   }

   static class CounterDroid implements Droid<CounterTask> {

      @Override
      public Worker<CounterTask> getNewWorker() {
         return new Worker<CounterTask>() {

            @Override
            public void execute(CounterTask task) throws DroidsException, IOException {
               for (int count = 0; !task.isAborted() && count < task.getMaxCounter(); ++count) {
                  try {
                     Thread.sleep(task.getDelayMs());
                  } catch (InterruptedException ex) {
                     throw new DroidsException(ex);
                  }
                  System.out.println(task.getId() + ", Count:" + (count + 1));
               }
            }
         };
      }

   }

   public TaskServiceTest() {
   }

   @Test
   @Ignore("This test is inconsistent and sometimes fails. TODO: Fix")
   public void testThreeTasksWithinTimeOut() throws InterruptedException {
      int threadPoolSize = 15;
      Queue<CounterTask> queue = new LinkedList<CounterTask>();
      queue.add(new CounterTask("Task One", 5, 250, 15));
      queue.add(new CounterTask("Task Two", 5, 250, 15));
      queue.add(new CounterTask("Task Three", 5, 250, 15));
      Droid<CounterTask> droid = new CounterDroid();

      ExecutorService executorService = new ScheduledThreadPoolExecutor(threadPoolSize);
      TaskService<CounterTask> taskService = new TaskService<CounterTask>(executorService);
      
      taskService.start(queue, droid);
      taskService.setMaxWorkers(4);
      Thread.sleep(1000);
      taskService.awaitTermination(30, TimeUnit.SECONDS);

      Assert.assertEquals(3, taskService.getCompletedTasks());
   }

   @Test
   @Ignore("This test is inconsistent and sometimes fails. TODO: Fix")
   public void testThreeTasksWithOneTimedOut() throws InterruptedException {
      int threadPoolSize = 15;
      Queue<CounterTask> queue = new LinkedList<CounterTask>();
      queue.add(new CounterTask("Task One", 5, 250, 15));
      queue.add(new CounterTask("Task Two", 5, 250, 15));

      // Insufficient time to execute
      queue.add(new CounterTask("Task Three", 30, 250, 2));
      Droid<CounterTask> droid = new CounterDroid();

      ExecutorService executorService = new ScheduledThreadPoolExecutor(threadPoolSize);
      TaskService<CounterTask> taskService = new TaskService<CounterTask>(executorService);

      taskService.start(queue, droid);
      taskService.setMaxWorkers(4);
      Thread.sleep(1000);
      taskService.awaitTermination(30, TimeUnit.SECONDS);

      Assert.assertEquals(2, taskService.getCompletedTasks());
   }

}