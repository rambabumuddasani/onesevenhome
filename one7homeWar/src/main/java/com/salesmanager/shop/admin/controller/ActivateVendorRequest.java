package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class ActivateVendorRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7456043109283336188L;
	
	private Long vendorId;

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

}
