package com.salesmanager.shop.admin.controller.products;

import java.util.List;

public class RecommendedProduct {
	
	List<ProductResponse> recommendedProducts;

	public List<ProductResponse> getRecommendedProducts() {
		return recommendedProducts;
	}

	public void setRecommendedProducts(List<ProductResponse> recommendedProducts) {
		this.recommendedProducts = recommendedProducts;
	}
	

}
