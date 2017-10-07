package com.salesmanager.shop.model.customer;

import java.util.List;

public class AddressResponse {
	private String customerName;
	private List<ReadableAddress> shippingAddress ;

	public List<ReadableAddress> getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(List<ReadableAddress> shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
