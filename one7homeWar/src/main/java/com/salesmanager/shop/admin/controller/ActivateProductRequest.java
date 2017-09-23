package com.salesmanager.shop.admin.controller;

public class ActivateProductRequest {
   
	private Long vendorProductId;
	private boolean status;
	public Long getVendorProductId() {
		return vendorProductId;
	}
	public void setVendorProductId(Long vendorProductId) {
		this.vendorProductId = vendorProductId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
