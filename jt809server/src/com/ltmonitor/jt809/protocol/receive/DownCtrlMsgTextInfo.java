package com.ltmonitor.jt809.protocol.receive;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.TerminalCommand;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.app.ServiceLauncher;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;
import com.ltmonitor.jt809.server.PlatformClient;
import com.ltmonitor.jt809.tool.Tools;
import com.ltmonitor.service.JT808Constants;


public class DownCtrlMsgTextInfo implements IReceiveProtocol {
	private static Logger logger = Logger.getLogger(DownCtrlMsgTextInfo.class);

	public String handle(JT809Message message) {
		String dataBody = message.getMessageBody();
		MessageParser mp = new MessageParser(dataBody, 28);

		int msgSequence = mp.getInt(4);

		int msgPriority = mp.getInt(1);
		int msgLength = mp.getInt(4);

		String msgContent = mp.getString(msgLength);
		message.setMsgDescr(msgContent);
		TerminalCommand tc = new TerminalCommand();
		tc.setCmdType(JT808Constants.CMD_SEND_TEXT);
		tc.setCmdData(msgPriority + ";" + msgContent);
		tc.setCmd(""+msgSequence);
		tc.setPlateNo(message.getPlateNo());
		tc.setOwner(TerminalCommand.FROM_GOV);
		try {
			ServiceLauncher.getBaseDao().save(tc);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		// 1503Ó¦´ð
		//byte result = 0;
		//T809Manager.UpCtrlMsgTextInfoAck(message.getPlateNo(), message.getPlateColor(),
				//msgSequence, result);

		return "";
	}

}
