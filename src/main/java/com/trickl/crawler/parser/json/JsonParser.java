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
package com.trickl.crawler.parser.json;

import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;
import org.apache.droids.parse.ParseImpl;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonParser implements Parser {

   private static final Logger logger = Logger.getLogger(JsonParser.class.getCanonicalName());

   private ObjectMapper objectMapper;

   public JsonParser() {
      objectMapper = new ObjectMapper();
   }

   @Override
   public Parse parse(ContentEntity entity, Task task) throws DroidsException, IOException {

      // create HTML parser
      InputStream stream = entity.obtainContent();
      
      JsonNode rootNode = objectMapper.readValue(stream, JsonNode.class);      
      stream.close();
      
      return new ParseImpl(task.getId(), rootNode, null);
   }
}
