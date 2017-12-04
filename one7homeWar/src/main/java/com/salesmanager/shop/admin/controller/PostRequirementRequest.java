package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class PostRequirementRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private String state;
	private String query;
	private Long customerId;
	private String category;
	/*public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}*/
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}
