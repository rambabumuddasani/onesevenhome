package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApproveTestimonialRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long testimonialId;
	private String status;
	
	public Long getTestimonialId() {
		return testimonialId;
	}
	public void setTestimonialId(Long testimonialId) {
		this.testimonialId = testimonialId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
