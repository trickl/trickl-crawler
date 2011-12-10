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
package com.trickl.crawler.robot.xslt;

import com.trickl.crawler.handle.XslTransformHandler;
import com.trickl.crawler.handle.LinkExtractor;
import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.parser.html.NekoHtmlParser;
import com.trickl.crawler.api.Droid;
import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import com.trickl.crawler.api.Worker;
import com.trickl.crawler.parser.ParserFactory;
import com.trickl.crawler.protocol.http.HttpProtocol;

import java.io.IOException;
import java.util.HashMap;
import java.util.Collection;
import java.net.URI;
        
import org.apache.droids.exception.DroidsException;

import org.w3c.dom.Document;

public class XsltDroid<T extends Task> implements Droid<T>
{
   private XslTransformHandler<T> xslTransformHandler;

   private LinkExtractor<T> linkExtractor;
   
   private ParserFactory parserFactory;
   
   private boolean forceAllow;

   public XsltDroid() throws DroidsException
   {
      parserFactory = new ParserFactory();
      Parser htmlParser = new NekoHtmlParser();
      //Parser htmlParser = new HtmlCleanerParser();
      //Parser htmlParser = new JTidyParser();
      //Parser htmlParser = new RedirectParser(System.out);

      parserFactory.setMap(new HashMap<String, Object>());
      parserFactory.getMap().put("text/html", htmlParser);

      xslTransformHandler = new XslTransformHandler<T>();
      linkExtractor = new LinkExtractor<T>();
   }
   
   public boolean getForceAllow() {
      return forceAllow;
   }

   public void setForceAllow(boolean forceAllow)
   {
      this.forceAllow = forceAllow;
   }

   public void setXsltFile(String file) throws DroidsException
   {
      xslTransformHandler.setClassLoaderObject(this);
      xslTransformHandler.setFile(file);
   }

   public void setOutputHandler(TaskResultHandler<T, Document> outputHandler)
   {
      xslTransformHandler.setOutputHandler(outputHandler);
   }

   public void setLinkHandler(TaskResultHandler<T, Collection<URI> > linkHandler)
   {
      this.linkExtractor.setOutputHandler(linkHandler);
   }

   public LinkExtractor<T> getLinkExtractor()
   {
      return linkExtractor;
   }

   public XslTransformHandler<T> getXslTransformHandler()
   {
      return xslTransformHandler;
   }

   public ParserFactory getParserFactory()
   {
      return parserFactory;
   }

   public void setRegexURLFile(String file) throws IOException
   {
      linkExtractor.setRegexURLFile(file);
   }   

   @Override
   public Worker<T> getNewWorker() {
      final XsltWorker<T> worker = new XsltWorker<T>(this);

      return worker;
   }
}
