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

import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;

public class ReparseHandler<T extends Task> implements TaskResultHandler<T, String> {

   private static final Logger logger = Logger.getLogger(ReparseHandler.class.getCanonicalName());
   private Parser parser;
   private TaskResultHandler<T, Object> outputHandler;

   public ReparseHandler() {
   }

   @Override
   public void handle(T task, String str) throws DroidsException, IOException {
      if (task == null || str == null) {
         throw new NullPointerException();
      }

      if (outputHandler != null) {
         try (final InputStream inputStream = new ByteArrayInputStream(str.getBytes())) {
            Parse parse = parser.parse(new ContentEntity() {

               @Override
               public InputStream obtainContent() throws IOException {
                  return inputStream;
               }

               @Override
               public String getMimeType() {
                  return "binary/octet-stream";
               }

               @Override
               public String getCharset() {
                  return null;
               }

               @Override
               public Parse getParse() {
                  throw new UnsupportedOperationException();
               }
            }, task);
            outputHandler.handle(task, parse.getData());
         }
      }
   }

   public void setOutputHandler(TaskResultHandler<T, Object> outputHandler) {
      this.outputHandler = outputHandler;
   }

   /**
    * @return the parser
    */
   public Parser getParser() {
      return parser;
   }

   /**
    * @param parser the parser to set
    */
   public void setParser(Parser parser) {
      this.parser = parser;
   }
}
