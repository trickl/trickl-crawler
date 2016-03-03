package com.trickl.crawler.protocol.http;

import org.apache.droids.protocol.http.DroidsHttpClient;
import org.apache.http.client.HttpRequestRetryHandler;

public class TaskHttpClient extends DroidsHttpClient
{
  @Override
  protected HttpRequestRetryHandler createHttpRequestRetryHandler()
  {
    return new TaskHttpRequestRetryHandler(3);
  }
}
