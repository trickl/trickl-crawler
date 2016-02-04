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

public class JsonToBeanHandler<T extends Task, BeanType> implements TaskResultHandler<T, JsonNode>
{
   private TaskResultHandler<T, BeanType> outputHandler;
   
   private Class<T> beanTypeClass;

   public JsonToBeanHandler()
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
         BeanType bean = (BeanType) mapper.treeToValue(node, beanTypeClass);

         outputHandler.handle(task, bean);
      }
   }

   public void setOutputHandler(TaskResultHandler<T, BeanType> outputHandler)
   {
     this.outputHandler = outputHandler;
   }

   /**
    * @return the beanTypeClass
    */
   public Class<T> getBeanTypeClass() {
      return beanTypeClass;
   }

   /**
    * @param beanTypeClass the beanTypeClass to set
    */
   public void setBeanTypeClass(Class<T> beanTypeClass) {
      this.beanTypeClass = beanTypeClass;
   }
}
