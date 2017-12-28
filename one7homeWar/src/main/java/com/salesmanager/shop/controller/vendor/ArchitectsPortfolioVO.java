package com.salesmanager.shop.controller.vendor;

import java.util.Date;

public class ArchitectsPortfolioVO {

	private Long architectPortfolioId;
	private Date createdate;
	private String portfolioName;
	private String imageURL;
	private String vendorName;
	private String vendorImageURL;
	private String vendorDescription;
	private String vendorShortDescription;
	private String status;
	public Long getArchitectPortfolioId() {
		return architectPortfolioId;
	}
	public void setArchitectPortfolioId(Long architectPortfolioId) {
		this.architectPortfolioId = architectPortfolioId;
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
}
