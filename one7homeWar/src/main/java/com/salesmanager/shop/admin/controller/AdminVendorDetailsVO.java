package com.salesmanager.shop.admin.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AdminVendorDetailsVO {

	private Long vendorId;
	private String vendorName;
	@JsonIgnore
	private String vendorUserProfile;
	private String count;
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
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
}
