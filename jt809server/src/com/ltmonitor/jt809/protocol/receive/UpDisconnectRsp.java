package com.ltmonitor.jt809.protocol.receive;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;


public class UpDisconnectRsp implements IReceiveProtocol {
	private Logger logger = Logger.getLogger(UpDisconnectRsp.class);

	public String handle(JT809Message message) {
		T809Manager.CloseMainLink();
		// tm.base.model.dao.action.impl.GNSSImpl.isOpenPlat = false;
		// tm.base.model.dao.action.impl.GNSSImpl.isRegist = false;
		this.logger.warn("上行平台注销请求，已经应答，开始注销主连接！");
		return "";
	}
}
