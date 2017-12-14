package com.salesmanager.shop.admin.controller.services;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServicesBookingVO {

	private Long servicesBookingId;
	private Date bookingDate;
	private String customerName;
	@JsonIgnore
	private String serviceId;
	private String serviceType;
	public Long getServicesBookingId() {
		return servicesBookingId;
	}
	public void setServicesBookingId(Long servicesBookingId) {
		this.servicesBookingId = servicesBookingId;
	}
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
}
