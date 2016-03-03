package com.trickl.crawler.protocol.http;

import java.io.IOException;
import java.io.InterruptedIOException;
  import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import com.google.common.base.Preconditions;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.protocol.HttpContext;

class TaskHttpRequestRetryHandler implements HttpRequestRetryHandler
{

  private final int retryCount;

  public TaskHttpRequestRetryHandler(int retryCount)
  {
    super();
    this.retryCount = retryCount;
  }

  @Override
  public boolean retryRequest(final IOException exception, int executionCount,
      final HttpContext context)
  {
    Preconditions.checkArgument(exception != null, "Exception parameter may not be null" );
    Preconditions.checkArgument(context != null, "HTTP context may not be null" );
    
    if (executionCount > this.retryCount) {
      // Do not retry if over max retry count
      return false;
    }
    if (exception instanceof NoHttpResponseException) {
      // Cannot connect
      return false;
    }
    if (exception instanceof InterruptedIOException) {
      // Timeout
      return false;
    }
    if (exception instanceof UnknownHostException) {
      // Unknown host
      return false;
    }
    if (exception instanceof HttpHostConnectException) {
      // Connection refused
      return false;
    }
    if (exception instanceof SSLHandshakeException) {
      // SSL handshake exception
      return false;
    }
    // otherwise retry
    return true;
  }

}
