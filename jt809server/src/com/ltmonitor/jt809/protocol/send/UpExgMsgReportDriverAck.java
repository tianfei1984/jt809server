package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.entity.DriverModel;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


public class UpExgMsgReportDriverAck implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpExgMsgReportDriverAck.class);

	private int msgType = 0x1200;
	private int subType = 0x120A;
	private DriverModel driver;

	public UpExgMsgReportDriverAck(DriverModel driver) {
		this.driver = driver;
	}

	public JT809Message wrapper() {
		int dataLength = 16 + 20 + 40 + 200;
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(driver.getPlateNo(), 21))
				.append(Tools.ToHexString(driver.getPlateColor(), 1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(driver.getDriverName(), 16))
				.append(Tools.ToHexString(driver.getDriverId(), 20))
				.append(Tools.ToHexString(driver.getLicense(), 40))
				.append(Tools.ToHexString(driver.getOrgName(), 200))
				;

		String body = sb.toString();

		JT809Message mm = new JT809Message(msgType, subType,body);
		mm.setPlateColor(driver.getPlateColor());
		mm.setPlateNo(driver.getPlateNo());
		return mm;
	}
}
