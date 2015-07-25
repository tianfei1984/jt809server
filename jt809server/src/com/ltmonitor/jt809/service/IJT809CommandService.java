package com.ltmonitor.jt809.service;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.JT809Command;


public interface IJT809CommandService {

	public abstract IBaseDao getBaseDao();

	public abstract void setBaseDao(IBaseDao value);

	public abstract ICommandHandler getOnRecvCommand();

	public abstract void setOnRecvCommand(ICommandHandler value);

	public abstract int getInterval();

	public abstract void setInterval(int value);

	// 启动命令解析线程，自动解析命令，并发送给终端
	public abstract void Start();

	public abstract void Stop();

	public abstract void ParseCommand();

	// 根据消息的流水号来更新状态
	//public abstract JT809Command UpdateStatus(String GpsId, int SN,
			//String status);

	public abstract void UpdateCommand(JT809Command tc);

	//public abstract JT809Command getCommandBySn(int sn);

	void save(JT809Command jc);

	// 不对非法命令格式进行解析，在命令录入时确保格式正确
	//public abstract T808Message Parse(JT809Command tc);

}