package com.salesmanager.core.business.modules.services;

import java.util.Date;

public class WorkerRatingVO {
	
	private Integer id;
	private Integer rating;
	private String ratingDesc;
	private Date createDate;
	private String customerName;
	private Integer totalRating;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getRatingDesc() {
		return ratingDesc;
	}
	public void setRatingDesc(String ratingDesc) {
		this.ratingDesc = ratingDesc;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getTotalRating() {
		return totalRating;
	}
	public void setTotalRating(Integer totalRating) {
		this.totalRating = totalRating;
	}
	
	

}
