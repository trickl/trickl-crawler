package com.trickl.model.xml.adapter;

import java.net.URL;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlURLAdapter extends XmlAdapter<String,URL>
{
   public URL unmarshal(String val) throws Exception
   {
      return (val == null || val.equals("")) ? null : new URL(val);
   }

   public String marshal(URL val) throws Exception
   {
      return val == null ? null : val.toString();
   }
}

