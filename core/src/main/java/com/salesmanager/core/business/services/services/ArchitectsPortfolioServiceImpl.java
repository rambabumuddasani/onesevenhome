package com.salesmanager.core.business.services.services;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.services.ArchitectsPortfolioRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;

@Service("architectsPortfolioService")
public class ArchitectsPortfolioServiceImpl extends SalesManagerEntityServiceImpl<Long, ArchitectsPortfolio> 
		implements ArchitectsPortfolioService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArchitectsPortfolioServiceImpl.class);
	
	private ArchitectsPortfolioRepository architectsPortfolioRepository;

	
	@Inject
	public ArchitectsPortfolioServiceImpl(ArchitectsPortfolioRepository architectsPortfolioRepository) {
		super(architectsPortfolioRepository);
		this.architectsPortfolioRepository = architectsPortfolioRepository;
	}
	@Override
	public List<ArchitectsPortfolio> findByVendorId(Long vendorId) {
		return architectsPortfolioRepository.findByVendorId(vendorId);
	}
	@Override
	public List<ArchitectsPortfolio> getAllPortfolios() {
		return architectsPortfolioRepository.findAll();
	}
	@Override
	public List<ArchitectsPortfolio> getPortfoliosBasedOnStatus(String status) {
		return architectsPortfolioRepository.getPortfoliosBasedOnStatus(status);
	}
	@Override
	public List<ArchitectsPortfolio> getApprovedVendor(Long vendorId) {
		return architectsPortfolioRepository.getApprovedVendor(vendorId);
	}
	@Override
	public List<ArchitectsPortfolio> getPortFoliosByVendorId(Long vendorId) {
		return architectsPortfolioRepository.findByVendorId(vendorId);
	}
	@Override
	public List<ArchitectsPortfolio> getPortfoliosBasedOnStatusAndVendorId(String status, Long vendorId) {
		return architectsPortfolioRepository.findPortfoliosBasedOnStatusAndVendorId(status,vendorId);
	}
	@Override
	public List<ArchitectsPortfolio> getArchitectPortfoliosSearchByVendorName(String searchString) {
		return architectsPortfolioRepository.getArchitectPortfoliosSearchByVendorName(searchString);
	}
	@Override
	public List<ArchitectsPortfolio> getArchitectPortfoliosSearchByVendorId(Long userId) {
		return architectsPortfolioRepository.getArchitectPortfoliosSearchByVendorId(userId);
	}
}
