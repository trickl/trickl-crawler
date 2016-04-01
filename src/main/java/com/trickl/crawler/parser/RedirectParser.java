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
package com.trickl.crawler.parser;

import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;
import org.apache.droids.parse.ParseImpl;


public class RedirectParser implements Parser {

   private static final Logger logger = Logger.getLogger(RedirectParser.class.getCanonicalName());

   OutputStream out;

   public RedirectParser(OutputStream out) {
      this.out = out;
   }

   public Parse parse(ContentEntity entity, Task newLink) throws DroidsException, IOException {

      try (InputStream in = entity.obtainContent()) {
         // TODO -Should this be threaded, not sure if the servlet is on the same thread
         byte[] imageBytes = new byte[1024];
         while (in.read(imageBytes) != -1)
         {
            out.write(imageBytes);
         }
      }
      catch (IOException e)
      {
         throw e;
      }

      return new ParseImpl(newLink.getId(), null, null);
   }
}
