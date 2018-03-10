package com.salesmanager.shop.admin.controller;

import java.util.List;

public class VendorRevenueResponse {

	List<VendorRevenueVO> vendorRevenues;
	List<ProductRevenueVO> productRevenues;
	Integer total;
	String errorMessage;
	String status;
	/*private Long vendorId;
	private String vendorName;
	private BigDecimal totalRevenue;
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
	public BigDecimal getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(BigDecimal totalRevenue) {
		this.totalRevenue = totalRevenue;
	}*/

	public List<VendorRevenueVO> getVendorRevenues() {
		return vendorRevenues;
	}

	public void setVendorRevenues(List<VendorRevenueVO> vendorRevenues) {
		this.vendorRevenues = vendorRevenues;
	}

	public List<ProductRevenueVO> getProductRevenues() {
		return productRevenues;
	}

	public void setProductRevenues(List<ProductRevenueVO> productRevenues) {
		this.productRevenues = productRevenues;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
