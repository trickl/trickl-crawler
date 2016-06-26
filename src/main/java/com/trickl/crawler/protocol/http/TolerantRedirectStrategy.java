/*
 * Copyright 2016 Trickl.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trickl.crawler.protocol.http;

import java.net.URI;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectStrategy;

class TolerantRedirectStrategy extends DefaultRedirectStrategy {
    
    @Override
    protected URI createLocationURI(String location) throws ProtocolException {
            
        // Allow spaces
        location = location.replaceAll(" ", "%20");        

        return super.createLocationURI(location);
    }
}
