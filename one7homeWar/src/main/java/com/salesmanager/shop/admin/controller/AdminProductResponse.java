package com.salesmanager.shop.admin.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.shop.admin.controller.products.Category;

public class AdminProductResponse {
	private Long productId;
	
	private String imageURL;
	private String productName;
	private BigDecimal productPrice;
	@JsonIgnore
	private BigDecimal productDiscountPrice;
	@JsonIgnore
	private String discountPercentage;
	@JsonIgnore
	private String vendorName;
	@JsonIgnore
	private String vendorLocation;
	@JsonIgnore
	private List<Category> categories;
	@JsonIgnore
	private Date productPriceSpecialEndDate;
	@JsonIgnore
	private Timestamp productPriceSpecialEndTime;
	private String featureProduct;
	private String newProduct;
    private String recommendedProduct;
    private String recentBought;
    private String dealOfDay;
    private String productDescription;
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
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
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public Date getProductPriceSpecialEndDate() {
		return productPriceSpecialEndDate;
	}
	public void setProductPriceSpecialEndDate(Date productPriceSpecialEndDate) {
		this.productPriceSpecialEndDate = productPriceSpecialEndDate;
	}
	public Timestamp getProductPriceSpecialEndTime() {
		return productPriceSpecialEndTime;
	}
	public void setProductPriceSpecialEndTime(Timestamp productPriceSpecialEndTime) {
		this.productPriceSpecialEndTime = productPriceSpecialEndTime;
	}
	public String getFeatureProduct() {
		return featureProduct;
	}
	public void setFeatureProduct(String featureProduct) {
		this.featureProduct = featureProduct;
	}
	public String getNewProduct() {
		return newProduct;
	}
	public void setNewProduct(String newProduct) {
		this.newProduct = newProduct;
	}
	public String getRecommendedProduct() {
		return recommendedProduct;
	}
	public void setRecommendedProduct(String recommendedProduct) {
		this.recommendedProduct = recommendedProduct;
	}
	public String getRecentBought() {
		return recentBought;
	}
	public void setRecentBought(String recentBought) {
		this.recentBought = recentBought;
	}
	public String getDealOfDay() {
		return dealOfDay;
	}
	public void setDealOfDay(String dealOfDay) {
		this.dealOfDay = dealOfDay;
	}
    

}
