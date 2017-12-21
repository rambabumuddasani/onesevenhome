package com.salesmanager.core.business.services.services;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.ServicesRating;

public interface ServicesRatingService extends SalesManagerEntityService<Long, ServicesRating> 
{
	
	List<ServicesRating> getServicesReviews(Long serviceId);

}
