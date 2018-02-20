package com.salesmanager.core.business.repositories.catalog.product;

import java.util.Date;
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.catalog.product.Product;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
	
    @Query("select pp from ProductPrice pp where pp.productPriceSpecialStartDate>=?1 and pp.productPriceSpecialEndDate<=?2 and pp.dealOfDay=?3")
	List<Product> findDealOfDay(Date startDate, Date endDate, String status);
    
    @Query("select distinct p from Product as p join fetch p.availabilities pa join fetch p.merchantStore merch join fetch p.descriptions pd "
    +"left join fetch p.categories categs left join fetch pa.prices pap left join fetch pap.descriptions papd left join fetch categs.descriptions categsd "
    +"left join fetch p.images images left join fetch p.attributes pattr left join fetch pattr.productOption po left join fetch po.descriptions pod "
    +"left join fetch pattr.productOptionValue pov left join fetch pov.descriptions povd left join fetch p.relationships pr left join fetch p.manufacturer manuf "
    +"left join fetch manuf.descriptions manufd left join fetch p.type type left join fetch p.taxClass tx where pap.productPriceSpecialStartDate=?1")
	List<Product> findTodayDeals(Date date);
    
    @Query(value = "SELECT MAX(PRODUCT_PRICE_AMOUNT) FROM PRODUCT_PRICE PRICE WHERE PRODUCT_PRICE_ID IN "
    		+ "(SELECT PRODUCT_PRICE_ID FROM PRODUCT_PRICE PRICE INNER JOIN PRODUCT_AVAILABILITY AVAIL "
    		+ "WHERE PRICE.PRODUCT_AVAIL_ID = AVAIL.PRODUCT_AVAIL_ID AND  PRODUCT_ID IN (SELECT PRODUCT_ID FROM PRODUCT_CATEGORY "
    		+ "WHERE CATEGORY_ID = (SELECT CATEGORY_ID FROM CATEGORY WHERE CODE = ?1)))",nativeQuery=true)
    Long getMaxProductPrice(String categoryCode);
    

    @Query("select distinct p from Product as p join fetch p.availabilities pa " 
    		+ "join fetch p.merchantStore merch join fetch p.descriptions pd left join fetch p.categories categs "
    		+ "left join fetch pa.prices pap left join fetch pap.descriptions papd left join fetch categs.descriptions categsd "
    		+ "left join fetch p.images images left join fetch p.attributes pattr left join fetch pattr.productOption po "
    		+ "left join fetch po.descriptions pod left join fetch pattr.productOptionValue pov left join fetch pov.descriptions povd "
    		+ "left join fetch p.relationships pr left join fetch p.manufacturer manuf left join fetch manuf.descriptions manufd "
    		+ "left join fetch p.type type left join fetch p.taxClass tx "
    		+ "where pap.productPriceSpecialStartDate > curdate() order by p.id asc")
	List<Product> findUpcomingDeals();

    @Query("select distinct p from Product as p join fetch p.availabilities pa " 
    		+ "join fetch p.merchantStore merch join fetch p.descriptions pd left join fetch p.categories categs "
    		+ "left join fetch pa.prices pap left join fetch pap.descriptions papd left join fetch categs.descriptions categsd "
    		+ "left join fetch p.images images left join fetch p.attributes pattr left join fetch pattr.productOption po "
    		+ "left join fetch po.descriptions pod left join fetch pattr.productOptionValue pov left join fetch pov.descriptions povd "
    		+ "left join fetch p.relationships pr left join fetch p.manufacturer manuf left join fetch manuf.descriptions manufd "
    		+ "left join fetch p.type type left join fetch p.taxClass tx "
    		+ "where pap.productPriceSpecialStartDate >= ?1 and pap.productPriceSpecialEndDate <= ?1 order by p.id asc")
	List<Product> findExpiredDeals(Date yesterDayDate);

    @Query("select distinct p from Product as p join fetch p.availabilities pa join fetch pa.prices pap where pap.productPriceSpecialStartDate>=?1 and pap.productPriceSpecialEndDate<=?2 and pap.dealOfDay=?3")
    List<Product> modifyDealOfDay(Date startDate, Date endDate, String status);

  
}
