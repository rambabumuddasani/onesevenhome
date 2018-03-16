package com.salesmanager.shop.admin.controller;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VendorRevenueRequest implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6377868374512717114L;
	private Date startDate;
	private Date endDate;
	@JsonIgnore
	private String level;
	private List<BigInteger> vendorIds;
	private List<String> productSkus;
	private String status;
	private String sortBy;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	public List<String> getProductSkus() {
		return productSkus;
	}
	public void setProductSkus(List<String> productSkus) {
		this.productSkus = productSkus;
	}
	public void setVendorIds(List<BigInteger> vendorIds) {
		this.vendorIds = vendorIds;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<BigInteger> getVendorIds() {
		return vendorIds;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	

}
