 package com.ltmonitor.jt809.protocol.receive;
 
 import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

 
 public class UpWarnMsgUrgeTodoAck
   implements IReceiveProtocol
 {
   private Logger logger = Logger.getLogger(UpWarnMsgUrgeTodoAck.class);
 
   public String handle(JT809Message message)
   {
     String strResult = message.getMessageBody();
     String RunBusNo = strResult.substring(0, 42);
     int RunBusColor = Integer.parseInt(strResult.substring(42, 2));
     String DataType = strResult.substring(44, 4);
     String DataLength = strResult.substring(48, 8);
 
     return null;
   }
 }

