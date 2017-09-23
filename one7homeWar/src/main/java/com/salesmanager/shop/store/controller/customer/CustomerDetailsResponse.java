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
    
}
