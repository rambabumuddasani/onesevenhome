/**
 * 
 */
package com.salesmanager.shop.store.controller.customer;

/**
 * @author welcome
 *
 */
public class CustomerDetailsResponse {
	
	private CustomerDetails customerDetails;
	
	private VendorDetails vendorDetails;
	
	private ServiceDetails serviceDetails;

	public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}

	public VendorDetails getVendorDetails() {
		return vendorDetails;
	}

	public void setVendorDetails(VendorDetails vendorDetails) {
		this.vendorDetails = vendorDetails;
	}

	public ServiceDetails getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(ServiceDetails serviceDetails) {
		this.serviceDetails = serviceDetails;
	}	
    
}
