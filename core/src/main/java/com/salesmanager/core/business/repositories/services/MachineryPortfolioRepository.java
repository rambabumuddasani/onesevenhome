package com.salesmanager.core.business.repositories.services;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.customer.MachineryPortfolio;


public interface MachineryPortfolioRepository extends JpaRepository<MachineryPortfolio, Long> {
    
	@Query("select pf from MachineryPortfolio pf where pf.customer.id = ?1")
	public List<MachineryPortfolio> findByVendorId(Long vendorId);

}
