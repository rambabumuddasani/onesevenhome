package com.salesmanager.shop.admin.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VendorProductVO {

	private Long vendorProductId;
	private Long vendorId;
	private String vendorName;
	private Long productId;
	private String productName;
	private String imageURL;
	private String productDescription;
	private String vendorAddress;
	private String vendorMobile;
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
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getVendorAddress() {
		return vendorAddress;
	}
	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}
	public String getVendorMobile() {
		return vendorMobile;
	}
	public void setVendorMobile(String vendorMobile) {
		this.vendorMobile = vendorMobile;
	}
	
	
}
