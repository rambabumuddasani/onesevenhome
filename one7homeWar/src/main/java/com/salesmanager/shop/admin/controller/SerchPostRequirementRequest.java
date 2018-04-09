package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class SerchPostRequirementRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3947859520635712293L;
	
	private String searchBy;
	private String searchString;
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
