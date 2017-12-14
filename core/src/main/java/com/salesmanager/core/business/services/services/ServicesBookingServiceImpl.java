package com.salesmanager.core.business.services.services;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.services.ServicesBookingRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.ServicesBooking;

@Service("servicesBookingService")
public class ServicesBookingServiceImpl extends SalesManagerEntityServiceImpl<Long, ServicesBooking> 
		implements ServicesBookingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesBookingServiceImpl.class);
	
	private ServicesBookingRepository servicesBookingRepository;

	
	@Inject
	public ServicesBookingServiceImpl(ServicesBookingRepository servicesBookingRepository) {
		super(servicesBookingRepository);
		this.servicesBookingRepository = servicesBookingRepository;
	}


	@Override
	public List<ServicesBooking> getAllServicesBookings() {
		
		return servicesBookingRepository.findAll();
	}


}
