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
package com.trickl.crawler.robot.image;

import com.trickl.crawler.api.Droid;
import com.trickl.crawler.api.Task;
import com.trickl.crawler.api.Worker;
import com.trickl.crawler.handle.ImageHandler;
import com.trickl.crawler.handle.TaskResultHandler;
import java.awt.image.BufferedImage;


public class ImageDroid<T extends Task> implements Droid<T>
{
   private ImageHandler<T> imageHandler;
   
   public ImageDroid()
   {
      imageHandler = new ImageHandler<T>();
   }

   public ImageHandler<T> getImageHandler()
   {
      return imageHandler;
   }

   public void setOutputHandler(TaskResultHandler<T, BufferedImage> outputHandler)
   {
      imageHandler.setOutputHandler(outputHandler);
   }

   @Override
   public Worker<T> getNewWorker() {
      final ImageWorker<T> worker = new ImageWorker<T>(this);

      return worker;
   }
}
