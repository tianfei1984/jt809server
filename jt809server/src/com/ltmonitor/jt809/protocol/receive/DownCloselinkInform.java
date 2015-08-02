package com.ltmonitor.jt809.protocol.receive;

import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

/**
 * 上级平台主动关闭链路通知 0x9008
 * @author tianfei
 *
 */
public class DownCloselinkInform implements IReceiveProtocol {
	public String handle(JT809Message message) {
		return null;
	}
}
