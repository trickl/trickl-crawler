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
package com.trickl.crawler.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.logging.*;

import org.apache.droids.api.ManagedContentEntity;
import org.apache.droids.api.Parse;

public class ImageContentEntity implements ManagedContentEntity {

   public static final Logger logger = Logger.getLogger(ImageContentEntity.class.getCanonicalName());
   private final BufferedImage image;
   private final String formatName;
   private Parse parse;

   public ImageContentEntity(BufferedImage image, String formatName) throws IOException {
      this.image = image;
      this.formatName = formatName;
   }

   public String getMimeType() {
      return "image/" + formatName;
   }

   public String getCharset() {
      return null;
   }

   public InputStream obtainContent() throws IOException {
      try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

         try {
            ImageIO.write(image, formatName, buffer);
         } catch (IOException e) {
            logger.log(Level.WARNING, "Error serializing image", e);
         }

         return new ByteArrayInputStream(buffer.toByteArray());
      }
   }

   public Parse getParse() {
      return this.parse;
   }

   public void setParse(Parse parse) {
      this.parse = parse;
   }

   public void finish() {
   }
}
