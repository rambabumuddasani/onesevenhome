package com.salesmanager.shop.admin.controller.categories;

import java.util.List;

public class CategoryJson {

	String type;
	String title;
	String url;
	String imageURL;
	List<SubCategoryJson> subCategory;
	
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<SubCategoryJson> getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(List<SubCategoryJson> subCategory) {
		this.subCategory = subCategory;
	}

	
}
