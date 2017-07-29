package com.salesmanager.shop.admin.controller.products;

import java.util.List;

import com.salesmanager.shop.store.model.paging.PaginationData;



public class TodaysDeals {

	List<ProductResponse> todaysDealsData;
	private PaginationData paginationData;
	
	public List<ProductResponse> getTodaysDealsData() {
		return todaysDealsData;
	}

	public void setTodaysDealsData(List<ProductResponse> todaysDealsData) {
		this.todaysDealsData = todaysDealsData;
	}

	public PaginationData getPaginationData() {
		return paginationData;
	}

	public void setPaginationData(PaginationData paginationData) {
		this.paginationData = paginationData;
	}
	
}
