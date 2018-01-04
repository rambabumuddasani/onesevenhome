package com.salesmanager.shop.admin.controller;

public class AdminVendorDetailsVO {

	private Long vendorId;
	private String vendorName;
	private String vendorUserProfile;
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
}
