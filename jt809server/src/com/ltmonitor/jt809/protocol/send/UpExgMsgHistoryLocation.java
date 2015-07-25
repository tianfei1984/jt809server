package com.ltmonitor.jt809.protocol.send;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.GnssData;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


public class UpExgMsgHistoryLocation implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpExgMsgHistoryLocation.class);
	
	private String plateNo;
	
	private int plateColor;

	private List<GnssData> gnssDataList;

	private int msgType = 0x1200;

	//补报定位数据类型
	private int subType = 0x1203;

	private GnssData gnssData;

	public UpExgMsgHistoryLocation(String plateNo, int plateColor, List<GnssData> gnss) {
		this.plateNo = plateNo;
		this.plateColor = plateColor;
		gnssDataList = gnss;
	}

	public JT809Message wrapper() {
		int gnssDataNum = gnssDataList.size();
		int dataLength = 1 + 36 * gnssDataNum;// 后续定位数据长度
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor, 1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(gnssDataNum, 1));
		for (int m = 0; m < gnssDataNum; m++) {
			GnssData gd = gnssDataList.get(m);
			sb.append(getGnssData(gd));
		}
		String body = sb.toString();
		JT809Message mm = new JT809Message(msgType,  subType,body);
		mm.setPlateColor(plateColor);
		mm.setPlateNo(plateNo);
		return mm;
	}

	// 定位数据部分
	private String getGnssData(GnssData gnssData) {
		Date gpsTime = gnssData.getPosTime();

		Calendar cd = Calendar.getInstance();
		cd.setTime(gpsTime);

		String strDate = Tools.ToHexString(cd.get(Calendar.DAY_OF_MONTH), 1)
				+ Tools.ToHexString(cd.get(Calendar.MONTH) + 1, 1)
				+ Tools.ToHexString(cd.get(Calendar.YEAR), 2);

		String strTime = Tools.ToHexString(cd.get(Calendar.HOUR_OF_DAY), 1)
				+ Tools.ToHexString(cd.get(Calendar.MINUTE), 1)
				+ Tools.ToHexString(cd.get(Calendar.SECOND), 1);
		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(gnssData.getPosEncrypt(), 1))
				.append(strDate).append(strTime)
				.append(Tools.ToHexString(gnssData.getLongitude(), 4))
				.append(Tools.ToHexString(gnssData.getLatitude(), 4))
				.append(Tools.ToHexString(gnssData.getGpsSpeed(), 2))
				.append(Tools.ToHexString(gnssData.getRecSpeed(), 2))
				.append(Tools.ToHexString(gnssData.getTotalMileage(), 4))
				.append(Tools.ToHexString(gnssData.getDirection(), 2))
				.append(Tools.ToHexString(gnssData.getAltitude(), 2))
				.append(Tools.ToHexString(gnssData.getVehicleState(), 4))
				.append(Tools.ToHexString(gnssData.getAlarmState(), 4));

		return sb.toString();
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public int getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(int plateColor) {
		this.plateColor = plateColor;
	}

}
