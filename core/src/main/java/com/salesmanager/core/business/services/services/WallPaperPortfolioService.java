package com.salesmanager.core.business.services.services;


import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.WallPaperPortfolio;

public interface WallPaperPortfolioService extends SalesManagerEntityService<Long, WallPaperPortfolio> {

	List<WallPaperPortfolio> findByVendorId(Long vendorId);

	List<WallPaperPortfolio> getAllPortfolios();

	List<WallPaperPortfolio> getPortfoliosBasedOnStatus(String status);

	List<WallPaperPortfolio> getPortfoliosBasedOnStatusAndVendorId(Long vendorId, String status);

	List<WallPaperPortfolio> getWallPaperPortfoliosSearchByVendorName(String searchString);

	List<WallPaperPortfolio> getWallPaperPortfoliosSearchByVendorId(Long userId);
	
	//public FinalPrice calculateProductPrice(WallPaperPortfolio w);
}
