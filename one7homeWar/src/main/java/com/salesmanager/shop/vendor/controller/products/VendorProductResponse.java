package com.salesmanager.shop.vendor.controller.products;

import java.util.List;



public class VendorProductResponse {
	
	private String status;
    private List<VendorProducts> vendorProducts;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<VendorProducts> getVendorProducts() {
		return vendorProducts;
	}
	public void setVendorProducts(List<VendorProducts> vendorProducts) {
		this.vendorProducts = vendorProducts;
	}

    	

}
