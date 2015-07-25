 package com.ltmonitor.jt809.protocol.receive;
 
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

 
 public class UpDissconnectRsp
   implements IReceiveProtocol
 {
   private Logger logger = Logger.getLogger(UpDissconnectRsp.class);
 
   public String handle(JT809Message message) {
     String strResult = message.getMessageBody();
     return null;
   }
 }

