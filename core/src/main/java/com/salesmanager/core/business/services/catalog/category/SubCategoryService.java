package com.salesmanager.core.business.services.catalog.category;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.category.SubCategoryImage;

public interface SubCategoryService extends SalesManagerEntityService<Long,SubCategoryImage> {
    
	
	List<SubCategoryImage> getAllSubCategoryImage();

	SubCategoryImage getByCategoryId(Long id);

}
