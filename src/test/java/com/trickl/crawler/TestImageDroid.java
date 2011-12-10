package com.trickl.crawler;

import com.trickl.crawler.robot.image.ImageDroid;
import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.robot.http.LinkTask;
import com.trickl.crawler.robot.http.SimpleLinkTask;

import java.awt.image.BufferedImage;
import java.util.logging.*;
import javax.imageio.ImageIO;
import java.net.URI;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.droids.exception.DroidsException;
import org.junit.Test;

public class TestImageDroid {

   @Test
   public void copyLocalImageFile() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      ImageDroid<LinkTask> droid = new ImageDroid<LinkTask>();
      droid.setOutputHandler(new TaskResultHandler<LinkTask, BufferedImage>()
      {
         @Override
         public void handle(LinkTask link, BufferedImage image) throws DroidsException, IOException
         {
            try
            {
               FileOutputStream stream = new FileOutputStream("trickl-copy.png");
               ImageIO.write(image, "png", stream);
               stream.close();
            }
            catch (FileNotFoundException e)
            {
               throw new DroidsException("File not found.", e);
            }
         }
      });

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("classpath:com/trickl/crawler/trickl.png"), 0));
   }

   @Test
   public void copyHttpImageFile() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      ImageDroid<LinkTask> droid = new ImageDroid<LinkTask>();
      droid.setOutputHandler(new TaskResultHandler<LinkTask, BufferedImage>()
      {
         @Override
         public void handle(LinkTask link, BufferedImage image) throws DroidsException, IOException
         {
            try
            {
               FileOutputStream stream = new FileOutputStream("poster.png");
               ImageIO.write(image, "png", stream);
               stream.close();
            }
            catch (FileNotFoundException e)
            {
               throw new DroidsException("File not found.", e);
            }
         }
      });

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://upload.wikimedia.org/wikipedia/en/thumb/0/07/Burbsposter.jpg/200px-Burbsposter.jpg"), 0));
   }
}
