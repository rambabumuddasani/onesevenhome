package com.salesmanager.shop.controller.vendor;

import java.io.Serializable;
import java.math.BigDecimal;

public class WallPaperRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1194851172261569559L;
	private String portfolioName;
	private Long vendorId;
	private Long portfolioId;
	private BigDecimal price;
	private String size;
	private String thickness;
	private String brand;
	private String status; 
	private BigDecimal serviceCharges;
	
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getServiceCharges() {
		return serviceCharges;
	}

	public void setServiceCharges(BigDecimal serviceCharges) {
		this.serviceCharges = serviceCharges;
	}
	
}
