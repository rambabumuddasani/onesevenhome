package com.salesmanager.core.business.repositories.services;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.customer.WallPaperPortfolio;


public interface WallPaperPortfolioRepository extends JpaRepository<WallPaperPortfolio, Long> {
    
	@Query("select pf from WallPaperPortfolio pf where pf.customer.id = ?1")
	public List<WallPaperPortfolio> findByVendorId(Long vendorId);
	
	@Query("select pf from WallPaperPortfolio pf where pf.status = ?1")
	public List<WallPaperPortfolio> findPortfoliosBasedOnStatus(String status);
	
	@Query("select pf from WallPaperPortfolio pf where pf.customer.id=?1 and pf.status = ?2")
	public List<WallPaperPortfolio> findPortfoliosBasedOnStatusAndVendorId(Long vendorId, String status);

	@Query("select pf from WallPaperPortfolio pf where pf.customer.vendorAttrs.vendorName like %?1%")
	public List<WallPaperPortfolio> findWallPaperPortfoliosByVendorName(String searchString);

}
