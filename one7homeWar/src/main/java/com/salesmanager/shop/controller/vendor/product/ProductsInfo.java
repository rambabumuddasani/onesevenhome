package com.salesmanager.shop.controller.vendor.product;

public class ProductsInfo {
	
	private Long productId;
	private String productName;
	private boolean isAlredyAdded;
	
	public boolean isAlredyAdded() {
		return isAlredyAdded;
	}
	public void setAlredyAdded(boolean isAlredyAdded) {
		this.isAlredyAdded = isAlredyAdded;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	

}
