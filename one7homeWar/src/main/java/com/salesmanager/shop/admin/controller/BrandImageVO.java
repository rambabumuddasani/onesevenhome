package com.salesmanager.shop.admin.controller;

public class BrandImageVO {

	private Long brandId;
	private String brandName;
	private String brangImage;
	private String status;
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrangImage() {
		return brangImage;
	}
	public void setBrangImage(String brangImage) {
		this.brangImage = brangImage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
