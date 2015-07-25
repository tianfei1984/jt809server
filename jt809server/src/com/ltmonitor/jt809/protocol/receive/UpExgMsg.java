package com.ltmonitor.jt809.protocol.receive;


import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;



public class UpExgMsg  implements IReceiveProtocol{
	public String handle(JT809Message message) {
		String dataBody = message.getMessageBody();
		MessageParser mp = new MessageParser(dataBody);

		message.setPlateNo(mp.getString(21));

		message.setPlateColor(mp.getInt(1));

		message.setSubType(mp.getInt(2));
		message.setContentLength(mp.getInt(4));
		return null;
	}
}
