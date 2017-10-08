package com.salesmanager.shop.admin.controller.products;

import java.math.BigDecimal;
import java.util.Date;



public class ProductDiscountRequest {

	Long productId;
	BigDecimal productDiscountPrice;
	private Date productPriceSpecialEndDate;
	private Date productPriceSpecialStartDate;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getProductDiscountPrice() {
		return productDiscountPrice;
	}
	public void setProductDiscountPrice(BigDecimal productDiscountPrice) {
		this.productDiscountPrice = productDiscountPrice;
	}
	public Date getProductPriceSpecialEndDate() {
		return productPriceSpecialEndDate;
	}
	public void setProductPriceSpecialEndDate(Date productPriceSpecialEndDate) {
		this.productPriceSpecialEndDate = productPriceSpecialEndDate;
	}
	public Date getProductPriceSpecialStartDate() {
		return productPriceSpecialStartDate;
	}
	public void setProductPriceSpecialStartDate(Date productPriceSpecialStartDate) {
		this.productPriceSpecialStartDate = productPriceSpecialStartDate;
	}
	
}
