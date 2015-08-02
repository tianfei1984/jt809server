package com.ltmonitor.jt809.app;

import org.apache.log4j.Logger;

/**
 * 后台启动809服务
 * @author tianfei
 *
 */
public class Jt809Server {
	
	private static final Logger LOGGER = Logger.getLogger(Jt809Server.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//加载配置文件 
		ServiceLauncher.launch();
		//加载平台参数
		GlobalConfig.initSystem();
		
		try {
			//启动JT809主从链路
			Boolean res = T809Manager.StartServer();
			if (res == false) {
				LOGGER.error("无法连接上级平台");
				return;
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(),ex);
		}
	}

}
