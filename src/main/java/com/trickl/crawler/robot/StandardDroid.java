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
package com.trickl.crawler.robot;

import com.trickl.crawler.api.Droid;
import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import com.trickl.crawler.api.Worker;
import com.trickl.crawler.handle.LinkExtractor;
import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.parser.ParserFactory;
import com.trickl.crawler.parser.html.NekoHtmlParser;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import org.apache.droids.exception.DroidsException;

public class StandardDroid<T extends Task> implements Droid<T>
{
   private TaskResultHandler<T, Object> outputHandler;

   private LinkExtractor<T> linkExtractor;
   
   private ParserFactory parserFactory;
   
   private boolean forceAllow;

   public StandardDroid() throws DroidsException
   {
      parserFactory = new ParserFactory();
      Parser htmlParser = new NekoHtmlParser();
      parserFactory.setMap(new HashMap<String, Object>());
      parserFactory.getMap().put("text/html", htmlParser);
      linkExtractor = new LinkExtractor<T>();
   }
   
   public boolean getForceAllow() {
      return forceAllow;
   }

   public void setForceAllow(boolean forceAllow)
   {
      this.forceAllow = forceAllow;
   }

   public void setOutputHandler(TaskResultHandler<T, Object> outputHandler)
   {
      this.outputHandler = outputHandler;
   }

   public void setLinkHandler(TaskResultHandler<T, Collection<URI> > linkHandler)
   {
      this.linkExtractor.setOutputHandler(linkHandler);
   }

   public LinkExtractor<T> getLinkExtractor()
   {
      return linkExtractor;
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
      final StandardWorker<T> worker = new StandardWorker<T>(this);

      return worker;
   }

   /**
    * @return the outputHandler
    */
   public TaskResultHandler<T, Object> getOutputHandler() {
      return outputHandler;
   }
}
