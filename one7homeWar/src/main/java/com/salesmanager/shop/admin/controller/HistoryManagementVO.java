package com.salesmanager.shop.admin.controller;

import java.math.BigDecimal;
import java.util.Date;

public class HistoryManagementVO {

	private Long HistoryManagementId;
	private Long productId;
	private String productName;
	private BigDecimal productPrice;
	private BigDecimal productDiscountPrice;
	private Date productPriceStartDate;
	private Date productPriceEndDate;
	private String enableFor;
	public Long getHistoryManagementId() {
		return HistoryManagementId;
	}
	public void setHistoryManagementId(Long historyManagementId) {
		HistoryManagementId = historyManagementId;
	}
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
	public Date getProductPriceStartDate() {
		return productPriceStartDate;
	}
	public void setProductPriceStartDate(Date productPriceStartDate) {
		this.productPriceStartDate = productPriceStartDate;
	}
	public Date getProductPriceEndDate() {
		return productPriceEndDate;
	}
	public void setProductPriceEndDate(Date productPriceEndDate) {
		this.productPriceEndDate = productPriceEndDate;
	}
	public String getEnableFor() {
		return enableFor;
	}
	public void setEnableFor(String enableFor) {
		this.enableFor = enableFor;
	}
}
