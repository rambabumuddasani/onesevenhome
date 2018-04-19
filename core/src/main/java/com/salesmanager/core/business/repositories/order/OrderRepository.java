package com.salesmanager.core.business.repositories.order;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.OrderTotal;
import com.salesmanager.core.model.order.orderproduct.OrderProduct;

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

    
    @Query("select distinct o from Order o join fetch o.orderProducts op where o.datePurchased BETWEEN ?1 AND ?2 and op.vendorId=?3 and o.status='COMPLETED'")     
	List<Order> findOrdersByVendor(Date startDate, Date endDate, Long value);

    @Query("select o from Order o join fetch o.orderProducts op where o.datePurchased BETWEEN ?1 AND ?2 and op.sku=?3 and o.status='COMPLETED'")
	List<Order> findOrdersByProduct(Date startDate, Date endDate, String value);

    @Query(value="SELECT DISTINCT VENDOR_ID FROM ORDER_PRODUCT as p inner join ORDERS as o " 
    		+" where o.DATE_PURCHASED BETWEEN ?1 AND ?2 and o.ORDER_ID=p.ORDER_ID",nativeQuery=true)
	List<BigInteger> findVendorIds(Date startDate, Date endDate);

    @Query(value="SELECT DISTINCT PRODUCT_SKU FROM ORDER_PRODUCT as p INNER JOIN ORDERS as o " 
    		+" WHERE o.DATE_PURCHASED BETWEEN ?1 AND ?2 and o.ORDER_ID=p.ORDER_ID AND p.PRODUCT_CATEGORY IS NULL",nativeQuery=true)
	List<String> findProductSkus(Date startDate, Date endDate);

    @Query("select distinct op from OrderProduct op where op.vendorId=?1")
	List<OrderProduct> findOrderProductByVendorIdAndSku(Long vendorId, String productSku);

    @Query(value = "SELECT distinct(op.VENDOR_ID) FROM ORDER_PRODUCT as op inner join ORDERS as o "
    		+" on o.ORDER_ID=op.ORDER_ID where o.DATE_PURCHASED between ?1 and ?2 ", nativeQuery=true)
	List<BigInteger> findRevenueVendors(Date startDate, Date endDate);

   /* @Query(value="SELECT DISTINCT VENDOR_ID FROM ORDER_PRODUCT as p inner join ORDERS as o " 
    		+ " where (o.BILLING_FIRST_NAME like %?1?% or o.BILLING_LAST_NAME like %?1%) or "
    		+ " o.DELIVERY_FIRST_NAME like %?1?% or o.DELIVERY_LAST_NAME like %?1%) and o.ORDER_ID=p.ORDER_ID",nativeQuery=true)
	List<BigInteger> getRevenueVendorsBySearch(String searchString);*/

    @Query(value = "SELECT distinct(op.VENDOR_ID) FROM ORDER_PRODUCT as op inner join ORDERS as o "
    		+" on o.ORDER_ID=op.ORDER_ID ", nativeQuery=true)
	List<BigInteger> searchRevenueVendors();

    @Query("select distinct o from Order o join fetch o.orderProducts op where op.vendorId=?1 and o.status='COMPLETED'")
	List<Order> findOrdersSearchyVendor(long vendorId);

    @Query(value="SELECT DISTINCT PRODUCT_SKU FROM ORDER_PRODUCT as p INNER JOIN ORDERS as o " 
    		+" WHERE o.ORDER_ID=p.ORDER_ID AND p.PRODUCT_CATEGORY IS NULL",nativeQuery=true)
	List<String> searchRevenueProducts();

    @Query("select o from Order o join fetch o.orderProducts op where op.sku=?1 and o.status='COMPLETED'")
	List<Order> findOrdersSearchByProduct(String productSku);

    @Query(value = "select distinct o from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.datePurchased BETWEEN ?1 AND ?2 and o.id = ?3",
   		 countQuery = "select count(distinct o) from Order o  where o.datePurchased BETWEEN ?1 AND ?2 and o.id = ?3")
	Page<Order> adminSearchOrdersByDatePurchasedBetween(Date fromDate, Date toDate, Long orderId,
			Pageable pageable);

    @Query(value = "select distinct o from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where  o.customerId in (select c.id from Customer c where c.billing.firstName like %?3% or c.billing.lastName like %?3% or c.delivery.firstName like %?3% or c.delivery.lastName like %?3% or c.secondaryDelivery.firstName like %?3% or c.secondaryDelivery.lastName like %?3%) and o.datePurchased BETWEEN ?1 AND ?2 ",
     		 countQuery = "select count(distinct o) from Order o  where  o.customerId in (select c.id from Customer c where c.billing.firstName like %?3% or c.billing.lastName like %?3% or c.delivery.firstName like %?3% or c.delivery.lastName like %?3% or c.secondaryDelivery.firstName like %?3% or c.secondaryDelivery.lastName like %?3%) and o.datePurchased BETWEEN ?1 AND ?2)")
	Page<Order> adminSearchOrdersByDatePurchasedBetweenAndName(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("searchString") String searchString,
			Pageable pageable);

    @Query("select distinct o from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.customerId in (select c.id from Customer c where c.billing.firstName like %?3% or c.billing.lastName like %?3% or c.delivery.firstName like %?3% or c.delivery.lastName like %?3% or c.secondaryDelivery.firstName like %?3% or c.secondaryDelivery.lastName like %?3%) and o.datePurchased BETWEEN ?1 AND ?2 ")
	List<Order> searchOrdersByCustomerName(Date fromDate, Date toDate, String searchString);

}
