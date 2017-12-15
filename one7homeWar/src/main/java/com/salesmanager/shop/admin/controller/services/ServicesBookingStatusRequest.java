package com.salesmanager.shop.admin.controller.services;

import java.io.Serializable;

public class ServicesBookingStatusRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2200496710264787846L;
	private Long servicesBookingId;
	private String status;
	private String comment;
	public Long getServicesBookingId() {
		return servicesBookingId;
	}
	public void setServicesBookingId(Long servicesBookingId) {
		this.servicesBookingId = servicesBookingId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
