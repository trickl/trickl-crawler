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

import java.net.URI;
import java.util.Date;
import org.apache.droids.api.Protocol;

public interface Task {
    /**
   * The id of the task. In a standard crawl that is most likely the url that
   * identifies the task
   * 
   * @return The id of the task
   */
  String getId();

  URI getURI();
  
  /**
   * When was the task created
   * 
   * @return the date when the task was created.
   */
  Date getTaskDate();
  
  /**
   * The protocol to use for this task
   * 
   * @return the protocol to use for this task 
   */
  Protocol getProtocol();

  public void abort();

  public boolean isAborted();
}
