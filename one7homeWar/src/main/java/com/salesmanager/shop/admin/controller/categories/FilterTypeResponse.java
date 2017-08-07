package com.salesmanager.shop.admin.controller.categories;

import java.util.List;

import com.salesmanager.core.model.catalog.product.filter.FilterType;

public class FilterTypeResponse {

	String filterName;
	List<FilterType> filterType;
	
	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public List<FilterType> getFilterType() {
		return filterType;
	}

	public void setFilterType(List<FilterType> filterType) {
		this.filterType = filterType;
	}

	
}
