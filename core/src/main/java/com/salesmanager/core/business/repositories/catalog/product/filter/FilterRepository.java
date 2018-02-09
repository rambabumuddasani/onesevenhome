package com.salesmanager.core.business.repositories.catalog.product.filter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.catalog.product.filter.Filter;
import com.salesmanager.core.model.catalog.product.filter.FilterType;


public interface FilterRepository extends JpaRepository<Filter, Long> {
	
	@Query("select f from Filter f left join fetch f.filterTypes ft join fetch f.category fc where fc.code=?1")
	public List<Filter> listByCategoryCode(String code);

	@Query("select ft from FilterType ft where ft.filterTypeName like %?1%")
	public List<FilterType> findFilterTypeNamesBySearch(String searchFilterString);
	
	
}
