package com.ltmonitor.jt809.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.ltmonitor.entity.JT809Command;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.service.IJT809CommandService;

/**
 * JT809主链路处理器
 * 
 * @author tianfei
 * 
 */
public class PlatformClientHandler extends IoHandlerAdapter {
	private Logger logger = Logger.getLogger(PlatformClientHandler.class);

	private IJT809CommandService jt809CommandService;

	public void exceptionCaught(IoSession session, Throwable e)
			throws Exception {
		this.logger.error("通讯时发生异常：" + e.getMessage(), e);
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		com.ltmonitor.jt809.server.PlatformClient.session = session;
		JT809Message mm = (JT809Message) message;
		//TODO:
		MessageAction action = new MessageAction();
		try {
			action.ResolveHandler(session, mm);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (mm.getMsgType() != 0x9005 && mm.getMsgType() != 0x1006) {
			JT809Command jc = new JT809Command();
			jc.setCmd(mm.getMsgType());
			jc.setSubCmd(mm.getSubType());
			jc.setPlateColor((byte) mm.getPlateColor());
			jc.setPlateNo(mm.getPlateNo());
			jc.setCmdData(mm.getDescr());
			jc.setSource(JT809Command.DOWN_NOTIFY);
			jc.setStatus(JT809Command.STATUS_RECEIVED);
			ParameterModel p = GlobalConfig.parModel;
			jc.setUserId((int) p.getPlatformCenterId());

			getJt809CommandService().save(jc);
		}
	}

	public void messageSent(IoSession session, Object message) throws Exception {
	}

	public void sessionClosed(IoSession session) throws Exception {
		session.close(true);
		this.logger.info("与本地服务器断开连接：" + session.getId());
	}

	public void sessionCreated(IoSession session) throws Exception {
		// this.logger.info(session.getId() + "????");
	}

	public void sessionIdle(IoSession session, IdleStatus idle)
			throws Exception {
		// this.logger.info(" Idle");
	}

	public void sessionOpened(IoSession session) throws Exception {
		// this.logger.info("connection province server" + session.getId() +
		// "??");
	}

	public IJT809CommandService getJt809CommandService() {
		return jt809CommandService;
	}

	public void setJt809CommandService(IJT809CommandService jt809CommandService) {
		this.jt809CommandService = jt809CommandService;
	}
}
