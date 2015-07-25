package com.ltmonitor.jt809.protocol.send;

import org.apache.log4j.Logger;

import com.ltmonitor.entity.WarnData;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.protocol.ISendProtocol;
import com.ltmonitor.jt809.server.PlatformClient;
import com.ltmonitor.jt809.tool.Tools;

/**
 * 上报报警处理结果信息 1403
 * @author DELL
 *
 */
public class UpWarnMsgAdptToDoInfo implements ISendProtocol {
	private static Logger logger = Logger.getLogger(UpWarnMsgAdptToDoInfo.class);
	private String plateNo;
	private int plateColor;
	private long infoId;
	private int result;

	private int msgType = 0x1400;
	private int subType = 0x1403;
	public UpWarnMsgAdptToDoInfo(String plateNo, int plateColor, long infoId, int result) {
		this.plateNo = plateNo;
		this.plateColor = plateColor;
		this.infoId = infoId;
		this.result = result;
	}
	

	public JT809Message wrapper() {
		int dataLength = 1  +4 ; // 后续数据体长度

		StringBuilder sb = new StringBuilder();
		sb.append(Tools.ToHexString(plateNo, 21))
				.append(Tools.ToHexString(plateColor, 1))
				.append(Tools.ToHexString(subType, 2))
				.append(Tools.ToHexString(dataLength, 4))
				.append(Tools.ToHexString(infoId, 4))
				.append(Tools.ToHexString(result, 1))
				;

		ParameterModel pm = GlobalConfig.parModel;
		String body = sb.toString();
		return new JT809Message(msgType,  subType,body);
	}

	/**
	 * 废弃不用
	 * @return
	 */
	public String ResolveHandle2() {
		String mess = "";

		ParameterModel parModel = GlobalConfig.parModel;
		try {
			String vehicleNo = Tools
					.turnStrLength(Tools.TurnISN("渝B12345"), 42);

			String vehicleColor = "02";

			String dataType = "1402";

			String alermInfoContent = Tools.TurnISN("报警信息");

			String warnSrc = "01";

			String warnType = "0001";

			String alermTime = "0000000050B5D5FD";

			String infoId = Tools.turnDataLength(
					Long.toHexString(GlobalConfig.getSN()), 8);

			int len = alermInfoContent.length() / 2;

			String lenStr = Tools.turnDataLength(Integer.toHexString(len), 8);

			String lastLenth = Tools.turnDataLength(
					Integer.toHexString(len + 19), 8);

			String childDataBody = vehicleNo + vehicleColor + dataType
					+ lastLenth + warnSrc + warnType + alermTime + infoId
					+ lenStr + alermInfoContent;

			//mess = Tools.getHeaderAndFlag(GNSSImpl.getSN(), childDataBody,
					//1400, parModel.getPlatformCenterId());
			logger.warn("实时上传的报警信息：" + mess);
			if ((mess != null) && (mess.length() > 0)) {
				PlatformClient.session.write(mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mess;
	}
}
