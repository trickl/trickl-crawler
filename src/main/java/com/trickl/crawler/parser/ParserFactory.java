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
import org.apache.droids.exception.DroidsException;
import org.apache.droids.exception.ParserNotFoundException;
import org.apache.droids.helper.factories.GenericFactory;

/**
 * Factory that will lookup a parser by its identifier and return it.
 * 
 * @version 1.0
 * 
 */
public class ParserFactory extends GenericFactory<Parser> {

  /**
   * Lookup a parser by its identifier (content type) and return it.
   * 
   * @param contentType
   *                for which content type we need a parser
   * @return null if we do not find a registered Parser otherwise the Parser
   * @throws DroidsException
   */
  public Parser getParser(String contentType) throws DroidsException {
    if (contentType == null) {
      throw new ParserNotFoundException(contentType);
    }
    return getMap().get(contentType);
  }

}
