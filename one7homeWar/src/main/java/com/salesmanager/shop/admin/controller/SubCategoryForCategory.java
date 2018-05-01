package com.salesmanager.shop.admin.controller;

import java.util.List;

public class SubCategoryForCategory {
	
	String categoryName;
	List<SubCategoryJson> SubCategory;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<SubCategoryJson> getSubCategory() {
		return SubCategory;
	}
	public void setSubCategory(List<SubCategoryJson> subCategory) {
		SubCategory = subCategory;
	}
	

}
