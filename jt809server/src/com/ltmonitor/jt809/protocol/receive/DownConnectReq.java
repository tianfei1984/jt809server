package com.ltmonitor.jt809.protocol.receive;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

/**
 * 从链路连接请求 0x9001
 * @author tianfei
 *
 */
public class DownConnectReq implements IReceiveProtocol {
	private Logger logger = Logger.getLogger(DownConnectReq.class);

	public String handle(JT809Message message) {
		String strResult = message.getMessageBody();
		MessageParser mp = new MessageParser(strResult);
		int result = mp.getInt(4);
		// int result = Integer.parseInt(strResult.substring(0, 8));
		message.setMsgDescr("从链路连接成功,校验码:" + result);
		this.logger.warn("从链路连接成功,校验码:" + result);
		
		T809Manager.setSubLinkState(true, "从链路连接成功");

		// 从链路连接应答消息
		T809Manager.DownConnectRsp();

		
		return null;
	}
}
