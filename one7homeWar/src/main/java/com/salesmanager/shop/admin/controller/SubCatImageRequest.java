package com.salesmanager.shop.admin.controller;

import java.io.Serializable;

public class SubCatImageRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	private String categoryName;
	private String subCategoryName;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
