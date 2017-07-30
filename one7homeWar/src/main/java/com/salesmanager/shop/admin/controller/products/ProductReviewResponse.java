package com.salesmanager.shop.admin.controller.products;

import java.util.List;

public class ProductReviewResponse {
	
	private Double avgReview;
	private Long totalRatingCount;
	private List<ProductReviewVO> productReviews;
	
	public List<ProductReviewVO> getProductReviews() {
		return productReviews;
	}
	public void setProductReviews(List<ProductReviewVO> productReviews) {
		this.productReviews = productReviews;
	}
	
	public Double getAvgReview() {
		return avgReview;
	}
	public void setAvgReview(Double avgReview) {
		this.avgReview = avgReview;
	}
	public Long getTotalRatingCount() {
		return totalRatingCount;
	}
	public void setTotalRatingCount(Long totalRatingCount) {
		this.totalRatingCount = totalRatingCount;
	}
		
}
