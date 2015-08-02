 package com.ltmonitor.jt809.protocol.receive;
 
import org.apache.log4j.Logger;
import com.ltmonitor.entity.WarnData;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

/**
 * 报警预警消息
 * @author tianfei
 *
 */
public class DownWarnMsgInformTips
   implements IReceiveProtocol
 {
   private Logger logger = Logger.getLogger(DownWarnMsgInformTips.class);
 
   public String handle(JT809Message message)
   {
	   String dataBody = message.getMessageBody();

		MessageParser mp = new MessageParser(dataBody, 28);
		WarnData wd = new WarnData();
		wd.setSrc(mp.getInt(1));
		wd.setType(mp.getInt(2));
		wd.setWarnTime(mp.getUtcDate());
		int contentLength = mp.getInt(4);
		wd.setContent(mp.getString(contentLength));
		
		message.setMsgDescr("预警内容:" + wd.getContent()
				+ "预警时间:" + wd.getWarnTime());
     return "";
   }
 }

