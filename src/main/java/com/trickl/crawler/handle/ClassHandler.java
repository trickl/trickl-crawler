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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.droids.exception.DroidsException;

public class ClassHandler<T extends Task, Object> implements TaskResultHandler<T, Object>
{
   private static final Logger logger = Logger.getLogger(ClassHandler.class.getCanonicalName());

   private Map<String, TaskResultHandler> outputHandlerMap;

   public ClassHandler() throws DroidsException
   {
   }

   @SuppressWarnings("unchecked")
   public void handle(T task, Object obj) throws DroidsException, IOException
   {
      if (task == null || obj == null) throw new NullPointerException();

      for (Map.Entry<String, TaskResultHandler> outputHandler : outputHandlerMap.entrySet())
      {
         try {
            Class handlerClass = Class.forName(outputHandler.getKey());
            if (handlerClass.isAssignableFrom(obj.getClass())) {
               outputHandler.getValue().handle(task, obj);
            }
         } catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "Handler class type not found.", ex);
            throw new DroidsException("Droid configuration error", ex);
         }

      }
   }

   public void setOutputHandlerMap(Map<String, TaskResultHandler>  outputHandlerMap)
   {
      this.outputHandlerMap = outputHandlerMap;
   }
}
