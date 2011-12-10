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
package com.trickl.crawler.robot.renderer;

import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.api.Droid;
import com.trickl.crawler.api.Worker;

import java.util.concurrent.Executor;
import java.awt.image.BufferedImage;

import org.w3c.dom.Document;

public class RenderingDroid<T extends RenderTask> implements Droid <T>
{   
   private Executor executor;

   private TaskResultHandler<T, Document> documentHandler;

   private TaskResultHandler<T, BufferedImage> screenshotHandler;

   public RenderingDroid()
   {      
   }

   @Override
   public Worker<T> getNewWorker()
   {
      MozillaRenderer<T> renderer = new MozillaRenderer<T>(this);
      return renderer;
   }

   public TaskResultHandler<T, Document> getDocumentHandler()
   {
      return documentHandler;
   }

   public void setDocumentHandler(TaskResultHandler<T, Document> documentHandler)
   {
      this.documentHandler = documentHandler;
   }

   public TaskResultHandler<T, BufferedImage> getScreenshotHandler()
   {
      return screenshotHandler;
   }

   public void setScreenshotHandler(TaskResultHandler<T, BufferedImage> screenshotHandler)
   {
      this.screenshotHandler = screenshotHandler;
   }

   public Executor getExecutor()
   {
      return executor;
   }

   public void setExecutor(Executor executor)
   {
      if (executor == null) throw new NullPointerException();
      this.executor = executor;
   }
}

