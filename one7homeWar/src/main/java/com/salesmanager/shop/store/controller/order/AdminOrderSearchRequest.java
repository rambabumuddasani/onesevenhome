package com.salesmanager.shop.store.controller.order;

import java.io.Serializable;

public class AdminOrderSearchRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5244532094108114729L;

	private String searchBy;
	private String searchstring;
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
	public String getSearchstring() {
		return searchstring;
	}
	public void setSearchstring(String searchstring) {
		this.searchstring = searchstring;
	}
}
