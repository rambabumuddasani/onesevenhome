package com.salesmanager.shop.controller.vendor;

import java.io.Serializable;
import java.math.BigDecimal;

public class VendorFilterRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2926054043626331065L;

	private BigDecimal rating;
	private String vendorType;
	private String searchSubCategory;
	public BigDecimal getRating() {
		return rating;
	}
	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
	public String getVendorType() {
		return vendorType;
	}
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	public String getSearchSubCategory() {
		return searchSubCategory;
	}
	public void setSearchSubCategory(String searchSubCategory) {
		this.searchSubCategory = searchSubCategory;
	}
	
}
