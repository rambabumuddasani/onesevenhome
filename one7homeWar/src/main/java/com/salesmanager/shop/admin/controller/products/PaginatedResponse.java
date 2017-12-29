package com.salesmanager.shop.admin.controller.products;

import java.util.List;

import com.salesmanager.shop.store.model.paging.PaginationData;

public class PaginatedResponse {
	
	private PaginationData paginationData;
	private List responseData;
	private String errorMsg;
	private Long maxProductPrice;
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
	
}
