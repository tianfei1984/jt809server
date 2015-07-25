package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;
/**
 * 主链路注销请求
 * @author DELL
 *
 */
public class UpDisconnectReq implements ISendProtocol {
	Logger logger = Logger.getLogger(UpDisconnectReq.class);

	private int msgType = 0x1003;

	public JT809Message wrapper() {
		ParameterModel parModel = GlobalConfig.parModel;

		long userName = parModel.getPlatformUserName();
		String passWord = parModel.getPlatformPass();
		try {
			String endUserName = Tools.ToHexString(userName, 4);
			String endPassWord = Tools.ToHexString(passWord, 8);

			String dataBody = endUserName + endPassWord;
			return new JT809Message(msgType, dataBody);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}
}
