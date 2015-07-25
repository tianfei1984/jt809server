package com.ltmonitor.jt809.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.GnssData;
import com.ltmonitor.entity.GpsInfo;
import com.ltmonitor.entity.JT809Command;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.entity.CheckRecord;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.service.ICommandHandler;
import com.ltmonitor.jt809.service.IJT809CommandService;
import com.ltmonitor.util.DateUtil;

//给终端下发命令的服务
public class JT809CommandService implements IJT809CommandService {
	private static Logger logger = Logger.getLogger(JT809CommandService.class);
	// protected static log4net.ILog logger =
	// log4net.LogManager.GetLogger(CommandParser.class);
	private IBaseDao baseDao;

	public final IBaseDao getBaseDao() {
		return baseDao;
	}

	public final void setBaseDao(IBaseDao value) {
		baseDao = value;
	}

	private ICommandHandler commandHandler;

	public final ICommandHandler getOnRecvCommand() {
		return commandHandler;
	}

	public final void setOnRecvCommand(ICommandHandler value) {
		commandHandler = value;
	}

	private Thread parseThread;
	private boolean IsContinue = true;
	// 访问数据库时间间隔
	private int interval;

	public final int getInterval() {
		return interval;
	}

	public final void setInterval(int value) {
		interval = value;
	}

	public JT809CommandService() {
		setInterval(1000); // 默认1s
	}

	// 启动命令解析线程，自动解析命令，并发送给终端
	public final void Start() {
		IsContinue = true;
		logger.info("启动监听客户端命令线程");
		parseThread = new Thread(new Runnable() {
			public void run() {
				ParseCommandThreadFunc();
			}
		});
		parseThread.start();

	}

	public final void Stop() {
		IsContinue = false;
		try {
			if (parseThread != null)
				parseThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void ParseCommandThreadFunc() {
		logger.info("开始监听客户端命令");
		while (IsContinue) {
			try {
				ParseCommand();
			} catch (RuntimeException ex) {
				logger.error(ex.getMessage());
				logger.error(ex.getStackTrace());
			}
			try {
				Thread.sleep(getInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * 解析数据库的命令，并调用通信服务器，发送给终端
	 */
	public final void ParseCommand() {
		String hsql = "from JT809Command where CreateDate > ? and Status = ? and UserId > 0";
		Date startTime = DateUtil.getDate(DateUtil.now(), Calendar.MINUTE, -5);
		List result = getBaseDao().query(hsql,
				new Object[] { startTime, JT809Command.STATUS_NEW });

		for (Object obj : result) {
			JT809Command tc = (JT809Command) obj;
			//平台查岗
			if (tc.getSubCmd() == 0x1301) {
				ParameterModel p = GlobalConfig.parModel;
				int platformId = (int)p.getPlatformCenterId();
				if(platformId != tc.getUserId())
					continue;
			}
				
			try {
				if (T809Manager.mainLinkConnected == false
						&& T809Manager.subLinkConnected == false) {
					tc.setStatus(JT809Command.STATUS_Disconnected);
				} else {
					// 链路没有连接，无法发送
					boolean rs = Parse(tc);
					tc.setStatus(rs ? "发送成功" : "发送失败");
				}
				// tc.setUpdateDate(new Date());
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				logger.error("commpand parse error:", ex);
				tc.setStatus(JT809Command.STATUS_INVALID);
				tc.setRemark(ex.getMessage());
			}
			UpdateCommand(tc);
		}
	}

	public void save(JT809Command jc) {
		baseDao.saveOrUpdate(jc);
	}


	/**
	 * 更新命令的执行状态
	 */
	public final void UpdateCommand(JT809Command tc) {
		try {
			tc.setUpdateDate(new Date());
			getBaseDao().saveOrUpdate(tc);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage());
			logger.error(ex.getStackTrace());
		}

	}


	/**
	 * 不对非法命令格式进行解析，在命令录入时确保格式正确
	 * 
	 * @param tc
	 * @return
	 * @throws Exception
	 */
	private final Boolean Parse(JT809Command tc) throws Exception {

		String cmdData = tc.getCmdData();
		String[] strData = cmdData == null ? null : cmdData.split(";");
		if (tc.getSubCmd() == 0x1207) {
			// 申请交换指定车辆定位信息
			Date start = DateUtil.stringToDatetime(strData[0],
					"yyyy-MM-dd HH:mm:ss");
			Date end = DateUtil.stringToDatetime(strData[1],
					"yyyy-MM-dd HH:mm:ss");

			return T809Manager.UpExgMsgApplyForMonitorStartup(tc.getPlateNo(),
					tc.getPlateColor(), start, end);
		} else if (tc.getSubCmd() == 0x1208) {
			// 结束申请交换指定车辆定位信息
			return T809Manager.UpExgMsgApplyForMonitorEnd(tc.getPlateNo(),
					tc.getPlateColor());
		} else if (tc.getCmd() == 0x1001) {
			// 主链路登录申请
			return T809Manager.UpConnectReq();
		} else if (tc.getSubCmd() == 0x1209) {
			// 申请上级平台补发定位数据请求
			Date start = DateUtil.stringToDatetime(strData[0],
					"yyyy-MM-dd HH:mm:ss");
			Date end = DateUtil.stringToDatetime(strData[1],
					"yyyy-MM-dd HH:mm:ss");
			return T809Manager.UpExgMsgApplyHisGnssDataReq(tc.getPlateNo(),
					tc.getPlateColor(), start, end);
		} else if (tc.getSubCmd() == 0x1203) {
			// 补发历史定位数据
			Date start = DateUtil.stringToDatetime(strData[0],
					"yyyy-MM-dd HH:mm:ss");
			Date end = DateUtil.stringToDatetime(strData[1],
					"yyyy-MM-dd HH:mm:ss");

			List<GnssData> gnssDatas = this.getHistoryGnssData(tc.getPlateNo(),
					start, end);
			int count = gnssDatas.size();
			if (count > 5) {
				int index = 0;
				while (index < gnssDatas.size()) {
					int endIndex = (index + 5);
					endIndex = endIndex > count ? count : endIndex;
					List gs = gnssDatas.subList(index, endIndex);
					T809Manager.UpExgMsgHistoryLocations(tc.getPlateNo(),
							tc.getPlateColor(), gs);
					index += 5;
				}
				return true;
			} else
				return false;

		} else if (tc.getCmd() == 0x1003) {
			// 主链路注销请求消息
			return T809Manager.UpDisconnectReq();

		} else if (tc.getCmd() == 0x1007) {
			// 主链路注销请求消息
			return T809Manager.UpDisconnectMainLinkInform();

		} else if (tc.getCmd() == 0x1008) {
			// 主链路注销请求消息
			return T809Manager.UpCloseLinkInform();

		} else if (tc.getSubCmd() == 0x1403) {
			// 主动上报报警处理结果
			int alarmId = Integer.parseInt(strData[0]);
			int result = Integer.parseInt(strData[1]);
			return T809Manager.UpWarnMsgAdptToDoInfo(tc.getPlateNo(),
					tc.getPlateColor(), alarmId, result);
		} else if (tc.getSubCmd() == 0x1301) {
			// 查岗应答
			CheckRecord pm = new CheckRecord();
			int objType = Integer.parseInt(strData[0]);
			pm.setObjType(objType);
			pm.setObjId(strData[1]);
			int infoId = Integer.parseInt(strData[2]);
			pm.setInfoId(infoId);
			pm.setMessage(strData[3]);
			return T809Manager.UpPlatFormMsgPostQueryAck(pm);
		}

		return false;
		// return ts;
	}

	private List<GnssData> getHistoryGnssData(String plateNo, Date start,
			Date end) {
		List<GnssData> result = new ArrayList<GnssData>();

		String hql = "from GpsInfo where sendTime >= ? and sendTime <= ? and plateNo = ?";

		List gpsInfoList = this.baseDao.query(hql, new Object[] { start, end,
				plateNo });
		for (Object obj : gpsInfoList) {
			GpsInfo g = (GpsInfo) obj;
			GnssData d = new GnssData();
			d.setPlateNo(plateNo);
			d.setGpsSpeed((int) (g.getVelocity() * 10));
			d.setRecSpeed((int) (g.getRecordVelocity() * 10));
			d.setAlarmState(g.getAlarmState());
			d.setAltitude((int) (g.getAltitude() * 10));
			d.setDirection(g.getDirection());
			d.setLatitude((int) (g.getLatitude() * 1000000));
			d.setLongitude((int) (g.getLongitude() * 1000000));
			d.setPosEncrypt(0);
			d.setPosTime(g.getSendTime());
			d.setTotalMileage((int) (g.getMileage() * 10));
			d.setVehicleState(g.getStatus());
			result.add(d);
		}
		return result;
	}
}