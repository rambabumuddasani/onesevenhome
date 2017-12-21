package com.salesmanager.core.business.services.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.services.ServicesRatingRepository;
import com.salesmanager.core.business.repositories.services.ServicesRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.ServicesRating;

@Service("servicesRatingService")
public class ServicesRatingServiceImpl extends SalesManagerEntityServiceImpl<Long, ServicesRating> 
		implements ServicesRatingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesRatingServiceImpl.class);
	
	private ServicesRatingRepository servicesRatingRepository;

	
	@Inject
	public ServicesRatingServiceImpl(ServicesRatingRepository servicesRatingRepository) {
		super(servicesRatingRepository);
		this.servicesRatingRepository = servicesRatingRepository;
	}
	
	@Override
	public List<ServicesRating> getServicesReviews(Long serviceId) {
		
		return servicesRatingRepository.getServicesReviews(serviceId);
	}


}
