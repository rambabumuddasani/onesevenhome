package com.salesmanager.shop.admin.controller.products;

import java.util.List;

import com.salesmanager.shop.store.model.paging.PaginationData;

public class PaginatedResponse {
	
	private PaginationData paginationData;
	private List responseData;
	
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
	
}
