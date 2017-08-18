package com.salesmanager.shop.store.controller.customer;

import java.io.Serializable;

public class VendorRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private String confirmPassword;
	private String vendorName;
	private String vendorOfficeAddress;
	private String vendorMobile;
	private String vendorTelephone;
	private String vendorFax;
	private String vendorConstFirm;
	private String vendorCompanyNature;
	private String vendorRegistrationNo;
	private String vendorPAN;
	private String vendorLicense;
	private String vendorAuthCert;
	private String vendorExpLine;
	private String vendorMajorCust;
	//private String vatRegNo;
	private String termsAndConditions;
	private String activationURL;
	private String vendorTIN;
	
	public String getTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

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
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorOfficeAddress() {
		return vendorOfficeAddress;
	}
	public void setVendorOfficeAddress(String vendorOfficeAddress) {
		this.vendorOfficeAddress = vendorOfficeAddress;
	}
	public String getVendorMobile() {
		return vendorMobile;
	}
	public void setVendorMobile(String vendorMobile) {
		this.vendorMobile = vendorMobile;
	}
	public String getVendorTelephone() {
		return vendorTelephone;
	}
	public void setVendorTelephone(String vendorTelephone) {
		this.vendorTelephone = vendorTelephone;
	}
	public String getVendorFax() {
		return vendorFax;
	}
	public void setVendorFax(String vendorFax) {
		this.vendorFax = vendorFax;
	}
	public String getVendorConstFirm() {
		return vendorConstFirm;
	}
	public void setVendorConstFirm(String vendorConstFirm) {
		this.vendorConstFirm = vendorConstFirm;
	}
	public String getVendorCompanyNature() {
		return vendorCompanyNature;
	}
	public void setVendorCompanyNature(String vendorCompanyNature) {
		this.vendorCompanyNature = vendorCompanyNature;
	}
	public String getVendorRegistrationNo() {
		return vendorRegistrationNo;
	}
	public void setVendorRegistrationNo(String vendorRegistrationNo) {
		this.vendorRegistrationNo = vendorRegistrationNo;
	}
	public String getVendorPAN() {
		return vendorPAN;
	}
	public void setVendorPAN(String vendorPAN) {
		this.vendorPAN = vendorPAN;
	}
	public String getVendorLicense() {
		return vendorLicense;
	}
	public void setVendorLicense(String vendorLicense) {
		this.vendorLicense = vendorLicense;
	}
	public String getVendorAuthCert() {
		return vendorAuthCert;
	}
	public void setVendorAuthCert(String vendorAuthCert) {
		this.vendorAuthCert = vendorAuthCert;
	}
	public String getVendorExpLine() {
		return vendorExpLine;
	}
	public void setVendorExpLine(String vendorExpLine) {
		this.vendorExpLine = vendorExpLine;
	}
	public String getVendorMajorCust() {
		return vendorMajorCust;
	}
	public void setVendorMajorCust(String vendorMajorCust) {
		this.vendorMajorCust = vendorMajorCust;
	}
/*	public String getVatRegNo() {
		return vatRegNo;
	}
	public void setVatRegNo(String vatRegNo) {
		this.vatRegNo = vatRegNo;
	}
*/	public String getActivationURL() {
		return activationURL;
	}
	public void setActivationURL(String activationURL) {
		this.activationURL = activationURL;
	}
	public String getVendorTIN() {
		return vendorTIN;
	}
	public void setVendorTIN(String vendorTIN) {
		this.vendorTIN = vendorTIN;
	}
	

}
