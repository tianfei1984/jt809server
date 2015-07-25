package com.ltmonitor.jt809.protocol.receive;

import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

public class DownLinkTestReq implements IReceiveProtocol {

	public String handle(JT809Message message) {
		message.setMsgDescr("ÐÄÌø");
		T809Manager.DownLinkTestRsp();
		return null;
	}
}
