package com.salesmanager.shop.store.controller.customer;

import java.io.Serializable;
import java.util.List;

public class ServicesRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private String confirmPassword;
	private String companyName;
	private String houseNumber;
	private String street;
	private String area;
	private String city;
	private String state;
	private String pinCode;
	private String country;
	private String contactNumber;
	private String profileImage;
	private String serviceFax;
	private String constFirm;
	private String companyNature;
	private String registrationNo;
	private String servicePAN;
	private String license;
	private String expLine;
	private String majorCust;
	private String vatRegNo;
	private boolean termsAndConditions;
	private String activationURL;
	private String serviceTIN;
	List<Integer> serviceIds;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public String getServiceFax() {
		return serviceFax;
	}
	public void setServiceFax(String serviceFax) {
		this.serviceFax = serviceFax;
	}
	public String getConstFirm() {
		return constFirm;
	}
	public void setConstFirm(String constFirm) {
		this.constFirm = constFirm;
	}
	public String getCompanyNature() {
		return companyNature;
	}
	public void setCompanyNature(String companyNature) {
		this.companyNature = companyNature;
	}
	public String getRegistrationNo() {
		return registrationNo;
	}
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}
	public String getServicePAN() {
		return servicePAN;
	}
	public void setServicePAN(String servicePAN) {
		this.servicePAN = servicePAN;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getExpLine() {
		return expLine;
	}
	public void setExpLine(String expLine) {
		this.expLine = expLine;
	}
	public String getMajorCust() {
		return majorCust;
	}
	public void setMajorCust(String majorCust) {
		this.majorCust = majorCust;
	}
	public String getVatRegNo() {
		return vatRegNo;
	}
	public void setVatRegNo(String vatRegNo) {
		this.vatRegNo = vatRegNo;
	}
	public boolean isTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(boolean termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
	public String getActivationURL() {
		return activationURL;
	}
	public void setActivationURL(String activationURL) {
		this.activationURL = activationURL;
	}
	public String getServiceTIN() {
		return serviceTIN;
	}
	public void setServiceTIN(String serviceTIN) {
		this.serviceTIN = serviceTIN;
	}
	public List<Integer> getServiceIds() {
		return serviceIds;
	}
	public void setServiceIds(List<Integer> serviceIds) {
		this.serviceIds = serviceIds;
	}
	
}
