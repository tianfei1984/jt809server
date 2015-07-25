package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;

/**
 * 平台信息应答1302
 * @author DELL
 *
 */
public class UpPlatFormMsgInfoAck implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpPlatFormMsgInfoAck.class);
	private int msgType = 0x1300;
	private int subType = 0x1302;
	private int infoId;

	public UpPlatFormMsgInfoAck(int infoId) {
		this.infoId = infoId;

	}

	public JT809Message wrapper() {
		int dataLength = 4;// 后续定位数据长度
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(infoId, 4));
		String body = sb.toString(); 

		JT809Message mm = new JT809Message(msgType,  subType,body);
		return mm;
	}
}
