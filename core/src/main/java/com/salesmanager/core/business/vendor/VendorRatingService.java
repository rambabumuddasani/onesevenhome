package com.salesmanager.core.business.vendor;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.VendorRating;

public interface VendorRatingService extends SalesManagerEntityService<Long, VendorRating> 
{
	
	List<VendorRating> getVendorReviews(Long vendorId);
}
