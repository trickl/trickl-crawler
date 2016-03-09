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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.htmlcleaner.*;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class HtmlCleanerDocumentBuilder implements DocumentBuilder {

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
         Map<String, String> namespaces = new HashMap<>();
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

      public Document createDOM(TagNode rootNode) throws ParserConfigurationException, XPathExpressionException {
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

   private static final Logger logger = Logger.getLogger(HtmlCleanerDocumentBuilder.class.getCanonicalName());
   private HtmlCleaner parserImpl;

   public HtmlCleanerDocumentBuilder() {
      parserImpl = new HtmlCleaner();
      CleanerProperties props = parserImpl.getProperties();
      props.setOmitComments(true);
      props.setOmitUnknownTags(false);
      props.setOmitXmlDeclaration(false);
      props.setOmitHtmlEnvelope(false);
      props.setOmitDoctypeDeclaration(false);
      props.setOmitDeprecatedTags(false);
      props.setNamespacesAware(true);
      props.setBooleanAttributeValues("self");
      props.setIgnoreQuestAndExclam(true);
   }
   
   public void setPruneTags(String pruneTags) {
       CleanerProperties props = parserImpl.getProperties();
       props.setPruneTags(pruneTags);
   }

   @Override
   public Document build(InputStream stream) {
      Document document = null;
      try {
         DomSerializerNS serializer = new DomSerializerNS(parserImpl.getProperties(), true);
         TagNode node = parserImpl.clean(stream);
         document = serializer.createDOM(node);
      } catch (ParserConfigurationException | XPathExpressionException | IOException e) {
         logger.log(Level.WARNING, "Error processing HTML", e);
      }

      return document;
   }
}

   
