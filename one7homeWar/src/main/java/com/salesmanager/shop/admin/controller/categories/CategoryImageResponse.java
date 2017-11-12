package com.salesmanager.shop.admin.controller.categories;

import java.util.List;

public class CategoryImageResponse {
       
	List<CategoryImageJson> categoryImagedata;

	public List<CategoryImageJson> getCategoryImagedata() {
		return categoryImagedata;
	}

	public void setCategoryImagedata(List<CategoryImageJson> categoryImagedata) {
		this.categoryImagedata = categoryImagedata;
	}
}
