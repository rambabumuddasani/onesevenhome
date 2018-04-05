package com.salesmanager.core.business.services.services;

import java.util.Date;
import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.ServicesBooking;

public interface ServicesBookingService extends SalesManagerEntityService<Long, ServicesBooking> {

	List<ServicesBooking> getAllServicesBookings();

	List<ServicesBooking> getClosedServicesBookings();

	List<ServicesBooking> getOpenedServicesBookings();

	List<ServicesBooking> getAllServicesBookings(Date startDate, Date endDate);

	List<ServicesBooking> getClosedServicesBookingsByDate(String status, Date startDate, Date endDate);

	List<ServicesBooking> getOpenedServicesBookingsByDate(String status, Date startDate, Date endDate);

	List<ServicesBooking> searchServicesBookingByName(String searchString);

	List<ServicesBooking> searchServicesBookingById(Long userId);
	
	
}
