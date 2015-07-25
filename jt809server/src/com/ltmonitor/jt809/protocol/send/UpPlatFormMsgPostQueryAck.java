package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.entity.CheckRecord;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;

/**
 * 平台查岗应答
 * @author DELL
 *
 */
public class UpPlatFormMsgPostQueryAck implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpPlatFormMsgPostQueryAck.class);
	private CheckRecord chagang;
	private int msgType = 0x1300;
	private int subType = 0x1301;

	public UpPlatFormMsgPostQueryAck(CheckRecord pm) {
		this.chagang = pm;

	}

	public JT809Message wrapper() {
		String content = Tools.ToHexString(chagang.getMessage());
		int infoLength = content.length() / 2;
		int dataLength = 1 + 12 + 4 + 4 + infoLength;// 后续定位数据长度
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(chagang.getObjType(), 1))
				.append(Tools.ToHexString(chagang.getObjId(), 12))
				.append(Tools.ToHexString(chagang.getInfoId(), 4))
				.append(Tools.ToHexString(infoLength, 4)).append(content);
		String body = sb.toString();
		JT809Message msg =  new JT809Message(msgType,  subType,body);
		msg.setMsgDescr(chagang.getMessage());
		return msg;
	}
}
