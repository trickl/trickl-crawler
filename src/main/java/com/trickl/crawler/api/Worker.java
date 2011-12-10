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
import org.apache.droids.exception.DroidsException;

/**
 * A worker is the unit that is doing the actual work. A {@link Droid} is the
 * "project manger" that delegates the work to worker units. Worker units are
 * implemented as threads to scale they number if more work is to do.
 *
 * @version 1.0
 *
 */
public interface Worker<T extends Task> {

  void execute( final T task ) throws DroidsException, IOException;

}
