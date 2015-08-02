package com.ltmonitor.jt809.protocol.receive;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

/**
 * 交换车辆静态信息
 * @author tianfei
 *
 */
public class DownExgMsgCarInfo implements IReceiveProtocol {
	private static Logger logger = Logger.getLogger(DownExgMsgCarInfo.class);

	public String handle(JT809Message message) {
		String outPut = "";

		String dataBody = message.getMessageBody();

		MessageParser mp = new MessageParser(dataBody);

		message.setPlateNo(mp.getString(21));

		message.setPlateColor(mp.getInt(1));

		message.setSubType(mp.getInt(2));
		int dataLength = mp.getInt(4);
		//静态车辆数据，以;号隔开，进行解析，可以入库，也可以不入库
		String vehicleInfo = mp.getString(dataLength);
		message.setMsgDescr(vehicleInfo);

		return null;
	}
}
