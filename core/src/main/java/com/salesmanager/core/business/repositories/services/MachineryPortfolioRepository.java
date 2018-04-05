package com.salesmanager.core.business.repositories.services;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.customer.ArchitectsPortfolio;
import com.salesmanager.core.model.customer.MachineryPortfolio;


public interface MachineryPortfolioRepository extends JpaRepository<MachineryPortfolio, Long> {
    
	@Query("select pf from MachineryPortfolio pf where pf.customer.id = ?1")
	public List<MachineryPortfolio> findByVendorId(Long vendorId);

	@Query("select pf from MachineryPortfolio pf where pf.status = ?1")
	public List<MachineryPortfolio> getPortfoliosBasedOnStatus(String status);
	
	@Query("select pf from MachineryPortfolio pf where pf.customer.id=?1 and pf.status = ?2")
	public List<MachineryPortfolio> findMachineryPortfolioBasedonStatusAndVendorId(Long vendorId, String status);

	@Query(value= "SELECT m.* FROM VENDOR_BOOKING as v join MACHINERY_PORTFOLIO as m on m.MACHINERY_ID = v.SERVICE_ID WHERE v.CUSTOMER_ID= ?1 and m.MACHINERY_ID=?2",nativeQuery = true)
	public MachineryPortfolio findgetMachineryPortfolio(Long id, Long id2);

	@Query("select pf from MachineryPortfolio pf where pf.customer.vendorAttrs.vendorName like %?1%")
	public List<MachineryPortfolio> getMachineryPortfoliosVendorName(String searchString);

	@Query("select pf from MachineryPortfolio pf where pf.customer.id = ?1")
	public List<MachineryPortfolio> getMachineryPortfoliosVendorId(Long userId);

}
