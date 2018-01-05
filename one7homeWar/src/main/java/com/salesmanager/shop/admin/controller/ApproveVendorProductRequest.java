package com.salesmanager.shop.admin.controller;

import java.util.List;

public class ApproveVendorProductRequest {

	List<Long> vendorProductIds;
	private boolean status;
	private Long vendorId;

	public List<Long> getVendorProductIds() {
		return vendorProductIds;
	}

	public void setVendorProductIds(List<Long> vendorProductIds) {
		this.vendorProductIds = vendorProductIds;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
}
