package com.salesmanager.core.business.services.services;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.services.MachineryPortfolioRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;
import com.salesmanager.core.model.customer.MachineryPortfolio;

@Service("machineryPortfolioService")
public class MachineryPortfolioServiceImpl extends SalesManagerEntityServiceImpl<Long, MachineryPortfolio> 
		implements MachineryPortfolioService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MachineryPortfolioServiceImpl.class);
	
	private MachineryPortfolioRepository machineryPortfolioRepository;

	
	@Inject
	public MachineryPortfolioServiceImpl(MachineryPortfolioRepository machineryPortfolioRepository) {
		super(machineryPortfolioRepository);
		this.machineryPortfolioRepository = machineryPortfolioRepository;
	}

	@Override
	public List<MachineryPortfolio> findByVendorId(Long vendorId) {
		return machineryPortfolioRepository.findByVendorId(vendorId);
	}

	@Override
	public List<MachineryPortfolio> getAllPortfoios() {
		return machineryPortfolioRepository.findAll();
	}

	@Override
	public List<MachineryPortfolio> getPortfoliosBasedOnStatus(String status) {
		return machineryPortfolioRepository.getPortfoliosBasedOnStatus(status);
	}

	@Override
	public List<MachineryPortfolio> getMachineryPortfolioBasedonStatusAndVendorId(Long vendorId, String status) {
		return machineryPortfolioRepository.findMachineryPortfolioBasedonStatusAndVendorId(vendorId,status);
	}

	@Override
	public MachineryPortfolio getMachineryPortfolio(Long id, Long id2) {
		return machineryPortfolioRepository.findgetMachineryPortfolio(id,id2);
	}

	@Override
	public List<MachineryPortfolio> getMachineryPortfoliosVendorName(String searchString) {
		return machineryPortfolioRepository.getMachineryPortfoliosVendorName(searchString);
	}

	@Override
	public List<MachineryPortfolio> getMachineryPortfoliosVendorId(Long userId) {
		return machineryPortfolioRepository.getMachineryPortfoliosVendorId(userId);
	}
}
