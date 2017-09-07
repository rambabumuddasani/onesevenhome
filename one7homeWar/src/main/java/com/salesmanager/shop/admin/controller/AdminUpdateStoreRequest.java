package com.salesmanager.shop.admin.controller;

import com.salesmanager.core.model.reference.country.Country;

public class AdminUpdateStoreRequest {
	
	private String storeName;
	private String storeCode;
	private String storePhone;
	private String emailAddress;
	private String storeAddress;
	private String storeCity;
	private String storeCountry;
	private String storeState;
	private String storePostalCode;
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getStorePhone() {
		return storePhone;
	}
	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getStoreCity() {
		return storeCity;
	}
	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}
	public String getStoreCountry() {
		return storeCountry;
	}
	public void setStoreCountry(String storeCountry) {
		this.storeCountry = storeCountry;
	}
	public String getStoreState() {
		return storeState;
	}
	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}
	public String getStorePostalCode() {
		return storePostalCode;
	}
	public void setStorePostalCode(String storePostalCode) {
		this.storePostalCode = storePostalCode;
	}

}
