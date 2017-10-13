package com.salesmanager.shop.admin.controller.categories;

import java.util.List;

import com.salesmanager.core.model.catalog.product.filter.FilterType;

public class CreateFilterTypeRequest {

	String filterName;
	List<String> filterTypes;
	String categoryName;
	Long productId;
	List<Long> filterTypeIds;
	
	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<String> getFilterTypes() {
		return filterTypes;
	}

	public void setFilterTypes(List<String> filterTypes) {
		this.filterTypes = filterTypes;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public List<Long> getFilterTypeIds() {
		return filterTypeIds;
	}

	public void setFilterTypeIds(List<Long> filterTypeIds) {
		this.filterTypeIds = filterTypeIds;
	}
	
}
