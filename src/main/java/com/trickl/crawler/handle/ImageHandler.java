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
package com.trickl.crawler.handle;

import com.trickl.crawler.api.Task;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.droids.api.ContentEntity;
import org.apache.droids.exception.DroidsException;

public class ImageHandler<T extends Task> implements TaskResultHandler<T, ContentEntity>
{
   private TaskResultHandler<T, BufferedImage> outputHandler;

   public ImageHandler()
   {
   }

   @Override
   public void handle(T task, ContentEntity entity) throws DroidsException, IOException
   {
      if (task == null || entity == null) throw new NullPointerException();

      if ((entity.getMimeType().startsWith("binary") ||
           entity.getMimeType().startsWith("image"))
          && outputHandler != null)
      {     
         try (InputStream input = entity.obtainContent())
         {
            BufferedImage image = ImageIO.read(input);
            if (image == null) throw new DroidsException("Unable to interpret stream as an image.");
            outputHandler.handle(task, image);
         }
         catch (IOException e)
         {
            throw new DroidsException("Unable to read image stream.", e);
         }
      }
   }

   public void setOutputHandler(TaskResultHandler<T, BufferedImage> outputHandler)
   {
      this.outputHandler = outputHandler;
   }
}
