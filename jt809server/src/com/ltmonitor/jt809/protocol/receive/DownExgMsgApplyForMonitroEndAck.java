 package com.ltmonitor.jt809.protocol.receive;
 
 import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

 
 public class DownExgMsgApplyForMonitroEndAck
   implements IReceiveProtocol
 {
   private static Logger logger = Logger.getLogger(DownExgMsgApplyForMonitroEndAck.class);
 
   public String handle(JT809Message message)
   {
     String dataBody = message.getMessageBody(); 
     //子数据头长度是28. 
     int result = Integer.parseInt(dataBody.substring(56, 58));
     
     String descr = "";
     if(result == 0)
    	 descr = "取消申请成功";
     else if(result == 1)
         descr = "之前没有对应申请";
     else if(result == 2)
         descr = "未知错误";
     
     message.setMsgDescr(descr);
     return null;
   }
 }

