package com.ltmonitor.jt809.app;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ltmonitor.dao.IBaseDao;
import com.ltmonitor.entity.PlatformState;
import com.ltmonitor.entity.VehicleData;
import com.ltmonitor.jt809.service.IJT809CommandService;
import com.ltmonitor.service.IBaseService;
import com.ltmonitor.service.ITransferService;

public class ServiceLauncher {

	private static Logger logger = Logger.getLogger(ServiceLauncher.class);
	protected static ApplicationContext context = null;

	private static IBaseDao BaseDao;
	
	private static IJT809CommandService commandService;
	
	private static ITransferService transferService;
	
	private static IBaseService baseService;

	public static void launch() {

		// PropertyConfigurator.configure("log4j.properties");
		// context = new ClassPathXmlApplicationContext(
		// "classpath:applicationContextService.xml");
		context = new ClassPathXmlApplicationContext(new String[] {
				"applicationContextService-resources.xml",
				"applicationContextService.xml",
				"applicationContext-jt809.xml",
				"applicationContextService-rmi.xml",
				"applicationContextService-dao.xml" });
		if (context == null) {
			int x = 0;
		}

		commandService = (IJT809CommandService) getBean("jt809CommandService");
		transferService = (ITransferService) getBean("transferService");
		baseService = (IBaseService) getBean("baseService");
		BaseDao = ((IBaseDao) getBean("baseDao"));
		logger.info("成功加载数据库和服务");
		// URL log4jRes = ServiceLauncher.class.getResource("log4j.properties");
	}

	public static Object getBean(String beanID) {
		return context.getBean(beanID);
	}

	public static void main(String[] args) {
		try {
			ServiceLauncher launcher = new ServiceLauncher();

			launcher.launch();
			IBaseDao dao = (IBaseDao) launcher.getBean("entityManager");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println(ex.getStackTrace());
		}
	}
	/**
	 * 获取平台的主从连接状态
	 * @return
	 */
	public static PlatformState getPlateformState()
	{
		return baseService.getPlatformState();
	}
	/**
	 * 更新平台的主从连接状态
	 * @param state
	 */
	public static void updatePlateformState(PlatformState state){
		baseService.saveOrUpdate(state);
	}

	public void CreateTestVehicle() {
		String plateNo = "测A";
		String sn = "139";
		for (int m = 0; m < 100; m++) {
			String str = "" + m;
			while (str.length() < 5) {
				str = "0" + str;
			}
			VehicleData vd = new VehicleData();
			vd.setPlateNo(str);

		}
	}

	public static IBaseDao getBaseDao() {
		return BaseDao;
	}

	public static void setBaseDao(IBaseDao baseDao) {
		BaseDao = baseDao;
	}

	public static IJT809CommandService getCommandService() {
		return commandService;
	}

	public static void setCommandService(IJT809CommandService commandService) {
		ServiceLauncher.commandService = commandService;
	}

}
