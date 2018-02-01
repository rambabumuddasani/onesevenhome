package com.salesmanager.shop.admin.controller.categories;

import java.util.Set;

import com.salesmanager.core.model.catalog.category.Category;

public class ParentChildCategory {

	Category parentCategory;
	Set<Category> chilCategory;
	public Category getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}
	public Set<Category> getChilCategory() {
		return chilCategory;
	}
	public void setChilCategory(Set<Category> chilCategory) {
		this.chilCategory = chilCategory;
	}
}
