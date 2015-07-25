package com.ltmonitor.jt809.protocol.receive;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ProtocolModel;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;
import com.ltmonitor.jt809.tool.xml.ClassUtils;


public class DownPlatformMsg implements IReceiveProtocol {
	public String handle(JT809Message message) {
		String body = message.getMessageBody();

		MessageParser mp = new MessageParser(body);
		message.setSubType(mp.getInt(2));
		message.setContentLength(mp.getInt(4));
		

		if (GlobalConfig.protocolMap.containsKey(Integer.valueOf(message.getSubType()))) {
			IReceiveProtocol protocolClass = (IReceiveProtocol) ClassUtils
					.getBean(((ProtocolModel) GlobalConfig.protocolMap.get(Integer
							.valueOf(message.getSubType()))).getName());
			return protocolClass.handle(message);
		}

		return "";
	}
}
