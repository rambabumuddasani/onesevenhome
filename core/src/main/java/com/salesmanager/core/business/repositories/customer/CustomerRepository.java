package com.salesmanager.core.business.repositories.customer;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {

	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions left join fetch c.groups where c.id = ?1")
	Customer findOne(Long id);
	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions left join fetch c.groups  where c.billing.firstName = ?1")
	List<Customer> findByName(String name);
	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions left join fetch c.groups  where c.nick = ?1")
	Customer findByNick(String nick);
	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions  left join fetch c.groups  where c.nick = ?1 and cm.id = ?2")
	Customer findByNick(String nick, int storeId);
	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions  left join fetch c.groups  where cm.id = ?1")
	List<Customer> findByStore(int storeId);
	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions left join fetch c.services cs  where cs.id = ?1")
	List<Customer> findByServiceId(Integer serviceId);
	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions left join fetch c.services cs  where cs.serviceType = ?1")
	List<Customer> findByServiceType(String serviceType);
	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions where c.customerType = ?1")
	List<Customer> findByVendorType(String userType);

	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions where c.customerType = '1'")
	List<Customer> getActivatedVendors();

	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions where c.customerType !='0'")
	List<Customer> getAllVendors();

	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions where c.customerType != '0' and c.activated = ?1")
	List<Customer> getVendorsBasedOnStatus(String status);
	
	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions where c.avgReview <= ?1 and c.customerType = ?2")
	List<Customer> findWallPaperVendorsByRating(BigDecimal rating,String vendorType);
 
	@Query(value="SELECT * FROM CUSTOMER WHERE CUSTOMER_TYPE = (CONCAT('',:customerType)) AND (BILLING_STREET_ADDRESS LIKE CONCAT('%',:searchString,'%') OR BILLING_CITY LIKE CONCAT('%',:searchString,'%')  OR BILLING_COMPANY LIKE CONCAT('%',:searchString,'%') OR CUSTOMER_AREA LIKE CONCAT('%',:searchString,'%'))", nativeQuery = true) 
	List<Customer> findVendorsByLocation(@Param("customerType") String customerType,@Param("searchString") String searchString);

	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions where c.customerType = ?1")
	List<Customer> findVendorsByCustomerType(String customerType);

	@Query("select c from Customer c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions where c.activated = ?1 and c.customerType = ?2")
	List<Customer> findVendorsBasedOnStatusAndCustomerType(String status, String customerType);

	@Query("select c from Customer c join fetch c.services cs where cs.serviceType = ?1 and (c.area like %?2% or c.billing.address like %?2% or c.billing.city like %?2% or c.billing.state like %?2% or c.billing.company like %?2%)")
	List<Customer> findServiceProvidersByLocation(String customerType, String searchString);
}
