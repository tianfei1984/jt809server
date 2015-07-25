package com.ltmonitor.jt809.protocol.send;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.GnssData;
import com.ltmonitor.entity.TakePhotoModel;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.tool.Tools;


public class UpCtrlMsgTakePhotoAck implements ISendProtocol {
	private static Logger logger = Logger
			.getLogger(UpCtrlMsgMonitorVehicleAck.class);

	private TakePhotoModel photo;
	private int msgType = 0x1500;
	// UP_CTRL_MSG_TAKE_PHOTO_ACK = 0x1502,
	private int subType = 0x1502;
	
	public UpCtrlMsgTakePhotoAck()
	{
		photo = new TakePhotoModel();
		
	}

	public UpCtrlMsgTakePhotoAck(TakePhotoModel _photo)
	{
		photo = _photo;
		
	}
	
	public JT809Message wrapper() {
		byte[] photoData = photo.getPhotoData();//getPhotoData(photo.getFilePath());
		if(photoData == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		int dataLength = 1 + 36 + 1 + 4 + 1 + 1 + photoData.length;; // 后续数据长度
		
		GnssData gnssData = photo.getGnssData();

		String photoDataStr = Tools.ToHexString(photoData);
		int dataLen = photoDataStr.length();
		
		String gnssDataStr = getGnssData(gnssData);
		int gnssStrLen = gnssDataStr.length();
		sb.append(Tools.ToHexString(photo.getPlateNo(), 21))
				.append(Tools.ToHexString(photo.getPlateColor(), 1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(photo.getReplayResult(), 1))
				.append(gnssDataStr)
				.append(Tools.ToHexString(photo.getLensId(), 1))
				.append(Tools.ToHexString(photoData.length, 4))
				.append(Tools.ToHexString(photo.getPhotoSizeType(), 1))
				.append(Tools.ToHexString(photo.getPhotoFormat(), 1))
		.append(photoDataStr) //照片数据
		;
		
		String messageBody = sb.toString();
		int len = messageBody.length();

		JT809Message mm = new JT809Message(msgType,  subType, messageBody);
		mm.setPlateColor(photo.getPlateColor());
		mm.setPlateNo(photo.getPlateNo());
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

	private int bufferLength = 409600;

	private byte[] getPhotoData(String fileName) {
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(
					new FileInputStream(fileName));
			ByteArrayOutputStream out = new ByteArrayOutputStream(bufferLength);

			System.out.println("Available bytes:" + in.available());

			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			in.close();

			byte[] content = out.toByteArray();
			return content;
		} catch (Exception ex) {

		}finally
		{
		    if(in != null)
		    {
		        try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    
		    }
		}
		return null;
	}

}
