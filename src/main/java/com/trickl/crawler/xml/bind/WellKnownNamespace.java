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
package com.trickl.crawler.xml.bind;

public enum WellKnownNamespace {

   TRICKL("trickl", "http://trickl.com"),
   XML_SCHEMA("xs", "http://www.w3.org/2001/XMLSchema"),
   XML_SCHEMA_INSTANCE("xsi", "http://www.w3.org/2001/XMLSchema-instance"),
   XML_SCHEMA_DATATYPES("xsd", "http://www.w3.org/2001/XMLSchema-datatypes"),
   XML_NAMESPACE_URI("ns", "http://www.w3.org/XML/1998/namespace"),
   XML_MIME_URI("mime", "http://www.w3.org/2005/05/xmlmime"),
   XHTML("xhtml", "http://www.w3.org/1999/xhtml"),
   JAXB("jaxb", "http://java.sun.com/xml/ns/jaxb"),
   UNDEFINED("undefined", "");

   private String prefix;
   private String uri;

   private WellKnownNamespace(String prefix, String uri) {
      this.prefix = prefix;
      this.uri = uri;
   }
   
   public String getPrefix() {
      return prefix;
   }
   
   public String getURI() {
      return uri;
   }

   @Override
   public String toString() {
      return uri;
   }
}
