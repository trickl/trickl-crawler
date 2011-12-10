package com.trickl.crawler;

import com.trickl.crawler.robot.xslt.XsltDroid;
import com.trickl.crawler.handle.DocumentStreamHandler;
import com.trickl.crawler.handle.StreamPipeHandler;
import com.trickl.crawler.robot.http.HttpPostTask;
import com.trickl.crawler.robot.http.LinkTask;
import com.trickl.crawler.robot.http.SimpleHttpPostTask;
import com.trickl.crawler.robot.http.SimpleLinkTask;

import java.net.URI;
import java.util.HashMap;
import java.util.logging.*;

import org.junit.Test;
import org.junit.Ignore;

public class TestXsltDroid {

   @Ignore("Completed")
   @Test
   public void createSimpleCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);  

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/xslt-droid.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/xslt-droid.xsl");
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("classpath:/com/trickl/crawler/xslt-droid-index.html"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createFilmReleasesCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/film-releases.xsl");
      droid.setForceAllow(true);
      //droid.setXsltFile("classpath:/com/trickl/crawler/pass-through.xsl");
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.film-releases.com/film-release-schedule-2010.php"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createTechnoratiCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/technorati.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://technorati.com/blogs/directory/business/finance/"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createTechnoratiWalker() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/technorati.xsl");
      droid.setForceAllow(true);
      //droid.setXsltFile("classpath:/com/trickl/crawler/pass-through.xsl");
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://technorati.com/blogs/www.boygeniusreport.com"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createWikipediaCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      //droid.setXsltFile("classpath:/com/trickl/crawler/wikipedia-film.xsl");
      droid.setXsltFile("classpath:/com/trickl/crawler/pass-through.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      //FileOutputStream fout = new FileOutputStream("the-whole-ten-yards.xml");
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      //droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://en.wikipedia.org/wiki/Johnny_English"), 0));
      //droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://en.wikipedia.org/wiki/Jellyfish_%28film%29"), 0));
      //droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://en.wikipedia.org/wiki/Johanna_%28film%29"), 0));
      //droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://en.wikipedia.org/wiki/Joe_%28film%29"), 0));
     // droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://en.wikipedia.org/wiki/The_Awakening_%28film%29"), 0));
      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://en.wikipedia.org/wiki/Titanic_%28film%29"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createWikipediaSearcher() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/wikipedia-search.xsl");
      droid.setForceAllow(true);
      //droid.setXsltFile("classpath:/com/trickl/crawler/pass-through.xsl");
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://en.wikipedia.org/w/index.php?title=Special:Search&redirs=0&fulltext=Search&ns0=1&search=Iron+Man+(film)"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createBoxOfficeMojoCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/boxofficemojo.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.boxofficemojo.com/movies/?id=ironman.htm"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createBoxOfficeMojoWalker() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/boxofficemojo.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.boxofficemojo.com/movies/?id=ironman.htm"), 0));
   }

   @Ignore
   @Test
   public void createLoveFilmSearcher() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/lovefilm-search.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.lovefilm.com/search/results/?query=Harry+Potter+And+The+Half-Blood+Prince"), 0));
   }

   @Ignore("Need to support cookies")
   @Test
   public void createNetflixSearcher() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      // Ignore the robots.txt in this case       
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/pass-through.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);
      droid.setForceAllow(true);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.netflix.com/Search?v1=Harry+Potter+and+the+Half-Blood+Prince"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createAmazonSearcher() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/amazon-title-search.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.amazon.com/s/ref=nbsb_noss?url=search-alias=aps&field-keywords=Adam+Rib"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createIMDBSearcher() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/imdb-title-search.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.imdb.com/find?s=tt&q=harry+potter+and+the+half+blood+prince"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createIMDBCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/imdb-title-search.xsl");
      //droid.setXsltFile("classpath:/com/trickl/crawler/pass-through.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.imdb.com/find?s=all&q=Aces+Iron+Eagle+III"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createYahooSiteExplorer() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      // Ignore the robots.txt in this case      
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/yahoo-siteexplorer.xsl");
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);
      droid.setForceAllow(true);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://siteexplorer.search.yahoo.com/uk/search?p=http%3A%2F%2Fgizmodo.com&bwm=i&bwmo=d&bwmf=s"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createAlexaCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
            
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/alexa.xsl");
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);
      droid.setForceAllow(true);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.alexa.com/siteinfo/gizmodo.com"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createGoogleDirectorySearch() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      // Ignore the robots.txt in this case      
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/google-directorysearch.xsl");
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);
      droid.setForceAllow(true);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.google.com/search?q=www.gizmodo.com&btnG=Search+Directory&hl=en&cat=gwd%2FTop"), 0));
   }

   @Ignore("Completed")
   @Test
   public void createGoogleDirectoryCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();      
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      //droid.setXsltFile("classpath:/com/trickl/crawler/google-directorypage.xsl");
      droid.setXsltFile("classpath:/com/trickl/crawler/pass-through.xsl");
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);
      droid.setForceAllow(true);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("http://www.google.com/Top/Home/Consumer_Information/Electronics/Weblogs/?il=1"), 0));
   }
   
   @Ignore("Completed")
   @Test
   public void createNasdaqTraderCrawler() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      XsltDroid<HttpPostTask> droid = new XsltDroid<HttpPostTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/deny-all.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/nasdaqtrader_tradehalts.xsl");
      //droid.setXsltFile("classpath:/com/trickl/crawler/pass-through.xsl");
      droid.setForceAllow(true);
      DocumentStreamHandler outputHandler = new DocumentStreamHandler();
      outputHandler.setOutputHandler(new StreamPipeHandler(System.out));
      droid.setOutputHandler(outputHandler);
      HashMap<String, String> headerData = new HashMap<String, String>();
      headerData.put("Referer", "www.nasdaqtrader.com");
      headerData.put("Content-Type", "application/json");
      HashMap<String, Object> postData = new HashMap<String, Object>();
      postData.put("id", 1);
      postData.put("method", "BL_GenericGrid.GetTableBySP");
      postData.put("params", "[\"nsp_tradehalt_get_halt\"]");
      postData.put("version", "1.1");
      droid.getNewWorker().execute(new SimpleHttpPostTask(new URI("http://www.nasdaqtrader.com/RPCHandler.axd"), 
              headerData, postData));
   }
}
