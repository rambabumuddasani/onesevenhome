package com.salesmanager.shop.admin.controller.products;

public class ProductReviewRequest {
	
		private Double rating;
		private Long userId;
		private Long productId;
		private String description;
		private String descriptionName;
		
		public Double getRating() {
			return rating;
		}
		
		public void setRating(Double rating) {
			this.rating = rating;
		}
		
		public String getDescription() {
			return description;
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		
		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getDescriptionName() {
			return descriptionName;
		}

		public void setDescriptionName(String descriptionName) {
			this.descriptionName = descriptionName;
		}
		
	
}
