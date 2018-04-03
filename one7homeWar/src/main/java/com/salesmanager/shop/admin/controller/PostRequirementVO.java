package com.salesmanager.shop.admin.controller;

import java.util.Date;

public class PostRequirementVO {

	private Long postRequirementId;
	//private String state;
	private String query;
	private Long customerId;
	private String customerName;
	private String category;
	//private Date postedDate;
	private Date dateAndTime;
	private String state;
	private String status;
	private String responseMessage;

	
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
	/*public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}*/
	public Date getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
