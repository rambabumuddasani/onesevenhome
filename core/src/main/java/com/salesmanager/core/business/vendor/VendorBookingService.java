package com.salesmanager.core.business.vendor;

import java.util.Date;
import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.VendorBooking;

public interface VendorBookingService extends SalesManagerEntityService<Long, VendorBooking> {

	List<VendorBooking> getAllVendorBookings();

	List<VendorBooking> getClosedVendorBookings();

	List<VendorBooking> getOpenedVendorBookings();

	List<VendorBooking> getVendorBookingsByVendorType(String vendorType);

	List<VendorBooking> getVendorBookingBasedOnStatus(String status,String vendorType);

	List<VendorBooking> getVendorBookingsByVendorType(String vendorType, Date startDate, Date endDate);

	List<VendorBooking> searchVendorBookingsByName(String vendorType, String searchString);

	List<VendorBooking> searchVendorBookingsById(String vendorType, Long userId);
	
	
}
