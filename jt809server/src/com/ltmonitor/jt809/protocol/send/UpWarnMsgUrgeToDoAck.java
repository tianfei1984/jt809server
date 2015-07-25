package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;

/**
 * ±¨¾¯¶½°ìÓ¦´ð1401
 * @author DELL
 *
 */
public class UpWarnMsgUrgeToDoAck implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpWarnMsgUrgeToDoAck.class);
	private int msgType = 0x1400;
	private int subType = 0x1401;
	private String plateNo;
	private int plateColor;
	private int superviseId;
	private byte result;

	public UpWarnMsgUrgeToDoAck(String plateNo, int plateColor, int superviseId, byte result) {
		this.plateNo = plateNo;
		this.plateColor = plateColor;
		this.superviseId = superviseId;
		this.result = result;
	}

	public JT809Message wrapper() {
		int dataLength = 4 + 1;
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor,1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(superviseId, 4))
				.append(Tools.ToHexString(result,1));

		String body = sb.toString();

		JT809Message mm = new JT809Message(msgType, subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}
}
