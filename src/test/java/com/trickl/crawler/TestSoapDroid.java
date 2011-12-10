package com.trickl.crawler;

import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.xml.bind.DefaultNamespace;
import com.trickl.crawler.robot.soap.SimpleSoapTask;
import com.trickl.crawler.robot.soap.SoapDroid;
import com.trickl.crawler.robot.soap.SoapTask;
import java.io.IOException;

import java.net.URI;
import java.util.logging.*;
import org.apache.droids.exception.DroidsException;
import org.cara.webcarasearch.wsdl.GetTitleListByYearRatingFullWithPagination;
import org.cara.webcarasearch.wsdl.GetTitleListByYearRatingFullWithPaginationResponse;
import org.cara.webcarasearch.wsdl.GetTitleListStringFullWithPagination;
import org.cara.webcarasearch.wsdl.GetTitleListStringFullWithPaginationResponse;
import org.junit.BeforeClass;

import org.junit.Test;
import org.mpaa.Rating;
import org.springframework.ws.client.core.WebServiceTemplate;

public class TestSoapDroid {

   @BeforeClass
   public static void beforeClass()
   {
      System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
      System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
   }

   @Test
   public void requestMPAAFilmRatingByTitle() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      SoapDroid<SoapTask<GetTitleListStringFullWithPagination>,
              GetTitleListStringFullWithPagination,
              GetTitleListStringFullWithPaginationResponse> droid
              = new SoapDroid<SoapTask<GetTitleListStringFullWithPagination>,
              GetTitleListStringFullWithPagination,
              GetTitleListStringFullWithPaginationResponse>();
      droid.setContextPath("org.cara.webcarasearch.wsdl");
      DefaultNamespace defaultNamespaceMapper = new DefaultNamespace();
      defaultNamespaceMapper.setDefaultNamespaceUri("http://cara.org");
      droid.setNamespacePrefixMapper(defaultNamespaceMapper);
      WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
      droid.setWebServiceTemplate(webServiceTemplate);
      droid.setOutputHandler(new TaskResultHandler<SoapTask<GetTitleListStringFullWithPagination>,
              GetTitleListStringFullWithPaginationResponse>() {
         @Override
         public void handle(SoapTask<GetTitleListStringFullWithPagination> task,
                            GetTitleListStringFullWithPaginationResponse response) throws DroidsException, IOException {
            System.out.println(response.getGetTitleListStringFullWithPaginationResult());
         }
      });
  
      GetTitleListStringFullWithPagination request = new GetTitleListStringFullWithPagination();
      request.setSearch("Love Actually");
      request.setStartRow(1);
      request.setEndRow(25);
      URI service = new URI("http://cara.org/filmRatings_Cara/WebCaraSearch/service.asmx");
      URI action = new URI("http://cara.org/GetTitleListStringFullWithPagination");

      droid.getNewWorker().execute(new SimpleSoapTask(service, request, action));
   }

   @Test
   public void requestMPAAFilmRatingByYearAndRating() throws Exception {

      java.util.logging.Handler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.FINEST);

      SoapDroid<SoapTask<GetTitleListByYearRatingFullWithPagination>,
              GetTitleListByYearRatingFullWithPagination,
              GetTitleListByYearRatingFullWithPaginationResponse> droid
              = new SoapDroid<SoapTask<GetTitleListByYearRatingFullWithPagination>,
              GetTitleListByYearRatingFullWithPagination,
              GetTitleListByYearRatingFullWithPaginationResponse>();
      droid.setContextPath("org.cara.webcarasearch.wsdl");
      DefaultNamespace defaultNamespaceMapper = new DefaultNamespace();
      defaultNamespaceMapper.setDefaultNamespaceUri("http://cara.org");
      droid.setNamespacePrefixMapper(defaultNamespaceMapper);
      WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
      droid.setWebServiceTemplate(webServiceTemplate);
      droid.setOutputHandler(new TaskResultHandler<SoapTask<GetTitleListByYearRatingFullWithPagination>,
              GetTitleListByYearRatingFullWithPaginationResponse>() {
         @Override
         public void handle(SoapTask<GetTitleListByYearRatingFullWithPagination> task,
                            GetTitleListByYearRatingFullWithPaginationResponse response) throws DroidsException, IOException {
            System.out.println(response.getGetTitleListByYearRatingFullWithPaginationResult());
         }
      });

      GetTitleListByYearRatingFullWithPagination request = new GetTitleListByYearRatingFullWithPagination();
      request.setYear("2010");
      request.setRating(Rating.PG.toString());
      request.setStartRow(1);
      request.setEndRow(25);
      URI service = new URI("http://cara.org/filmRatings_Cara/WebCaraSearch/service.asmx");
      URI action = new URI("http://cara.org/GetTitleListByYearRatingFullWithPagination");
      droid.getNewWorker().execute(new SimpleSoapTask(service, request, action));
   }
}
