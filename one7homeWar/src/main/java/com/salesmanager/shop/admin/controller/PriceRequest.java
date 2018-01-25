package com.salesmanager.shop.admin.controller;

import java.io.Serializable;
import java.math.BigDecimal;

public class PriceRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8862390338708333045L;
	private Long  productId;
	private boolean status;
	private BigDecimal discountValue;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}
}
