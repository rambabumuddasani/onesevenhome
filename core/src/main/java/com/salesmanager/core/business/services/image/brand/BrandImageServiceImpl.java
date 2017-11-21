package com.salesmanager.core.business.services.image.brand;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.image.brand.BrandImageRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.image.brand.BrandImage;

@Service("brandImageService")
public class BrandImageServiceImpl extends SalesManagerEntityServiceImpl<Long,BrandImage> implements BrandImageService{
	
	
	private BrandImageRepository brandImageRepository;
	
	@Inject
	public BrandImageServiceImpl(BrandImageRepository brandImageRepository) {
		super(brandImageRepository);
		this.brandImageRepository = brandImageRepository;
	}

	@Override
	public List<BrandImage> getAllBrandImages() {
		
		return brandImageRepository.findAll();
	}

	@Override
	public List<BrandImage> getEnableBrandImages() {
		
		return brandImageRepository.getEnableBrandImages();
	}

	@Override
	public List<BrandImage> getDisableBrandImages() {
		
		return brandImageRepository.getDisableBrandImages();
	}
}
