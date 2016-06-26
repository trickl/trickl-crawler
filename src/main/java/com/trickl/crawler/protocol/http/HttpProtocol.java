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
package com.trickl.crawler.protocol.http;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.droids.api.ManagedContentEntity;
import org.apache.droids.api.Protocol;
import org.apache.droids.norobots.NoRobotClient;
import org.apache.droids.norobots.NoRobotException;
import org.apache.droids.protocol.http.HttpClientContentLoader;
import org.apache.droids.protocol.http.HttpContentEntity;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpProtocol implements Protocol {

  private final Logger log = LoggerFactory.getLogger(HttpProtocol.class);
  
  private boolean forceAllow = false;
  private String method = HttpGet.METHOD_NAME;
  private Map<String, Object> postData = new HashMap<>();
  private Map<String, String> headerData = new HashMap<>();
  private final PoolingHttpClientConnectionManager connManager;
  private final CredentialsProvider credentialsProvider;
  private final RequestConfig requestConfig;
  private final RedirectStrategy redirectStrategy;

  
  public HttpProtocol() {
      // Client HTTP connection objects when fully initialized can be bound to
        // an arbitrary network socket. The process of network socket initialization,
        // its connection to a remote address and binding to a local one is controlled
        // by a connection socket factory.

        // SSL context for secure connections can be created either based on
        // system or application specific properties.
        SSLContext sslcontext = SSLContexts.createSystemDefault();

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslcontext))
            .build();

        // Create a connection manager with custom configuration.
        connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
            .setTcpNoDelay(true)
            .build();
        
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);
        
        // Validate connections after 1 sec of inactivity
        connManager.setValidateAfterInactivity(1000);
        
        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(10);
                
        // Use custom credentials provider if necessary.
        credentialsProvider = new BasicCredentialsProvider();
        
        // Create global request configuration
        requestConfig = RequestConfig.custom()
            .setCookieSpec(CookieSpecs.DEFAULT)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
            .build();    
        
        redirectStrategy = new TolerantRedirectStrategy();
  }

  @Override
  public ManagedContentEntity load(URI uri) throws IOException {    
    HttpRequestBase httpRequest;
    
    if (method.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
        HttpPost httpPost = new HttpPost(uri);
       
        // Add header data
        headerData.entrySet().stream().forEach((headerDataEntry) -> {
            httpPost.setHeader(headerDataEntry.getKey(), headerDataEntry.getValue());
        });
        
        // Add post data
        String contentType = headerData.get("Content-Type");
        if (contentType == null || "application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {           
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            postData.entrySet().stream().forEach((postDataEntry) -> {
                nameValuePairs.add(new BasicNameValuePair(postDataEntry.getKey(), postDataEntry.getValue().toString()));
            });
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        else if ("application/json".equalsIgnoreCase(contentType)) {
           ObjectMapper mapper = new ObjectMapper();         
           StringEntity se;
            try {
               String jsonString = mapper.writeValueAsString(postData);
               se = new StringEntity(jsonString);
               httpPost.setEntity(se);
            } catch (JsonGenerationException | JsonMappingException ex) {
               log.error("Failed to generate JSON.", ex);
            }
        }
        httpRequest = httpPost;
    }
    else {
       httpRequest = new HttpGet(uri);
    }            
    
    // Create an HttpClient with the given custom dependencies and configuration.
    try (CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(connManager)
            .setConnectionManagerShared(true)
            .setDefaultCredentialsProvider(credentialsProvider)
            .setDefaultRequestConfig(requestConfig)
            .setRedirectStrategy(redirectStrategy)
            .build())
    {
        HttpResponse response = httpClient.execute(httpRequest);
        StatusLine statusline = response.getStatusLine();
        if (statusline.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
          httpRequest.abort();
          throw new HttpResponseException(
              statusline.getStatusCode(), statusline.getReasonPhrase());
        }
        HttpEntity entity = response.getEntity();
        if (entity == null) {
          // Should _almost_ never happen with HTTP GET requests.
          throw new ClientProtocolException("Empty entity");
        }
        
        return new HttpContentEntity(entity, 0);
    }
  }

  @Override
  public boolean isAllowed(URI uri) throws IOException {
    if (forceAllow) {
      return forceAllow;
    }

    URI baseURI;
    try {
      baseURI = new URI(
          uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), 
          "/", null, null);
    } catch (URISyntaxException ex) {
      log.error("Unable to determine base URI for " + uri);
      return false;
    }
    
    try (CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(connManager)
            .setConnectionManagerShared(true)
            .setDefaultCredentialsProvider(credentialsProvider)
            .setDefaultRequestConfig(requestConfig)
            .setRedirectStrategy(redirectStrategy)
            .build())
    {    
        NoRobotClient nrc = new NoRobotClient(new HttpClientContentLoader(httpClient), "Apache-Droids/1.1 (java 1.8)");
        try {
          nrc.parse(baseURI);
        } catch (NoRobotException ex) {
          log.error("Failure parsing robots.txt: " + ex.getMessage());
          return false;
        }
        boolean test = nrc.isUrlAllowed(uri);
        if (log.isInfoEnabled()) {
          log.info(uri + " is " + (test ? "allowed" : "denied"));
        }

        return test;
    }
  }

  /**
   * You can force that a site is allowed (ignoring the robots.txt). This should
   * only be used on server that you control and where you have the permission
   * to ignore the robots.txt.
   * 
   * @return <code>true</code> if you are rude and ignore robots.txt.
   *         <code>false</code> if you are playing nice.
   */
  public boolean isForceAllow() {
    return forceAllow;
  }

  /**
   * You can force that a site is allowed (ignoring the robot.txt). This should
   * only be used on server that you control and where you have the permission
   * to ignore the robots.txt.
   * 
   * @param forceAllow
   *                if you want to force an allow and ignore the robot.txt set
   *                to <code>true</code>. If you want to obey the rules and
   *                be polite set to <code>false</code>.
   */
  public void setForceAllow(boolean forceAllow) {
    this.forceAllow = forceAllow;
  }
  
   /**
    * @return the method
    */
   public String getMethod() {
      return method;
   }

   /**
    * @param method the method to set
    */
   public void setMethod(String method) {
      this.method = method;
   }

   /**
    * @return the postData
    */
   public Map<String, Object> getPostData() {
      return postData;
   }

   /**
    * @param postData the postData to set
    */
   public void setPostData(Map<String, Object> postData) {
      this.postData = postData;
   }

   public void setHeaderData(Map<String, String> headerData) {
      this.headerData = headerData;
   }
}
