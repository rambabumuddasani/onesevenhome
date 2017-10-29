package com.salesmanager.shop.admin.controller.products;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.salesmanager.core.model.catalog.product.image.ProductImage;



public class ProductDetails {

	Long productId;
	String productName;
	String sku;
	String productTitle;
	String productDescription;
	String shortDesc;
	String productDescTitle;
	BigDecimal productOriginalPrice;
	BigDecimal productDiscountPrice;
	String discountPercentage;
	String defaultImage;
	List<String> productImages;
	List<ProductFilterType> productFilterTypeList;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
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
	public String getDefaultImage() {
		return defaultImage;
	}
	public void setDefaultImage(String defaultImage) {
		this.defaultImage = defaultImage;
	}
	public List<String> getProductImages() {
		return productImages;
	}
	public void setProductImages(List<String> productImages) {
		this.productImages = productImages;
	}
	public List<ProductFilterType> getProductFilterTypeList() {
		return productFilterTypeList;
	}
	public void setProductFilterTypeList(
			List<ProductFilterType> productFilterTypeList) {
		this.productFilterTypeList = productFilterTypeList;
	}
	
}
