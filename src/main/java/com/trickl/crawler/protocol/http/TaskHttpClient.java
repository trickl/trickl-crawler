package com.trickl.crawler.protocol.http;

import org.apache.droids.protocol.http.DroidsHttpClient;
import static org.apache.droids.protocol.http.DroidsHttpClient.MAX_BODY_LENGTH;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.params.HttpParams;

public class TaskHttpClient extends DroidsHttpClient
{
  @Override
  protected HttpRequestRetryHandler createHttpRequestRetryHandler()
  {
    return new TaskHttpRequestRetryHandler(3);
  }
  
  @Override
  protected HttpParams createHttpParams()
  {
    HttpParams params = super.createHttpParams();
    params.setLongParameter(MAX_BODY_LENGTH, 2 * 1024 * 1024); // 2Mb
    return params;
  }
}
