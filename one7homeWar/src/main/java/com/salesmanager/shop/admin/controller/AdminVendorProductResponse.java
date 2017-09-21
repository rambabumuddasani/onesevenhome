package com.salesmanager.shop.admin.controller;

import java.util.List;

public class AdminVendorProductResponse {

	List<VendorProductVO> vendorProducts;
	private String errorMsg;

	public List<VendorProductVO> getVendorProducts() {
		return vendorProducts;
	}

	public void setVendorProducts(List<VendorProductVO> vendorProducts) {
		this.vendorProducts = vendorProducts;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
