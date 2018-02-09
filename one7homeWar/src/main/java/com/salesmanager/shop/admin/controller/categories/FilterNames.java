package com.salesmanager.shop.admin.controller.categories;

import java.util.ArrayList;
import java.util.List;

public class FilterNames {

	private String categoryCode;
	private String filterName;
	//List<FilterTypes> filterTypesList;
	private List<FilterTypes> filterTypes;
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
/*	public List<FilterTypes> getFilterTypesLst() {
		return filterTypesList;
	}
	public void setFilterTypes(List<FilterTypes> filterTypesList) {
		this.filterTypesList = filterTypesList;
	}*/
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public List<FilterTypes> getFilterTypes() {
		return filterTypes;
	}
	public void setFilterTypes(List<FilterTypes> filterTypes) {
		this.filterTypes = filterTypes;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoryCode == null) ? 0 : categoryCode.hashCode());
		result = prime * result + ((filterName == null) ? 0 : filterName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterNames other = (FilterNames) obj;
		if (categoryCode == null) {
			if (other.categoryCode != null)
				return false;
		} else if (!categoryCode.equals(other.categoryCode))
			return false;
		if (filterName == null) {
			if (other.filterName != null)
				return false;
		} else if (!filterName.equals(other.filterName))
			return false;
		return true;
	}
	public boolean addFilterTypes(FilterTypes e) {
		return filterTypes.add(e);
	}
	public boolean removeFilterTypes(Object o) {
		return filterTypes.remove(o);
	}
	
}
