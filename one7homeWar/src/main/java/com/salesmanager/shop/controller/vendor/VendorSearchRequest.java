package com.salesmanager.shop.controller.vendor;

import java.io.Serializable;

public class VendorSearchRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4356429645325028718L;
	private String searchString;
	private String customerType;
	private String searchSubCategory;
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getSearchSubCategory() {
		return searchSubCategory;
	}
	public void setSearchSubCategory(String searchSubCategory) {
		this.searchSubCategory = searchSubCategory;
	}
}
