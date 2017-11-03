package com.salesmanager.core.business.services.catalog.category;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.catalog.category.SubCategoryRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.category.SubCategoryImage;

@Service("subCategoryService")
public class SubCategoryServiceImpl extends SalesManagerEntityServiceImpl<Long,SubCategoryImage> implements SubCategoryService {

	@Inject
	private SubCategoryRepository subCategoryRepository;
	
	@Inject
	public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository) {
		super(subCategoryRepository);
		this.subCategoryRepository = subCategoryRepository;
	}

	@Override
	public List<SubCategoryImage> getAllSubCategoryImage() {
		// TODO Auto-generated method stub
		return subCategoryRepository.getAllSubCategoryImage();
	}

	@Override
	public SubCategoryImage getByCategoryId(Long id) {
		// TODO Auto-generated method stub
		return subCategoryRepository.getByCategoryId(id);
	}

}
