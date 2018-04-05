package com.salesmanager.core.business.services.services;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;
import com.salesmanager.core.model.customer.MachineryPortfolio;

public interface MachineryPortfolioService extends SalesManagerEntityService<Long, MachineryPortfolio> {

	List<MachineryPortfolio> findByVendorId(Long vendorId);

	List<MachineryPortfolio> getAllPortfoios();

	List<MachineryPortfolio> getPortfoliosBasedOnStatus(String status);

	List<MachineryPortfolio> getMachineryPortfolioBasedonStatusAndVendorId(Long vendorId, String status);

	MachineryPortfolio getMachineryPortfolio(Long id, Long id2);

	List<MachineryPortfolio> getMachineryPortfoliosVendorName(String searchString);

	List<MachineryPortfolio> getMachineryPortfoliosVendorId(Long userId);
}
