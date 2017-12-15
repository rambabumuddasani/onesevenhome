package com.salesmanager.shop.admin.controller.services;

import java.io.Serializable;

public class AdminServicesBookingRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3324315458331372988L;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
