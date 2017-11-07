package com.salesmanager.shop.admin.controller;

import java.util.List;

public class AdminTestimonialResponse {

	private List<CustomerTestimonialVO> customerTestimonials;

	public List<CustomerTestimonialVO> getCustomerTestimonials() {
		return customerTestimonials;
	}

	public void setCustomerTestimonials(List<CustomerTestimonialVO> customerTestimonials) {
		this.customerTestimonials = customerTestimonials;
	}
}
