package com.ltmonitor.jt809.protocol.receive;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;


/**
 * 补发定位信息的应答9209
 * @author Administrator
 *
 */
public class DownExgMsgApplyHisGnssDataAck implements IReceiveProtocol {
	public String handle(JT809Message message) {

		String dataBody = message.getMessageBody();
		// 子数据头长度是28.
		int result = Integer.parseInt(dataBody.substring(56, 58));

		String descr = "";
		if (result == 0)
			descr = "成功,上级平台立即补发";
		else if (result == 1)
			descr = "成功,上级平台择机补发";
		else if (result == 2)
			descr = "失败,上级平台无对应申请的定位数据";
		else if (result == 3)
			descr = "失败，申请内容不正确";
		else if (result == 4)
			descr = "失败，其他原因";

		message.setMsgDescr(descr);
		return null;
	}
}
