package com.salesmanager.shop.admin.controller;

public class VendorRevenueVO {

	private Long vendorId;
	private String vendorName;
	private Integer totalRevenue;
	
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
	public Integer getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(Integer totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

}
