package com.salesmanager.core.business.vendor;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.vendor.VendorBookingRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.VendorBooking;

@Service("vendorBookingService")
public class VendorBookingServiceImpl extends SalesManagerEntityServiceImpl<Long, VendorBooking> 
		implements VendorBookingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VendorBookingServiceImpl.class);
	
	private VendorBookingRepository vendorBookingRepository;

	
	@Inject
	public VendorBookingServiceImpl(VendorBookingRepository vendorBookingRepository) {
		super(vendorBookingRepository);
		this.vendorBookingRepository = vendorBookingRepository;
	}


	@Override
	public List<VendorBooking> getAllVendorBookings() {
		
		return vendorBookingRepository.findAll();
	}


	@Override
	public List<VendorBooking> getClosedVendorBookings() {
	
		return vendorBookingRepository.getClosedVendorBookings();
	}


	@Override
	public List<VendorBooking> getOpenedVendorBookings() {
		
		return vendorBookingRepository.getOpenedVendorBookings();
	}


	@Override
	public List<VendorBooking> getVendorBookingsByVendorType(String vendorType) {
		return vendorBookingRepository.getVendorBookingsByVendorType(vendorType) ;
	}


	@Override
	public List<VendorBooking> getVendorBookingBasedOnStatus(String status,String vendorType) {
		return vendorBookingRepository.getVendorBookingBasedOnStatus(status,vendorType);
	}


	@Override
	public List<VendorBooking> getVendorBookingsByVendorType(String vendorType, Date startDate, Date endDate) {
		return vendorBookingRepository.getVendorBookingsByVendorType(vendorType, startDate, endDate);
	}


	@Override
	public List<VendorBooking> searchVendorBookingsByName(String vendorType, String searchString) {
		return vendorBookingRepository.searchVendorBookingsByName(vendorType, searchString);
	}


	@Override
	public List<VendorBooking> searchVendorBookingsById(String vendorType, Long userId) {
		return vendorBookingRepository.searchVendorBookingsById(vendorType, userId);
	}


}
