 package com.ltmonitor.jt809.protocol.receive;
 
 import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

 
 public class DownDisconnectReq
   implements IReceiveProtocol
 {
   private static Logger logger = Logger.getLogger(DownDisconnectReq.class);
 
   public String handle(JT809Message message)
   { 
     String messageBody = message.getMessageBody();
 
     int verifyCode = Integer.parseInt(messageBody.substring(0, 8));
     
     message.setMsgDescr("" + verifyCode);
     
     T809Manager.DownDisconnectRsp();
     
     T809Manager.setSubLinkState(false, "上级平台主动注销");
     
     return null;
   }
 }

