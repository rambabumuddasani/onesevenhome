package com.salesmanager.core.business.services.services;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.ServicesBooking;

public interface ServicesBookingService extends SalesManagerEntityService<Long, ServicesBooking> {

	List<ServicesBooking> getAllServicesBookings();
	
	
}
