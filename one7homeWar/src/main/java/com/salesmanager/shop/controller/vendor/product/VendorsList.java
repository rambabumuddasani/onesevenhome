package com.salesmanager.shop.controller.vendor.product;

import java.util.List;

import com.salesmanager.shop.store.controller.customer.VendorResponse;

public class VendorsList {

	private List<VendorResponse> vendorsDataForProduct;

	public List<VendorResponse> getVendorsDataForProduct() {
		return vendorsDataForProduct;
	}

	public void setVendorsDataForProduct(List<VendorResponse> vendorsDataForProduct) {
		this.vendorsDataForProduct = vendorsDataForProduct;
	}
	
}
