package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


/**
 * 上报行车记录仪消息应答
 * 
 * @author DELL
 * 
 */
public class UpCtrlMsgTakeTravelAck implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpCtrlMsgTakeTravelAck.class);
	private int msgType = 0x1500;
	private int subType = 0x1504;
	private String plateNo;
	private int plateColor;
	private byte cmdType;
	private byte[] cmdData;

	public UpCtrlMsgTakeTravelAck(String plateNo, int plateColor, byte cmdType, byte[] _cmdData) {
		this.plateNo = plateNo;
		this.plateColor = plateColor;
		this.cmdType = cmdType;
		this.cmdData = _cmdData;
	}

	public JT809Message wrapper() {
		//String body = getBody();
		//body = Tools.TurnISN(body);
		String body = Tools.ToHexString(cmdData);
		int bodyLen = body.length() / 2;
		int dataLength = 4 + 1 + bodyLen;
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor, 1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(cmdType, 1))
				.append(Tools.ToHexString(bodyLen, 4))
				.append(body);

		body = sb.toString();

		JT809Message mm = new JT809Message(msgType, subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}


}
