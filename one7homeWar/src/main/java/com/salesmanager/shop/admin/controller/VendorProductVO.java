package com.salesmanager.shop.admin.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VendorProductVO {

	private Long vendorProductId;
	@JsonIgnore
	private Long vendorId;
	private String vendorName;
	private Long productId;
	private String productName;
	private String imageURL;
	private String productDescription;
	@JsonIgnore
	private String vendorMobile;
	private String houseNumber;
	private String street;
	private String area;
	private String city;
	private String state;
	private String pinCode;
	private String vendorTelephone;
	private Boolean status;
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setVendorProductId(Long id) {
		this.vendorProductId = id;
	}
	public Long getVendorProductId() {
		return vendorProductId;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getVendorMobile() {
		return vendorMobile;
	}
	public void setVendorMobile(String vendorMobile) {
		this.vendorMobile = vendorMobile;
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
	public String getVendorTelephone() {
		return vendorTelephone;
	}
	public void setVendorTelephone(String vendorTelephone) {
		this.vendorTelephone = vendorTelephone;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	
}
