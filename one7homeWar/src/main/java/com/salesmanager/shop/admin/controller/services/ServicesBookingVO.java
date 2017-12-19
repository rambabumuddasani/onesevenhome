package com.salesmanager.shop.admin.controller.services;

import java.util.Date;

public class ServicesBookingVO {

	private Long servicesBookingId;
	private Date bookingDate;
	private String serviceType;
	CustomerBookingDetails customerBookingdetails;
	ServiceProviderDetails serviceProviderDetails;
	private String status;
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
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public ServiceProviderDetails getServiceProviderDetails() {
		return serviceProviderDetails;
	}
	public void setServiceProviderDetails(ServiceProviderDetails serviceProviderDetails) {
		this.serviceProviderDetails = serviceProviderDetails;
	}
	public CustomerBookingDetails getCustomerBookingdetails() {
		return customerBookingdetails;
	}
	public void setCustomerBookingdetails(CustomerBookingDetails customerBookingdetails) {
		this.customerBookingdetails = customerBookingdetails;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
