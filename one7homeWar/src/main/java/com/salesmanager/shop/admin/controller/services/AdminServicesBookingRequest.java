package com.salesmanager.shop.admin.controller.services;

import java.io.Serializable;
import java.util.Date;

public class AdminServicesBookingRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3324315458331372988L;
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
