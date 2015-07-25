package com.ltmonitor.jt809.protocol.send;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.entity.VehicleRegisterInfo;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.model.VehicleModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.server.PlatformClient;
import com.ltmonitor.jt809.tool.Tools;


public class UpExgMsgRegister implements ISendProtocol {
	private static Logger logger = Logger.getLogger(UpExgMsgRegister.class);
	// 是否注册车辆表中的所有车辆
	private boolean RegisterAllVehicle = false;
	// 协议命令类型
	private int msgType = 0x1200;
	// 子类型
	private int subType = 0x1201;
	// 车辆数据
	private VehicleRegisterInfo vehicleModel;

	public UpExgMsgRegister(VehicleRegisterInfo _vm) {
		vehicleModel = _vm;
	}

	public JT809Message wrapper() {
		String vehicleNo = Tools.ToHexString(vehicleModel.getPlateNo(), 21);
		String vehicleColor = Tools
				.ToHexString(vehicleModel.getPlateColor(), 1);

		ParameterModel pm = GlobalConfig.parModel;
		int dataLength = 11 + 11 + 20 + 7 + 12; // 后续数据长度
		StringBuilder data = new StringBuilder();
		data.append(vehicleNo).append(vehicleColor)
		.append(Tools.ToHexString(subType, 2))
		.append(Tools.ToHexString(dataLength, 4))
		.append(Tools.ToHexString(vehicleModel.getPlateformId(), 11)) // 平台唯一编码
				.append(Tools.ToHexString(vehicleModel.getTerminalVendorId(), 11)) // 终端厂商唯一编码
				.append(Tools.ToHexString(vehicleModel.getTerminalModel(), 20)) // 终端型号
				.append(Tools.ToHexString(vehicleModel.getTerminalId(), 7)) // 终端编号
				.append(Tools.ToHexString(vehicleModel.getSimNo(), 12));

		JT809Message mm = new JT809Message(msgType,  subType, data.toString());
		mm.setPlateColor(vehicleModel.getPlateColor());
		mm.setPlateNo(vehicleModel.getPlateNo());
		return mm;
	}

	public boolean isRegisterAllVehicle() {
		return RegisterAllVehicle;
	}

	public void setRegisterAllVehicle(boolean registerAllVehicle) {
		RegisterAllVehicle = registerAllVehicle;
	}
}
