 package com.ltmonitor.jt809.protocol.send;
 
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.ISendProtocol;

 
 public class DownConnectRsp
   implements ISendProtocol
 {
   public JT809Message wrapper()
   {
     String dataBody = null;
 
     String error_code = "00";
     dataBody = error_code;
 
     return new JT809Message(0x9002, dataBody);
   }
 }

