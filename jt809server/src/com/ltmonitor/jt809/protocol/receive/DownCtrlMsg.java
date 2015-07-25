package com.ltmonitor.jt809.protocol.receive;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ProtocolModel;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;
import com.ltmonitor.jt809.tool.xml.ClassUtils;


public class DownCtrlMsg implements IReceiveProtocol {
	public String handle(JT809Message message) {
		String dataBody = message.getMessageBody();
		MessageParser mp = new MessageParser(dataBody);

		message.setPlateNo(mp.getString(21));

		message.setPlateColor(mp.getInt(1));

		message.setSubType(mp.getInt(2));
		message.setContentLength(mp.getInt(4));

		String mess = "";
		if (GlobalConfig.protocolMap.containsKey(Integer.valueOf(message
				.getSubType()))) {
			IReceiveProtocol protocolClass = (IReceiveProtocol) ClassUtils
					.getBean(((ProtocolModel) GlobalConfig.protocolMap.get(Integer
							.valueOf(message.getSubType()))).getName());
			mess = protocolClass.handle(message);
		}

		return mess;
	}
}
