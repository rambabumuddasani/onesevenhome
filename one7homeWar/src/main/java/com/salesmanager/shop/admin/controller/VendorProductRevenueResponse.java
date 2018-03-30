package com.salesmanager.shop.admin.controller;

import java.util.List;

public class VendorProductRevenueResponse {

	List<RevenueProducts> revenueProducts;
	List<RevenueVendors> revenueVendors;

	public List<RevenueProducts> getRevenueProducts() {
		return revenueProducts;
	}

	public void setRevenueProducts(List<RevenueProducts> revenueProducts) {
		this.revenueProducts = revenueProducts;
	}

	public List<RevenueVendors> getRevenueVendors() {
		return revenueVendors;
	}

	public void setRevenueVendors(List<RevenueVendors> revenueVendors) {
		this.revenueVendors = revenueVendors;
	}
	
}
