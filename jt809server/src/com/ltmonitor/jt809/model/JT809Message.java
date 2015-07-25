package com.ltmonitor.jt809.model;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.tool.Tools;


public class JT809Message {
	// 0x5b开始标志
	private String headFlag;
	// 整个809包的长度 1 + 4 + 4 + 2 + 4 + 3 + 1 + 4 + 2 + 1 + 数据体的长度
	private long msgLength; // 4个字节无符号，只能向高一级的类型转换
	// 4个字节的流水号
	private long msgSN;
	// 2个字节
	private int msgType;
	// 下级平台接入码
	private long msgGNSSCenterID;
	// 版本三个字节
	private byte[] versionFlag;
	// 加密标志 0 不加密 1加密
	private byte encryptFlag;

	// 加密密钥 4个字节 无符号整数
	private long encryptKey;
	// 数据体
	private String messageBody;
	// 2个字节
	private String CRCCode;
	// 0x5d结束标志
	private String endFlag;
	//子类型标识
	private int subType;
	//车牌号
	private String plateNo;
	//车牌颜色
	private int plateColor;
	
	private int contentLength;
	//消息内容描述
	private String msgDescr;
	
	private String PacketDescr;

	public JT809Message(int _msgType) {
		msgType = _msgType;
		headFlag = "5b";
		endFlag = "5d";
		versionFlag = new byte[] { 1, 1, 1 };
		msgGNSSCenterID = GlobalConfig.parModel.getPlatformCenterId();
	}
	
	public JT809Message(int _msgType, String body)
	{		
		msgType = _msgType;
		messageBody = body;
		headFlag = "5b";
		endFlag = "5d";
		versionFlag = new byte[] { 1, 1, 1 };
		msgGNSSCenterID = GlobalConfig.parModel.getPlatformCenterId();		
	}
	
	public JT809Message(int _msgType, int _subType,String body)
	{		
		msgType = _msgType;
		subType = _subType;
		messageBody = body;
		headFlag = "5b";
		endFlag = "5d";
		versionFlag = new byte[] { 1, 1, 1 };
		msgGNSSCenterID = GlobalConfig.parModel.getPlatformCenterId();		
	}
	

	public String ToHexString() {
		return Tools.getHeaderAndFlag(GlobalConfig.getSN(), messageBody, msgType,
				msgGNSSCenterID, false);
	}

	public String getCRCCode() {
		return this.CRCCode;
	}

	public void setCRCCode(String CRCCode) {
		this.CRCCode = CRCCode;
	}

	public byte getEncryptFlag() {
		return this.encryptFlag;
	}

	public void setEncryptFlag(byte encryptFlag) {
		this.encryptFlag = encryptFlag;
	}

	public long getEncryptKey() {
		return this.encryptKey;
	}

	public void setEncryptKey(long encryptKey) {
		this.encryptKey = encryptKey;
	}

	public String getHeadFlag() {
		return this.headFlag;
	}

	public void setHeadFlag(String headFlag) {
		this.headFlag = headFlag;
	}

	public String getMessageBody() {
		return this.messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public long getMsgGNSSCenterID() {
		return this.msgGNSSCenterID;
	}

	public void setMsgGNSSCenterID(long msgGNSSCenterID) {
		this.msgGNSSCenterID = msgGNSSCenterID;
	}

	public int getMsgType() {
		return this.msgType;
	}

	public void setMsgType(int msgID) {
		this.msgType = msgID;
	}

	public long getMsgLength() {
		return this.msgLength;
	}

	public void setMsgLength(long msgLength) {
		this.msgLength = msgLength;
	}

	public long getMsgSN() {
		return this.msgSN;
	}

	public void setMsgSN(long msgSN) {
		this.msgSN = msgSN;
	}

	public byte[] getVersionFlag() {
		return this.versionFlag;
	}

	public void setVersionFlag(byte[] versionFlag) {
		this.versionFlag = versionFlag;
	}

	public String getEndFlag() {
		return this.endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public int getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(int plateColor) {
		this.plateColor = plateColor;
	}

	public String getDescr() {
		return msgDescr;
	}

	public void setMsgDescr(String msgDescr) {
		this.msgDescr = msgDescr;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setPacketDescr(String packetDescr) {
		PacketDescr = packetDescr;
	}

	public String getPacketDescr() {
		return PacketDescr;
	}
}
