package com.salesmanager.shop.model.customer;

import java.io.Serializable;

public class ReadableAddress implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5517843346988367141L;

	private String company;
	private String phone;
	private String address;
	private String city;
	private String postalCode;
	private String area;
	private String state;
	private String country;
	private  String preferenceOrder ; // 1 -> 1st , 2 -> 2nd and 3 -> 3rd
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getPreferenceOrder() {
		return preferenceOrder;
	}
	public void setPreferenceOrder(String preferenceOrder) {
		this.preferenceOrder = preferenceOrder;
	}
}
