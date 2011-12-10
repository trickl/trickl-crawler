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
package com.trickl.crawler.robot.soap;

import com.trickl.crawler.protocol.soap.SoapProtocol;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.io.Serializable;
import org.apache.droids.api.Protocol;

public class SimpleSoapTask<RequestType> implements SoapTask<RequestType>, Serializable
{
   private final URI uri;

   private Date started;

   private boolean aborted = false;

   RequestType request;

   URI action;

   public SimpleSoapTask(URI uri, RequestType request, URI action)
   {
      this.uri = uri;
      this.started = new Date();
      this.request = request;
      this.action = action;
   }

   @Override
   public URI getURI()
   {
      return uri;
   }

   @Override
   public String getId()
   {
      return uri.toString();
   }

   @Override
   public Date getTaskDate()
   {
      return started;
   }
 
   @Override
   public void abort()
   {
      aborted = true;
   }
 
   @Override
   public boolean isAborted()
   {
      return aborted;
   }

   @Override
   public RequestType getSOAPRequest() {
      return request;
   }
   
   @Override
   public URI getSOAPAction() {
      return action;
   }

   @Override
   public Protocol getProtocol() {
      return new SoapProtocol();
   }
}

