package com.ltmonitor.jt809.protocol.receive;

import java.util.Date;
import org.apache.log4j.Logger;
import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.jt809.app.ServiceLauncher;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.EmergencyAccessInfo;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;
import com.ltmonitor.service.JT808Constants;
/**
 * 车辆应急接入监控平台请求消息
 * @author tianfei
 *
 */
public class DownCtrlMsgEmergencyMonitoringReq implements IReceiveProtocol {
	public static Logger logger = Logger
			.getLogger(DownCtrlMsgEmergencyMonitoringReq.class);

	public String handle(JT809Message message) {
		int startIndex = 28; // 直接解析
		MessageParser mp = new MessageParser(message.getMessageBody(),
				startIndex);

		EmergencyAccessInfo acc = new EmergencyAccessInfo();
		acc.setAuthenticatetionCode(mp.getString(10));
		acc.setAccessPointName(mp.getString(20));
		acc.setUserName(mp.getString(49));
		acc.setPassword(mp.getString(22));
		acc.setServerIp(mp.getString(32));
		acc.setTcpPort(mp.getInt(2));
		acc.setUdpPort(mp.getInt(2));
		acc.setUtcTime(new Date(mp.getLong() * 1000));

		message.setMsgDescr(acc.ToString());

		TerminalCommand tc = new TerminalCommand();
		tc.setCmdType(JT808Constants.CMD_CONTROL_TERMINAL);
		tc.setCmd("" + 2); // 控制终端连接指定服务器命令字
		StringBuilder sb = new StringBuilder();
		sb.append(0).append(";").append(acc.getAuthenticatetionCode()).append(";").append(acc.getAccessPointName())
				.append(";").append(acc.getUserName()).append(";").append(acc.getPassword())
				.append(";").append(acc.getServerIp()).append(";").append(acc.getTcpPort())
				.append(";").append(acc.getUdpPort()).append(";").append(acc.getUtcTime());
		tc.setCmdData(sb.toString());

		tc.setPlateNo(message.getPlateNo());
		tc.setOwner(TerminalCommand.FROM_GOV);
		try {
			ServiceLauncher.getBaseDao().save(tc);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		byte result = 0;
		T809Manager.UpCtrlMsgEmergencyMonitoringAck(message.getPlateNo(),
				message.getPlateColor(), result);
		return "";
	}
}
