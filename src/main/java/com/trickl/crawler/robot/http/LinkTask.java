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

import com.trickl.crawler.api.Task;
import java.net.URI;
import java.util.Collection;
import java.util.Date;

public interface LinkTask extends Task
{
   /**
   * 
   * @return The depth of the task
   */
  int getDepth();

  /**
   * @return the Anchor text for this link
   */
  String getAnchorText();
  
  /**
   * From where the link was created
   * 
   * @return the parent link from where the link was coming from
   */
  LinkTask getFrom();

  /**
   * To where the link is pointing to
   * 
   * @return the location where the link is pointing to
   */
  Collection<URI> getTo();

  /**
   * last modified date
   * 
   * @return last modified date
   */
  Date getLastModifiedDate();
}
