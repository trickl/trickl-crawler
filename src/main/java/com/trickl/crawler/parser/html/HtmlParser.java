package com.trickl.crawler.parser.html;

import com.trickl.crawler.api.Parser;
import com.trickl.crawler.api.Task;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.droids.api.ContentEntity;
import org.apache.droids.api.Parse;
import org.apache.droids.exception.DroidsException;
import org.apache.droids.parse.ParseImpl;
import org.w3c.dom.Document;

public class HtmlParser implements Parser {

    private static final Logger logger = Logger.getLogger(HtmlParser.class.getCanonicalName());
    
    private final Collection<DocumentBuilder> documentBuilders;

    public HtmlParser(Collection<DocumentBuilder> documentBuilders) {
        this.documentBuilders = documentBuilders;
    }

    @Override
    public Parse parse(ContentEntity entity, Task newLink) throws DroidsException, IOException {

        Document document = null;
        InputStream stream = entity.obtainContent();
        try 
        {
            for (DocumentBuilder builder : documentBuilders) {
                if (document != null) {
                    // Reconvert the existing document back to a stream
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    Source xmlSource = new DOMSource(document);
                    Result outputTarget = new StreamResult(outputStream);
                    TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
                    stream.close();
                    stream = new ByteArrayInputStream(outputStream.toByteArray());
                    }
                }
                
                document = builder.build(stream);
            }
        } catch (TransformerException ex) {
            logger.log(Level.WARNING, "Unable to process URI " + newLink.getURI().toASCIIString(), ex);
        }
        finally {
            stream.close();
        }
        
        return new ParseImpl(newLink.getId(), document, null);
    }
}
