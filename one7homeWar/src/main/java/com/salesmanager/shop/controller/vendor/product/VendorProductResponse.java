package com.salesmanager.shop.controller.vendor.product;

import java.util.List;



public class VendorProductResponse {
	
	private String venderId;
	private List<ProductsInfo> vendorProducts;

    public String getVenderId() {
		return venderId;
	}
	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}
	public List<ProductsInfo> getVendorProducts() {
		return vendorProducts;
	}
	public void setVendorProducts(List<ProductsInfo> vendorProducts) {
		this.vendorProducts = vendorProducts;
	}

    	

}
