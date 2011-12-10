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

import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Worker;
import com.trickl.crawler.protocol.soap.SoapProtocol;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.droids.api.ManagedContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;
import org.w3c.dom.Document;

public class SoapWorker<T extends SoapTask<RequestType>, RequestType, ResponseType> implements Worker<T> {

   public final static Logger logger = Logger.getLogger(SoapWorker.class.getCanonicalName());
   private final SoapDroid<T, RequestType, ResponseType> droid;

   public SoapWorker(SoapDroid<T, RequestType, ResponseType> droid) {
      this.droid = droid;
   }

   @Override
   public void execute(T task) throws DroidsException, IOException {
      final String userAgent = this.getClass().getCanonicalName();
      logger.log(Level.FINE, "Starting {0}", userAgent);
      URI uri = task.getURI();
      URI action = task.getSOAPAction();
      RequestType request = task.getSOAPRequest();

      // Marshall the request into XML

      ByteArrayOutputStream xmlOut = new ByteArrayOutputStream();
      try {
         droid.getRequestMarshaller().marshal(request, xmlOut);
      } catch (JAXBException ex) {
         throw new DroidsException("Unable to translate SOAP request", ex);
      }
      ByteArrayInputStream xmlIn = new ByteArrayInputStream(xmlOut.toByteArray());
      Source xmlSource = new StreamSource(xmlIn);

      final SoapProtocol protocol = droid.getProtocol();
      if (protocol.isAllowed(uri)) {
         logger.log(Level.INFO, "Loading {0}", uri);
         protocol.setRequest(xmlSource);
         protocol.setAction(action);
         ManagedContentEntity entity = protocol.load(uri);
         try {
            Parser parser = droid.getParser();
            if (parser != null) {
               Parse parse = parser.parse(entity, task);
               droid.getJaxbHandler().handle(task, (Document) parse.getData());
            }
         } finally {
            entity.finish();
         }
      } else {
         logger.info("Stopping processing since"
                 + " bots are not allowed for this url.");
      }
   }
}

