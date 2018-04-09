package com.salesmanager.core.business.services.services;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.services.WallPaperPortfolioRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.price.FinalPrice;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.WallPaperPortfolio;

@Service("wallPaperPortfolioService")
public class WallPaperPortfolioServiceImpl extends SalesManagerEntityServiceImpl<Long, WallPaperPortfolio> 
		implements WallPaperPortfolioService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WallPaperPortfolioServiceImpl.class);
	
	private WallPaperPortfolioRepository wallPaperPortfolioRepository;

	
	@Inject
	public WallPaperPortfolioServiceImpl(WallPaperPortfolioRepository wallPaperPortfolioRepository) {
		super(wallPaperPortfolioRepository);
		this.wallPaperPortfolioRepository = wallPaperPortfolioRepository;
	}
	
	@Override
	public List<WallPaperPortfolio> findByVendorId(Long vendorId) {
		return wallPaperPortfolioRepository.findByVendorId(vendorId);
	}

	@Override
	public List<WallPaperPortfolio> getAllPortfolios() {
		return wallPaperPortfolioRepository.findAll();
	}

	@Override
	public List<WallPaperPortfolio> getPortfoliosBasedOnStatus(String status) {
		return wallPaperPortfolioRepository.findPortfoliosBasedOnStatus(status);
	}

	@Override
	public List<WallPaperPortfolio> getPortfoliosBasedOnStatusAndVendorId(Long vendorId, String status) {
		return wallPaperPortfolioRepository.findPortfoliosBasedOnStatusAndVendorId(vendorId, status);
	}

	@Override
	public List<WallPaperPortfolio> getWallPaperPortfoliosSearchByVendorName(String searchString) {
		// TODO Auto-generated method stub
		return wallPaperPortfolioRepository.findWallPaperPortfoliosByVendorName(searchString);
	}

	@Override
	public List<WallPaperPortfolio> getWallPaperPortfoliosSearchByVendorId(Long userId) {
		// TODO Auto-generated method stub
		return wallPaperPortfolioRepository.findByVendorId(userId);
	}


}
