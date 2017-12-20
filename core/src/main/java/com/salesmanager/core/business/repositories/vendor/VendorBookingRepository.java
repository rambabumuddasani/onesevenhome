package com.salesmanager.core.business.repositories.vendor;

import com.salesmanager.core.model.customer.VendorBooking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendorBookingRepository extends JpaRepository<VendorBooking, Long> {
    
	@Query("select sb from VendorBooking sb where sb.status = 'Y'")
	List<VendorBooking> getClosedVendorBookings();

	@Query("select sb from VendorBooking sb where sb.status = 'N'")
	List<VendorBooking> getOpenedVendorBookings();

}
