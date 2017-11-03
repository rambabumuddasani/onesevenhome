package com.salesmanager.shop.admin.controller;

import java.util.List;
import java.util.Map;

public class AdminSubCatImgResponse {
     
    private Map<String,List<SubCategoryImageVO>> subCatagoryImgsObjByCatagory;

	public Map<String,List<SubCategoryImageVO>> getSubCatagoryImgsObjByCatagory() {
		return subCatagoryImgsObjByCatagory;
	}

	public void setSubCatagoryImgsObjByCatagory(Map<String,List<SubCategoryImageVO>> subCatagoryImgsObjByCatagory) {
		this.subCatagoryImgsObjByCatagory = subCatagoryImgsObjByCatagory;
	}
	 
}
