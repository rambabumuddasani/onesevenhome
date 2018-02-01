package com.salesmanager.shop.admin.controller.products;

import java.io.Serializable;

public class TodaysDealRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9028330089889918911L;

	private String categoryCode;
	private String status;
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
