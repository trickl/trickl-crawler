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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.droids.exception.DroidsException;
import org.w3c.dom.Document;

public class DocumentStreamHandler<T extends Task> implements TaskResultHandler<T, Document>
{
   private static final Logger logger = Logger.getLogger(DocumentStreamHandler.class.getCanonicalName());

   private TaskResultHandler<T, InputStream> outputHandler; 

   private final Transformer transformer;

   public DocumentStreamHandler() throws DroidsException
   {
      try 
      {        
         transformer = TransformerFactory.newInstance().newTransformer();
         transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      }
      catch (TransformerConfigurationException e)
      {
         throw new DroidsException("Failed to instantiate XML transform", e);
      }
   }

   public void handle(T task, Document document) throws DroidsException, IOException
   {
      if (task == null || document == null) throw new NullPointerException();

      if (outputHandler != null)
      {     
         // Transform XML
         try
         {            
               ByteArrayOutputStream buffer = new ByteArrayOutputStream();

               final Source source = new DOMSource(document);
               final Result result = new StreamResult(buffer);

               transformer.transform(source, result);
               outputHandler.handle(task, new ByteArrayInputStream(buffer.toByteArray()));
         }
         catch (TransformerException e)
         {
            throw new DroidsException("Failed to transform XML", e);
         }
      }
   }

   public void setOutputHandler(TaskResultHandler<T, InputStream> outputHandler)
   {
     this.outputHandler = outputHandler;
   }
}
