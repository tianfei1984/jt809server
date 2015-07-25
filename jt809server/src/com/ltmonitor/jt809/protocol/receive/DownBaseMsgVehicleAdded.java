package com.ltmonitor.jt809.protocol.receive;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.MemberInfo;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt809.app.ServiceLauncher;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.VehicleModel;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

public class DownBaseMsgVehicleAdded implements IReceiveProtocol {
	Logger logger = Logger.getLogger(DownBaseMsgVehicleAdded.class);

	public String handle(JT809Message message) {
		String dataBody = message.getMessageBody();
		MessageParser mp = new MessageParser(dataBody);
		// 1205应答
		VehicleModel vm = getVehicleModel(message.getPlateNo());
		if (vm != null)
			T809Manager.UpBaseMsgVehicleAddedAck(vm);
		else
			logger.error("找不到该车辆信息" + message.getPlateNo());

		this.logger.info("申请交换指定车辆定位信息应答包入库成功" + dataBody);

		return "";
	}

	private VehicleModel getVehicleModel(String plateNo) {
		try {
			String hql = "from VehicleData where plateNo = ?";
			VehicleData vd = (VehicleData) ServiceLauncher.getBaseDao().find(
					hql, plateNo);
			if (vd != null) {
				VehicleModel vm = new VehicleModel();
				vm.setPlateNo(plateNo);
				vm.setPlateColor(vd.getPlateColor());
				vm.setVehicleType(vd.getVehicleType()); // 车辆类型
				vm.setNationallity(vd.getRegion()); // 车籍贯
				vm.setTransType(vd.getIndustry()); // 运输行业代码

				try {
					// 车辆所属业户信息
					MemberInfo mi = (MemberInfo) ServiceLauncher.getBaseDao()
							.load(MemberInfo.class, vd.getMemberId());
					vm.setOwnerName(mi.getName());
					vm.setOwnerTel(mi.getContactPhone());
					vm.setOwnerId("" + mi.getEntityId());
				} catch (Exception ex) {
					vm.setOwnerName("四川宜宾众凯物流有限公司");
					vm.setOwnerTel("15814030918");
					vm.setOwnerId("12345678");
				}

				return vm;
			}
		} catch (Exception ex) {

		}
		return null;
	}

}
