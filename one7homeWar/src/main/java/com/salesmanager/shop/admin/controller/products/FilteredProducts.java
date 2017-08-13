package com.salesmanager.shop.admin.controller.products;

import java.util.List;

import com.salesmanager.shop.store.model.paging.PaginationData;



public class FilteredProducts {

	List<ProductResponse> filteredProducts;
	private PaginationData paginationData;
	public List<ProductResponse> getFilteredProducts() {
		return filteredProducts;
	}
	public void setFilteredProducts(List<ProductResponse> filteredProducts) {
		this.filteredProducts = filteredProducts;
	}
	public PaginationData getPaginationData() {
		return paginationData;
	}
	public void setPaginationData(PaginationData paginationData) {
		this.paginationData = paginationData;
	}
	
}
