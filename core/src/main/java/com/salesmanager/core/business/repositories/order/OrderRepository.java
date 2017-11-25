package com.salesmanager.core.business.repositories.order;

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
    
    @Query(value = "select distinct o from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.customerId = ?1",
    	       countQuery = "select count(distinct o) from Order o join fetch o.orderProducts op join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.customerId = ?1")
    Page<Order> findPaginatedOrdersByCustomer(@Param("id") Long id,Pageable pageable);
}
