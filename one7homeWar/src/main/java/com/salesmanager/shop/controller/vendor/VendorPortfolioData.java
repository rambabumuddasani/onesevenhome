package com.salesmanager.shop.controller.vendor;

import java.math.BigDecimal;

public class VendorPortfolioData {
	
	private Long portfolioId;
	private String portfolioName;
	private Long vendorId;
	private String imageURL;
	private BigDecimal price;
	private String size;
	private String thickness;
	private String brand;
	private String status;
	private String equipmentName;
	private BigDecimal equipmentPrice;
	private String hiringType;
	private BigDecimal serviceCharges;
	
	public Long getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(Long portfolioId) {
		this.portfolioId = portfolioId;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public void setHiringType(String hiringType) {
		this.hiringType = hiringType;
	}

	public BigDecimal getServiceCharges() {
		return serviceCharges;
	}

	public void setServiceCharges(BigDecimal serviceCharges) {
		this.serviceCharges = serviceCharges;
	}
	
}
