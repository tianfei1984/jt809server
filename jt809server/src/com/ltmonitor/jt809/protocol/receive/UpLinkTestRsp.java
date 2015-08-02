 package com.ltmonitor.jt809.protocol.receive;
 
 import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

 /**
  * 主链路连接保持应答
  * @author tianfei
  *
  */
 public class UpLinkTestRsp implements IReceiveProtocol
 {
   private static Logger logger = Logger.getLogger(Logger.class);
 
   public String handle(JT809Message message)
   {
     return "";
   }
 }

