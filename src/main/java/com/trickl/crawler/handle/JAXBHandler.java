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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.droids.exception.DroidsException;
import org.w3c.dom.Document;

public class JAXBHandler<T extends Task, JAXBType> implements TaskResultHandler<T, Document>
{
   private TaskResultHandler<T, JAXBType> outputHandler;

   private Unmarshaller unmarshaller;

   public JAXBHandler()
   {
   }

   @SuppressWarnings("unchecked")
   @Override
   public void handle(T task, Document document) throws DroidsException, IOException
   {
      if (task == null || document == null) throw new NullPointerException();

      if (unmarshaller == null) throw new DroidsException("Unmarshaller is not allocated. JAXB Context path must be set.");

      if (outputHandler != null)
      {     
         try
         {
            JAXBType jaxbObject = (JAXBType) unmarshaller.unmarshal(document);

            outputHandler.handle(task, jaxbObject);
         }
         catch (JAXBException e)
         {
            throw new DroidsException("Unable to unmarshal XML into object.", e);
         }
      }
   }

   public void setOutputHandler(TaskResultHandler<T, JAXBType> outputHandler)
   {
     this.outputHandler = outputHandler;
   }

   public void setContextPath(String contextPath) throws DroidsException
   { 
      try
      {
         JAXBContext context = JAXBContext.newInstance(contextPath);
         unmarshaller = context.createUnmarshaller();
      }
      catch (JAXBException ex)
      {
         throw new DroidsException("Unable to set JAXB context path.", ex);
      }
   }
}
