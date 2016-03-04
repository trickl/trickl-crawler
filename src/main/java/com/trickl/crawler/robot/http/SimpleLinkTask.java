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
package com.trickl.crawler.robot.http;

import com.trickl.crawler.api.Task;
import com.trickl.crawler.protocol.http.HttpProtocol;
import com.trickl.crawler.protocol.resource.ResourceProtocol;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.droids.api.Link;
import org.apache.droids.api.Protocol;
import org.apache.droids.exception.ProtocolNotFoundException;
import org.apache.droids.helper.factories.ProtocolFactory;
import org.apache.droids.protocol.file.FileProtocol;

public class SimpleLinkTask implements Link, Task, Serializable {

   private static final long serialVersionUID = -44808094386453088L;
   private Date started;
   private final int depth;
   private final URI uri;
   private final Link from;
   private Date lastModifedDate;
   private Collection<URI> linksTo;
   private String anchorText;
   private int weight;
   private boolean aborted = false;
   private ProtocolFactory protocolFactory;

   public SimpleLinkTask(Link from, URI uri, int depth) {
      this(from, uri, depth, 0);
   }

   public SimpleLinkTask(Link from, URI uri, int depth, int weight) {
      this.from = from;
      this.uri = uri;
      this.depth = depth;
      this.started = new Date();
      this.weight = weight;

      protocolFactory = new ProtocolFactory();
      HttpProtocol httpProtocol = new HttpProtocol();
      FileProtocol fileProtocol = new FileProtocol();
      ResourceProtocol resourceProtocol = new ResourceProtocol();
      protocolFactory.setMap(new HashMap<String, Object>());
      protocolFactory.getMap().put("http", httpProtocol);
      protocolFactory.getMap().put("file", fileProtocol);
      protocolFactory.getMap().put("classpath", resourceProtocol);
   }

   @Override
   public String getId() {
      return uri.toString();
   }

   @Override
   public Date getTaskDate() {
      return started;
   }

   public void setTaskDate(Date started) {
      this.started = started;
   }

   @Override
   public int getDepth() {
      return depth;
   }

   @Override
   public Link getFrom() {
      return from;
   }

   @Override
   public Collection<URI> getTo() {
      return linksTo;
   }

   @Override
   public Date getLastModifiedDate() {
      return lastModifedDate;
   }

   public void setLastModifedDate(Date lastModifedDate) {
      this.lastModifedDate = lastModifedDate;
   }

   public void setLinksTo(Collection<URI> linksTo) {
      this.linksTo = linksTo;
   }

   @Override
   public URI getURI() {
      return uri;
   }

   @Override
   public String getAnchorText() {
      return anchorText;
   }

   public void setAnchorText(String anchorText) {
      this.anchorText = anchorText;
   }

   public int getWeight() {
      return weight;
   }

   public void setWeight(int weight) {
      this.weight = weight;
   }

   @Override
   public void abort() {
      aborted = true;
   }

   @Override
   public boolean isAborted() {
      return aborted;
   }

   @Override
   public Protocol getProtocol() {
      try {
         return protocolFactory.getProtocol(uri);
      } catch (ProtocolNotFoundException ex) {
         Logger.getLogger(SimpleLinkTask.class.getName()).log(Level.SEVERE, null, ex);
         return null;
      }
   }
}