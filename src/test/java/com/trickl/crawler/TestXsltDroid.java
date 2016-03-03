package com.trickl.crawler;

import com.trickl.crawler.handle.InputOutputStream;
import com.trickl.crawler.handle.SourceStreamHandler;
import com.trickl.crawler.robot.http.LinkTask;
import com.trickl.crawler.robot.http.SimpleLinkTask;
import com.trickl.crawler.robot.xslt.XsltDroid;
import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import org.junit.Ignore;
import org.junit.Test;

public class TestXsltDroid {

   @Test
   public void createSimpleCrawler() throws Exception {

      XsltDroid<LinkTask> droid = new XsltDroid<LinkTask>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/xslt-droid.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/xslt-droid.xsl");
      SourceStreamHandler outputHandler = new SourceStreamHandler();      
      outputHandler.setOutputHandler(new InputOutputStream(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("classpath:/com/trickl/crawler/xslt-droid-index.html"), 0));
   }
}
