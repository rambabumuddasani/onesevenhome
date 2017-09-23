package com.salesmanager.shop.admin.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VendorProductVO {

	private Long vendorProductId;
	private Long vendorId;
	private String vendorName;
	private Long productId;
	private String productName;
	@JsonIgnore
	private String description;
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
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setVendorProductId(Long id) {
		this.vendorProductId = id;
	}
	public Long getVendorProductId() {
		return vendorProductId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
