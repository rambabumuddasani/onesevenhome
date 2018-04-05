package com.salesmanager.shop.admin.controller.services;

import java.io.Serializable;

public class SearchServicesBookingRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4670118532415029484L;
	private String searchFor;
	private String searchBy;
	private String searchString;
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

}
