package com.salesmanager.shop.admin.controller.categories;

import java.util.Set;

public class SearchFilterResponse {

	/*private List<FilterTypeVO> searchFilterData;

	public List<FilterTypeVO> getSearchFilterData() {
		return searchFilterData;
	}

	public void setSearchFilterData(List<FilterTypeVO> searchFilterData) {
		this.searchFilterData = searchFilterData;
	}*/
	private Set<FilterNames> filteredData;

	public Set<FilterNames> getFilteredData() {
		return filteredData;
	}

	public void setFilteredData(Set<FilterNames> filteredData) {
		this.filteredData = filteredData;
	}
}
