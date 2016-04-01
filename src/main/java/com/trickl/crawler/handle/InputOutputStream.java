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
import java.io.*;
import org.apache.droids.exception.DroidsException;

public class InputOutputStream<T extends Task> implements TaskResultHandler<T, InputStream> {

   private OutputStream outputStream;

   public InputOutputStream() {
   }
   
   public InputOutputStream(OutputStream outputStream) {
      this.outputStream = outputStream;
   }

   @Override
   public void handle(T task, InputStream stream) throws DroidsException, IOException {
      if (task == null || stream == null) {
         throw new NullPointerException();
      }

      try (Reader reader = new InputStreamReader(stream);
           Writer output = new OutputStreamWriter(outputStream)) {
         pipe(reader, output);
      }
   }

   protected static void pipe(Reader reader, Writer writer) throws IOException {
      char[] buf = new char[1024];
      int read;
      while ((read = reader.read(buf)) >= 0) {
         writer.write(buf, 0, read);
      }
      writer.flush();
   }

   /**
    * @return the outputStream
    */
   public OutputStream getOutputStream() {
      return outputStream;
   }

   /**
    * @param outputStream the outputStream to set
    */
   public void setOutputStream(OutputStream outputStream) {
      this.outputStream = outputStream;
   }
}
