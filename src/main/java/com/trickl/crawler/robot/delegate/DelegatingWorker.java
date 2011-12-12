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
package com.trickl.crawler.robot.delegate;

import com.trickl.crawler.api.Droid;
import com.trickl.crawler.api.Task;
import com.trickl.crawler.api.Worker;
import java.io.IOException;
import org.apache.droids.exception.DroidsException;

public class DelegatingWorker<T extends Task> implements Worker<T> {

   private DelegatingDroid<T> droid;

   public DelegatingWorker(DelegatingDroid<T> droid) {
      this.droid = droid;
   }

   @SuppressWarnings("unchecked")
   @Override
   public void execute(final T task) throws DroidsException, IOException {
      Droid delegate = droid.getDroidForTask(task);
      if (delegate != null) {
         Worker worker = delegate.getNewWorker();
         worker.execute(task);
      }
   }
}
