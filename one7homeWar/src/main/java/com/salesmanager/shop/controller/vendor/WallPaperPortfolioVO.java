package com.salesmanager.shop.controller.vendor;

import java.math.BigDecimal;

public class WallPaperPortfolioVO {

	private String portfolioName;
	private Long vendorId;
	private Long portfolioId;
	private BigDecimal price;
	private String size;
	private String thickness;
	private String brand;
	private BigDecimal serviceCharges;
	private String vendorName;
	private String vendorImageURL;
	private String vendorDescription;
	private String vendorShortDescription;
	private String status;
	private String imageURL;
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
	public Long getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(Long portfolioId) {
		this.portfolioId = portfolioId;
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
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getVendorImageURL() {
		return vendorImageURL;
	}
	public void setVendorImageURL(String vendorImageURL) {
		this.vendorImageURL = vendorImageURL;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public BigDecimal getServiceCharges() {
		return serviceCharges;
	}
	public void setServiceCharges(BigDecimal serviceCharges) {
		this.serviceCharges = serviceCharges;
	}
}
