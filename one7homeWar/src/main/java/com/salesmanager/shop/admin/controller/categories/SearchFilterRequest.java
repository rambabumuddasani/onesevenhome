package com.salesmanager.shop.admin.controller.categories;

import java.io.Serializable;

public class SearchFilterRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String searchFilterString;
	private Long filterTypeId;
	private String filterTypeName;
	public String getSearchFilterString() {
		return searchFilterString;
	}
	public void setSearchFilterString(String searchFilterString) {
		this.searchFilterString = searchFilterString;
	}
	public Long getFilterTypeId() {
		return filterTypeId;
	}
	public void setFilterTypeId(Long filterTypeId) {
		this.filterTypeId = filterTypeId;
	}
	public String getFilterTypeName() {
		return filterTypeName;
	}
	public void setFilterTypeName(String filterTypeName) {
		this.filterTypeName = filterTypeName;
	}
}
