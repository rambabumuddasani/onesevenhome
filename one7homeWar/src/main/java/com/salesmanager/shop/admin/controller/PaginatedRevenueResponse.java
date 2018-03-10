package com.salesmanager.shop.admin.controller;

import java.util.List;

import com.salesmanager.shop.store.model.paging.PaginationData;

public class PaginatedRevenueResponse {

	private PaginationData paginationData;
	private List<VendorRevenueVO> vendorRevenues;
	private List<ProductRevenueVO> productRevenues;
	private List<VendorProductRevenueVO> vendorProductRevenueData;
	private List<ProductVendorRevenueVO> ProductVendorRevenueData;
	private String errorMsg;
	private String status;
	private Integer total;
	public PaginationData getPaginationData() {
		return paginationData;
	}
	public void setPaginationData(PaginationData paginationData) {
		this.paginationData = paginationData;
	}
	public List<VendorRevenueVO> getVendorRevenues() {
		return vendorRevenues;
	}
	public void setVendorRevenues(List<VendorRevenueVO> vendorRevenues) {
		this.vendorRevenues = vendorRevenues;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<ProductRevenueVO> getProductRevenues() {
		return productRevenues;
	}
	public void setProductRevenues(List<ProductRevenueVO> productRevenues) {
		this.productRevenues = productRevenues;
	}
	public List<VendorProductRevenueVO> getVendorProductRevenueData() {
		return vendorProductRevenueData;
	}
	public void setVendorProductRevenueData(List<VendorProductRevenueVO> vendorProductRevenueData) {
		this.vendorProductRevenueData = vendorProductRevenueData;
	}
	public List<ProductVendorRevenueVO> getProductVendorRevenueData() {
		return ProductVendorRevenueData;
	}
	public void setProductVendorRevenueData(List<ProductVendorRevenueVO> productVendorRevenueData) {
		ProductVendorRevenueData = productVendorRevenueData;
	}
	
}
