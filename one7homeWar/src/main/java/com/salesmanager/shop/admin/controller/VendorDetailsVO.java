package com.salesmanager.shop.admin.controller;

public class VendorDetailsVO {

	private Long vendorId;
	private String vendorName;
	private String vendorUserProfile;
	private String status;
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorUserProfile() {
		return vendorUserProfile;
	}
	public void setVendorUserProfile(String vendorUserProfile) {
		this.vendorUserProfile = vendorUserProfile;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
