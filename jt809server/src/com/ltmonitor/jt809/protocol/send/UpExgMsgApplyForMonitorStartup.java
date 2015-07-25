package com.ltmonitor.jt809.protocol.send;

import java.util.Date;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.model.VehicleModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


/**
 * 
 * @author DELL 申请交换指定车辆定位信息请求 UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP = 0x1207
 */
public class UpExgMsgApplyForMonitorStartup implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpExgMsgApplyForMonitorStartup.class);
	private int msgType = 0x1200;
	private int subType = 0x1207;
	private String plateNo;
	private Byte plateColor;
	private Date start;
	private Date end;

	public UpExgMsgApplyForMonitorStartup(String PlateNo, Byte PlateColor,
			Date start, Date end) {
		plateNo = PlateNo;
		plateColor = PlateColor;
		this.start = start;
		this.end = end;

	}

	public JT809Message wrapper() {
		
		short cmdType = 1200;
		int dataLength =  8 + 8; //后续数据体长度

		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
		.append(Tools.ToHexString(plateColor))
		.append(Tools.ToHexString(subType, 2))
		.append(Tools.ToHexString(dataLength, 4))
		.append(Tools.getUTC(start))
		.append(Tools.getUTC(end));

		ParameterModel pm = GlobalConfig.parModel;
		String body = sb.toString();

		JT809Message mm = new JT809Message(msgType,  subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}

	private Object getBusMessageBody(VehicleModel busModel) {
		String DateBody = "";
		String Bus_No = busModel.getPlateNo();
		String turnbus_No = Tools.TurnISN(Bus_No);
		String endBus_No = Tools.turnDataLength(turnbus_No, 21);

		int Bus_Color = busModel.getPlateColor();
		String turnBus_Color = Integer.toHexString(Bus_Color);
		String endBus_Color = Tools.turnDataLength(turnBus_Color, 1);

		int BusFlay = 1207;
		String turnBusFlay = Integer.toHexString(BusFlay);
		String endBusFlay = Tools.turnDataLength(turnBusFlay, 2);

		int LastDateLength = 4;
		String turnLastDateLength = Integer.toHexString(LastDateLength);
		String endLastDateLength = Tools.turnDataLength(turnLastDateLength, 4);

		DateBody = endBus_No + endBus_Color + endBusFlay + endLastDateLength;
		return DateBody;
	}
}
