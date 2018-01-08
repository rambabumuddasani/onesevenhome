package com.salesmanager.shop.controller.vendor;

public class UserArchitectsRequest {

	private Long vendorId;
	private String status;
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
