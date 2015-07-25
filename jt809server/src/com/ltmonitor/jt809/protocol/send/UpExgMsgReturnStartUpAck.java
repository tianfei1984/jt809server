package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


public class UpExgMsgReturnStartUpAck implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpExgMsgReturnStartUpAck.class);
	private int msgType = 0x1200;
	private int subType = 0x1205;
	private String plateNo;
	private int plateColor;

	public UpExgMsgReturnStartUpAck(String plateNo, int plateColor) {
		this.plateNo = plateNo;
		this.plateColor = plateColor;
	}

	public JT809Message wrapper() {
		int dataLength = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor,1)).append(Tools.ToHexString(subType,2))
				.append(Tools.ToHexString(dataLength, 4))
				;

		ParameterModel pm = GlobalConfig.parModel;
		String body = sb.toString();

		JT809Message mm = new JT809Message(msgType,  subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}
}
