package com.salesmanager.shop.admin.controller.products;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;



public class CreateProductRequest {

	Long productId;
	String sku;
	String description;
	String title;
	String shortDesc;
	String productDescTitle;
	String discountPrice;
	String productName;
	BigDecimal productPrice;
	String category;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getProductDescTitle() {
		return productDescTitle;
	}
	public void setProductDescTitle(String productDescTitle) {
		this.productDescTitle = productDescTitle;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

}
