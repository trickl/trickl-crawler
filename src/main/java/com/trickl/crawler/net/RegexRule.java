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
package com.trickl.crawler.net;

/**
 * A generic regular expression rule.
 * 
 * @version 1.0
 * 
 */
public abstract class RegexRule {

  private boolean sign = false;

  /**
   * Constructs a new regular expression rule.
   * 
   * @param sign
   *                specifies if this rule must filter-in or filter-out. A
   *                <code>true</code> value means that any url matching this
   *                rule must be accepted, a <code>false</code> value means
   *                that any url matching this rule must be rejected.
   */
  protected RegexRule(boolean sign) {
    this.sign = sign;
  }

  /**
   * Return if this rule is used for filtering-in or out.
   * 
   * @return <code>true</code> if any url matching this rule must be accepted,
   *         otherwise <code>false</code>.
   */
  protected boolean accept() {
    return sign;
  }

  /**
   * Checks if a url matches this rule.
   * 
   * @param url
   *                is the url to check.
   * @return <code>true</code> if the specified url matches this rule,
   *         otherwise <code>false</code>.
   */
  protected abstract boolean match(String url);

}
