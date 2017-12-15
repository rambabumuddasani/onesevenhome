package com.salesmanager.core.business.repositories.services;

import com.salesmanager.core.model.customer.ServicesBooking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServicesBookingRepository extends JpaRepository<ServicesBooking, Long> {
    
	@Query("select sb from ServicesBooking sb where sb.status = 'Y'")
	List<ServicesBooking> getClosedServicesBookings();

	@Query("select sb from ServicesBooking sb where sb.status = 'N'")
	List<ServicesBooking> getOpenedServicesBookings();

}
