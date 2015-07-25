package com.ltmonitor.jt809.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ltmonitor.jt809.protocol.send.UpConnectReq;
import com.ltmonitor.jt809.protocol.send.UpExgMsgRealLocation;
import com.ltmonitor.jt809.protocol.send.UpExgMsgRegister;
import com.ltmonitor.jt809.protocol.send.UpLinkTestReq;
import com.ltmonitor.jt809.server.PlatformClient;


/**
 * 定时任务类，主链路心跳、断线重连和车辆注册
 * @author DELL
 *
 */
public class DNSSTimer {
	private Logger logger = Logger.getLogger(DNSSTimer.class);

	private static DNSSTimer instance = null;
	private int counter = 0;
	String message = "";
	public ScheduledExecutorService scheduleService = null;
	
	public DNSSTimer()
	{
	}

	public static final synchronized DNSSTimer getInstance() {
		if (instance == null) {
			instance = new DNSSTimer();
		}
		return instance;
	}
	
	
	public  final void Stop()
	{
		GlobalConfig.isTimerStart = false;
		try {
			if(scheduleService != null)
			scheduleService.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public  final boolean start() {

		if (!GlobalConfig.isTimerStart) {

			scheduleService = Executors.newScheduledThreadPool(1);
			run();
			GlobalConfig.isTimerStart = true;
		}
		return GlobalConfig.isTimerStart;
	}

	public void run() {
		scheduleService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try
				{
					DNSSTimer.this.counter += 1;
					if (DNSSTimer.this.counter % 30 == 0) {
						if (!PlatformClient.session.isConnected())
								PlatformClient.connect();
					}
					if ((DNSSTimer.this.counter > 0 && DNSSTimer.this.counter % 30 == 0)
							&& (GlobalConfig.isConnection)) {
						if ((PlatformClient.session.isConnected())
								&& (!GlobalConfig.isOpenPlat)) {
							// 登录连接请求
							T809Manager.UpConnectReq();
						}
					}
					if ((GlobalConfig.isConnection) && (T809Manager.mainLinkConnected))
					{
							if (DNSSTimer.this.counter % 20 == 0) {
								//主链路心跳
								T809Manager.UpLinkTestReq();
							}
					}
			
				}catch(Exception ex){
					DNSSTimer.this.logger.warn("GNSSTimer异常" + ex);
					
				}
			}
		}, 2L, 1L, TimeUnit.SECONDS);
	}
}
