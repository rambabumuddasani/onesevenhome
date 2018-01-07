package com.salesmanager.core.business.repositories.order;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @Query("select o from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.id = ?1")
	Order findOne(Long id);
    
    @Query("select distinct o from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.customerId = ?1")
	List<Order> findOrdersByCustomer(Long id);
    
/*    @Query(value = "select distinct o from Order o join o.orderProducts op join o.orderTotal ot left join o.orderHistory oh left join op.downloads opd left join op.orderAttributes opa left join op.prices opp where o.customerId = ?1",
    	       countQuery = "select count(distinct o) from Order o join o.orderProducts op join o.orderTotal ot left join o.orderHistory oh left join op.downloads opd left join op.orderAttributes opa left join op.prices opp where o.customerId = ?1")
    Page<Order> findPaginatedOrdersByCustomer(@Param("id") Long id,Pageable pageable);
*/

/**

https://stackoverflow.com/questions/21549480/spring-data-fetch-join-with-paging-is-not-working

*/ 	   
    @Query(value = "select distinct o from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.customerId = ?1",
//    @Query(value = "select distinct o from Order o  where o.customerId = ?1",
    	       countQuery = "select count(distinct o) from Order o  where o.customerId = ?1")
    Page<Order> findPaginatedOrdersByCustomer(@Param("id") Long id,Pageable pageable);

/*    @Query(value = "select distinct o from Order o join fetch o.orderProducts op"
    		+ " join fetch o.orderTotal ot left join fetch o.orderHistory oh left"
    		+ " join fetch op.downloads opd left join fetch op.orderAttributes opa left"
	       	+ "	join  op.prices opp "
    		+" where op.id = ?1"
    //@Query(value = "select distinct o from Order o join fetch o.orderProducts op  where op.vendorId = ?1")
 	       , countQuery = "select count(distinct o) from Order o join o.orderProducts op"
 	       		+ " join  o.orderTotal ot left join  o.orderHistory oh left "
 	       		+ "	join  op.downloads opd left join  op.orderAttributes opa left "
 	       		+ "	join  op.prices opp "
 	       		+ " where op.id = ?1")
    Page<Order> findVendorPaginatedOrders(@Param("id") Long id,Pageable pageable);
*/
    @Query(value = "select distinct o from Order o join fetch o.orderProducts op "
    		+ "join fetch o.orderTotal ot left join fetch o.orderHistory oh left "
    		+ "join fetch op.downloads opd left join fetch op.orderAttributes opa left "
    		+ "join fetch op.prices opp "
    		+ "where op.vendorId = ?1"
    //@Query(value = "select distinct o from Order o join fetch o.orderProducts op  where op.vendorId = ?1"
 	       , countQuery = "select count(distinct o) from Order o join  o.orderProducts op  where op.vendorId = ?1")
    Page<Order> findVendorPaginatedOrders(@Param("id") Long id,Pageable pageable);

    @Query(value = "select distinct o from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.datePurchased BETWEEN ?1 AND ?2",
 	       countQuery = "select count(distinct o) from Order o  where o.datePurchased BETWEEN ?1 AND ?2")
    Page<Order> findByDatePurchasedBetween(@Param("startDate") Date startDate,@Param("endDate")Date endDate,Pageable pageable);

}
