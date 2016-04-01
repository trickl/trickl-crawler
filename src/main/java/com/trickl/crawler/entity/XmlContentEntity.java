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
package com.trickl.crawler.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.logging.*;

import org.apache.droids.api.ManagedContentEntity;
import org.apache.droids.api.Parse;
import org.w3c.dom.Document;

public class XmlContentEntity implements ManagedContentEntity
{
   public static final Logger logger = Logger.getLogger(XmlContentEntity.class.getCanonicalName());

   private final Document document;

   private final Transformer transformer;

   private Parse parse;
  
   public XmlContentEntity(Transformer transformer, Document document) throws IOException
   {
      if (transformer == null || document == null) throw new NullPointerException();
      this.transformer = transformer;
      this.document = document;
   }

   @Override
   public String getMimeType()
   {
      return "text/xml";
   }

   @Override
   public String getCharset()
   {
      return "UTF-8";
   }

   @Override
   public InputStream obtainContent() throws IOException
   {
      try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

         final Source source = new DOMSource(document);
         final Result result = new StreamResult(buffer);

         try
         {
            transformer.transform(source, result);              
         }
         catch (TransformerException e)
         {
            throw new IOException("Unable to serialize XML.", e);
         }      

         return new ByteArrayInputStream(buffer.toByteArray());
      }
   }

   @Override
   public Parse getParse()
   {
      return this.parse;
   }

   @Override
   public void setParse(Parse parse)
   {
      this.parse = parse;
   }

   @Override
   public void finish()
   {
   }
}
