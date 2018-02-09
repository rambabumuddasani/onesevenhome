package com.salesmanager.core.business.services.catalog.product.filter;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.product.filter.Filter;
import com.salesmanager.core.model.catalog.product.filter.FilterType;

public interface FilterService extends SalesManagerEntityService<Long, Filter> {

	List<Filter> listByCategoryCode(String code) throws ServiceException;

	List<FilterType> getFilterTypeNamesBySearch(String searchFilterString) throws ServiceException;

}
