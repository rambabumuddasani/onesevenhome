package com.salesmanager.shop.admin.controller.products;




public class ProductImageRequest {

	Long productId;
	boolean isDefaultImage;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public boolean isDefaultImage() {
		return isDefaultImage;
	}
	public void setDefaultImage(boolean isDefaultImage) {
		this.isDefaultImage = isDefaultImage;
	}
	
}
