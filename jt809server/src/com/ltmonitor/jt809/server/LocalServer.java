package com.ltmonitor.jt809.server;

import java.net.InetSocketAddress;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.app.ServiceLauncher;

/**
 * JT809从链路服务
 * @author tianfei
 *
 */
public class LocalServer {
	private static Logger logger = Logger.getLogger(LocalServer.class);
	public static boolean isOpen = false;

	private static LocalServer instance = null;

	public static IoAcceptor dataAccepter = null;
	private LocalServerHandler handler = new LocalServerHandler();
	public static IoSession session;

	public static void Stop() {
		try {
			isOpen = false;
			if (null != dataAccepter) {
				dataAccepter.unbind();
				dataAccepter.getFilterChain().clear(); // 清空Filter
														// chain，防止下次重新启动时出现重名错误
				dataAccepter.dispose(); // 可以另写一个类存储IoAccept，通过spring来创建，这样调用dispose后也会重新创建一个新的。或者可以在init方法内部进行创建。
				dataAccepter = null;
				// System.exit(0); 将导致容器停止
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public static boolean Send(String msg) {
		try {
			if (session != null && session.isConnected()) {
				LocalServer ls = LocalServer.getInstance();
				WriteFuture wf = session.write(msg);
				return true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return false;
	}

	public static final synchronized LocalServer getInstance() {
		if (instance == null) {
			instance = new LocalServer();
		}
		return instance;
	}
	/**
	 * 启动JT809从链路服务
	 * @return
	 */
	public boolean start() {
		try {
			if (isOpen)
				return true;
			dataAccepter = new NioSocketAcceptor();

			LoggingFilter log = new LoggingFilter();
			log.setMessageReceivedLogLevel(LogLevel.WARN);
			dataAccepter.getFilterChain().addLast("logger", log);

			dataAccepter.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(new TiamaesMessageCodecFactory()));

			IoSessionConfig config = dataAccepter.getSessionConfig();

			config.setReadBufferSize(2048);

			config.setIdleTime(IdleStatus.BOTH_IDLE, 60);

			dataAccepter.setHandler(this.handler);
			this.handler.setJt809CommandService(ServiceLauncher
					.getCommandService());

			dataAccepter.bind(new InetSocketAddress(GlobalConfig.parModel
					.getLocalPort()));
			logger.info("数据服务器启动成功!端口号:" + GlobalConfig.parModel.getLocalPort());
			isOpen = true;
		} catch (Exception e) {
			isOpen = false;
			logger.error("从链路服务器启动失败:" + e.getMessage(), e);
			// e.printStackTrace();
		}
		return isOpen;
	}
}
