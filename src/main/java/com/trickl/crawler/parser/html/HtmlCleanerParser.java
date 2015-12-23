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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;
import org.apache.droids.parse.ParseImpl;
import org.htmlcleaner.*;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HtmlCleanerParser implements Parser {

   // Namespace-aware serializer
   public static class DomSerializerNS {

      protected CleanerProperties props;
      protected boolean escapeXml = true;

      public DomSerializerNS(CleanerProperties props, boolean escapeXml) {
         this.props = props;
         this.escapeXml = escapeXml;
      }

      public DomSerializerNS(CleanerProperties props) {
         this(props, true);
      }

      private Map<String, String> getDeclaredNamespaces(TagNode rootNode) {
         Map<String, String> namespaces = new HashMap<String, String>();
         for (Object attribute : rootNode.getAttributes().keySet()) {
            String attr = attribute.toString().toLowerCase();
            if (attr.startsWith("xmlns")) {
               String prefix = "";
               int prefixIndex = attr.indexOf(':');
               if (prefixIndex > 0) {
                  prefix = attr.substring(prefixIndex + 1);
               }

               String uri = (String) rootNode.getAttributes().get(attribute);
               namespaces.put(prefix, uri);
            }
         }

         return namespaces;
      }

      private String getNamespace(TagNode node, Map<String, String> namespaces) {
         String qualifiedName = node.getName();
         String namespace = namespaces.get("");
         int prefixIndex = qualifiedName.indexOf(':');
         if (prefixIndex > 0) {
            String prefix = qualifiedName.substring(0, prefixIndex);
            namespace = namespaces.get(prefix);
         }
         if (namespace == null) {
            namespace = "";
         }
         return namespace;
      }

      public Document createDOM(TagNode rootNode) throws ParserConfigurationException {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

         Map<String, String> namespaces = getDeclaredNamespaces(rootNode);

         Document document = factory.newDocumentBuilder().newDocument();
         String namespace = getNamespace(rootNode, namespaces);
         Element rootElement = document.createElementNS(namespace, rootNode.getName().toLowerCase());
         document.appendChild(rootElement);

         createSubnodes(document, rootElement, rootNode.getChildren(), namespaces);

         return document;
      }

      private void createSubnodes(Document document, Element element, List tagChildren, Map<String, String> namespaces) {
         if (tagChildren != null) {
            Iterator it = tagChildren.iterator();
            while (it.hasNext()) {
               Object item = it.next();
               if (item instanceof CommentNode) {
                  CommentNode commentToken = (CommentNode) item;
                  Comment comment = document.createComment(commentToken.getContent());
                  element.appendChild(comment);
               } else if (item instanceof ContentNode) {
                  String nodeName = element.getNodeName();
                  ContentNode contentToken = (ContentNode) item;
                  String content = contentToken.getContent();
                  boolean specialCase = props.isUseCdataForScriptAndStyle()
                          && ("script".equalsIgnoreCase(nodeName) || "style".equalsIgnoreCase(nodeName));
                  if (escapeXml && !specialCase) {
                     content = Utils.escapeXml(content, props, true);
                  }
                  element.appendChild(specialCase ? document.createCDATASection(content) : document.createTextNode(content));
               } else if (item instanceof TagNode) {
                  TagNode subTagNode = (TagNode) item;

                  String namespace = getNamespace(subTagNode, namespaces);
                  Element subelement = document.createElementNS(namespace, subTagNode.getName().toLowerCase());
                  Map attributes = subTagNode.getAttributes();
                  Iterator entryIterator = attributes.entrySet().iterator();
                  while (entryIterator.hasNext()) {
                     Map.Entry entry = (Map.Entry) entryIterator.next();
                     String attrName = (String) entry.getKey();
                     String attrValue = (String) entry.getValue();
                     if (escapeXml) {
                        attrValue = Utils.escapeXml(attrValue, props, true);
                     }
                     subelement.setAttribute(attrName, attrValue);
                  }

                  // recursively create subnodes
                  createSubnodes(document, subelement, subTagNode.getChildren(), namespaces);

                  element.appendChild(subelement);
               } else if (item instanceof List) {
                  List sublist = (List) item;
                  createSubnodes(document, element, sublist, namespaces);
               }
            }
         }
      }
   }

   private static final Logger logger = Logger.getLogger(HtmlCleanerParser.class.getCanonicalName());
   private HtmlCleaner parserImpl;

   public HtmlCleanerParser() {
      parserImpl = new HtmlCleaner();
      CleanerProperties props = parserImpl.getProperties();
      props.setOmitComments(true);
      props.setOmitUnknownTags(false);
      props.setOmitXmlDeclaration(false);
      props.setOmitHtmlEnvelope(false);
      props.setOmitDoctypeDeclaration(false);
      props.setOmitDeprecatedTags(false);
      props.setNamespacesAware(true);
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

   public Document parse(InputStream stream) {
      Document document = null;
      try {
         DomSerializerNS serializer = new DomSerializerNS(parserImpl.getProperties(), true);
         TagNode node = parserImpl.clean(stream);
         document = serializer.createDOM(node);
      } catch (ParserConfigurationException e) {
         logger.log(Level.WARNING, "Parse configuration error processing HTML", e);
         e.printStackTrace();
      } catch (IOException e) {
         logger.log(Level.WARNING, "IO error processing HTML", e);
      }

      return document;
   }
}

   
