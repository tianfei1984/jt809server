package com.ltmonitor.jt809.protocol.receive;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;
import com.ltmonitor.jt809.server.PlatformClient;
import com.ltmonitor.jt809.tool.Tools;

public class DownExgMsgTakeEwaybillAck implements IReceiveProtocol {
	private Logger logger = Logger.getLogger(DownExgMsgTakeEwaybillAck.class);

	public String handle(JT809Message message) {
		String receBody = message.getMessageBody();

		String vehivalInfo = receBody.substring(0, 44);

		String sendBody = vehivalInfo + "120B" + "00000027" + vehivalInfo
				+ "120B" + "0000000B" + "0000001C" + "00000000000000";

		ParameterModel pm = GlobalConfig.parModel;

		String mess = "";// Tools.getHeaderAndFlag(GNSSImpl.getSN(), sendBody,
							// 1200, pm.getPlatformCenterId());

		if ((mess.length() > 0) && (mess != null)) {
			this.logger.error("mess–≈œ¢”–ŒÛ");
			PlatformClient.session.write(mess);
		}
		return mess;
	}
}
