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
package com.trickl.crawler.handle;

import com.trickl.crawler.api.Task;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Handler;
import org.apache.droids.exception.DroidsException;

public class LegacyHandler<T extends Task> implements TaskResultHandler<T, ContentEntity>
{
   private Handler outputHandler;

   public LegacyHandler(Handler outputHandler)
   {
      if (outputHandler == null) throw new NullPointerException();
      this.outputHandler = outputHandler;
   }

   @Override
   public void handle(T task, ContentEntity entity) throws DroidsException, IOException
   {
      try {
         outputHandler.handle(new URI(task.getId()), entity);
      } catch (URISyntaxException ex) {
         Logger.getLogger(LegacyHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
