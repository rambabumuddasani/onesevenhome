package com.salesmanager.shop.admin.controller;

public class ProductRevenueVO {

	private String productId;
	private String productName;
	private Integer totalRevenue;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(Integer totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
}
