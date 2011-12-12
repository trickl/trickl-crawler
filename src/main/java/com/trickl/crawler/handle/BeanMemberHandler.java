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
import org.apache.commons.beanutils.BeanMap;
import org.apache.droids.exception.DroidsException;

public class BeanMemberHandler<T extends Task, BeanType, BeanMemberType> implements TaskResultHandler<T, BeanType>
{
   private String propertyName = "result";
   
   private TaskResultHandler<T, BeanMemberType> outputHandler;

   public BeanMemberHandler()
   {
   }

   @SuppressWarnings("unchecked")
   @Override
   public void handle(T task, BeanType bean) throws DroidsException, IOException
   {
      if (task == null || bean == null) throw new NullPointerException();

      if (outputHandler != null)
      {     
         BeanMap beanMap = new BeanMap(bean);
         BeanMemberType beanMember = (BeanMemberType) beanMap.get(propertyName);
         outputHandler.handle(task, beanMember);
      }
   }

   public void setOutputHandler(TaskResultHandler<T, BeanMemberType> outputHandler)
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
