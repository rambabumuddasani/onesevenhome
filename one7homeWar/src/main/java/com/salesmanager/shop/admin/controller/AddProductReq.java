package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class AddProductReq implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productId;
	//private String startDate;
	//private String endDate;
	//private String discountPrice;
	//private String isfeatureProduct;
	private String status;
	private String title;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/*public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}*/
	/*public String getIsfeatureProduct() {
		return isfeatureProduct;
	}
	public void setIsfeatureProduct(String isfeatureProduct) {
		this.isfeatureProduct = isfeatureProduct;
	}*/
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
