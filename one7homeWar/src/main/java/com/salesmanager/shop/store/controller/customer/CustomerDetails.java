package com.salesmanager.shop.store.controller.customer;

import java.util.Date;

import com.salesmanager.core.model.customer.CustomerGender;
import com.salesmanager.shop.model.customer.Address;

public class CustomerDetails {
	
	private String email;
	private Address billing;
	private Address delivery;
	private CustomerGender gender;
	private String language;
	private String firstName;
	private String lastName;
	private String storeCode;
	private String customerType;
	private String activated;
	private String area;
	private Date dob;
	private String userName;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Address getBilling() {
		return billing;
	}
	public void setBilling(Address billing) {
		this.billing = billing;
	}
	public Address getDelivery() {
		return delivery;
	}
	public void setDelivery(Address delivery) {
		this.delivery = delivery;
	}
	public CustomerGender getGender() {
		return gender;
	}
	public void setGender(CustomerGender gender) {
		this.gender = gender;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getActivated() {
		return activated;
	}
	public void setActivated(String activated) {
		this.activated = activated;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
}
