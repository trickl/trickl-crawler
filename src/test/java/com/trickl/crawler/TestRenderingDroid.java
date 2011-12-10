package com.trickl.crawler;

import com.trickl.crawler.robot.renderer.RenderingDroid;
import com.trickl.crawler.robot.renderer.RenderTask;
import com.trickl.crawler.robot.renderer.SimpleRenderTask;
import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.robot.renderer.MozillaRenderer;

import java.net.URI;
import java.util.logging.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.droids.exception.DroidsException;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Ignore;

import org.w3c.dom.Document;

public class TestRenderingDroid {

   @BeforeClass
   public static void setup() {
      Handler[] handlers = Logger.getLogger("").getHandlers();
      for (int index = 0; index < handlers.length; index++) {
         handlers[index].setLevel(Level.FINEST);
      }
   }

   @Test
   @Ignore("TODO: Fix this test")
   public void singleThreadedRender() throws Exception {
      Logger.getLogger(MozillaRenderer.class.getCanonicalName()).setLevel(Level.FINEST);

      RenderingDroid<RenderTask> droid = new RenderingDroid<RenderTask>();

      droid.setExecutor(new ThreadPoolExecutor(4, 8, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()));
      droid.setDocumentHandler(new TaskResultHandler<RenderTask, Document>() {

         @Override
         public void handle(RenderTask task, Document document) throws DroidsException {
            try {
               FileOutputStream documentOutputStream = new FileOutputStream(task.getId().replaceAll("[:/.]", "") + ".xml");
               Source source = new DOMSource(document);
               Result result = new StreamResult(documentOutputStream);

               Transformer xformer = TransformerFactory.newInstance().newTransformer();
               xformer.transform(source, result);

               documentOutputStream.close();
            } catch (TransformerConfigurationException e) {
               throw new DroidsException("Error deserializing document. ", e);
            } catch (TransformerException e) {
               throw new DroidsException("Error deserializing document: ", e);
            } catch (IOException e) {
               throw new DroidsException("Error deserializing document: ", e);
            }
         }
      });

      droid.setScreenshotHandler(new TaskResultHandler<RenderTask, BufferedImage>() {

         @Override
         public void handle(RenderTask task, BufferedImage image) throws DroidsException {
            try {
               FileOutputStream imageOutputStream = new FileOutputStream(task.getId().replaceAll("[:/.]", "") + ".png");
               ImageIO.write(image, "png", imageOutputStream);
               imageOutputStream.close();
            } catch (IOException e) {
               throw new DroidsException("Error deserializing document: ", e);
            }
         }
      });

      droid.getNewWorker().execute(new SimpleRenderTask(new URI("http://www.trickl.com")));
   }
}
