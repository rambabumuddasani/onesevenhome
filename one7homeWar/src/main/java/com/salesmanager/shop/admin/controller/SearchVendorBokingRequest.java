package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class SearchVendorBokingRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8342481552877993243L;
	
	private String searchFor;
	private String searchBy;
	private String searchString;
	private String vendorType;
	public String getSearchFor() {
		return searchFor;
	}
	public void setSearchFor(String searchFor) {
		this.searchFor = searchFor;
	}
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public String getVendorType() {
		return vendorType;
	}
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

}
