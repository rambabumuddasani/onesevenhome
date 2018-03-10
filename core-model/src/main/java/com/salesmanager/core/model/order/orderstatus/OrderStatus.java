package com.salesmanager.core.model.order.orderstatus;

public enum OrderStatus {
	
	INITIALIZED("initialized"),
	ORDERED("ordered"),
	PROCESSED("processed"),
	DELIVERED("delivered"),
	REFUNDED("refunded"),
	CANCELED("canceled"),
	COMPLETED("completed"),
	FAILURE("failure");
	
	private String value;
	
	private OrderStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
