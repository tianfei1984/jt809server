 package com.ltmonitor.jt809.protocol.send;
 
 import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;

 /**
  * 应急接入应答 1505
  * @author DELL
  *
  */
 public class UpCtrlMsgEmergencyMonitoringAck
   implements ISendProtocol
 {
   private static Logger logger = Logger.getLogger(UpCtrlMsgEmergencyMonitoringAck.class);

	private int msgType = 0x1500;
	private int subType = 0x1505;
	private String plateNo;
	private int plateColor;

	private byte ackResult;

	public UpCtrlMsgEmergencyMonitoringAck(String plateNo, int plateColor,byte ackResult) {
		this.plateNo = plateNo;
		this.plateColor = plateColor;
		this.ackResult = ackResult;
	}

	public JT809Message wrapper() {
		int dataLength = 1;
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor, 1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(ackResult, 1));

		//ParameterModel pm = GNSSImpl.parModel;
		String body = sb.toString();
		//String mess = Tools.getHeaderAndFlag(GNSSImpl.getSN(), body, msgType,
				//pm.getPlatformCenterId());
		JT809Message mm = new JT809Message(msgType, subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}

 }

