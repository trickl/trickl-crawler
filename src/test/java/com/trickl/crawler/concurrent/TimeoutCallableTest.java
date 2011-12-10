package com.trickl.crawler.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Ignore;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.junit.Assert;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tgee
 */
public class TimeoutCallableTest {

   static class Counter implements Callable<Integer> {

      int maxCount = 0;
      int delayMs = 500;
      boolean print = true;

      Counter(int maxCount, int delayMs, boolean print) {
         this.maxCount = maxCount;
         this.delayMs = delayMs;
         this.print = print;
      }

      @Override
      public Integer call() throws Exception {
         int count = 0;
         for (count = 0; count < maxCount; ++count)
         {
            Thread.sleep(delayMs);
            if (print) {
               System.out.println("Count:" + (count + 1));
            }            
         }

         return count;
      }
   }

   public TimeoutCallableTest() {
   }

   @BeforeClass
   public static void setUpClass() throws Exception {
   }

   @AfterClass
   public static void tearDownClass() throws Exception {
   }

   @Test
   public void testCallWithSufficientTime() throws Exception {

      Counter counter = new Counter(20, 250, true);
      int threadPoolSize = 5;
      ExecutorService executorService = new ScheduledThreadPoolExecutor(threadPoolSize);
      TimeoutCallable timeoutCallable = new TimeoutCallable(counter, 30, TimeUnit.SECONDS, executorService);

      Future<Integer> future = executorService.submit(timeoutCallable);

      Thread.sleep(1000);
      executorService.shutdown();
      executorService.awaitTermination(15, TimeUnit.SECONDS);

      Assert.assertTrue(future.isDone());
      Integer result = future.get();
      Assert.assertNotNull(result);
      Assert.assertEquals(20, result.intValue());
   }

   @Test(expected=ExecutionException.class)
   public void testCallWithInsufficientTime() throws Exception {

      Counter counter = new Counter(20, 250, true);
      int threadPoolSize = 5;
      ExecutorService executorService = new ScheduledThreadPoolExecutor(threadPoolSize);
      TimeoutCallable timeoutCallable = new TimeoutCallable(counter, 4, TimeUnit.SECONDS, executorService);
      
      Future<Integer> future = executorService.submit(timeoutCallable);

      Thread.sleep(1000);
      executorService.shutdown();
      executorService.awaitTermination(15, TimeUnit.SECONDS);

      Assert.assertTrue(future.isDone());
      future.get();
   }
}