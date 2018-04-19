package com.salesmanager.shop.admin.controller;

import java.io.Serializable;
import java.util.Date;

public class VendorRevenueSearchRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -436790969412441737L;
	
	private String searchBy;
	private String searchString;
	private String sortBy;
	private Date startDate;
	private Date endDate;
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
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
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

}
