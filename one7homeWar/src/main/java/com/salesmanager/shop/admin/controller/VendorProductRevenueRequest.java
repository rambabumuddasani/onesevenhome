package com.salesmanager.shop.admin.controller;

import java.io.Serializable;
import java.util.Date;

public class VendorProductRevenueRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5723599437687401790L;
	private Date startDate;
	private Date endDate;
	private Long vendorId;
	private String productSku;
	private String searchString;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public String getProductSku() {
		return productSku;
	}
	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}
