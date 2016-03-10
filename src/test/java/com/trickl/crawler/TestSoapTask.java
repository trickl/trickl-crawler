package com.trickl.crawler;

import com.trickl.crawler.handle.JaxbHandler;
import com.trickl.crawler.handle.TaskResultHandler;
import com.trickl.crawler.robot.StandardDroid;
import com.trickl.crawler.xml.bind.DefaultNamespace;
import com.trickl.crawler.robot.soap.SimpleSoapTask;
import java.io.IOException;

import java.net.URI;
import org.apache.droids.exception.DroidsException;
import org.cara.webcarasearch.wsdl.GetTitleListByYearRatingFullWithPagination;
import org.cara.webcarasearch.wsdl.GetTitleListByYearRatingFullWithPaginationResponse;
import org.cara.webcarasearch.wsdl.GetTitleListStringFullWithPagination;
import org.cara.webcarasearch.wsdl.GetTitleListStringFullWithPaginationResponse;
import org.junit.BeforeClass;

import org.junit.Test;
import org.mpaa.Rating;
import org.springframework.ws.client.core.WebServiceTemplate;

public class TestSoapTask {

   @BeforeClass
   public static void beforeClass()
   {
      System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
      System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "trace");
   }

   @Test
   public void requestMPAAFilmRatingByTitle() throws Exception {

      StandardDroid droid = new StandardDroid();
      
      // Define the handler
      JaxbHandler<SimpleSoapTask<GetTitleListStringFullWithPagination>, GetTitleListStringFullWithPaginationResponse> jaxbHandler
              = new JaxbHandler<SimpleSoapTask<GetTitleListStringFullWithPagination>, GetTitleListStringFullWithPaginationResponse>();
      jaxbHandler.setOutputHandler(new TaskResultHandler<SimpleSoapTask<GetTitleListStringFullWithPagination>,
              GetTitleListStringFullWithPaginationResponse>() {
         @Override
         public void handle(SimpleSoapTask<GetTitleListStringFullWithPagination> task,
                            GetTitleListStringFullWithPaginationResponse response) throws DroidsException, IOException {
            System.out.println(response.getGetTitleListStringFullWithPaginationResult());
         }
      });
  
      // Define the task
      SimpleSoapTask<GetTitleListStringFullWithPagination> soapTask = new SimpleSoapTask<GetTitleListStringFullWithPagination>();      
      GetTitleListStringFullWithPagination request = new GetTitleListStringFullWithPagination();
      request.setSearch("Love Actually");
      request.setStartRow(1);
      request.setEndRow(25);
      URI service = new URI("http://cara.org/filmRatings_Cara/WebCaraSearch/service.asmx");
      URI action = new URI("http://cara.org/GetTitleListStringFullWithPagination");
      soapTask.setUri(service);
      soapTask.setAction(action);
      soapTask.setContextPath("org.cara.webcarasearch.wsdl");      
      DefaultNamespace defaultNamespaceMapper = new DefaultNamespace();
      defaultNamespaceMapper.setDefaultNamespaceUri("http://cara.org");
      soapTask.setNamespacePrefixMapper(defaultNamespaceMapper);
      WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
      soapTask.setWebServiceTemplate(webServiceTemplate);
      soapTask.setRequest(request);

      droid.getNewWorker().execute(soapTask);
   }

   @Test
   public void requestMPAAFilmRatingByYearAndRating() throws Exception {

      StandardDroid droid = new StandardDroid();
      
      // Define the handler
      JaxbHandler<SimpleSoapTask<GetTitleListByYearRatingFullWithPagination>, GetTitleListByYearRatingFullWithPaginationResponse> jaxbHandler
              = new JaxbHandler<SimpleSoapTask<GetTitleListByYearRatingFullWithPagination>, GetTitleListByYearRatingFullWithPaginationResponse>();
      jaxbHandler.setOutputHandler(new TaskResultHandler<SimpleSoapTask<GetTitleListByYearRatingFullWithPagination>,
              GetTitleListByYearRatingFullWithPaginationResponse>() {
         @Override
         public void handle(SimpleSoapTask<GetTitleListByYearRatingFullWithPagination> task,
                            GetTitleListByYearRatingFullWithPaginationResponse response) throws DroidsException, IOException {
            System.out.println(response.getGetTitleListByYearRatingFullWithPaginationResult());
         }
      });
  
      // Define the task
      SimpleSoapTask<GetTitleListByYearRatingFullWithPagination> soapTask = new SimpleSoapTask<GetTitleListByYearRatingFullWithPagination>();      
      GetTitleListByYearRatingFullWithPagination request = new GetTitleListByYearRatingFullWithPagination();
      request.setYear("2010");
      request.setRating(Rating.PG.toString());
      request.setStartRow(1);
      request.setEndRow(25);
      URI service = new URI("http://cara.org/filmRatings_Cara/WebCaraSearch/service.asmx");
      URI action = new URI("http://cara.org/GetTitleListByYearRatingFullWithPagination");
      soapTask.setUri(service);
      soapTask.setAction(action);
      soapTask.setContextPath("org.cara.webcarasearch.wsdl");      
      DefaultNamespace defaultNamespaceMapper = new DefaultNamespace();
      defaultNamespaceMapper.setDefaultNamespaceUri("http://cara.org");
      soapTask.setNamespacePrefixMapper(defaultNamespaceMapper);
      WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
      soapTask.setWebServiceTemplate(webServiceTemplate);
      soapTask.setRequest(request);

      droid.getNewWorker().execute(soapTask);
   }
}
