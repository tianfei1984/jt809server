 package com.ltmonitor.jt809.protocol.send;
 
 import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ProtocolModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;
import com.ltmonitor.jt809.tool.xml.ClassUtils;

 
 public class UpCtrlMsg
   implements ISendProtocol
 {
   private static Logger logger = Logger.getLogger(UpCtrlMsg.class);
 
   public JT809Message wrapper()
   {
     String outPut = "";
 
     String Vehicle_no = "";
     int Vehicle_color = 0;
    
 
     String vehicleNo_P = Vehicle_no;
     String turnVehicleNo_P = Tools.TurnISN(vehicleNo_P);
     String endVehicleNo_P = Tools.turnStrLength(turnVehicleNo_P, 21);
 
     int vehicleColor_P = Vehicle_color;
     String turnVehicleColor_P = Integer.toHexString(vehicleColor_P);
     String endVehicleColor_P = Tools.turnDataLength(turnVehicleColor_P, 1);
 
     String dataType_P = "1500";
 
     int dataLength_P = 29;
     String turnDataLength_P = Integer.toHexString(dataLength_P);
     String endDataLength_P = Tools.turnDataLength(turnDataLength_P, 4);
     String mess;
     if (GlobalConfig.protocolMap.containsKey(Integer.valueOf(1505)))
     {
       ISendProtocol protocolClass = (ISendProtocol)
         ClassUtils.getBean(((ProtocolModel)GlobalConfig.protocolMap.get(Integer.valueOf(1505))).getName());
       //mess = protocolClass.ResolveHandle();
     }
     else
     {
       if (GlobalConfig.protocolMap.containsKey(Integer.valueOf(1501)))
       {
         ISendProtocol protocolClass = (ISendProtocol)
           ClassUtils.getBean(((ProtocolModel)GlobalConfig.protocolMap.get(Integer.valueOf(1501))).getName());
         //mess = protocolClass.ResolveHandle();
       } else if (GlobalConfig.protocolMap.containsKey(Integer.valueOf(1503)))
       {
         ISendProtocol protocolClass = (ISendProtocol)
           ClassUtils.getBean(((ProtocolModel)GlobalConfig.protocolMap.get(Integer.valueOf(1503))).getName());
         //mess = protocolClass.ResolveHandle();
       }
     }
     String parentDataBody = endVehicleNo_P + endVehicleColor_P + dataType_P + 
       endDataLength_P;
     return null;
   }
 }

