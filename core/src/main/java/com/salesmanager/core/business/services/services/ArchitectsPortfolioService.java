package com.salesmanager.core.business.services.services;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;

public interface ArchitectsPortfolioService extends SalesManagerEntityService<Long, ArchitectsPortfolio> {

	List<ArchitectsPortfolio> findByVendorId(Long vendorId);

	List<ArchitectsPortfolio> getAllPortfolios();

	List<ArchitectsPortfolio> getPortfoliosBasedOnStatus(String status);

	List<ArchitectsPortfolio> getApprovedVendor(Long vendorId);

	List<ArchitectsPortfolio> getPortFoliosByVendorId(Long vendorId);

	List<ArchitectsPortfolio> getPortfoliosBasedOnStatusAndVendorId(String status, Long vendorId);

	List<ArchitectsPortfolio> getArchitectPortfoliosSearchByVendorName(String searchString);

	List<ArchitectsPortfolio> getArchitectPortfoliosSearchByVendorId(Long userId);
}
