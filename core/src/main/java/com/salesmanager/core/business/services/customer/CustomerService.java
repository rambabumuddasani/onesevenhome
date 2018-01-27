package com.salesmanager.core.business.services.customer;


import java.math.BigDecimal;
import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.services.ServicesWorkerVO;
import com.salesmanager.core.business.modules.services.WorkerServiceResponse;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.common.Address;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.CustomerCriteria;
import com.salesmanager.core.model.customer.CustomerList;
import com.salesmanager.core.model.merchant.MerchantStore;



public interface CustomerService  extends SalesManagerEntityService<Long, Customer> {

	public List<Customer> getByName(String firstName);

	List<Customer> listByStore(MerchantStore store);

	Customer getByNick(String nick);
	void saveOrUpdate(Customer customer) throws ServiceException ;

	CustomerList listByStore(MerchantStore store, CustomerCriteria criteria);

	Customer getByNick(String nick, int storeId);

	/**
	 * Return an {@link com.salesmanager.core.business.common.model.Address} object from the client IP address. Uses underlying GeoLocation module
	 * @param store
	 * @param ipAddress
	 * @return
	 * @throws ServiceException
	 */
	Address getCustomerAddress(MerchantStore store, String ipAddress)
			throws ServiceException;

	Customer getById(Long vendorId);

	List<ServicesWorkerVO> findByServiceType(String serviceType);
	List<Customer> findByServiceId(Integer serviceId);
	List<ServicesWorkerVO> findByVendorType(String userType);

	public List<Customer> getActivatedVendors();

	public List<Customer> getAllVendors();

	public List<Customer> getVendorsBasedOnStatus(String status);
	
	public List<Customer> getWallPaperVendorsByRating(BigDecimal rating,String vendorType);

	public List<Customer> getVendorsByLocation(String customerType,String searchString);

	public List<Customer> getVendorsByCustomerType(String customerType);

	public List<Customer> getVendorsBasedOnStatusAndCustomerType(String status, String customerType);

	public List<Customer> getServiceProvidersByLocation(String customerType,String searchString);

}
