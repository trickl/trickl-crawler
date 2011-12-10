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
package com.trickl.crawler.api;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public interface TaskMaster<T extends Task> {

   public enum ExecutionState {

      INITALIZED,
      RUNNING,
      SHUTTING_DOWN,
      COMPLETE
   };

   void start(final Queue<T> queue, final Droid<T> droid);

   boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

   public boolean isTerminated();

   public boolean isShutdown();

   public void shutdown();

   Date getStartTime();

   Date getFinishedWorking();

   long getCompletedTasks();

   T getLastCompletedTask();
}
