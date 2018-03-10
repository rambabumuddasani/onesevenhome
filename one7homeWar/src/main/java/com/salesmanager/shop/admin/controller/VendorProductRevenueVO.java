package com.salesmanager.shop.admin.controller;

import java.util.List;

public class VendorProductRevenueVO {

	private Long vendorId;
	private String vendorName;
	private List<VendorProducts> vendorProducts;
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
	public List<VendorProducts> getVendorProducts() {
		return vendorProducts;
	}
	public void setVendorProducts(List<VendorProducts> vendorProducts) {
		this.vendorProducts = vendorProducts;
	}
	public Integer getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(Integer totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
}
