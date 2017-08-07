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
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.product.filter.Filter;

@Service("filterService")
public class FilterServiceImpl extends SalesManagerEntityServiceImpl<Long, Filter> implements FilterService {
	
	 private FilterRepository filterRepository;
	
	@Inject
	public FilterServiceImpl(FilterRepository filterRepository) {
		super(filterRepository);
		this.filterRepository = filterRepository;
	}
	
	
	@Override
	public List<Filter> listByCategoryCode(String code) throws ServiceException {
		
		try {
			return filterRepository.listByCategoryCode(code);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
	}

}
