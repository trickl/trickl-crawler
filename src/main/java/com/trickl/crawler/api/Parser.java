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
package com.trickl.crawler.api;

import java.io.IOException;
import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;

/**
 * Simple parser that is only forcing to return a parse object.
 * 
 * @see Parse
 * @version 1.0
 * 
 */
public interface Parser {
  /**
   * Creates the parse for some content.
   * 
   * @param entity
   *                the underlying stream we are using
   * @param link
   *                the link that correspond to the stream
   * @return the parse object
   */
  Parse parse(ContentEntity entity, Task link) throws DroidsException, IOException;
}
