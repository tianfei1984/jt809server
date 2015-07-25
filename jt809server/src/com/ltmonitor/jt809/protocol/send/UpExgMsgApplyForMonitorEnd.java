package com.ltmonitor.jt809.protocol.send;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.model.VehicleModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


public class UpExgMsgApplyForMonitorEnd implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpExgMsgApplyForMonitorEnd.class);
	private int msgType = 0x1200;
	private int subType = 0x1208;
	private String plateNo;
	private Byte plateColor;

	public UpExgMsgApplyForMonitorEnd(String PlateNo, Byte PlateColor) {
		plateNo = PlateNo;
		plateColor = PlateColor;
	}

	public JT809Message wrapper() {
		int dataLength =  0; //后续数据体长度

		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
		.append(Tools.ToHexString(plateColor))
		.append(Tools.ToHexString(subType, 2))
		.append(Tools.ToHexString(dataLength, 4));

		ParameterModel pm = GlobalConfig.parModel;
		String body = sb.toString();

		JT809Message mm = new JT809Message(msgType, subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}

}
