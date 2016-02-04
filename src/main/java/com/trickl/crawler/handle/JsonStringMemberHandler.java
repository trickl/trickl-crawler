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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trickl.crawler.api.Task;
import java.io.IOException;
import org.apache.droids.exception.DroidsException;

public class JsonStringMemberHandler<T extends Task> implements TaskResultHandler<T, JsonNode>
{
   private String propertyName = "result";
   
   private TaskResultHandler<T, String> outputHandler;

   public JsonStringMemberHandler()
   {
   }

   @SuppressWarnings("unchecked")
   @Override
   public void handle(T task, JsonNode node) throws DroidsException, IOException
   {
      if (task == null || node == null) throw new NullPointerException();

      if (outputHandler != null)
      {     
         
         ObjectMapper mapper = new ObjectMapper();         
         JsonNode property = node.path(propertyName);
         if (property.isMissingNode()) {
            throw new DroidsException("JSON does not contain a value for member '" + propertyName + "'");
         }
         if (property.isTextual()) {
            outputHandler.handle(task, property.textValue());
         }
         else {
            throw new DroidsException("JSON member '" + propertyName + "' is not a textual value.");
         }
      }
   }

   public void setOutputHandler(TaskResultHandler<T, String> outputHandler)
   {
     this.outputHandler = outputHandler;
   }

   /**
    * @return the propertyName
    */
   public String getPropertyName() {
      return propertyName;
   }

   /**
    * @param propertyName the propertyName to set
    */
   public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
   }
}
