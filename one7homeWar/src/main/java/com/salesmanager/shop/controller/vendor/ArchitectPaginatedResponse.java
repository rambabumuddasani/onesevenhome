package com.salesmanager.shop.controller.vendor;

import java.util.List;

import com.salesmanager.shop.store.model.paging.PaginationData;

public class ArchitectPaginatedResponse {

	private PaginationData paginationData;
	private List responseData;
	private String errorMsg;
	private Long maxProductPrice;
	private String portfolioName;
	private String vendorName;
	private String vendorImageURL;
	private String vendorDescription;
	private String vendorShortDescription;
	private String status;
	public PaginationData getPaginationData() {
		return paginationData;
	}
	public void setPaginationData(PaginationData paginationData) {
		this.paginationData = paginationData;
	}
	public List getResponseData() {
		return responseData;
	}
	public void setResponseData(List responseData) {
		this.responseData = responseData;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Long getMaxProductPrice() {
		return maxProductPrice;
	}
	public void setMaxProductPrice(Long maxProductPrice) {
		this.maxProductPrice = maxProductPrice;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
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
