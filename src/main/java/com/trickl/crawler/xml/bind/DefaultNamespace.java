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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import java.util.Arrays;

public class DefaultNamespace extends NamespacePrefixMapper {

   public final static String PROPERTY_NAME = "com.sun.xml.bind.namespacePrefixMapper";
   private String defaultNamespaceUri = "";
   private String[] predeclaredNamespaceUris = new String[0];

   public DefaultNamespace() {
   }

   public DefaultNamespace(String defaultNamespaceUri) {
      this.defaultNamespaceUri = defaultNamespaceUri;
   }

   public DefaultNamespace(String... uris) {
      if (uris.length > 0) {
         this.defaultNamespaceUri = uris[0];
         if (uris.length > 1) {
            this.predeclaredNamespaceUris = Arrays.copyOfRange(uris, 1, uris.length);
         }
      }
   }

   public void setDefaultNamespaceUri(String defaultNamespaceUri) {
      this.defaultNamespaceUri = defaultNamespaceUri;
   }

   public void setPreDeclaredNamespaceUris(String[] predeclaredNamespaceUris) {
      this.predeclaredNamespaceUris = predeclaredNamespaceUris;
   }

   @Override
   public String[] getPreDeclaredNamespaceUris() {
      return predeclaredNamespaceUris;
   }

   @Override
   public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
      if (!requirePrefix && namespaceUri.equals(defaultNamespaceUri)) {
         return "";
      }

      for (WellKnownNamespace namespace : WellKnownNamespace.values()) {
         if (namespaceUri.equals(namespace.getURI())) {
            return namespace.getPrefix();
         }
      }

      return suggestion;
   }
}
