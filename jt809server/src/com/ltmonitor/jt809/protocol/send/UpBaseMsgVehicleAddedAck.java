package com.ltmonitor.jt809.protocol.send;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.model.VehicleModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;

/**
 * 补报静态请求的应答
 * @author DELL
 *
 */
public class UpBaseMsgVehicleAddedAck implements ISendProtocol {
	private Logger logger = Logger.getLogger(UpBaseMsgVehicleAddedAck.class);
	private int msgType = 0x1600;
	private int subType = 0x1601;
	private String plateNo;
	private int plateColor;

	private VehicleModel vm;

	public UpBaseMsgVehicleAddedAck(VehicleModel vm) {
		plateNo = vm.getPlateNo();
		plateColor = vm.getPlateColor();
		this.vm = vm;
	}

	public JT809Message wrapper() {
		String carInfo = getCarInfo(vm);
		String hexCarInfo = Tools.ToHexString(carInfo);
		int dataLength = hexCarInfo.length() / 2; // 后续数据体长度

		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor, 1))
				.append(Tools.ToHexString(subType,2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(hexCarInfo);

		String body = sb.toString();
		JT809Message message =  new JT809Message(msgType, subType, body);
		message.setMsgDescr(carInfo);
		return message;
	}
	
	private String getCarInfo(VehicleModel vm)
	{
		StringBuilder sb = new StringBuilder();

        sb.append("VIN:=").append(vm.getPlateNo())
        .append(";VEHICLE_COLOR:=").append(vm.getPlateColor())
        .append(";VEHICLE_TYPE:=").append(vm.getVehicleType())
        .append(";TRANS_TYPE:=").append(vm.getTransType()) //运输行业编码
        //.append(";VEHICLE_NATIONALITY:=").append(vm.getNationallity()) //行政区规划代码
        .append(";VEHICLE_NATIONALITY:=").append(vm.getNationallity()) //行政区规划代码
        .append(";OWERS_ID:=").append(vm.getOwnerId()) //车辆业主信息(不是平台业主信息)
        .append(";OWERS_NAME:=").append(vm.getOwnerName())
        .append(";OWERS_TEL:=").append(vm.getOwnerTel());
		return sb.toString();
	}

}
