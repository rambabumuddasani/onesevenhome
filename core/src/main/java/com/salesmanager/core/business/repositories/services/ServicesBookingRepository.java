package com.salesmanager.core.business.repositories.services;

import com.salesmanager.core.model.customer.ServicesBooking;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServicesBookingRepository extends JpaRepository<ServicesBooking, Long> {
    
	@Query("select sb from ServicesBooking sb where sb.status = 'Y'")
	List<ServicesBooking> getClosedServicesBookings();

	@Query("select sb from ServicesBooking sb where sb.status = 'N'")
	List<ServicesBooking> getOpenedServicesBookings();

	@Query("select sb from ServicesBooking sb where sb.bookingDate between ?1 and ?2 ")
	List<ServicesBooking> getAllServicesBookings(Date startDate, Date endDate);

	@Query("select sb from ServicesBooking sb where sb.status = ?1 and sb.bookingDate between ?2 and ?3")
	List<ServicesBooking> getClosedServicesBookingsByDate(String status, Date startDate, Date endDate);

	@Query("select sb from ServicesBooking sb where sb.status = ?1 and sb.bookingDate between ?2 and ?3")
	List<ServicesBooking> getOpenedServicesBookingsByDate(String status, Date startDate, Date endDate);

	@Query("select sb from ServicesBooking sb where sb.customer.billing.firstName like %?1% or sb.customer.billing.lastName like %?1% or "
			+ " sb.service.vendorAttrs.vendorName like %?1%")  
	List<ServicesBooking> searchServicesBookingByName(String searchString);

	@Query("select sb from ServicesBooking sb where sb.customer.id = ?1 or sb.service.id = ?1 ")
	List<ServicesBooking> searchServicesBookingById(Long userId);

}
