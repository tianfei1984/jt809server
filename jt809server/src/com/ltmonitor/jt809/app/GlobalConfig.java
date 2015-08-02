package com.ltmonitor.jt809.app;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.ltmonitor.jt809.entity.CheckRecord;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.model.ProtocolModel;
import com.ltmonitor.jt809.model.VehicleModel;
import com.ltmonitor.jt809.tool.xml.ConfigUtils;
import com.ltmonitor.jt809.tool.xml.XmlUtils;

public class GlobalConfig {
	private static Logger logger = Logger.getLogger(GlobalConfig.class);
	//809平台参数
	public static ParameterModel parModel;
	public static HashMap<String, VehicleModel> vehicleMap;
	
	public static String filterPlateNo;
	
	public static boolean displayMsg;

	public static boolean isConnection;
	public static boolean isTimerStart;
	public static HashMap<Integer, ProtocolModel> protocolMap;
	// 主链路是否已经建立
	public static boolean isOpenPlat = false;
	// 静态车辆数据是否已经注册
	public static boolean isRegist = false;
	// 流水号
	private static long upcounter = 0L;
	public static long downcounter = 0L;
	public static HashMap<String, CheckRecord> chagang = new HashMap<String, CheckRecord>();

	private static Queue<JT809Message> msgQueue = new ConcurrentLinkedQueue<JT809Message>();

	public static void putMsg(JT809Message tm) {
		if (displayMsg == false) {
			//
			if (tm.getMsgType() == 0x1005 || tm.getMsgType() == 0x1006
					|| tm.getMsgType() == 0x1200 || tm.getMsgType() == 0x1400 
					|| tm.getMsgType() == 0x9005 || tm.getMsgType() == 0x9006)
				return;
		}
		if(filterPlateNo!= null && filterPlateNo.length() > 0 )
		{
			//过滤车牌号
			if(tm.getPlateNo() == null || tm.getPlateNo().indexOf(filterPlateNo) < 0)
				return;
		}
		msgQueue.offer(tm);
	}

	public static JT809Message pollMsg() {
		return msgQueue.poll();
	}

	// 获取流水号，用于包的发送
	public static long getSN() {
		return upcounter++;
	}

	public static void initSystem() {
		upcounter = 0;
		try {
			initSendProtocolMap();
			// 平台网络参数配置
			parModel = getModel();
		} catch (Exception ex) {
			logger.error("系统初始化异常", ex);
		}
	}

	public static ParameterModel getModel() {
		Document doc = XmlUtils.parseXml(".\\parameter\\parameter.xml");

		ParameterModel pm = new ParameterModel();
		if (doc != null) {
			Element rootElement = doc.getRootElement();
			List list = rootElement.selectNodes("//parameter");
			int i = 0;
			for (int count = list.size(); i < count; i++) {
				Element parElement = (Element) list.get(i);
				pm.setPlatformIP(parElement.attributeValue("platip"));
				pm.setIdleTime(Integer.valueOf(
						parElement.attributeValue("idletime")).intValue());

				pm.setLocalPort(Integer.valueOf(
						parElement.attributeValue("localport")).intValue());

				pm.setPlatformCenterId(Long.parseLong(parElement
						.attributeValue("platid")));
				pm.setPlatformPass(parElement.attributeValue("platpassword"));
				pm.setPlatformPort(Integer.valueOf(
						parElement.attributeValue("platport")).intValue());

				pm.setPlatformUserName(Integer.valueOf(
						parElement.attributeValue("platuserid")).intValue());

				pm.setMiyaoM(Long.parseLong(parElement.attributeValue("miyaom")));
				pm.setMiyaoA(Long.parseLong(parElement.attributeValue("miyaoa")));
				pm.setMiyaoC(Long.parseLong(parElement.attributeValue("miyaoc")));
				pm.setProtocolVer(parElement.attributeValue("protocolver"));
				pm.setLocalIp(parElement.attributeValue("localip"));
				pm.setLicenseNo(parElement.attributeValue("licenseno"));
				pm.setUsername(parElement.attributeValue("username"));
			}
			return pm;
		}
		return null;
	}

	public static void setModel(ParameterModel pm) {
		Document doc = DocumentHelper.createDocument();

		Element root = doc.addElement("config");
		doc.setRootElement(root);

		Element pe = root.addElement("parameter");
		pe.addAttribute("platip", pm.getPlatformIP());
		pe.addAttribute("idletime", String.valueOf(pm.getIdleTime()));
		pe.addAttribute("localport", String.valueOf(pm.getLocalPort()));
		pe.addAttribute("platid", String.valueOf(pm.getPlatformCenterId()));
		pe.addAttribute("platport", String.valueOf(pm.getPlatformPort()));
		pe.addAttribute("platuserid", String.valueOf(pm.getPlatformUserName()));
		pe.addAttribute("platpassword", String.valueOf(pm.getPlatformPass()));
		pe.addAttribute("miyaom", String.valueOf(pm.getMiyaoM()));
		pe.addAttribute("miyaoa", String.valueOf(pm.getMiyaoA()));
		pe.addAttribute("miyaoc", String.valueOf(pm.getMiyaoC()));
		pe.addAttribute("protocolver", pm.getProtocolVer());
		pe.addAttribute("localip", pm.getLocalIp());
		pe.addAttribute("licenseno", pm.getLicenseNo());
		pe.addAttribute("username", pm.getUsername());
		try {
			FileWriter out = new FileWriter(".\\parameter\\database.xml");
			XmlUtils.printXml(doc, out);
		} catch (Exception ex) {
			//this.logger.warn("获取参数异常：" + ex.getMessage());
		}
	}

	public static void initSendProtocolMap() {
		protocolMap = ConfigUtils.getConfig(".\\parameter\\protocol.xml");
	}
}
