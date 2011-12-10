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

import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import com.trickl.crawler.parser.xml.XmlParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Logger;

import org.w3c.tidy.Tidy;


import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;
import org.apache.droids.parse.ParseImpl;

import org.w3c.dom.Document;

public class JTidyParser implements Parser {

   private static final Logger logger = Logger.getLogger(JTidyParser.class.getCanonicalName());

   private Tidy htmlParser;
   private XmlParser xmlParser;

   public JTidyParser() {
      htmlParser = new Tidy();
      
      htmlParser.setFixUri(true);
      htmlParser.setXmlOut(true);
      htmlParser.setForceOutput(true);
      htmlParser.setMakeClean(true);
      htmlParser.setMakeBare(true);
      htmlParser.setSmartIndent(false);
      htmlParser.setIndentContent(false);
      htmlParser.setIndentAttributes(false);
      htmlParser.setWraplen(0);

      xmlParser = new XmlParser();
   }

   public Parse parse(ContentEntity entity, Task newLink) throws DroidsException, IOException {

      // create HTML parser
      Document document = null;
      InputStream stream = entity.obtainContent();
      try {
         document = parse(stream);
      } finally {
         stream.close();
      }
      return new ParseImpl(newLink.getId(), document, null);
   }

   public Document parse(InputStream stream) throws DroidsException {
      Document document = null;

      // Convert HTML to XML
      ByteArrayOutputStream xmlOut = new ByteArrayOutputStream();
      ByteArrayOutputStream errOut = new ByteArrayOutputStream();
      htmlParser.setErrout(new PrintWriter(errOut));
      htmlParser.parse(stream, xmlOut);

      if (errOut.size() > 0)
      {
         logger.warning("JTidy encountered errors:" + errOut.toString());
      }
      else
      {
         // Parse into an XML DOM
         ByteArrayInputStream xmlIn = new ByteArrayInputStream(xmlOut.toByteArray());
         document = xmlParser.parse(xmlIn);
      }

      return document;
   }
}
