package com.salesmanager.shop.admin.controller;

public class EnableBrandImageRequest {

	private String status;
	private Long brandImageId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getBrandImageId() {
		return brandImageId;
	}

	public void setBrandImageId(Long brandImageId) {
		this.brandImageId = brandImageId;
	}
}
