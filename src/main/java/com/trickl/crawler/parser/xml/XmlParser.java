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
package com.trickl.crawler.parser.xml;

import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;
import org.apache.droids.parse.ParseImpl;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlParser implements Parser {

   private static final Logger logger = Logger.getLogger(XmlParser.class.getCanonicalName());
   private final DocumentBuilder documentBuilder;

   public XmlParser() {

      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         dbf.setValidating(false);
         dbf.setFeature("http://xml.org/sax/features/namespaces", true);
         dbf.setFeature("http://xml.org/sax/features/validation", false);
         dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
         dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

         documentBuilder = dbf.newDocumentBuilder();
      } catch (ParserConfigurationException ex) {
         throw new IllegalStateException(ex);
      }
   }

   @Override
   public Parse parse(ContentEntity entity, Task task) throws DroidsException, IOException {

      // create HTML parser
      Document document = null;
      InputStream stream = entity.obtainContent();
      try {
         document = parse(stream);
      } finally {
         stream.close();
      }
      return new ParseImpl(task.getId(), document, null);
   }

   public Document parse(InputStream stream) throws DroidsException {
      Document document = null;

      // Parse into an XML DOM
      try {
         document = documentBuilder.parse(stream);
      } catch (SAXException | IOException ex) {
          try {
              logger.log(Level.WARNING, "Error processing stream", ex);
              Transformer transformer;
              transformer = TransformerFactory.newInstance().newTransformer();              
              transformer.setOutputProperty(OutputKeys.INDENT, "yes");
              ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
              transformer.transform(new StreamSource(stream), new StreamResult(outputStream));
              logger.log(Level.FINEST, "Input:\n{0}", outputStream.toString());              
              throw new DroidsException(ex);
          } catch (TransformerException ex2) {
              Logger.getLogger(XmlParser.class.getName()).log(Level.SEVERE, "Unable to dump stream.", ex2);
          }
      } 

      return document;
   }
}
