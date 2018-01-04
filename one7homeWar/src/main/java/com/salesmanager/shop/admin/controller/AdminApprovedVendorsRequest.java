package com.salesmanager.shop.admin.controller;

public class AdminApprovedVendorsRequest {

	private String status;
	private Long vendorId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
}
