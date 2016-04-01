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
package com.trickl.crawler.protocol.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import org.apache.droids.api.ManagedContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.api.Protocol;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;

public class SoapProtocol implements Protocol {

   private WebServiceTemplate webServiceTemplate;
   private Source request;
   private URI    action;

   public SoapProtocol() {
   }

   @Override
   public boolean isAllowed(URI uri) {
      return true;
   }

   @Override
   public ManagedContentEntity load(URI uri) throws IOException {
      if (uri == null) throw new NullPointerException();

      try (ByteArrayOutputStream xmlOut = new ByteArrayOutputStream()) {
         StreamResult response = new StreamResult(xmlOut);
         boolean success = getWebServiceTemplate().sendSourceAndReceiveToResult(uri.toString(),
                 getRequest(),
                 new WebServiceMessageCallback() {
                    // Set the SOAP header
                     @Override
                    public void doWithMessage(WebServiceMessage message) {
                       URI action = SoapProtocol.this.getAction();
                       if (action != null) {
                          ((SoapMessage) message).setSoapAction(action.toString());
                       }
                    }
                 },
                 response);

         if (!success) {
            throw new IOException("No soap response received from'" + uri.toString() + "'");
         }
         return new TransformResultContentEntity(xmlOut);
      }
   }

   public WebServiceTemplate getWebServiceTemplate() {
      return webServiceTemplate;
   }

   public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
      this.webServiceTemplate = webServiceTemplate;
   }

   public Source getRequest() {
      return request;
   }

   public void setRequest(Source request) {
      this.request = request;
   }

   public URI getAction() {
      return action;
   }

   public void setAction(URI action) {
      this.action = action;
   }

   static class TransformResultContentEntity implements ManagedContentEntity {

      private final ByteArrayOutputStream xmlOut;
      private Parse parse = null;

      public TransformResultContentEntity(ByteArrayOutputStream xmlOut) throws IOException {
         super();
         this.xmlOut = xmlOut;
      }

      @Override
      public InputStream obtainContent() throws IOException {
         ByteArrayInputStream xmlIn = new ByteArrayInputStream(xmlOut.toByteArray());
         return xmlIn;
      }

      @Override
      public void finish() {
      }

      @Override
      public String getMimeType() {
         return "text/xml";
      }

      @Override
      public String getCharset() {
         return "ISO-8859-1";
      }

      @Override
      public Parse getParse() {
         return this.parse;
      }

      @Override
      public void setParse(Parse parse) {
         this.parse = parse;
      }
   }
}
