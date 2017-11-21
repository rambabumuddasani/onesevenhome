package com.salesmanager.core.business.services.image.brand;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.image.brand.BrandImage;

public interface BrandImageService extends SalesManagerEntityService<Long,BrandImage>{

	List<BrandImage> getAllBrandImages();

	List<BrandImage> getEnableBrandImages();

	List<BrandImage> getDisableBrandImages();

}
