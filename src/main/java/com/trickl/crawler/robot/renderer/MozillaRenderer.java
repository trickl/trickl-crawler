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

import com.trickl.crawler.api.Worker;
import com.trickl.crawler.handle.TaskResultHandler;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.*;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.droids.exception.DroidsException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.mozilla.dom.NodeFactory;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMWindow;
import org.mozilla.interfaces.nsIWebBrowser;
import org.w3c.dom.Document;


/**
* A distributable scraper task using the Eclipse SWT browser widget
*/
public class MozillaRenderer<T extends RenderTask> implements Worker<T>, ProgressListener
{
   public final static Logger logger = Logger.getLogger(MozillaRenderer.class.getCanonicalName());

   public enum LoadState 
   {
      EMPTY, PROGRESS, COMPLETE
   }

   private boolean receivedProgressEvent = false;

   private RenderingDroid<T> droid;

   private Display display;

   private Shell shell;

   private Browser browser;

   private Document document;

   private BufferedImage screenshot;

   private Point mousePos;

   private LoadState loadState = LoadState.EMPTY;

   static private AtomicLong runningCount = new AtomicLong();

   // Configuration parameters
   private int loadTimeout = 120;
   private TimeUnit loadTimeoutUnit = TimeUnit.SECONDS;

   private int stallTimeout = 1;
   private TimeUnit stallTimeoutUnit = TimeUnit.SECONDS;

   private int threadTimeout = 5;
   private TimeUnit threadTimeoutUnit = TimeUnit.SECONDS;

   private int screenshotTimeout = 5;
   private TimeUnit screenshotTimeoutUnit = TimeUnit.SECONDS;

   private int documentTimeout = 5;
   private TimeUnit documentTimeoutUnit = TimeUnit.SECONDS;

   public MozillaRenderer(RenderingDroid<T> droid)
   {
      if (droid == null) throw new NullPointerException();
      this.droid = droid;
   }

   public LoadState getLoadState()
   {
      return loadState;
   }

   public void setLoadTimeout(int timeout, TimeUnit timeoutUnit)
   {
      this.loadTimeout = timeout;
      this.loadTimeoutUnit = timeoutUnit;
   }

   public void setStallTimeout(int timeout, TimeUnit timeoutUnit)
   {
      this.stallTimeout = timeout;
      this.stallTimeoutUnit = timeoutUnit;
   }

   public void setThreadTimeout(int timeout, TimeUnit timeoutUnit)
   {
      this.threadTimeout = timeout;
      this.threadTimeoutUnit = timeoutUnit;
   }

   public void setScreenshotTimeout(int timeout, TimeUnit timeoutUnit)
   {
      this.screenshotTimeout = timeout;
      this.screenshotTimeoutUnit = timeoutUnit;
   }

   public void setDocumentTimeout(int timeout, TimeUnit timeoutUnit)
   {
      this.documentTimeout = timeout;
      this.documentTimeoutUnit = timeoutUnit;
   }

   private synchronized void init(final T task) throws TimeoutException, InterruptedException
   {
      display = Display.getDefault();
      if (display.getThread().equals(Thread.currentThread()))
      {
         // We don't want this to be the UI thread
         display.dispose();

         Executor executor = droid.getExecutor();

         // Spawn a local thread for the display event loop 
         executor.execute(new Runnable() {
            @Override
            public void run() {

               try
               {
                  logger.info("Display thread spawned");

                  // Environment variables are relevant for the flash plugin
                  // and should be set for the glassfish user.
                  // get the system environment variables
                  StringBuilder envBuffer = new StringBuilder("Environment Variables:");
                  String delimiter = "";
                  java.util.Map<String, String> envMap = System.getenv();

                  // print the system environment variables
                  for (String key : envMap.keySet())
                  {
                     envBuffer.append(delimiter);
                     envBuffer.append(key).append(" = ").append(envMap.get(key));
                     delimiter = ", ";
                  }
                  logger.info(envBuffer.toString());

                  display = Display.getDefault();

                  // Notify the worker thread the display is initialized
                  synchronized (MozillaRenderer.this)
                  {
                     MozillaRenderer.this.notifyAll();
                  }

                  //while (runningCount.get() > 0) {
                  // TODO Horrible HACK to get Display always alive, doesn't dispose of nicely
                  while (runningCount.get() >= 0) {
                     if (!display.readAndDispatch())
                        display.sleep();
                  }

                  // Perform any final display actions
                  while (display.readAndDispatch());
               }
               catch (Throwable e)
               {
                  logger.log(Level.WARNING, "Display thread error", e);
               }
               finally
               {
                  if(display != null && !display.isDisposed())
                  {
                     logger.info("Disposing display.");
                     display.dispose();
                  }

                  logger.info("Display thread terminated");
               }
            }
         });

         // Wait for the display to be constructed
         threadTimeoutUnit.timedWait(this, threadTimeout);
         
         if (display == null)
         {
            throw new TimeoutException("Timed out waiting for the display thread to start.");
         }
      }
      else
      {
         logger.info("Display thread available on separate thread.");
      }

      // Initialized data
      display.asyncExec(new Runnable() {
         public void run() {
            shell       = new Shell(display);
            browser     = new Browser(shell, SWT.MOZILLA);

            browser.addProgressListener(MozillaRenderer.this);
         }
      });

      mousePos = new Point(1, 1);
   }

   @Override
   public void changed(ProgressEvent event)
   {
      if (event.total == 0) return;
      if (loadState == LoadState.EMPTY || 
          loadState == LoadState.PROGRESS)
         onLoadProgress(event.current);
   }

   @Override
   public void completed(ProgressEvent event) {
      if (loadState == LoadState.PROGRESS)
         onLoadComplete();
   }

   private synchronized void onLoadProgress(int bytes)
   {
      logger.log(Level.FINER, " : Loaded {0} bytes", bytes);
      receivedProgressEvent = true;
      loadState = LoadState.PROGRESS;
      notifyAll();
   }

   private synchronized void onLoadComplete()
   {
      logger.info(" : Load completed");
      loadState = LoadState.COMPLETE;
      notifyAll();
   }

   @Override
   public void execute(final T task) throws DroidsException, IOException
   {
      if (droid.getExecutor() == null) throw new DroidsException("Executor not set.");

      runningCount.incrementAndGet();

      try
      {
         init(task);

         open(task);

         load(task);

         TaskResultHandler<T, Document> documentHandler = droid.getDocumentHandler();
         if (documentHandler != null)
         {
            Document doc = getDocument();
            documentHandler.handle(task, doc);
         }

         TaskResultHandler<T, BufferedImage> screenshotHandler = droid.getScreenshotHandler();
         if (screenshotHandler != null)
         {
            BufferedImage image = getScreenshot(task.getWindowWidth(), task.getWindowHeight());
            screenshotHandler.handle(task, image);
         }
      }
      catch (InterruptedException ex)
      {
         throw new DroidsException("Interrupted during task processing.", ex);
      }
      catch (TimeoutException ex)
      {
         throw new DroidsException("Task timed out.", ex);
      }
      finally
      {
         close();

         runningCount.decrementAndGet();
      }
   }

   private void open(final T task)
   {
      display.asyncExec(new Runnable() {
         @Override
         public void run() {
            synchronized (MozillaRenderer.class)
            {
               shell.setLayout(new FillLayout());
               shell.setSize(task.getWindowWidth(), task.getWindowHeight()); // Default Size
               shell.open();
            }
         }
      });

      display.wake();
   }

   private void close()
   {
      if (display != null && !display.isDisposed())
      {
         logger.info("Requesting shell be disposed.");
         display.asyncExec(new Runnable() {
            @Override
            public void run() {
               if (shell != null && !shell.isDisposed())
               {
                  shell.close();
                  shell.dispose();
               }
               else logger.info("No need to dispose shell, already disposed.");
            }
         });
      }
      else logger.info("No need to dispose shell, display already disposed.");

      display.wake();
   }

   private synchronized void load(final T task) throws TimeoutException, InterruptedException
   {
      logger.fine("Starting load...");

      display.asyncExec(new Runnable() {
         @Override
         public void run() {
            browser.setUrl(task.getURI().toString());
         }
      });

      long startTime = System.currentTimeMillis();
      long loadTimeoutMillis = loadTimeoutUnit.toMillis(loadTimeout);

      while (loadState != LoadState.COMPLETE && System.currentTimeMillis() - startTime < loadTimeoutMillis)
      {
         if (receivedProgressEvent) receivedProgressEvent = false;
         stallTimeoutUnit.timedWait(this, stallTimeout);

         // Prevent a stall
         if (!receivedProgressEvent)
         {
            logger.finer("Detected possible stall...sending mouse event to get things moving.");
            moveMouse();
         }
      }

      if (loadState != LoadState.COMPLETE)
      {
         display.asyncExec(new Runnable() {
            @Override
            public void run() {
               browser.stop();
            }
         });

         throw new TimeoutException("Timed out waiting for load.");
      }

      logger.fine("finished load");
   }

   private void moveMouse()
   {
      display.asyncExec(new Runnable() {
         @Override
         public void run() {
            synchronized (MozillaRenderer.class)
            {
               shell.moveAbove(null);
               shell.setFocus();
               shell.update();

               // Wiggle the mouse pointer up and down
               mousePos.y = (mousePos.y % 20) + 5;
               final Point pt = display.map(browser, null, mousePos.x, mousePos.y);
               Event event = new Event();
               event.type = SWT.MouseMove;
               event.widget = browser;
               event.x = pt.x;
               event.y = pt.y;
               display.post(event);
            }
         }
      });

      display.wake();
   }

   /**
   * Capture the rendered web page as an image.
   */
   public synchronized BufferedImage getScreenshot(int screenshotWidth, int screenshotHeight) throws TimeoutException, InterruptedException
   {
      screenshot = null;
      final org.eclipse.swt.graphics.Image unscaledImage = new org.eclipse.swt.graphics.Image(display, screenshotWidth, screenshotHeight);

      // Request that the browser gains focus
      display.asyncExec(new Runnable() {
         @Override
         public void run() {
            synchronized (MozillaRenderer.class)
            {
               // Move on top and force a redraw
               shell.moveAbove(null);
               shell.setFocus();
               shell.update();

               GC gc = new GC(browser);
               gc.copyArea(unscaledImage, 0, 0);
               gc.dispose();

               screenshot = convertToAWT(unscaledImage.getImageData());

               synchronized(MozillaRenderer.this)
               {
                  MozillaRenderer.this.notifyAll();
               }
            }
         }
      });

      display.wake();

      long startTime = System.currentTimeMillis();
      long screenshotTimeoutOriginalMillis = screenshotTimeoutUnit.toMillis(screenshotTimeout);
      long screenshotTimeoutMillis = screenshotTimeoutOriginalMillis;

      while (screenshot == null && screenshotTimeoutMillis > 0)
      {
         TimeUnit.MILLISECONDS.timedWait(this, screenshotTimeoutMillis);
         screenshotTimeoutMillis = screenshotTimeoutOriginalMillis- (System.currentTimeMillis() - startTime);
      }

      if (screenshot == null) throw new TimeoutException("Timed out aquiring screenshot.");

      return screenshot;
   }


   /**
   * Capture the DOM of the processed web page (after Javascript processing)
   */
   public synchronized Document getDocument() throws TimeoutException, InterruptedException
   {
      document = null;

      display.asyncExec(new Runnable() {
         @Override
         public void run() {
            nsIWebBrowser webBrowser = (nsIWebBrowser) browser.getWebBrowser();
            if (webBrowser == null)
            {
               throw new MissingResourceException("Unable to get the XPCOM interface to the browser. The classes from JavaXPCOM >= 1.8.1.2 may not be resolvable (MozillaGlue.jar required).", "org.mozilla.xpcom.internal.XPCOMUtils", "MozillaGlue.jar");
            }

            nsIDOMWindow domWindow = webBrowser.getContentDOMWindow();
            nsIDOMDocument domDocument = domWindow.getDocument();

            document = (Document) NodeFactory.getNodeInstance(domDocument);

            synchronized(MozillaRenderer.this)
            {
               MozillaRenderer.this.notifyAll();
            }
         }
      });

      display.wake();

      long startTime = System.currentTimeMillis();
      long documentTimeoutOriginalMillis = documentTimeoutUnit.toMillis(documentTimeout);
      long documentTimeoutMillis = documentTimeoutOriginalMillis;

      while (document == null && documentTimeoutMillis > 0)
      {
         TimeUnit.MILLISECONDS.timedWait(this, documentTimeoutMillis);
         documentTimeoutMillis = documentTimeoutOriginalMillis - (System.currentTimeMillis() - startTime);
      }

      if (document == null) throw new TimeoutException("Timed out aquiring document.");

      return document;
   }

   private static BufferedImage convertToAWT(ImageData data)
   {
      ColorModel colorModel = null;
      PaletteData palette = data.palette;
      if (palette.isDirect) {
         colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask,
         palette.blueMask);
         BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel
          .createCompatibleWritableRaster(data.width, data.height), false, null);
         WritableRaster raster = bufferedImage.getRaster();

         int[] pixelArray = new int[3];
         for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
               int pixel = data.getPixel(x, y);
               RGB rgb = palette.getRGB(pixel);
               pixelArray[0] = rgb.red;
               pixelArray[1] = rgb.green;
               pixelArray[2] = rgb.blue;
               raster.setPixels(x, y, 1, 1, pixelArray);
            }
         }
         return bufferedImage;
      }
      else
      {
         RGB[] rgbs = palette.getRGBs();
         byte[] red = new byte[rgbs.length];
         byte[] green = new byte[rgbs.length];
         byte[] blue = new byte[rgbs.length];

         for (int i = 0; i < rgbs.length; i++)
         {
            RGB rgb = rgbs[i];
            red[i] = (byte) rgb.red;
            green[i] = (byte) rgb.green;
            blue[i] = (byte) rgb.blue;
         }

         if (data.transparentPixel != -1) {
            colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue,
               data.transparentPixel);
         }
         else
         {
            colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
         }

         BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel
            .createCompatibleWritableRaster(data.width, data.height), false, null);
         WritableRaster raster = bufferedImage.getRaster();

         int[] pixelArray = new int[1];
         for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
               int pixel = data.getPixel(x, y);
               pixelArray[0] = pixel;
               raster.setPixel(x, y, pixelArray);
            }
         }
         return bufferedImage;
      }
   }

   private static BufferedImage getScaledInstance(BufferedImage img,
                                                  int targetWidth,
                                                  int targetHeight)
   {
      int type = (img.getTransparency() == Transparency.OPAQUE) ?
         BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
      BufferedImage scaledImage = img;
      int w = img.getWidth();
      int h = img.getHeight();
        
      do {
         if (w > targetWidth) {
            w /= 2;
            if (w < targetWidth) {
               w = targetWidth;
            }
         }

         if (h > targetHeight) {
            h /= 2;
            if (h < targetHeight) {
               h = targetHeight;
            }
         }

         BufferedImage tempImage = new BufferedImage(w, h, type);
         Graphics2D g = tempImage.createGraphics();
         g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
         g.drawImage(scaledImage, 0, 0, w, h, null);
         g.dispose();

         scaledImage = tempImage;
      } while (w != targetWidth || h != targetHeight);

      return scaledImage;
   }

}

