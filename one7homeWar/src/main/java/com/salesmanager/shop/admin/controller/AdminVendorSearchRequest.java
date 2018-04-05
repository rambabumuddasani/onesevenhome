package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class AdminVendorSearchRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7849296072130256876L;
	private String searchType;
	private String searchFor;
	private String searchString;
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSearchFor() {
		return searchFor;
	}
	public void setSearchFor(String searchFor) {
		this.searchFor = searchFor;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

}
