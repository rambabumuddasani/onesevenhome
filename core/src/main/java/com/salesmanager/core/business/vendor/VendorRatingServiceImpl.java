package com.salesmanager.core.business.vendor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.vendor.VendorRatingRepository;
import com.salesmanager.core.business.repositories.services.ServicesRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.VendorRating;

@Service("vendorRatingService")
public class VendorRatingServiceImpl extends SalesManagerEntityServiceImpl<Long, VendorRating> 
		implements VendorRatingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VendorRatingServiceImpl.class);
	
	private VendorRatingRepository vendorRatingRepository;

	
	@Inject
	public VendorRatingServiceImpl(VendorRatingRepository vendorRatingRepository) {
		super(vendorRatingRepository);
		this.vendorRatingRepository = vendorRatingRepository;
	}

	@Override
	public List<VendorRating> getVendorReviews(Long vendorId) {
		
		return vendorRatingRepository.getVendorReviews(vendorId);
	}
}
