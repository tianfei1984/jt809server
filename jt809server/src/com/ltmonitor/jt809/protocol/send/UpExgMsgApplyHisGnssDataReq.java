package com.ltmonitor.jt809.protocol.send;

import java.util.Date;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


//补发车辆定位请求消息
public class UpExgMsgApplyHisGnssDataReq implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpExgMsgApplyHisGnssDataReq.class);
	private int msgType = 0x1200;
	private int subType = 0x1209;
	private String plateNo;
	private Byte plateColor;
	private Date start;
	private Date end;

	public UpExgMsgApplyHisGnssDataReq(String PlateNo, Byte PlateColor,
			Date start, Date end) {
		plateNo = PlateNo;
		plateColor = PlateColor;
		this.start = start;
		this.end = end;
	}

	public JT809Message wrapper() {
		int dataLength = 8 + 8; // 后续数据体长度

		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor, 1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.getUTC(start)).append(Tools.getUTC(end));

		ParameterModel pm = GlobalConfig.parModel;
		String body = sb.toString();
		//String mess = Tools.getHeaderAndFlag(GNSSImpl.getSN(), body, msgType,
				//pm.getPlatformCenterId());

		JT809Message mm = new JT809Message(msgType,  subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}

}
