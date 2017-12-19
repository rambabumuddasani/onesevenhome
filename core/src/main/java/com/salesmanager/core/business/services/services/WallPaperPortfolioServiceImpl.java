package com.salesmanager.core.business.services.services;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.services.WallPaperPortfolioRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
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

}
