package com.salesmanager.shop.admin.controller.products;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;



public class ProductResponse {

	Long productId;
	String imageURL;
	String productName;
	BigDecimal productPrice;
	BigDecimal productDiscountPrice;
	String discountPercentage;
	String vendorName;
	String vendorLocation;
	List<Category> categories;
	private Date productPriceSpecialEndDate;
	private Timestamp productPriceSpecialEndTime;

	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Date getProductPriceSpecialEndDate() {
		return productPriceSpecialEndDate;
	}
	public void setProductPriceSpecialEndDate(Date productPriceSpecialEndDate) {
		this.productPriceSpecialEndDate = productPriceSpecialEndDate;
	}
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public String getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorLocation() {
		return vendorLocation;
	}
	public void setVendorLocation(String vendorLocation) {
		this.vendorLocation = vendorLocation;
	}
	public Timestamp getProductPriceSpecialEndTime() {
		return productPriceSpecialEndTime;
	}
	public void setProductPriceSpecialEndTime(Timestamp productPriceSpecialEndTime) {
		this.productPriceSpecialEndTime = productPriceSpecialEndTime;
	}
	public BigDecimal getProductDiscountPrice() {
		return productDiscountPrice;
	}
	public void setProductDiscountPrice(BigDecimal productDiscountPrice) {
		this.productDiscountPrice = productDiscountPrice;
	}
	
}