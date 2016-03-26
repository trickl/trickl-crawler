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

import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import com.trickl.crawler.api.Worker;
import com.trickl.crawler.handle.TaskResultHandler;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.droids.api.ManagedContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.api.Protocol;
import org.apache.droids.exception.DroidsException;

public class StandardWorker<T extends Task> implements Worker<T> {

   public final static Logger logger = Logger.getLogger(StandardWorker.class.getCanonicalName());
   private final StandardDroid<T> droid;

   public StandardWorker(StandardDroid<T> droid) {
      this.droid = droid;
   }

   @Override
   public void execute(T task) throws DroidsException, IOException {
      final String userAgent = this.getClass().getCanonicalName();
      logger.log(Level.FINE, "Starting {0}", userAgent);
      URI uri = task.getURI();
      final Protocol protocol = task.getProtocol();
            
      if (droid.getForceAllow() || protocol.isAllowed(uri)) {
         logger.log(Level.FINE, "Loading {0}", uri);
         
         ManagedContentEntity entity = protocol.load(uri);
         try {
            String contentType = entity.getMimeType();
            logger.log(Level.FINE, "Content type {0}", contentType);
            if (contentType == null) {
               logger.log(Level.WARNING, "Missing content type... can't parse unknown mime type...");
            } else {
               Parser parser = droid.getParserFactory().getParser(contentType);
               if (parser != null) {
                  Parse parse = parser.parse(entity, task);
                  Object parseData = parse.getData();
                  for(TaskResultHandler<T, Object> handler : droid.getOutputHandlers()) {
                     handler.handle(task, parseData);
                  }
               }
               else 
               {
                   logger.log(Level.WARNING, "No parser understands content type: " + contentType);                   
               }
            }
         }
         catch (DroidsException | IOException ex) {
            logger.log(Level.WARNING, "Output could not be processed. Reason: {0}", ex.getMessage());
            throw ex;
         }
         catch (Error ex) {
            logger.log(Level.SEVERE, "Output could not be processed. Reason: {0}", ex.getMessage());
            throw ex;
         }
         finally {            
            entity.finish();
         }
      } else {
         logger.info("Stopping processing since"
                 + " bots are not allowed for this url.");
      }
   }
}

