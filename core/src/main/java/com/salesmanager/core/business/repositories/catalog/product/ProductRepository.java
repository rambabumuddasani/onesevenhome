package com.salesmanager.core.business.repositories.catalog.product;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.catalog.product.Product;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query("select pp from ProductPrice pp where pp.productPriceSpecialStartDate>=?1 and pp.productPriceSpecialEndDate<=?2 and pp.dealOfDay=?3")
	List<Product> findDealOfDay(Date startDate, Date endDate, String status);
	
}
