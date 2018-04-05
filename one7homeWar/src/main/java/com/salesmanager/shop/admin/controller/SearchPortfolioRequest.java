package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class SearchPortfolioRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7628206082788384642L;
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
