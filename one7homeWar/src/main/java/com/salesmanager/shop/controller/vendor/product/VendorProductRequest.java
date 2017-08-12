package com.salesmanager.shop.controller.vendor.product;

import java.util.List;

public class VendorProductRequest {
	
	private String vendorId;
    private List<String> productId;
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public List<String> getProductId() {
		return productId;
	}
	public void setProductId(List<String> productId) {
		this.productId = productId;
	}
    
}
