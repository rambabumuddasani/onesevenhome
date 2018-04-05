package com.salesmanager.core.business.repositories.services;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;


public interface ArchitectsPortfolioRepository extends JpaRepository<ArchitectsPortfolio, Long> {
    
	@Query("select pf from ArchitectsPortfolio pf where pf.customer.id = ?1")
	public List<ArchitectsPortfolio> findByVendorId(Long vendorId);

	@Query("select pf from ArchitectsPortfolio pf where pf.status = ?1")
	public List<ArchitectsPortfolio> getPortfoliosBasedOnStatus(String status);

	@Query("select pf from ArchitectsPortfolio pf where pf.customer.id=?1")
	public List<ArchitectsPortfolio> getApprovedVendor(Long vendorId);

	@Query("select pf from ArchitectsPortfolio pf where pf.status = ?1 and pf.customer.id=?2")
	public List<ArchitectsPortfolio> findPortfoliosBasedOnStatusAndVendorId(String status, Long vendorId);

	@Query("select pf from ArchitectsPortfolio pf where pf.customer.vendorAttrs.vendorName like %?1%")
	public List<ArchitectsPortfolio> getArchitectPortfoliosSearchByVendorName(String searchString);

	@Query("select pf from ArchitectsPortfolio pf where pf.customer.id = ?1")
	public List<ArchitectsPortfolio> getArchitectPortfoliosSearchByVendorId(Long userId);

}
