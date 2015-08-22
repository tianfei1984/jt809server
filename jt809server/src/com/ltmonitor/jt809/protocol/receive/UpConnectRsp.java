 package com.ltmonitor.jt809.protocol.receive;
 
 import org.apache.log4j.Logger;

import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.protocol.IReceiveProtocol;

/**
 * 主链路登录应答  0X1002 
 * @author tianfei
 *
 */
 public class UpConnectRsp
   implements IReceiveProtocol
 {
   private Logger logger = Logger.getLogger(UpConnectRsp.class);
 
   public String handle(JT809Message message)
   {
     String content = message.getMessageBody();
     int flag = Integer.valueOf(content.substring(0, 2), 16).intValue();
     String result = "";
     boolean isSuccess = false;
     switch (flag) {
     case 0:
       com.ltmonitor.jt809.app.GlobalConfig.isOpenPlat = true;
       result = "主连接平台成功！";
       isSuccess = true;
       break;
     case 1:
       result = "IP地址不正确！";
       break;
     case 2:
       result = "接入码不正确！";
       break;
     case 3:
       result = "用户没有注册!";
       break;
     case 4:
       result = "密码错误!";
       break;
     case 5:
       result = "资源紧张稍后再连接！";
       break;
     case 6:
       result = "其他异常！";
       break;
     default:
       result = "Default异常";
     }
     logger.warn(result);
     message.setMsgDescr(result);
     T809Manager.setMainLinkState(isSuccess, result);
 
     return "";
   }
 }

