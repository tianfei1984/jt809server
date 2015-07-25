 package com.ltmonitor.jt809.protocol.receive;
 
 import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

 
 public class DownDisconnectInform
   implements IReceiveProtocol
 {
   private static Logger logger = Logger.getLogger(DownDisconnectInform.class);
 
   public String handle(JT809Message message)
   {
     String content = message.getMessageBody();
     int errorCode = Integer.valueOf(content.substring(0, 2), 16).intValue();
     switch (errorCode) {
     case 0:
       logger.warn("错误代码00：无法连接下级平台指定的服务IP与端口！");
       break;
     case 1:
       logger.warn("错误代码01：上级平台客户端与下级平台服务端断开！");
       break;
     case 2:
       logger.warn("错误代码02：其他原因！");
       break;
     default:
       logger.warn("错误代码" + errorCode);
     }
 
     return "";
   }
 }

