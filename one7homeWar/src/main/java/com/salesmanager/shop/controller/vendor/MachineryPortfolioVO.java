package com.salesmanager.shop.controller.vendor;

import java.math.BigDecimal;
import java.util.Date;

public class MachineryPortfolioVO {

	private Long machineryPortfolioId;
	private Date createdate;
	private String portfolioName;
	private String imageURL;
	private String vendorName;
	private String vendorImageURL;
	private String vendorDescription;
	private String vendorShortDescription;
	private String status;
	private String equipmentName;
	private BigDecimal equipmentPrice;
	private String hiringType;
	public Long getMachineryPortfolioId() {
		return machineryPortfolioId;
	}
	public void setMachineryPortfolioId(Long machineryPortfolioId) {
		this.machineryPortfolioId = machineryPortfolioId;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
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
	
}
