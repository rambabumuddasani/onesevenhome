package com.salesmanager.shop.admin.controller;

import java.util.List;

import com.salesmanager.shop.admin.controller.products.ProductResponse;
import com.salesmanager.shop.store.model.paging.PaginationData;

public class AdminTodaysDeals {

	List<AdminDealProductResponse> todaysDealsData;
	private PaginationData paginationData;
	private String errorMesg;
	
	public List<AdminDealProductResponse> getTodaysDealsData() {
		return todaysDealsData;
	}

	public void setTodaysDealsData(List<AdminDealProductResponse> todaysDealsData) {
		this.todaysDealsData = todaysDealsData;
	}

	public PaginationData getPaginationData() {
		return paginationData;
	}

	public void setPaginationData(PaginationData paginationData) {
		this.paginationData = paginationData;
	}

	public String getErrorMesg() {
		return errorMesg;
	}

	public void setErrorMesg(String errorMesg) {
		this.errorMesg = errorMesg;
	}
}
