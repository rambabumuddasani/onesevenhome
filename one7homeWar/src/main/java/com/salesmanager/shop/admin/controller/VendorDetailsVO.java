package com.salesmanager.shop.admin.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VendorDetailsVO {

	private Long vendorId;
	private String vendorName;
	@JsonIgnore
	private String vendorUserProfile;
	private String status;
	private String vendorType;
	private String customerName;
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
	public String getVendorType() {
		return vendorType;
	}
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
