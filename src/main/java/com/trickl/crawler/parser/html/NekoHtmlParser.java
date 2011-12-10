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
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cyberneko.html.HTMLConfiguration;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import org.apache.xerces.parsers.DOMParser;
import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;
import org.apache.droids.parse.ParseImpl;

import org.w3c.dom.Document;

public class NekoHtmlParser implements Parser {

   private static final Logger logger = Logger.getLogger(NekoHtmlParser.class.getCanonicalName());

   private DOMParser parserImpl;

   public NekoHtmlParser() {
      parserImpl = new DOMParser(new HTMLConfiguration());
      
      try {
         parserImpl.setFeature(
                 "http://cyberneko.org/html/features/scanner/fix-mswindows-refs",
                 true);         
         parserImpl.setFeature(
                 "http://cyberneko.org/html/features/report-errors",
                 false);
         parserImpl.setProperty(
                 "http://cyberneko.org/html/properties/names/elems",
                 "lower");
      } catch (SAXNotRecognizedException ex) {
         throw new IllegalStateException(ex);
      } catch (SAXNotSupportedException ex) {
         throw new IllegalStateException(ex);
      }
      
   }

   @Override
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

   public Document parse(InputStream stream) {
      Document document = null;
      try {
         parserImpl.parse(new InputSource(stream));
         document = parserImpl.getDocument();
      } catch (SAXException e) {
         logger.log(Level.WARNING, "XML error processing HTML", e);
      } catch (IOException e) {
         logger.log(Level.WARNING, "IO error processing HTML", e);
      }

      return document;
   }
}
