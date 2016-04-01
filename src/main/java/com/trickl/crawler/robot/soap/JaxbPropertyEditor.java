package com.trickl.crawler.robot.soap;

import com.trickl.crawler.xml.bind.DefaultNamespace;
import java.beans.PropertyEditorSupport;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

@SuppressWarnings(value = "unchecked")
public class JaxbPropertyEditor extends PropertyEditorSupport {

   private final Marshaller marshaller;
   private final Unmarshaller unmarshaller;

   public JaxbPropertyEditor(String contextPath, DefaultNamespace defaultNamespace) throws JAXBException {             
        JAXBContext context = JAXBContext.newInstance(contextPath);
        marshaller = context.createMarshaller();
        marshaller.setProperty(DefaultNamespace.PROPERTY_NAME, defaultNamespace);
        unmarshaller = context.createUnmarshaller();        
   }

   @Override
   public String getAsText() {
      StringWriter writer = new StringWriter();
      try {
         marshaller.marshal(getValue(), writer);
      } catch (JAXBException ex) {
         return "<error/>";
      }
      return writer.toString();
   }

   @Override
   public void setAsText(String text) throws IllegalArgumentException {
      Object jaxbObject = null;
      
      try (StringReader reader = new StringReader(text)) {      
         jaxbObject = unmarshaller.unmarshal(reader);
      } catch (JAXBException ex) {
         jaxbObject = null;
      }
      setValue(jaxbObject);
   }
}
