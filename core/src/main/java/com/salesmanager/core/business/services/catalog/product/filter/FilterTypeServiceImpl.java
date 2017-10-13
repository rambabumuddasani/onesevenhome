package com.salesmanager.core.business.services.catalog.product.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.catalog.product.filter.FilterRepository;
import com.salesmanager.core.business.repositories.catalog.product.filter.FilterTypeRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.product.filter.Filter;
import com.salesmanager.core.model.catalog.product.filter.FilterType;

@Service("filterTypeService")
public class FilterTypeServiceImpl extends SalesManagerEntityServiceImpl<Long, FilterType> implements FilterTypeService {

	 private FilterTypeRepository filterTypeRepository;
		
	@Inject
	public FilterTypeServiceImpl(FilterTypeRepository filterTypeRepository) {
		super(filterTypeRepository);
		this.filterTypeRepository = filterTypeRepository;
	}

}
