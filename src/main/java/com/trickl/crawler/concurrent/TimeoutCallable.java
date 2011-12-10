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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class TimeoutCallable<T> implements Callable<T> {

   private ExecutorService executorService;
   private Callable<T> task;
   private int timeout;
   private TimeUnit unit;

   public TimeoutCallable(Callable<T> task, int timeout, TimeUnit unit, ExecutorService executorService) {
      this.executorService = executorService;
      this.task = task;
      this.timeout = timeout;
      this.unit = unit;
   }

   @Override
   public T call() throws Exception {
      Future<T> future = executorService.submit(task);
      try {
         return future.get(timeout, unit);
      }
      catch (TimeoutException ex) {
          future.cancel(true);
          throw ex;
      }
   }
}
