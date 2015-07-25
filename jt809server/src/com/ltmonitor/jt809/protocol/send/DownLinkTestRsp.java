package com.ltmonitor.jt809.protocol.send;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;
import com.ltmonitor.jt809.protocol.ISendProtocol;


public class DownLinkTestRsp implements ISendProtocol {
	int msgType = 0x9006;

	public JT809Message wrapper() {
		return new JT809Message(msgType);
	}
}
