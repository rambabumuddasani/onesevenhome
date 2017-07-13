package com.salesmanager.core.business.services.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.services.ServicesRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.services.Services;

@Service("services")
public class ServicesServiceImpl extends SalesManagerEntityServiceImpl<Integer, Services> 
		implements ServicesService {
	
	private ServicesRepository servicesRepository;

	
/*	@Inject
	private ContentService contentService;
	
	@Inject
	private MerchantConfigurationService merchantConfigurationService;
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private UserService userService;
	
	@Inject
	private OrderService orderService;
	
	@Inject
	private CustomerService customerService;*/
	
	
	
	@Inject
	public ServicesServiceImpl(ServicesRepository servicesRepository) {
		super(servicesRepository);
		this.servicesRepository = servicesRepository;
	}

	@Override
	public List<Services> getAllServices() throws ServiceException {
		try {
			System.out.println("invoking repo method ...");
			List<Services> services = new ArrayList<Services>();
			services = servicesRepository.findAll();
			System.out.println("services in impl ..."+services);
			return services;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	
/*	@Override
	public void delete(MerchantStore merchant) throws ServiceException {
		
		merchant = this.getById(merchant.getId());
		
		
		//reference
		List<Manufacturer> manufacturers = manufacturerService.listByStore(merchant);
		for(Manufacturer manufacturer : manufacturers) {
			manufacturerService.delete(manufacturer);
		}
		
		List<MerchantConfiguration> configurations = merchantConfigurationService.listByStore(merchant);
		for(MerchantConfiguration configuration : configurations) {
			merchantConfigurationService.delete(configuration);
		}
		

		//TODO taxService
		List<TaxClass> taxClasses = taxClassService.listByStore(merchant);
		for(TaxClass taxClass : taxClasses) {
			taxClassService.delete(taxClass);
		}
		
		//content
		contentService.removeFiles(merchant.getCode());
		//TODO staticContentService.removeImages
		
		//category / product
		List<Category> categories = categoryService.listByStore(merchant);
		for(Category category : categories) {
			categoryService.delete(category);
		}

		//users
		List<User> users = userService.listByStore(merchant);
		for(User user : users) {
			userService.delete(user);
		}
		
		//customers
		List<Customer> customers = customerService.listByStore(merchant);
		for(Customer customer : customers) {
			customerService.delete(customer);
		}
		
		//orders
		List<Order> orders = orderService.listByStore(merchant);
		for(Order order : orders) {
			orderService.delete(order);
		}
		
		super.delete(merchant);
		
	}*/
	

}
