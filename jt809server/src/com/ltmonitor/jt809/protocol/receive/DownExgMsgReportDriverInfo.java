package com.ltmonitor.jt809.protocol.receive;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.DriverInfo;
import com.ltmonitor.entity.StringUtil;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt809.app.ServiceLauncher;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.entity.DriverModel;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

/**
 * 驾驶员身份信息请求920A,应答120A
 * 
 * @author DELL
 * 
 */
public class DownExgMsgReportDriverInfo implements IReceiveProtocol {
	Logger logger = Logger.getLogger(DownExgMsgReportDriverInfo.class);

	public String handle(JT809Message message) {
		String dataBody = message.getMessageBody();
		if (StringUtil.isNullOrEmpty(message.getPlateNo()) == false) {
			DriverModel dm = this.getDriverInfo(message.getPlateNo());// queryDriverInfo(plateNo);
			if (dm != null) {
				// 120A应答
				T809Manager.UpExgMsgReportDriverAck(dm);
			}
		}else
		{
			message.setMsgDescr("收到车牌号为空，无法上报");
		}
		return "";
	}

	private DriverModel getDriverInfo(String plateNo) {
		String hql = "from VehicleData where plateNo = ?";
		VehicleData vd = (VehicleData) ServiceLauncher.getBaseDao().find(hql,
				plateNo);
		
		if(vd == null)
			return null;

		hql = "from DriverInfo where vehicleId = ?";
		DriverInfo dr = (DriverInfo) ServiceLauncher.getBaseDao().find(hql,
				vd.getEntityId());
		if (dr != null) {
			DriverModel dm = new DriverModel();
			dm.setDriverName(dr.getDriverName());
			dm.setDriverId(dr.getIdentityCard());
			dm.setLicense(dr.getDriverCode());
			dm.setOrgName(dr.getCompanyNo());
			dm.setPlateNo(plateNo);
			dm.setPlateColor(vd.getPlateColor());
			return dm;
		}
		return null;
	}

}
