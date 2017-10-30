package com.salesmanager.shop.admin.controller.products;


import java.io.Serializable;

public class ProductImageRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	Long productId;
	String defaultImage;
	String imageURL;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getDefaultImage() {
		return defaultImage;
	}
	public void setDefaultImage(String defaultImage) {
		this.defaultImage = defaultImage;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
}
