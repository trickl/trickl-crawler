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
package com.trickl.crawler.robot.renderer;

import com.trickl.crawler.protocol.http.HttpProtocol;
import com.trickl.crawler.protocol.resource.ResourceProtocol;
import com.trickl.crawler.robot.http.SimpleLinkTask;
import java.net.URI;
import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.droids.api.Protocol;
import org.apache.droids.exception.ProtocolNotFoundException;
import org.apache.droids.helper.factories.ProtocolFactory;
import org.apache.droids.protocol.file.FileProtocol;

public class SimpleRenderTask implements RenderTask, Serializable {

   private final URI uri;
   private Date started;
   private boolean aborted = false;
   private int windowWidth = 1024;
   private int windowHeight = 768;
   private ProtocolFactory protocolFactory;

   public SimpleRenderTask(URI uri) {
      this.uri = uri;
      this.started = new Date();

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
   public URI getURI() {
      return uri;
   }

   @Override
   public String getId() {
      return uri.toString();
   }

   @Override
   public Date getTaskDate() {
      return started;
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
   public int getWindowHeight() {
      return windowHeight;
   }

   @Override
   public int getWindowWidth() {
      return windowWidth;
   }

   public void setWindowHeight(int height) {
      this.windowHeight = height;
   }

   public void setWindowWidth(int width) {
      this.windowWidth = width;
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
