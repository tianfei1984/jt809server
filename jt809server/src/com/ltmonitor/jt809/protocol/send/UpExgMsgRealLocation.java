 package com.ltmonitor.jt809.protocol.send;
 
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.GnssData;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.model.VehicleModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.server.PlatformClient;
import com.ltmonitor.jt809.tool.Tools;

 
 public class UpExgMsgRealLocation
   implements ISendProtocol
 {
   private Logger logger = Logger.getLogger(UpExgMsgRealLocation.class);
 
   private static ArrayList<String> vehicleList = new ArrayList();
   
   private int msgType = 0x1200;
   
   private int subType = 0x1202;
   
   private GnssData gnssData;
   
   public UpExgMsgRealLocation(GnssData gnss)
   {
	   gnssData = gnss;
   }
   
   public JT809Message wrapper()
   {
	   int dataLength = 36;//后续定位数据长度
		StringBuilder sb = new StringBuilder();
	   sb.append(Tools.ToHexString(gnssData.getPlateNo(), 21))
		.append(Tools.ToHexString(gnssData.getPlateColor(), 1))
		.append(Tools.ToHexString(subType, 2))
		.append(Tools.ToHexString(dataLength, 4))
		.append(getGnssData(gnssData));
	   String body =  sb.toString();

		JT809Message mm = new JT809Message(msgType,  subType,body);
		mm.setPlateColor(gnssData.getPlateColor());
		mm.setPlateNo(gnssData.getPlateNo());
		return mm;
   }
 //定位数据部分
 	private String getGnssData(GnssData gnssData)
 	{
 		Date gpsTime = gnssData.getPosTime();
 		
 		Calendar cd = Calendar.getInstance();
 		cd.setTime(gpsTime);

 		String strDate = Tools.ToHexString(cd.get(Calendar.DAY_OF_MONTH), 1)
 				+ Tools.ToHexString(cd.get(Calendar.MONTH) +1, 1)
 				+ Tools.ToHexString(cd.get(Calendar.YEAR), 2);

 		String strTime = Tools.ToHexString(cd.get(Calendar.HOUR_OF_DAY), 1)
 				+ Tools.ToHexString(cd.get(Calendar.MINUTE), 1)
 				+ Tools.ToHexString(cd.get(Calendar.SECOND), 1);
 		StringBuilder sb = new StringBuilder();
 		sb.append(Tools.ToHexString(gnssData.getPosEncrypt(), 1))
 		.append(strDate)
 		.append(strTime)
 		.append(Tools.ToHexString(gnssData.getLongitude(), 4))
 		.append(Tools.ToHexString(gnssData.getLatitude(), 4))
 		.append(Tools.ToHexString(gnssData.getGpsSpeed(), 2))
 		.append(Tools.ToHexString(gnssData.getRecSpeed(), 2))
 		.append(Tools.ToHexString(gnssData.getTotalMileage(), 4))
 		.append(Tools.ToHexString(gnssData.getDirection(), 2))
 		.append(Tools.ToHexString(gnssData.getAltitude(), 2))
 		.append(Tools.ToHexString(gnssData.getVehicleState(), 4))
 		.append(Tools.ToHexString(gnssData.getAlarmState(), 4));
 		
 		return sb.toString();
 	}
 
 
   public static void main(String[] args) {
     UpExgMsgRealLocation up = new UpExgMsgRealLocation(new GnssData());
     up.wrapper();
   }
 }

