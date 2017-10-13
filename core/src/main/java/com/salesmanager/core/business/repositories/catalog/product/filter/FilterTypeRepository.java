package com.salesmanager.core.business.repositories.catalog.product.filter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.catalog.product.filter.FilterType;;


public interface FilterTypeRepository extends JpaRepository<FilterType, Long> {
	
	
}
