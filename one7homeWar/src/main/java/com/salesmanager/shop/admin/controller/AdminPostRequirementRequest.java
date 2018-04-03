package com.salesmanager.shop.admin.controller;

import java.io.Serializable;
import java.util.Date;

public class AdminPostRequirementRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7249226075458488426L;
	private String status;
	private Date startDate;
	private Date endDate;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
