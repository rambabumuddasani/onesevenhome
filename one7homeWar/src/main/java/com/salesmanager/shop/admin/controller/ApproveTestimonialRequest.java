package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class ApproveTestimonialRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private Long customerId;
	private Long testimonialId;
	private boolean enable;
	/*public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}*/
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public Long getTestimonialId() {
		return testimonialId;
	}
	public void setTestimonialId(Long testimonialId) {
		this.testimonialId = testimonialId;
	}
}
