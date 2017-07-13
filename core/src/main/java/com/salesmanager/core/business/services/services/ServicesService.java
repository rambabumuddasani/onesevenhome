package com.salesmanager.core.business.services.services;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.services.Services;

public interface ServicesService extends SalesManagerEntityService<Integer, Services> 
{
	

	List<Services> getAllServices()
			throws ServiceException;
	
}
