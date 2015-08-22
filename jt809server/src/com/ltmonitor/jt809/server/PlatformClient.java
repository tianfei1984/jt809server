package com.ltmonitor.jt809.server;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.ltmonitor.entity.JT809Command;
import com.ltmonitor.entity.T809Constants;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.app.ServiceLauncher;
import com.ltmonitor.jt809.model.ParameterModel;

/**
 * JT809主链路
 * @author tianfei
 *
 */
public class PlatformClient {
	public static Logger logger = Logger.getLogger(PlatformClient.class);
	public static IoSession session = null;

	private static PlatformClient instance = null;
	private static ConnectFuture future;
	private static IoConnector connector = null;
	//主链路处理器
	private static PlatformClientHandler handler = new PlatformClientHandler();

	public static final synchronized PlatformClient getInstance() {
		if (instance == null) {
			instance = new PlatformClient();
		}
		return instance;
	}

	public static final boolean connect() {
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TiamaesMessageCodecFactory()));
		connector.setHandler(handler);
		try {
			future = connector.connect(new InetSocketAddress(GlobalConfig.parModel
					.getPlatformIP(), GlobalConfig.parModel.getPlatformPort()));

			future.awaitUninterruptibly();
			session = future.getSession();
			GlobalConfig.isConnection = true;
		} catch (Exception e) {
			GlobalConfig.isConnection = false;
			logger.error("主链路连接失败,服务器:" + GlobalConfig.parModel.getPlatformIP()
					+ ":" + GlobalConfig.parModel.getPlatformPort()+"错误信息："+e.getMessage());
			// logger.error("Socket Connection Exception", e);
			e.printStackTrace();
			GlobalConfig.isOpenPlat = false;
		} finally {
			return GlobalConfig.isConnection;
		}
	}

	public static boolean Send(String msg) {
		try {
			if (session != null && session.isConnected()) {
				WriteFuture wf = session.write(msg);
				return true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return false;

	}

	/**
	 * 启动809主链路
	 * @return
	 */
	public boolean start() {
		ParameterModel parmodel = GlobalConfig.parModel;

		connector = new NioSocketConnector();

		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TiamaesMessageCodecFactory()));

		this.handler
				.setJt809CommandService(ServiceLauncher.getCommandService());
		connector.setHandler(this.handler);
		try {
			future = connector.connect(new InetSocketAddress(parmodel
					.getPlatformIP(), parmodel.getPlatformPort()));

			future.awaitUninterruptibly();
			session = future.getSession();
			GlobalConfig.isConnection = true;
			logger.info("JT809 mainLink OK");
		} catch (Exception e) {
			GlobalConfig.isConnection = false;

			logger.error("主链路连接失败,服务器:" + GlobalConfig.parModel.getPlatformIP()
					+ ":" + GlobalConfig.parModel.getPlatformPort(), e);

			try {
				JT809Command tc = new JT809Command();
				tc.setCmd(T809Constants.MAIN_LINK_FAILED);
				tc.setCmdData("主链路连接失败,服务器:"
						+ GlobalConfig.parModel.getPlatformIP() + ":"
						+ GlobalConfig.parModel.getPlatformPort());
				tc.setStatus(JT809Command.STATUS_FAILED);
				if (ServiceLauncher.getBaseDao() != null)
					ServiceLauncher.getBaseDao().save(tc);
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
		}
		return GlobalConfig.isConnection;
	}

	public static void Stop() {
		try {
			if (null != connector) {
				if (session != null)
					session.close(true);
				connector.getFilterChain().clear(); // 清空Filter
													// chain，防止下次重新启动时出现重名错误
				connector.dispose(); // 可以另写一个类存储IoAccept，通过spring来创建，这样调用dispose后也会重新创建一个新的。或者可以在init方法内部进行创建。
				connector = null;

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
