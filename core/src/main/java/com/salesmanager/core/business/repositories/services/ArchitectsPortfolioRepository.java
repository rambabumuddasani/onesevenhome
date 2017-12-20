package com.salesmanager.core.business.repositories.services;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;


public interface ArchitectsPortfolioRepository extends JpaRepository<ArchitectsPortfolio, Long> {
    
	@Query("select pf from ArchitectsPortfolio pf where pf.customer.id = ?1")
	public List<ArchitectsPortfolio> findByVendorId(Long vendorId);

}