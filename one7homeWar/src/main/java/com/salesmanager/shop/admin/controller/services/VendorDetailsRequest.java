package com.salesmanager.shop.admin.controller.services;

import java.io.Serializable;

public class VendorDetailsRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	} 
}
