 package com.ltmonitor.jt809.protocol.receive;
 
 import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

 
 public class DownExgMsgApplyForMonitorStartupAck
   implements IReceiveProtocol
 {
   private Logger logger = Logger.getLogger(DownExgMsgApplyForMonitorStartupAck.class);
 
   public String handle(JT809Message message)
   {
     String dataBody = message.getMessageBody(); 
     //子数据头长度是28. 
     int result = Integer.parseInt(dataBody.substring(56, 58));
     
     String descr = "";
     if(result == 0)
    	 descr = "申请成功";
     else if(result == 1)
         descr = "上级平台没有该车辆";
     else if(result == 2)
         descr = "申请时间段错误";
     else if(result == 3)
         descr = "未知错误";
     
     message.setMsgDescr(descr);
     
     return "";
   }

 }

