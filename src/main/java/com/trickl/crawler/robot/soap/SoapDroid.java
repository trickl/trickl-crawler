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

import com.trickl.crawler.xml.bind.DefaultNamespace;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import com.trickl.crawler.protocol.soap.SoapProtocol;
import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.api.Droid;
import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Worker;
import com.trickl.crawler.handle.JAXBHandler;
import com.trickl.crawler.parser.xml.XmlParser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;


import org.apache.droids.exception.DroidsException;
import org.springframework.ws.client.core.WebServiceTemplate;

public class SoapDroid<T extends SoapTask<RequestType>, RequestType, ResponseType> implements Droid<T>
{
   private JAXBHandler<T, ResponseType> jaxbHandler;
   
   private Parser parser;

   private SoapProtocol soapProtocol;

   private Marshaller requestMarshaller = null;

   private NamespacePrefixMapper namespacePrefixMapper = null;

   public SoapDroid() throws DroidsException
   {      
      parser = new XmlParser();
      soapProtocol = new SoapProtocol();
      jaxbHandler = new JAXBHandler();
   }

   public JAXBHandler<T, ResponseType> getJaxbHandler() {
      return jaxbHandler;
   }

   public void setOutputHandler(TaskResultHandler<T, ResponseType> outputHandler)
   {
      jaxbHandler.setOutputHandler(outputHandler);
   }

   public SoapProtocol getProtocol()
   {
      return soapProtocol;
   }

   public Parser getParser()
   {
      return parser;
   }

   public Marshaller getRequestMarshaller()
   {
      return requestMarshaller;
   }

   @Override
   public Worker<T> getNewWorker() {
      final SoapWorker<T, RequestType, ResponseType> worker =
              new SoapWorker<T, RequestType, ResponseType>(this);

      return worker;
   }

   public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
      this.soapProtocol.setWebServiceTemplate(webServiceTemplate);
   }

   public void setNamespacePrefixMapper(NamespacePrefixMapper namespacePrefixMapper) throws PropertyException
   {
      this.namespacePrefixMapper = namespacePrefixMapper;
      if (requestMarshaller != null) requestMarshaller.setProperty(DefaultNamespace.PROPERTY_NAME, namespacePrefixMapper);
   }

   public void setContextPath(String contextPath) throws JAXBException, PropertyException, DroidsException
   {
      JAXBContext context= JAXBContext.newInstance(contextPath);
      requestMarshaller = context.createMarshaller();
      requestMarshaller.setProperty("jaxb.fragment", Boolean.TRUE);
      if (namespacePrefixMapper != null) setNamespacePrefixMapper(namespacePrefixMapper);

      getJaxbHandler().setContextPath(contextPath);
   }
}
