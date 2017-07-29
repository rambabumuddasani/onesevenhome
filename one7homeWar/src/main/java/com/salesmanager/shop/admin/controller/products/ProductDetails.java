package com.salesmanager.shop.admin.controller.products;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.salesmanager.core.model.catalog.product.image.ProductImage;



public class ProductDetails {

	Long productId;
	String productTitle;
	String productDescription;
	BigDecimal productOriginalPrice;
	BigDecimal productDiscountPrice;
	String discountPercentage;
	String defaultImage;
	List<String> productImages;
	
	public String getDefaultImage() {
		return defaultImage;
	}
	public void setDefaultImage(String defaultImage) {
		this.defaultImage = defaultImage;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductTitle() {
		return productTitle;
	}
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public BigDecimal getProductOriginalPrice() {
		return productOriginalPrice;
	}
	public void setProductOriginalPrice(BigDecimal productOriginalPrice) {
		this.productOriginalPrice = productOriginalPrice;
	}
	public BigDecimal getProductDiscountPrice() {
		return productDiscountPrice;
	}
	public void setProductDiscountPrice(BigDecimal productDiscountPrice) {
		this.productDiscountPrice = productDiscountPrice;
	}
	public String getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public List<String> getProductImages() {
		return productImages;
	}
	public void setProductImages(List<String> productImages) {
		this.productImages = productImages;
	}
	
	
}
