package com.ltmonitor.jt809.protocol.receive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.GovPlatformMsg;
import com.ltmonitor.jt809.app.ServiceLauncher;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

/**
 * 平台间报文请求消息
 * 
 * @author DELL
 * 
 */
public class DownPlatformMsgInfoReq implements IReceiveProtocol {
	Logger logger = Logger.getLogger(DownPlatformMsgInfoReq.class);

	public String handle(JT809Message message) {
		String msgBody = message.getMessageBody();

		MessageParser mp = new MessageParser(msgBody);

		int dataType = mp.getInt(2);
		int dataLength = mp.getInt(4);
		int objType = mp.getInt(1); // 查岗对象类型
		String objId = mp.getString(12); // 查岗对象ID
		int infoId = mp.getInt(4); // 信息ID
		int infoLength = mp.getInt(4); // 信息内容长度

		String content = mp.getString(infoLength);

		StringBuilder sb = new StringBuilder();
		sb.append(objType).append(';').append(objId).append(';').append(infoId)
				.append(';').append(content);
		message.setMsgDescr(sb.toString());
			String objTypeName = "";
		switch (objType) {
		case 0:
			objTypeName = " 下级平台所属单一平台";
			break;
		case 1:
			objTypeName = " 当前连接的下级平台";
			break;
		case 2:
			objTypeName = " 下级平台所属单一业户";
			break;
		case 3:
			objTypeName = " 下级平台所属所有业户";
			break;
		case 4:
			objTypeName = "下级平台所属所有平台";
			break;
		case 5:
			objTypeName = "下级平台所属所有平台和业户";
			break;
		case 6:
			objTypeName = " 下级平台所属所有政府监管平台（含监控端）";
			break;
		case 7:
			objTypeName = "下级平台所属所有企业监控平台 ";
			break;
		case 8:
			objTypeName = "下级平台所属所有经营性企业监控平台 ";
			break;
		case 9:
			objTypeName = "下级平台所属所有非经营性企业监控平台 ";
		}

		T809Manager.UpPlatFormMsgInfoAck(infoId);
		message.setMsgDescr("信息ID：" + infoId + "," + objTypeName
				+ "下发平台间报文应答消息");

		/**
		 * if (updateInfoState(content)) { this.logger.info("更新报文请求消息状态"); }
		 */
		//this.logger.warn(objTypeName + "下发平台间报文应答消息");

		return "";
	}

}
