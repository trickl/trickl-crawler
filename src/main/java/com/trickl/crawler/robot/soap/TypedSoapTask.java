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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import com.trickl.crawler.xml.bind.DefaultNamespace;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.stream.StreamSource;
import org.apache.droids.exception.DroidsException;

public class TypedSoapTask<RequestType> extends UntypedSoapTask {

   private RequestType request;
   private Marshaller requestMarshaller = null;
   private NamespacePrefixMapper namespacePrefixMapper = null;
   
   public TypedSoapTask() {
      super();
   }
   
   /**
    * @return the request
    */
   public RequestType getRequest() {
      return request;
   }

   /**
    * @param request the request to set
    */
   public void setRequest(RequestType request) throws JAXBException {
      this.request = request;

      // Marshall the request into XML
      ByteArrayOutputStream xmlOut = new ByteArrayOutputStream();
      requestMarshaller.marshal(request, xmlOut);
      
      ByteArrayInputStream xmlIn = new ByteArrayInputStream(xmlOut.toByteArray());
      source = new StreamSource(xmlIn);
   }

   public void setNamespacePrefixMapper(NamespacePrefixMapper namespacePrefixMapper) throws PropertyException {
      this.namespacePrefixMapper = namespacePrefixMapper;
      if (requestMarshaller != null) {
         requestMarshaller.setProperty(DefaultNamespace.PROPERTY_NAME, namespacePrefixMapper);
      }
   }
   
   public void setContextPath(String contextPath) throws JAXBException, PropertyException, DroidsException {
      JAXBContext context = JAXBContext.newInstance(contextPath);
      requestMarshaller = context.createMarshaller();
      requestMarshaller.setProperty("jaxb.fragment", Boolean.TRUE);
      if (namespacePrefixMapper != null) {
         setNamespacePrefixMapper(namespacePrefixMapper);
      }
   }
}
