package com.ltmonitor.jt809.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.AlarmRecord;
import com.ltmonitor.entity.BasicData;
import com.ltmonitor.entity.Department;
import com.ltmonitor.entity.GPSRealData;
import com.ltmonitor.entity.GnssData;
import com.ltmonitor.entity.TakePhotoModel;
import com.ltmonitor.entity.UserInfo;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.entity.WarnData;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.app.ServiceLauncher;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.entity.DriverModel;
import com.ltmonitor.jt809.entity.VehicleRegisterInfo;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.send.UpExgMsgRegister;
import com.ltmonitor.jt809.protocol.send.UpExgMsgReportDriverInfo;
import com.ltmonitor.service.ITransferService;
import com.ltmonitor.service.IVehicleService;
import com.ltmonitor.util.DateUtil;

/**
 * 实时数据转发服务
 * 
 * @author DELL
 * 
 */
public class TransferService implements ITransferService {

	private IBaseDao baseDao;

	private static Logger logger = Logger.getLogger(TransferService.class);

	private boolean transferFromDb;// 从数据库中读取实时数据进行转发

	/**
	 * 实时数据数据处理线程
	 */
	private Thread processRealDataThread;

	private IVehicleService vehicleService;
	/**
	 * 转发实时数据时，根据平台Id进行过滤
	 */
	private boolean transferByPlatformId = false;
	/**
	 * 转发实时数据，根据用户帐户分配的车辆进行过滤
	 */
	private boolean transferByUserId = true;

	private String userId;

	Map<Integer, Integer> authorizedDepIdMap = new HashMap<Integer, Integer>();
	Hashtable sendRegisterMap = new Hashtable();

	public void startTransfer() {
		if (transferFromDb) {
			processRealDataThread = new Thread(new Runnable() {
				public void run() {
					ProcessRealDataThreadFunc();
				}
			});
			processRealDataThread.start();

			if (transferByUserId) {
				loadDepIdMap();
			}
		}
	}

	public void stopTransfer() {
		if (processRealDataThread != null) {
			transferFromDb = false;
			try {
				processRealDataThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void loadDepIdMap() {
		try {
			String hsql = "from UserInfo where loginName = ? and deleted = ?";

			UserInfo user = (UserInfo) this.baseDao.find(hsql,
					new Object[] { userId, false });
			if(user == null)
			{
				logger.error("没有找到此用户:"+userId);
			}


			Set<Department> depSet = user.getDepartments();

			for (Department dep : depSet) {
				if (dep.getDeleted() == false) {
					authorizedDepIdMap
							.put(dep.getEntityId(), dep.getEntityId());
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			logger.error(ex.getStackTrace());
		}

	}

	private void TransferRegiserMessage() {
		IBaseDao baseDao = ServiceLauncher.getBaseDao();

		List result = null;
		Date date = DateUtil.getDate(new Date(), Calendar.SECOND, -120);
		if (this.transferByPlatformId == false) {
			String hql = "from VehicleRegisterInfo where updateDate > ? and plateNo is not null";
			result = baseDao.query(hql, new Object[] {  date });
		} else {
			String hql = "from VehicleRegisterInfo where updateDate > ? and depId = ? and plateNo is not null";

			ParameterModel p = GlobalConfig.parModel;
			int pId =  (int)p.getPlatformCenterId();
			result = baseDao.query(hql, new Object[] { date, pId });
		}

		for (Object obj : result) {
			VehicleRegisterInfo vi = (VehicleRegisterInfo) obj;
			if (this.transferByUserId && this.authorizedDepIdMap.containsKey(vi.getDepId()) == false)
			{
				continue;
			}
			
			if (sendRegisterMap.containsKey(vi.getPlateNo())) {
				Date sendTime = (Date) sendRegisterMap.get(vi.getPlateNo());
				if (sendTime != null
						&& sendTime.compareTo(vi.getUpdateDate()) >= 0)
					continue;
			}
			sendRegisterMap.put(vi.getPlateNo(), vi.getUpdateDate());

			T809Manager.UpExgMsgRegister(vi);
		}
	}

	private void ProcessRealDataThreadFunc() {

		Hashtable sendMap = new Hashtable();

		while (transferFromDb) {
			IBaseDao baseDao = ServiceLauncher.getBaseDao();
			if (baseDao != null
					&& (T809Manager.mainLinkConnected || T809Manager.subLinkConnected)) {
				try {
					Date start = new Date();
					List result = null;
					if (this.transferByPlatformId ) {
						String hql = "from GPSRealData where online = ? and sendTime >= ? and depId = ? and velocity < 150 and latitude > 0 and longitude > 0";

						Date date = DateUtil.getDate(new Date(),
								Calendar.SECOND, -30);
						ParameterModel p = GlobalConfig.parModel;
						int pId = (int) p.getPlatformCenterId();
						result = baseDao.query(hql, new Object[] { true, date,
								pId });
					}else
					 {
						String hql = "from GPSRealData where sendTime >= ? and online = ? and velocity < 150 and latitude > 0 and longitude > 0";

						Date date = DateUtil.getDate(new Date(),
								Calendar.SECOND, -120);
						result = baseDao
								.query(hql, new Object[] {  date ,true});
					}
					for (Object obj : result) {
						GPSRealData rd = (GPSRealData) obj;
						if (this.transferByUserId && this.authorizedDepIdMap.containsKey(rd.getDepId()) == false)
						{
							continue;
						}

						if (sendMap.containsKey(rd.getPlateNo())) {
							Date sendTime = (Date) sendMap.get(rd.getPlateNo());
							if (sendTime != null
									&& sendTime.compareTo(rd.getSendTime()) >= 0)
								continue;
						}
						sendMap.put(rd.getPlateNo(), rd.getSendTime());

						GnssData gd = new GnssData();
						gd.setAltitude((int) (rd.getAltitude() * 10));
						gd.setDirection(rd.getDirection());
						gd.setGpsSpeed((int) (rd.getVelocity()));
						gd.setLatitude((int) (rd.getLatitude() * 1000000));
						gd.setLongitude((int) (rd.getLongitude() * 1000000));
						gd.setPlateNo(rd.getPlateNo());
						gd.setTotalMileage((int) (rd.getMileage() * 10));
						gd.setDirection(rd.getDirection());
						long alarmState = Long.valueOf(rd.getAlarmState(), 2);
						gd.setAlarmState(alarmState);
						long intStatus = Long.valueOf(rd.getStatus(), 2);
						gd.setVehicleState(intStatus);
						gd.setPosTime(rd.getSendTime());

						// VehicleData vd =
						// this.GetVehicleBySimNo(rd.getSimNo());
						// if (vd != null)
						// gd.setPlateColor(vd.getPlateColor());
						gd.setPlateColor(2);
						try {
							VehicleData vd = vehicleService.getVehicleData(rd
									.getVehicleId());
							if (vd != null)
								gd.setPlateColor(vd.getPlateColor());
						} catch (Exception ex) {
							logger.error(ex.getMessage(), ex);
						}

						UpExgMsgRealLocation(gd);// 发送实时数据
						//transferAlarm(rd);

					}
					TransferRegiserMessage();
					Date end = new Date();
					double sec = DateUtil.getSeconds(start, end);
					if (sec > 5) {
						logger.error("数据库转发耗时:" + sec + "秒");
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e1) {
			}
		}
	}

	private void transferAlarm(GPSRealData rd) {
		String alarmState = rd.getAlarmState();

		char[] newChars = (char[]) alarmState.toCharArray();
		for (int m = 29; m < newChars.length; m++) {
			String status = "" + newChars[m];
			int alarmId = 31 - m; // 倒序，转换为部标的报警序号
			String alarmType = "" + alarmId;
			int jt809WarnType = 0;
			String alarmDescr = "";
			if (alarmType.equals("0")) {
				jt809WarnType = WarnData.EMERGENCY;
				alarmDescr = "紧急报警";
			} else if (alarmType.equals("1")) {
				jt809WarnType = WarnData.OVER_SPEED;
				alarmDescr = "超速报警";
			} else if (alarmType.equals("2")) {
				jt809WarnType = WarnData.TIRED;
				alarmDescr = "疲劳驾驶报警";
			} else
				continue;
			sendAlarmData(rd, jt809WarnType, alarmDescr);
		}

	}

	void sendAlarmData(GPSRealData rd, int warnType, String alarmDescr) {
		WarnData wd = new WarnData();
		wd.setPlateNo(rd.getPlateNo());
		wd.setInfoId(rd.getID());
		wd.setSrc(WarnData.FROM_TERMINAL);
		wd.setWarnTime(rd.getSendTime());
		wd.setType(warnType);
		wd.setContent(alarmDescr);
		wd.setPlateColor(2);
		try {
			VehicleData vd = vehicleService.getVehicleData(rd.getVehicleId());
			if (vd != null)
				wd.setPlateColor(vd.getPlateColor());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		UpWarnMsgAdptInfo(wd);
	}

	/**
	 * 实时转发定位信息
	 */
	public void UpExgMsgRealLocation(GnssData gnssData) {
		// logger.error("ifno");
		T809Manager.UpExgMsgRealLocation(gnssData);
	}

	/**
	 * 拍照应答
	 * 
	 * @param _photo
	 * @return
	 */
	@Override
	public boolean UpCtrlMsgTakePhotoAck(TakePhotoModel _photo) {

		return T809Manager.UpCtrlMsgTakePhotoAck(_photo);
	}

	/**
	 * 上报电子运单
	 * 
	 * @param plateNo
	 * @param plateColor
	 * @param eContent
	 * @return
	 */
	@Override
	public boolean UpExgMsgReportTakeEWayBill(String plateNo, int plateColor,
			String eContent) {
		return T809Manager.UpExgMsgReportTakeEWayBill(plateNo, plateColor,
				eContent);
	}

	/**
	 * 主动上报驾驶员身份
	 * 
	 * @param dm
	 * @return
	 */
	public boolean UpExgMsgReportDriverInfo(DriverModel dm) {
		return T809Manager.UpExgMsgReportDriverInfo(dm);
	}

	/**
	 * 上报报警信息
	 * 
	 * @param wd
	 * @return
	 */
	public boolean UpWarnMsgAdptInfo(WarnData wd) {
		return T809Manager.UpWarnMsgAdptInfo(wd);

	}

	/**
	 * 行车记录仪应答
	 * 
	 * @param plateNo
	 * @param plateColor
	 * @param cmdType
	 * @param cmdData
	 * @return
	 */
	@Override
	public boolean UpCtrlMsgTakeTravelAck(String plateNo, int plateColor,
			byte cmdType, byte[] cmdData) {
		return T809Manager.UpCtrlMsgTakeTravelAck(plateNo, plateColor, cmdType,
				cmdData);
	}

	@Override
	public boolean UpCtrlMsgTextInfoAck(String plateNo, int plateColor,
			int msgId, byte result) {
		return T809Manager.UpCtrlMsgTextInfoAck(plateNo, plateColor, msgId,
				result);
	}

	@Override
	public boolean UpCtrlMsgEmergencyMonitoringAck(String plateNo,
			int plateColor, byte result) {
		return T809Manager.UpCtrlMsgEmergencyMonitoringAck(plateNo, plateColor,
				result);
	}

	/**
	 * 单向监听
	 * 
	 * @param plateNo
	 * @param plateColor
	 * @param result
	 * @return
	 */
	@Override
	public boolean UpCtrlMsgMonitorVehicleAck(String plateNo, int plateColor,
			byte result) {
		return T809Manager.UpCtrlMsgMonitorVehicleAck(plateNo, plateColor,
				result);
	}

	/**
	 * 静态车辆注册1201
	 * 
	 * @param vm
	 * @return
	 */
	public boolean UpExgMsgRegister(VehicleRegisterInfo vm) {
		return T809Manager.UpExgMsgRegister(vm);
	}

	public IBaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(IBaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public boolean isTransferFromDb() {
		return transferFromDb;
	}

	public void setTransferFromDb(boolean transferFromDb) {
		this.transferFromDb = transferFromDb;
	}

	public IVehicleService getVehicleService() {
		return vehicleService;
	}

	public void setVehicleService(IVehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

	public boolean isTransferByPlatformId() {
		return transferByPlatformId;
	}

	public void setTransferByPlatformId(boolean transferByPlatformId) {
		this.transferByPlatformId = transferByPlatformId;
	}

	public boolean isTransferByUserId() {
		return transferByUserId;
	}

	public void setTransferByUserId(boolean transferByUserId) {
		this.transferByUserId = transferByUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
