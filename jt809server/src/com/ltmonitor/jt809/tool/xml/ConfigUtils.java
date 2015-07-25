 package com.ltmonitor.jt809.tool.xml;
 
 import java.util.HashMap;
 import java.util.List;
 import org.dom4j.Document;
 import org.dom4j.Element;

import com.ltmonitor.jt809.model.ProtocolModel;
 
 public class ConfigUtils
 {
   private static String NODE_BEAN = "bean";
 
   private static String ATRRIBUTE_TYPE = "type";
 
   private static String ATRRIBUTE_CLASS = "class";
 
   public static HashMap<Integer, ProtocolModel> getConfig(String url) {
     HashMap<Integer, ProtocolModel> configMap = new HashMap<Integer, ProtocolModel>();
     Document doc = XmlUtils.parseXml(url);
     if (doc != null) {
       Element rootElement = doc.getRootElement();
       List list = rootElement.selectNodes("//" + NODE_BEAN);
 
       int i = 0; for (int count = list.size(); i < count; i++) {
         Element beanElement = (Element)list.get(i);
         ProtocolModel pm = new ProtocolModel();
         pm.setType(Integer.valueOf(beanElement.attributeValue(ATRRIBUTE_TYPE), 16).intValue());
 
         pm.setName(beanElement.attributeValue(ATRRIBUTE_CLASS));
         configMap.put(Integer.valueOf(pm.getType()), pm);
       }
     }
     return configMap;
   }
 
   public static HashMap<Integer, String> getCmdConfig(String url)
   {
     HashMap configMap = new HashMap();
     Document doc = XmlUtils.parseXml(url);
     if (doc != null) {
       Element rootElement = doc.getRootElement();
       List list = rootElement.selectNodes("//bean");
       int i = 0; for (int count = list.size(); i < count; i++) {
         Element beanElement = (Element)list.get(i);
         configMap.put(Integer.valueOf(beanElement.attributeValue("type")), 
           beanElement.attributeValue("class"));
       }
     }
     return configMap;
   }
 }

