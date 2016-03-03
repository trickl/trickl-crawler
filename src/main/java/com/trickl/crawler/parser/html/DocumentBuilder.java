package com.trickl.crawler.parser.html;

import java.io.InputStream;
import org.apache.droids.exception.DroidsException;
import org.w3c.dom.Document;

public interface DocumentBuilder {
    Document build(InputStream stream) throws DroidsException;
}
