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
package com.trickl.crawler.robot.soap;

import com.trickl.crawler.protocol.soap.SoapProtocol;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import javax.xml.transform.Source;
import org.apache.droids.api.Protocol;
import org.springframework.ws.client.core.WebServiceTemplate;

public class UntypedSoapTask implements SoapTask, Serializable {

   private URI uri;
   private Date started;
   private boolean aborted = false;
   protected Source source;
   private URI action;
   private WebServiceTemplate webServiceTemplate = null;
   
   public UntypedSoapTask() {
      this.started = new Date();      
   }
   
   @Override
   public URI getURI() {
      return uri;
   }
   
   @Override
   public String getId() {
      return uri.toString();
   }
   
   @Override
   public Date getTaskDate() {
      return started;
   }
   
   @Override
   public void abort() {
      aborted = true;
   }
   
   @Override
   public boolean isAborted() {
      return aborted;
   }
   
   @Override
   public Source getSOAPRequest() {
      return source;
   }
   
   @Override
   public URI getSOAPAction() {
      return action;
   }
   
   @Override
   public Protocol getProtocol() {
      SoapProtocol soapProtocol = new SoapProtocol();
      soapProtocol.setAction(action);      
      soapProtocol.setRequest(source);
      soapProtocol.setWebServiceTemplate(webServiceTemplate);
      return soapProtocol;
   }

   /**
    * @return the uri
    */
   public URI getUri() {
      return uri;
   }

   /**
    * @param uri the uri to set
    */
   public void setUri(URI uri) {
      this.uri = uri;
   }

   /**
    * @return the source
    */
   public Source getSource() {
      return source;
   }

   /**
    * @param source the source to set
    */
   public void setSource(Source source) {
      this.source = source;
   }

   /**
    * @return the action
    */
   public URI getAction() {
      return action;
   }

   /**
    * @param action the action to set
    */
   public void setAction(URI action) {
      this.action = action;
   }

   /**
    * @return the webServiceTemplate
    */
   public WebServiceTemplate getWebServiceTemplate() {
      return webServiceTemplate;
   }
   
   public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
      this.webServiceTemplate = webServiceTemplate;
   }
}
