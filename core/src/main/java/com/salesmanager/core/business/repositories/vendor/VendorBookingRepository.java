package com.salesmanager.core.business.repositories.vendor;

import com.salesmanager.core.model.customer.VendorBooking;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendorBookingRepository extends JpaRepository<VendorBooking, Long> {
    
	@Query("select sb from VendorBooking sb where sb.status = 'Y'")
	List<VendorBooking> getClosedVendorBookings();

	@Query("select sb from VendorBooking sb where sb.status = 'N'")
	List<VendorBooking> getOpenedVendorBookings();

	@Query("select vb from VendorBooking vb where vb.vendor.customerType = ?1 order by vb.bookingDate desc")
	List<VendorBooking> getVendorBookingsByVendorType(String vendorType);

	@Query("select vb from VendorBooking vb where vb.status = ?1 and vb.vendor.customerType = ?2")
	List<VendorBooking> getVendorBookingBasedOnStatus(String status,String vendorType);

	@Query("select vb from VendorBooking vb where vb.vendor.customerType = ?1 and vb.bookingDate between ?2 and ?3 order by vb.bookingDate desc")
	List<VendorBooking> getVendorBookingsByVendorType(String vendorType, Date startDate, Date endDate);

	@Query("select vb from VendorBooking vb where vb.vendor.customerType = ?1 and (vb.vendor.vendorAttrs.vendorName like %?2% or vb.customer.billing.firstName like %?2% or vb.customer.billing.lastName like %?2%) order by vb.bookingDate desc")
	List<VendorBooking> searchVendorBookingsByName(String vendorType, String searchString);

	@Query("select vb from VendorBooking vb where vb.vendor.customerType = ?1 and (vb.vendor.id = ?2 or vb.customer.id = ?2) order by vb.bookingDate desc")
	List<VendorBooking> searchVendorBookingsById(String vendorType, Long userId);

}
