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
package com.trickl.crawler.handle;

import com.trickl.crawler.api.Task;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.droids.exception.DroidsException;
import org.apache.droids.helper.factories.URLFiltersFactory;
import org.apache.droids.net.RegexURLFilter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

public class LinkExtractor<T extends Task> implements TaskResultHandler<T, Document> {
   
   public class HrefAttributeFilter implements NodeFilter
   {
      @Override
      public short acceptNode(Node node) {         
         if (node.getNodeType() == Node.ELEMENT_NODE)
         {
            Element element = (Element) node;
            if (element.getNodeName().toLowerCase().equals("a"))
            {
               Attr attribute = element.getAttributeNode("href");
               if (attribute == null) return FILTER_REJECT;
               else if (filtersFactory.accept(attribute.getNodeValue())) {
                  return NodeFilter.FILTER_ACCEPT;
               }
               else return NodeFilter.FILTER_REJECT;
            }
            else return NodeFilter.FILTER_SKIP;
         }
         else return NodeFilter.FILTER_SKIP;
      }      
   }

   private static final Logger logger = Logger.getLogger(LinkExtractor.class.getCanonicalName());

   private TaskResultHandler<T, Collection<URI>> outputHandler;

   private URLFiltersFactory filtersFactory;

   public LinkExtractor() {
   }

   @Override
   public void handle(T task, Document document) throws DroidsException, IOException {
      if (task == null || document == null) {
         throw new NullPointerException();
      }

      if (outputHandler != null) {
         NodeFilter filter = new HrefAttributeFilter();
         DocumentTraversal traversal = (DocumentTraversal) document;
         int whatToShow = NodeFilter.SHOW_ELEMENT;
         TreeWalker walker = traversal.createTreeWalker(document.getDocumentElement(), whatToShow, filter, false);         
         Collection<URI> links = new HashSet<URI>();
         URI baseUri = null;
         try
         {
            baseUri = new URI(task.getId());
         }
         catch (URISyntaxException ex)
         {
            logger.log(Level.WARNING, "Base URI for task {0} not interpretable", task.getId());
         }
         for (Node node = walker.nextNode(); node != null; node = walker.nextNode())
         {
            Element element = (Element) node;
            Attr attribute = element.getAttributeNode("href");

            URI uri = getURI(baseUri, attribute.getNodeValue());
            if (!links.contains(uri)) {
               links.add(uri);
            }            
         }

         outputHandler.handle(task, links);
      }
   }

   public void setOutputHandler(TaskResultHandler<T, Collection<URI>> outputHandler) {
      this.outputHandler = outputHandler;
   }

   public void setRegexURLFile(String file) throws IOException {
      filtersFactory = new URLFiltersFactory();
      RegexURLFilter regexURLFilter = new RegexURLFilter();      

      if (file.startsWith("classpath:/")) {
         // Use this class loader for the class path
         URL url = this.getClass().getClassLoader().getResource(
            file.substring("classpath:/".length()));
         file = url.toString();
      }

      regexURLFilter.setFile(file);
      filtersFactory.setMap(new HashMap<String, Object>());
      filtersFactory.getMap().put("http", regexURLFilter);
   }

   private URI getURI(URI baseURI, String target) {
      target = target.replaceAll("\\s", "%20");
      try {
         if (!target.toLowerCase().startsWith("javascript")
                 && !target.contains(":/")) {
            return baseURI.resolve(target.split("#")[0]);
         } else if (!target.toLowerCase().startsWith("javascript")) {
            return new URI(target.split("#")[0]);
         }
      } catch (Exception e) {
         logger.log(Level.FINE, "Ignoring unreadable target: {0}", target);
      }
      return null;
   }
}
