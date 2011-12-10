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

import com.trickl.crawler.protocol.http.HttpProtocol;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.droids.api.Protocol;
import org.apache.http.client.methods.HttpPost;

public class SimpleHttpPostTask implements HttpPostTask, Serializable
{
   private URI uri;
   private HashMap<String, String> headerData;
   private HashMap<String, Object> postData;
   private Date started;
   private boolean aborted = false;

   public SimpleHttpPostTask(URI uri, Map<String, String> headerData, Map<String, Object> postData)
   {
      this.uri = uri;
      this.started = new Date();
      this.headerData = new HashMap<String, String>(headerData);
      this.postData = new HashMap<String, Object>(postData);
   }
   
   @Override
   public Map<String, String> getHeaderData() {
      return headerData;
   }

   @Override
   public Map<String, Object> getPostData() {
      return postData;
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
   public Protocol getProtocol() {
      HttpProtocol protocol = new HttpProtocol();     
      protocol.setMethod(HttpPost.METHOD_NAME);
      protocol.setHeaderData(headerData);
      protocol.setPostData(postData);
      return protocol;
   }

   @Override
   public void abort() {
      this.aborted = true;
   }

   @Override
   public boolean isAborted() {
      return aborted;
   }

}

