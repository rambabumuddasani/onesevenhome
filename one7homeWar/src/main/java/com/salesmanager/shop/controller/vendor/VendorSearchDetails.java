package com.salesmanager.shop.controller.vendor;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VendorSearchDetails {

	private Integer id;
	private String companyName;
	private String houseNumber;
	private String street;
	private String area;
	private String city;
	private String state;
	private String pinCode;
	private String country;
	@JsonIgnore
	private String websiteName;
	private String contactNumber;
	private String imageUrl;
	private BigDecimal avgRating;
	private Integer totalRating;
	private String shortDescription;
	private String description;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getWebsiteName() {
		return websiteName;
	}
	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public BigDecimal getAvgRating() {
		return avgRating;
	}
	public void setAvgRating(BigDecimal avgRating) {
		this.avgRating = avgRating;
	}
	public Integer getTotalRating() {
		return totalRating;
	}
	public void setTotalRating(Integer totalRating) {
		this.totalRating = totalRating;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
