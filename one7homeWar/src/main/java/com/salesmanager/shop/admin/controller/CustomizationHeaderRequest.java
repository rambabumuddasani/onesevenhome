package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class CustomizationHeaderRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1440813639804051104L;
	
	private String customizationId;

	public String getCustomizationId() {
		return customizationId;
	}

	public void setCustomizationId(String customizationId) {
		this.customizationId = customizationId;
	}
	

}
