package com.salesmanager.shop.admin.controller;

import java.math.BigDecimal;
import java.util.Date;

public class VendorBookingVO {

	private Long id;
	private String customerName;
	private String vendorName;
	private Long vendorId;
	private Date appointmentDate;
	private String status;
	private String address;
	private Date closingDate;
	private String description;
	private Date bookingDate;
	private String comment;
	private String bookingType;
	private String customerEmailId;
	private String customerMobileNumber;
	private String vendorEmailId;
	private String vendorMobileNumber;
	private String equipmentName;
	private BigDecimal equipmentPrice;
	private String hiringType;
	private String imageURL;
	private String portfolioName;
	private Long machineryPortfolioId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getClosingDate() {
		return closingDate;
	}
	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getBookingType() {
		return bookingType;
	}
	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}
	public String getCustomerEmailId() {
		return customerEmailId;
	}
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}
	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}
	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}
	public String getVendorEmailId() {
		return vendorEmailId;
	}
	public void setVendorEmailId(String vendorEmailId) {
		this.vendorEmailId = vendorEmailId;
	}
	public String getVendorMobileNumber() {
		return vendorMobileNumber;
	}
	public void setVendorMobileNumber(String vendorMobileNumber) {
		this.vendorMobileNumber = vendorMobileNumber;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public BigDecimal getEquipmentPrice() {
		return equipmentPrice;
	}
	public void setEquipmentPrice(BigDecimal equipmentPrice) {
		this.equipmentPrice = equipmentPrice;
	}
	public String getHiringType() {
		return hiringType;
	}
	public void setHiringtype(String hiringType) {
		this.hiringType = hiringType;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public void setHiringType(String hiringType) {
		this.hiringType = hiringType;
	}
	public Long getMachineryPortfolioId() {
		return machineryPortfolioId;
	}
	public void setMachineryPortfolioId(Long machineryPortfolioId) {
		this.machineryPortfolioId = machineryPortfolioId;
	}
	
}
