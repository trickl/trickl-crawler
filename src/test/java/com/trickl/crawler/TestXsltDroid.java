package com.trickl.crawler;

import com.trickl.crawler.api.Task;
import com.trickl.crawler.handle.InputOutputStream;
import com.trickl.crawler.handle.SourceStreamHandler;
import com.trickl.crawler.robot.http.SimpleLinkTask;
import com.trickl.crawler.robot.xslt.XsltDroid;
import java.net.URI;
import org.junit.Test;

public class TestXsltDroid {

   @Test
   public void createSimpleCrawler() throws Exception {

      XsltDroid<Task> droid = new XsltDroid<>();
      
      droid.setRegexURLFile("classpath:/com/trickl/crawler/xslt-droid.filter");
      droid.setXsltFile("classpath:/com/trickl/crawler/xslt-droid.xsl");
      SourceStreamHandler outputHandler = new SourceStreamHandler();      
      outputHandler.setOutputHandler(new InputOutputStream(System.out));
      droid.setOutputHandler(outputHandler);

      droid.getNewWorker().execute(new SimpleLinkTask(null, new URI("classpath:/com/trickl/crawler/xslt-droid-index.html"), 0));
   }
}
