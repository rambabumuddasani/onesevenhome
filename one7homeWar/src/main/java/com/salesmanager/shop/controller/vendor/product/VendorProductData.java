package com.salesmanager.shop.controller.vendor.product;

public class VendorProductData {

	private String productName;
	private String productPrice;
	private String productImg;
	private long productId;
	private String productCode;
	private Long vendorProuctId;

	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public Long getVendorProuctId() {
		return vendorProuctId;
	}
	public void setVendorProuctId(Long vendorProuctId) {
		this.vendorProuctId = vendorProuctId;
	}

	
}
