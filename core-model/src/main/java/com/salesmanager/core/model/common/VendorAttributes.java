package com.salesmanager.core.model.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.zone.Zone;

@Embeddable
public class VendorAttributes {
	
	@Column (name ="VENDOR_NAME", length=64)
	private String vendorName;

	@Column (name ="VENDOR_OFFICE_ADDRESS", length=256)
	private String vendorOfficeAddress;

	@Column(name="VENDOR_MOBILE", length=32)
	private String vendorMobile;

	@Column(name="VENDOR_TELEPHONE", length=32)
	private String vendorTelephone;

	@Column (name ="VENDOR_FAX", length=32)
	private String vendorFax;
	
	@Column (name ="VENDOR_CONSTITUTION_FIRM", length=120)
	private String vendorConstFirm;

	@Column (name ="VENDOR_COMPANY_NATURE", length=120)
	private String vendorCompanyNature;

	@Column (name ="VENDOR_REGISTRATION_NUMBER", length=20)
	private String vendorRegistrationNo;

	@Column (name ="VENDOR_PAN_NUMBER", length=20)
	private String vendorPAN;

	@Column (name ="VENDOR_LICENCE_NUMBER", length=20)
	private String vendorLicense;

	@Column (name ="VENDOR_AUTH_CERT", length=200)
	private String vendorAuthCert;

	@Column (name ="VENDOR_EXP_LINE", length=200)
	private String vendorExpLine;

	@Column (name ="VENDOR_MAJOR_CUST", length=200)
	private String vendorMajorCust;
	
/*	@Column (name ="VENDOR_ACCEPT_TERMS", length=2)
	private String vendorTerms;
*/
	@Column (name ="VENDOR_VAT_REG_NO", length=20)
	private String vendorVatRegNo;

	@Column (name ="VENDOR_TIN_NUMBER", length=20)
	private String vendorTinNumber;

	@Column (name ="VENDOR_DESC", length=200)
	private String vendorDescription;

	@Column (name ="VENDOR_SHORT_DESC", length=100)
	private String vendorShortDescription;

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

/*	public String getVendorTerms() {
		return vendorTerms;
	}

	public void setVendorTerms(String vendorTerms) {
		this.vendorTerms = vendorTerms;
	}
*/
	public String getVendorVatRegNo() {
		return vendorVatRegNo;
	}

	public void setVendorVatRegNo(String vendorVatRegNo) {
		this.vendorVatRegNo = vendorVatRegNo;
	}

	public String getVendorTinNumber() {
		return vendorTinNumber;
	}

	public void setVendorTinNumber(String vendorTinNumber) {
		this.vendorTinNumber = vendorTinNumber;
	}

	public String getVendorDescription() {
		return vendorDescription;
	}

	public void setVendorDescription(String vendorDescription) {
		this.vendorDescription = vendorDescription;
	}

	public String getVendorShortDescription() {
		return vendorShortDescription;
	}

	public void setVendorShortDescription(String vendorShortDescription) {
		this.vendorShortDescription = vendorShortDescription;
	}
	
}
