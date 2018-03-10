package com.salesmanager.shop.admin.controller;

import java.math.BigDecimal;
import java.util.Date;

public class VendorProducts {

	private Long productId;
	private String productName;
	private Integer productRevenue;
	private Long orderId;
	private Date purchasedDate;
	private BigDecimal productPrice;
	private Integer productquantity;
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
	public Integer getProductRevenue() {
		return productRevenue;
	}
	public void setProductRevenue(Integer productRevenue) {
		this.productRevenue = productRevenue;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Date getPurchasedDate() {
		return purchasedDate;
	}
	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public Integer getProductquantity() {
		return productquantity;
	}
	public void setProductquantity(Integer productquantity) {
		this.productquantity = productquantity;
	}
}
