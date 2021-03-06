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
package com.trickl.crawler.parser.html;

import com.trickl.crawler.parser.xml.XmlParser;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.tidy.Tidy;


import org.apache.droids.exception.DroidsException;

import org.w3c.dom.Document;

public class JTidyDocumentBuilder implements DocumentBuilder {

   private static final Logger logger = Logger.getLogger(JTidyDocumentBuilder.class.getCanonicalName());

   private Tidy htmlParser;
   private XmlParser xmlParser;

   public JTidyDocumentBuilder() {
      htmlParser = new Tidy();
      
      // Defaults
      htmlParser.setFixUri(true);
      htmlParser.setTidyMark(false);
      htmlParser.setXHTML(true);
      htmlParser.setForceOutput(true);
      htmlParser.setMakeClean(true);
      htmlParser.setMakeBare(true);
      htmlParser.setSmartIndent(false);
      htmlParser.setIndentContent(false);
      htmlParser.setIndentAttributes(false);
      htmlParser.setHideComments(true);
      htmlParser.setWraplen(0);            

      xmlParser = new XmlParser();
   }
   
   public void setXHTML(boolean xhtml) {
       htmlParser.setXHTML(xhtml);
   }
   
   public void setXmlOut(boolean xmlOut) {
       htmlParser.setXmlOut(xmlOut);
   }

   @Override
   public Document build(InputStream stream) throws DroidsException {
      Document document = null;

        // Convert HTML to XML
        try (ByteArrayOutputStream xmlOut = new ByteArrayOutputStream();
             ByteArrayOutputStream errOut = new ByteArrayOutputStream();
             PrintWriter errWriter = new PrintWriter(errOut)) {
            htmlParser.setErrout(errWriter);
            htmlParser.parse(stream, xmlOut);

            if (logger.isLoggable(Level.FINEST) && errOut.size() > 0) {
                logger.log(Level.FINEST, "JTidy encountered errors:{0}", errOut.toString());
            }

            // Parse into an XML DOM
            try (ByteArrayInputStream xmlIn = new ByteArrayInputStream(xmlOut.toByteArray())) {

                if (logger.isLoggable(Level.FINER)) {
                    logger.log(Level.FINER, "JTidy output: {0}", xmlOut.toString());
                }

                document = xmlParser.parse(xmlIn);
            } catch (IOException ex) {
                throw new DroidsException("Unable to close stream", ex);
            }
        } catch (IOException ex) {
            throw new DroidsException("Unable to close stream", ex);
        }

        return document;
    }
}
