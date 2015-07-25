package com.ltmonitor.jt809.model;

import java.util.ArrayList;

public class VehicleModel {
	// 车牌号
	private String plateNo;
	// 车牌颜色
	private int plateColor;
	/**
	 * 行业运输类型
	 */
	private String transType;
	/**
	 * 车籍地
	 */
	private String nationallity;
	private String vehicleType;
	private String ownerName;
	private String ownerId;
	private String ownerTel;

	public VehicleModel() {
		plateNo = "苏A53251";
		plateColor = 1;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public void setOwnerName(String owners_name) {
		this.ownerName = owners_name;
	}

	public String getOwnerId() {
		return this.ownerId;
	}



	public String getTransType() {
		return this.transType;
	}

	public void setTransType(String trans_type) {
		this.transType = trans_type;
	}


	public String getNationallity() {
		return this.nationallity;
	}


	public String getPlateNo() {
		return this.plateNo;
	}

	public void setPlateNo(String vehicle_no) {
		this.plateNo = vehicle_no;
	}


	public String getVehicleType() {
		return this.vehicleType;
	}


	public int getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(int plateColor) {
		this.plateColor = plateColor;
	}

	public String getOwnerTel() {
		return ownerTel;
	}

	public void setOwnerTel(String ownerTel) {
		this.ownerTel = ownerTel;
	}

	public void setNationallity(String nationallity) {
		this.nationallity = nationallity;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
