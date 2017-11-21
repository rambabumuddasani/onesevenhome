package com.salesmanager.shop.admin.controller;

import java.util.List;

public class AdminBrandImageResponse {

	List<BrandImageVO> brandImages;

	public List<BrandImageVO> getBrandImages() {
		return brandImages;
	}

	public void setBrandImages(List<BrandImageVO> brandImages) {
		this.brandImages = brandImages;
	}
}
