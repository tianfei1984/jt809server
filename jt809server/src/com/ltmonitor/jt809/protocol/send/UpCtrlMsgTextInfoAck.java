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

 
 public class UpCtrlMsgTextInfoAck
   implements ISendProtocol
 {
   private static Logger logger = Logger.getLogger(UpCtrlMsgTextInfoAck.class);
   private int msgType = 0x1500;
	private int subType = 0x1503;
	private String plateNo;
	private int plateColor;
	private long msgId; //对应下发报文请求消息中的MSG_ID
	private byte result;

	public UpCtrlMsgTextInfoAck(String plateNo, int plateColor, long msgId, byte result) {
		this.plateNo = plateNo;
		this.plateColor = plateColor;
		this.msgId = msgId;
		this.result = result;
	}

	public JT809Message wrapper() {
		int dataLength = 4 +1;
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor,1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(msgId, 4))
				.append(Tools.ToHexString(result, 1));

		ParameterModel pm = GlobalConfig.parModel;
		String body = sb.toString();

		JT809Message mm = new JT809Message(msgType, subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}
   
 }

