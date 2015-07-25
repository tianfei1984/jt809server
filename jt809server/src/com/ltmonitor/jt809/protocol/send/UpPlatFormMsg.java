package com.ltmonitor.jt809.protocol.send;

import java.util.ArrayList;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ProtocolModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;
import com.ltmonitor.jt809.tool.xml.ClassUtils;


public class UpPlatFormMsg implements ISendProtocol {
	public JT809Message wrapper() {
		String dataType_P = "1300";

		int dataLength_P = 29;
		String turnDataLength_P = Integer.toHexString(dataLength_P);
		String endDataLength_P = Tools.turnDataLength(turnDataLength_P, 4);
		String mess = "";

		if (GlobalConfig.protocolMap.containsKey("1301")) {
			ISendProtocol protocolClass = (ISendProtocol) ClassUtils
					.getBean(((ProtocolModel) GlobalConfig.protocolMap.get("1301"))
							.getName());
			//mess = protocolClass.ResolveHandle();
		}

		return null;
	}
}
