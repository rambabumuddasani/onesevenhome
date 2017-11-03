package com.salesmanager.core.business.repositories.catalog.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.catalog.category.SubCategoryImage;

public interface SubCategoryRepository extends JpaRepository<SubCategoryImage, Long>, SubCategoryRepositoryCustom {

	@Query("select distinct sci from SubCategoryImage sci left join fetch sci.category c left join fetch c.descriptions cd ")
	List<SubCategoryImage> getAllSubCategoryImage();
    
	@Query("select distinct sci from SubCategoryImage sci left join fetch sci.category c left join fetch c.descriptions cd where c.id=?1")
	SubCategoryImage getByCategoryId(Long id);

}
