 package com.ltmonitor.jt809.protocol.send;
 
 import java.util.ArrayList;
import java.util.Map;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ProtocolModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;
import com.ltmonitor.jt809.tool.xml.ClassUtils;

 
 public class UpExgMsg
   implements ISendProtocol
 {
   private int flag;
 
   public int getFlag()
   {
     return this.flag;
   }
 
   public void setFlag(int flag) {
     this.flag = flag;
   }
 
   public JT809Message wrapper()
   {
     Map busM = GlobalConfig.vehicleMap;
 
     int dataLength_P = 29;
     String turnDataLength_P = Integer.toHexString(dataLength_P);
     String endDataLength_P = Tools.turnDataLength(turnDataLength_P, 4);
     ArrayList arrayList = null;
     String mess = "";
     if (GlobalConfig.protocolMap.containsKey(Integer.valueOf(getFlag())))
     {
       ISendProtocol protocolClass = (ISendProtocol)
         ClassUtils.getBean(((ProtocolModel)GlobalConfig.protocolMap.get(Integer.valueOf(getFlag()))).getName());
       //mess = protocolClass.ResolveHandle();
     }
     return null;
   }
 }

