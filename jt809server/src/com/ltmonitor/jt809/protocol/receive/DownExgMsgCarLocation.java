package com.ltmonitor.jt809.protocol.receive;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.GnssData;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

/**
 * 交换车辆定位信息
 * @author tianfei
 *
 */
public class DownExgMsgCarLocation implements IReceiveProtocol {
	private static Logger logger = Logger
			.getLogger(DownExgMsgCarLocation.class);

	public String handle(JT809Message message) {
		String dataBody = message.getMessageBody();

		MessageParser mp = new MessageParser(dataBody);

		message.setPlateNo(mp.getString(21));

		message.setPlateColor(mp.getInt(1));

		message.setSubType(mp.getInt(2));
		int dataLength = mp.getInt(4);
		// 接收到定位数据
		GnssData gd = new GnssData();
		gd.setPlateColor(message.getPlateColor());
		gd.setPlateNo(message.getPlateNo());
		// 开始解析GPS数据
		gd.setPosEncrypt(mp.getInt(1));
		String dateSTr = mp.getString(7); // 日期总共是7个字节
		gd.setLongitude(mp.getInt(4));
		gd.setLatitude(mp.getInt(4));
		gd.setGpsSpeed(mp.getInt(2));
		gd.setRecSpeed(mp.getInt(2));
		gd.setTotalMileage(mp.getInt(4));
		gd.setDirection(mp.getInt(2));
		gd.setAltitude(mp.getInt(2));
		gd.setVehicleState(mp.getInt(4));
		gd.setAlarmState(mp.getInt(4));

		message.setMsgDescr("定位数据:" + gd.getLongitude() / 1000000 + ","
				+ gd.getLatitude() / 1000000);

		return null;
	}
}
