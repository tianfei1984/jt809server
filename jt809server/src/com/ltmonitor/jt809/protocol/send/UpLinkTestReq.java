 package com.ltmonitor.jt809.protocol.send;
 
 import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.server.PlatformClient;
import com.ltmonitor.jt809.tool.Tools;

 
 public class UpLinkTestReq
   implements ISendProtocol
 {
   private static Logger logger = Logger.getLogger(UpLinkTestReq.class);
 
   private int msgType = 0x1005;
   public JT809Message wrapper() {
     String body = "";
     return new JT809Message(msgType);
   }
 }

