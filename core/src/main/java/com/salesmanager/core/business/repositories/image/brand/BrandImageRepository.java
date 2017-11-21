package com.salesmanager.core.business.repositories.image.brand;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.image.brand.BrandImage;

public interface BrandImageRepository extends JpaRepository<BrandImage, Long>, BrandImageRepositoryCustom{

	@Query("select bg from BrandImage bg where bg.status = 'Y'")
	List<BrandImage> getEnableBrandImages();
 
	@Query("select bg from BrandImage bg where bg.status = 'N'")
	List<BrandImage> getDisableBrandImages();

}
