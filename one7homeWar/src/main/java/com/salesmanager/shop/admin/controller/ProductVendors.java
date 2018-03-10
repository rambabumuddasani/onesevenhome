package com.salesmanager.shop.admin.controller;

public class ProductVendors {

	private Long vendorId;
	private String vendorName;
	private Long orderId;
	
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
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
}
