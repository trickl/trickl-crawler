package com.trickl.crawler;

import com.trickl.crawler.api.Task;
import com.trickl.crawler.robot.image.ImageDroid;
import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.robot.http.SimpleLinkTask;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.net.URI;

import org.apache.droids.exception.DroidsException;
import org.junit.Test;

public class TestImageDroid {

   @Test
   public void copyLocalImageFile() throws Exception {

      ImageDroid<Task> droid = new ImageDroid<>();
      droid.setOutputHandler(new TaskResultHandler<Task, BufferedImage>()
      {
         @Override
         public void handle(Task link, BufferedImage image) throws DroidsException, IOException
         {
            try
            {
               String filename = "trickl-copy.png";
               String packagePath = this.getClass().getPackage().getName().replaceAll("\\.", "/");
               File outputFile = new File("src/test/resources/"
                     + packagePath
                     + "/" + filename);
                     
               FileOutputStream stream = new FileOutputStream(outputFile);
               ImageIO.write(image, "png", stream);
               stream.close();
            }
            catch (FileNotFoundException e)
            {
               throw new DroidsException("File not found.", e);
            }
         }
      });

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("classpath:/com/trickl/crawler/trickl.png"), 0));
   }

   @Test
   public void copyHttpImageFile() throws Exception {

      ImageDroid<Task> droid = new ImageDroid<>();
      droid.setOutputHandler(new TaskResultHandler<Task, BufferedImage>()
      {
         @Override
         public void handle(Task link, BufferedImage image) throws DroidsException, IOException
         {
            try
            {
               String filename = "burbs-poster.png";
               String packagePath = this.getClass().getPackage().getName().replaceAll("\\.", "/");
               File outputFile = new File("src/test/resources/"
                     + packagePath
                     + "/" + filename);
                     
               FileOutputStream stream = new FileOutputStream(outputFile);
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
