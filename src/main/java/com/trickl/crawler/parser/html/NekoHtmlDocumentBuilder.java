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

import org.w3c.dom.Document;

public class NekoHtmlDocumentBuilder implements DocumentBuilder {

   private static final Logger logger = Logger.getLogger(NekoHtmlDocumentBuilder.class.getCanonicalName());

   private DOMParser parserImpl;

   public NekoHtmlDocumentBuilder() {
      parserImpl = new DOMParser(new HTMLConfiguration());
      
      try {
         parserImpl.setFeature(
                 "http://cyberneko.org/html/features/scanner/fix-mswindows-refs",
                 true);         
         parserImpl.setFeature(
                 "http://cyberneko.org/html/features/report-errors",
                 logger.isLoggable(Level.FINER));
         parserImpl.setProperty(
                 "http://cyberneko.org/html/properties/names/elems",
                 "lower");
      } catch (SAXNotRecognizedException | SAXNotSupportedException ex) {
         throw new IllegalStateException(ex);
      }
      
   }

   @Override
   public Document build(InputStream stream) {
      Document document = null;
      try {
         parserImpl.parse(new InputSource(stream));
         document = parserImpl.getDocument();
      } catch (SAXException | IOException e) {
         logger.log(Level.WARNING, "Error processing HTML", e);
      }

      return document;
   }
}
