package com.salesmanager.shop.admin.controller;

import java.util.List;

public class ProductVendorRevenueVO {

	private String productSku;
	private String productName;
	private List<ProductVendors> productVendors;
	private Integer totalRevenue;
	public String getProductSku() {
		return productSku;
	}
	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<ProductVendors> getProductVendors() {
		return productVendors;
	}
	public void setProductVendors(List<ProductVendors> productVendors) {
		this.productVendors = productVendors;
	}
	public Integer getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(Integer totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
}
