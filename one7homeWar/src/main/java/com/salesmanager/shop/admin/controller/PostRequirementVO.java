package com.salesmanager.shop.admin.controller;

import java.util.Date;

public class PostRequirementVO {

	private Long postRequirementId;
	//private String state;
	private String query;
	private Long customerId;
	private String customerName;
	private String category;
	private Date postedDate;
	public Long getPostRequirementId() {
		return postRequirementId;
	}
	public void setPostRequirementId(Long postRequirementId) {
		this.postRequirementId = postRequirementId;
	}
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
}
