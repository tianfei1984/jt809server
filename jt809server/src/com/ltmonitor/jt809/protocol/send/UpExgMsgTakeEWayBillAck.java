package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


/**
 * 上报电子运单请求消息应答 120B
 * @author DELL
 *
 */
public class UpExgMsgTakeEWayBillAck implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpExgMsgTakeEWayBillAck.class);
	private int msgType = 0x1200;
	private int subType = 0x120B;
	private String plateNo;
	private int plateColor;
	private String ebillContent;

	public UpExgMsgTakeEWayBillAck(String plateNo, int plateColor, String ebillContent) {
		this.plateNo = plateNo;
		this.plateColor = plateColor;
		this.ebillContent = ebillContent;
	}

	public JT809Message wrapper() {
		String content = Tools.ToHexString(ebillContent);
		int contentLength = content.length() / 2;
		int dataLength = 4 + contentLength;
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor, 1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(contentLength, 4))
				.append(content);

		String body = sb.toString();

		JT809Message mm = new JT809Message(msgType,  subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}

}
