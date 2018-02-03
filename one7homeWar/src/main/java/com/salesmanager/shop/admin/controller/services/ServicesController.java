package com.salesmanager.shop.admin.controller.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.modules.services.ServicesResponse;
import com.salesmanager.core.business.modules.services.ServicesWorkerVO;
import com.salesmanager.core.business.modules.services.WorkerRatingResponse;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.services.ServicesBookingService;
import com.salesmanager.core.business.services.services.ServicesRatingService;
import com.salesmanager.core.business.services.services.ServicesService;
import com.salesmanager.core.business.services.services.WorkerService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.ServicesBooking;
import com.salesmanager.core.model.customer.ServicesRating;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.services.Services;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.controller.vendor.VendorSearchRequest;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.EmailTemplatesUtils;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;




@Controller
@CrossOrigin
public class ServicesController extends AbstractController{

	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesController.class);

	@Inject
	ServicesService servicesService;

	@Inject
	ServicesRatingService servicesRatingService;

	@Inject
	ServicesBookingService servicesBookingService;

	@Inject
	WorkerService workerService;

	@Inject
    private CustomerService customerService;
	
	@Inject
	EmailService emailService;

	@Inject
	private LabelUtils messages;

	@Inject
	private EmailTemplatesUtils emailTemplatesUtils;

	@Inject
	private EmailUtils emailUtils;

    @Inject
    MerchantStoreService merchantStoreService ;
    
    @Inject
	private UserService userService;

	private final static String SERVICE_BOOKING_TMPL = "email_template_service_booking.ftl";
	//private final static String ADMIN_SERVICE_BOOKING_TMPL = "email_template_admin_service_booking.ftl";
	private final static String SERVICEPROVIDER_SERVICE_BOOKING_TMPL = "email_template_serviceprovider_service_booking.ftl";

	@RequestMapping(value="/services", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ServicesResponse getAllServices() {
		//response.setJoin_url("https://zoom.us/j/123456789");
		LOGGER.debug("Entered getAllServices");
		ServicesResponse servicesResponse = new ServicesResponse();
		try
		{
			
			List<Services> services = servicesService.getAllServices();
			servicesResponse.setServices(services);

		}catch(Exception e){
			LOGGER.error("Error while getting services");
		}
		LOGGER.debug("Ended getAllServices");
		return servicesResponse;
	}

	@RequestMapping(value="/services/{serviceid}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	public List<Customer> getWorkerByService(@PathVariable Integer serviceid) {
		LOGGER.debug("Entered getWorkerByService by Id");
		//public Set<CompanyService> getWorkerByService(@PathVariable Integer serviceid) {		
		//return workerService.getWorkerByService(serviceid);
		return customerService.findByServiceId(serviceid);
	}

	@RequestMapping(value="/vendortypes/{type}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	public PaginatedResponse getVendorsByType(@PathVariable String type, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) {
	
		LOGGER.debug("Entered getWorkerByService by type");
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		PaginationData paginaionData=createPaginaionData(page,size);
		try {
			List<ServicesWorkerVO> paginatedResponses = customerService.findByVendorType(type);
	    	calculatePaginaionData(paginaionData,size, paginatedResponses.size());
	    	paginatedResponse.setPaginationData(paginaionData);
			if(paginatedResponses == null || paginatedResponses.isEmpty() || paginatedResponses.size() < paginaionData.getCountByPage()){
				paginatedResponse.setResponseData(paginatedResponses);
				return paginatedResponse;
			}
	    	paginatedResponses = paginatedResponses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	    	paginatedResponse.setResponseData(paginatedResponses);
		} catch(Exception e) {
			e.printStackTrace();
			paginatedResponse.setErrorMsg("Error while retrieving getVendorsByType"+e.getMessage());
			LOGGER.error("Error while retrieving getVendorsByType");
			return paginatedResponse;
		}
    	return paginatedResponse;
    	
	}

	@RequestMapping(value="/services/{type}/workers", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResponse getWorkerByService(@PathVariable String type, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) {
		LOGGER.debug("Entered getWorkerByService by type");
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		PaginationData paginaionData=createPaginaionData(page,size);
		try {
			List<ServicesWorkerVO> paginatedResponses = customerService.findByServiceType(type);
	    	calculatePaginaionData(paginaionData,size, paginatedResponses.size());
	    	paginatedResponse.setPaginationData(paginaionData);
			if(paginatedResponses == null || paginatedResponses.isEmpty() || paginatedResponses.size() < paginaionData.getCountByPage()){
				paginatedResponse.setResponseData(paginatedResponses);
				return paginatedResponse;
			}
	    	paginatedResponses = paginatedResponses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	    	paginatedResponse.setResponseData(paginatedResponses);
		} catch(Exception e) {
			e.printStackTrace();
			paginatedResponse.setErrorMsg("Error while retrieving getWorkerByService"+e.getMessage());
			LOGGER.error("Error while retrieving getWorkerByService");
			return paginatedResponse;
		}
    	return paginatedResponse;
    	
	}

	@RequestMapping(value="/services/{type}/workers/{workerId}/rating", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	public WorkerRatingResponse  getWorkerByServiceType(@PathVariable Integer workerId) {
		LOGGER.debug("Entered getWorkerRating ");
		return workerService.getWorkrRatingdByWorker(workerId);
	}
	
    @RequestMapping(value="/servicerating/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public ServicesRatingResponse saveServicesRating(@RequestBody ServicesRatingRequest servicesRatingRequest) throws Exception {
    	LOGGER.debug("Entered saveServicesRating");
    	ServicesRatingResponse servicesRatingResponse = new ServicesRatingResponse();
    	
    	Customer customer = customerService.getById(servicesRatingRequest.getCustomerId());
    	Customer service = customerService.getById(servicesRatingRequest.getServiceId());
    	Services services = servicesService.getById(servicesRatingRequest.getServiceTypeId());
    	
    	if(customer == null || service == null || services == null){
        	servicesRatingResponse.setErrorMessage("Either customer/service or servicetype invalid.");
        	servicesRatingResponse.setStatus(false);
        	LOGGER.debug("Ended bookServices with errors");
        	return servicesRatingResponse;
    	}
    	ServicesRating servicesRating = new ServicesRating();
    	servicesRating.setCreateDate(new Date());
    	servicesRating.setCustomer(customer);
    	servicesRating.setService(service);
    	servicesRating.setServiceType(services);
    	
    	servicesRating.setRating(servicesRatingRequest.getRating());
    	servicesRating.setReviewTitle(servicesRatingRequest.getReviewTitle());
    	servicesRating.setReviewDescription(servicesRatingRequest.getReviewDescription());
    	servicesRatingService.save(servicesRating);
    	servicesRatingResponse.setSuccessMessage("Rating Saved successfully");
    	LOGGER.debug("saveServicesRating saved");
    	servicesRatingResponse.setStatus(true);
    	LOGGER.debug("Ended saveServicesRating");
    	return servicesRatingResponse;
    }
    
    @RequestMapping(value="/services/booking", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public ServicesRatingResponse bookServices(@RequestBody ServicesBookingRequest servicesBookingRequest) throws Exception {
    	LOGGER.debug("Entered saveServicesRating");
    	ServicesRatingResponse servicesRatingResponse = new ServicesRatingResponse();
    	
    	Customer customer = customerService.getById(servicesBookingRequest.getCustomerId());
    	Customer service = customerService.getById(servicesBookingRequest.getServiceId());
    	Services services = servicesService.getById(servicesBookingRequest.getServiceTypeId());
    	
    	
    	if(customer == null || service == null || services == null){
        	servicesRatingResponse.setErrorMessage("Either customer/service or servicetype invalid.");
        	servicesRatingResponse.setStatus(false);
        	LOGGER.debug("Ended bookServices with errors");
        	return servicesRatingResponse;
    	}
    	
    	ServicesBooking servicesBooking = new ServicesBooking();
    	servicesBooking.setCustomer(customer);
    	servicesBooking.setService(service);
    	servicesBooking.setServiceType(services);
    	servicesBooking.setBookingDate(new Date());
    	servicesBooking.setStatus("N");
    	
    	servicesBookingService.save(servicesBooking);
    	servicesRatingResponse.setSuccessMessage("Service booking accepted.");
    	LOGGER.debug("bookServices saved");
    	servicesRatingResponse.setStatus(true);
    	
    	MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");
    	final Locale locale  = new Locale("en");
    	
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
        if(customer.getCustomerType().equals("0")) {
        templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getBilling().getFirstName());
        templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, customer.getBilling().getLastName());
        } else {
        	templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getVendorAttrs().getVendorName());
        	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, "");
        }
        templateTokens.put(EmailConstants.EMAIL_USER_NAME, customer.getEmailAddress());
        //ServicesBooking serviceBooking = servicesBookingService.getById(servicesBooking.getId());
        templateTokens.put(EmailConstants.EMAIL_SERVICE_TYPE, services.getServiceType());
        templateTokens.put(EmailConstants.EMAIL_SERVICEPROVIDER_NAME, service.getVendorAttrs().getVendorName());
        templateTokens.put(EmailConstants.EMAIL_SERVICEPRIVIDER_IMAGE,service.getVendorAttrs().getVendorAuthCert());
		
        Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("eamil.customer.service.booking",locale));
		email.setTo(customer.getEmailAddress());
		//email.setTo("surendervarmac@gmail.com");
		email.setTemplateName(SERVICE_BOOKING_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		LOGGER.debug("Email sent to customer");
		sendEmailToServiceProvider(merchantStore,customer,services,service);
		//sendEmailToAdmin(customer,services,merchantStore,service);
		LOGGER.debug("Email sent successful");
    	LOGGER.debug("Ended bookServices");
    	return servicesRatingResponse;
    }

 /*   private void sendEmailToAdmin(Customer customer, Services services, MerchantStore merchantStore,Customer service) throws Exception {
    	User user = userService.getById(1l);
    	final Locale locale  = new Locale("en");
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
        templateTokens.put(EmailConstants.EMAIL_ADMIN_NAME, user.getAdminName());
        if(customer.getCustomerType().equals("0")) {
            templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getBilling().getFirstName());
            templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, customer.getBilling().getLastName());
            } else {
            	templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getVendorAttrs().getVendorName());
            	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, "");
            }
        templateTokens.put(EmailConstants.EMAIL_SERVICEPROVIDER_NAME, service.getVendorAttrs().getVendorName());
        templateTokens.put(EmailConstants.EMAIL_SERVICEPRIVIDER_IMAGE,service.getVendorAttrs().getVendorAuthCert());
        templateTokens.put(EmailConstants.EMAIL_PRODUCT_LABEL, messages.getMessage("email.vendor.add.request.product", locale));
        
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("eamil.customer.service.booking",locale));
		//email.setTo("surendervarmac@gmail.com");
		email.setTo(user.getAdminEmail());
		email.setTemplateName(ADMIN_SERVICE_BOOKING_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		
		LOGGER.debug("Email sent to admin");
		
	}*/

	private void sendEmailToServiceProvider(MerchantStore merchantStore, Customer customer, Services services,Customer service) throws Exception{
		final Locale locale  = new Locale("en");
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
        if(customer.getCustomerType().equals("0")) {
            templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getBilling().getFirstName());
            templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, customer.getBilling().getLastName());
            } else {
            	templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getVendorAttrs().getVendorName());
            	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, "");
            }
        templateTokens.put(EmailConstants.EMAIL_SERVICEPROVIDER_NAME, service.getVendorAttrs().getVendorName());
        templateTokens.put(EmailConstants.EMAIL_SERVICEPRIVIDER_IMAGE,service.getVendorAttrs().getVendorAuthCert());
        templateTokens.put(EmailConstants.EMAIL_SERVICE_TYPE, services.getServiceType());
        
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("eamil.customer.service.booking",locale));
		//email.setTo("surendervarmac@gmail.com");
		email.setTo(service.getEmailAddress());
		email.setTemplateName(SERVICEPROVIDER_SERVICE_BOOKING_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		
		LOGGER.debug("Email sent to Service provider");
		
	}

	@RequestMapping(value="/admin/getServicesBooking", method = RequestMethod.POST)
	@ResponseBody
	public PaginatedResponse getServicesBooking(@RequestBody AdminServicesBookingRequest adminServicesBookingRequest, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		LOGGER.debug("Entered getServicesBooking");
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		CustomerBookingDetails customerBookingDetails = new CustomerBookingDetails();
		try {
		List<ServicesBookingVO> servicesBookingVOList = new ArrayList<ServicesBookingVO>();
		if(adminServicesBookingRequest.getStatus().equals("ALL")) {
			List<ServicesBooking> servicesBookings = servicesBookingService.getAllServicesBookings();	
		for(ServicesBooking servicesBooking : servicesBookings) {
			ServicesBookingVO servicesBookingVO = new ServicesBookingVO();
			servicesBookingVO.setBookingDate(servicesBooking.getBookingDate());
			servicesBookingVO.setServicesBookingId(servicesBooking.getId());
			servicesBookingVO.setServiceType(servicesBooking.getServiceType().getServiceType());
			servicesBookingVO.setStatus(servicesBooking.getStatus());
			if(servicesBooking.getCustomer().getCustomerType().equals("0")){
			customerBookingDetails.setCustomerName(servicesBooking.getCustomer().getBilling().getFirstName().concat(" ").concat(servicesBooking.getCustomer().getBilling().getLastName()));
			customerBookingDetails.setContactNumber(servicesBooking.getCustomer().getBilling().getTelephone());
			customerBookingDetails.setStreet(servicesBooking.getCustomer().getBilling().getAddress());
			customerBookingDetails.setArea(servicesBooking.getCustomer().getArea());
			customerBookingDetails.setCity(servicesBooking.getCustomer().getBilling().getState());
			customerBookingDetails.setState(servicesBooking.getCustomer().getBilling().getState());
			customerBookingDetails.setPinCode(servicesBooking.getCustomer().getBilling().getPostalCode());
			customerBookingDetails.setEmailAddress(servicesBooking.getCustomer().getEmailAddress());
			}
			else {
				customerBookingDetails.setCustomerName(servicesBooking.getCustomer().getVendorAttrs().getVendorName());
				customerBookingDetails.setContactNumber(servicesBooking.getCustomer().getBilling().getTelephone());
				customerBookingDetails.setStreet(servicesBooking.getCustomer().getVendorAttrs().getVendorOfficeAddress());
				customerBookingDetails.setArea(servicesBooking.getCustomer().getArea());
				customerBookingDetails.setCity(servicesBooking.getCustomer().getBilling().getState());
				customerBookingDetails.setState(servicesBooking.getCustomer().getBilling().getState());
				customerBookingDetails.setPinCode(servicesBooking.getCustomer().getBilling().getPostalCode());
				customerBookingDetails.setEmailAddress(servicesBooking.getCustomer().getEmailAddress());
			}
			Customer serviceProvider = servicesBooking.getService();
			Long serviceProviderId = serviceProvider.getId();
			ServiceProviderDetails serviceProviderDetails = new ServiceProviderDetails();
			serviceProviderDetails.setServiceProviderId(serviceProviderId);
			serviceProviderDetails.setArea(serviceProvider.getArea());
			serviceProviderDetails.setCity(serviceProvider.getBilling().getCity());
			serviceProviderDetails.setState(serviceProvider.getBilling().getState());
			serviceProviderDetails.setPinCode(serviceProvider.getBilling().getPostalCode());
			serviceProviderDetails.setEmailAddress(serviceProvider.getEmailAddress());
			//serviceProviderDetails.setContactNumber(serviceProvider.getVendorAttrs().getVendorTelephone());
			//serviceProviderDetails.setCompanyName(serviceProvider.getVendorAttrs().getVendorName());
			servicesBookingVO.setServiceProviderDetails(serviceProviderDetails);
			servicesBookingVO.setCustomerBookingdetails(customerBookingDetails);
			servicesBookingVOList.add(servicesBookingVO);
		}
		}
		if(adminServicesBookingRequest.getStatus().equals("Y")) {
			List<ServicesBooking> servicesBookings = servicesBookingService.getClosedServicesBookings();
			for(ServicesBooking servicesBooking : servicesBookings) {
				ServicesBookingVO servicesBookingVO = new ServicesBookingVO();
				servicesBookingVO.setBookingDate(servicesBooking.getBookingDate());
				servicesBookingVO.setServicesBookingId(servicesBooking.getId());
				servicesBookingVO.setServiceType(servicesBooking.getServiceType().getServiceType());
				servicesBookingVO.setStatus(servicesBooking.getStatus());
				if(servicesBooking.getCustomer().getCustomerType().equals("0")){
					customerBookingDetails.setCustomerName(servicesBooking.getCustomer().getBilling().getFirstName().concat(" ").concat(servicesBooking.getCustomer().getBilling().getLastName()));
					customerBookingDetails.setContactNumber(servicesBooking.getCustomer().getBilling().getTelephone());
					customerBookingDetails.setStreet(servicesBooking.getCustomer().getBilling().getAddress());
					customerBookingDetails.setArea(servicesBooking.getCustomer().getArea());
					customerBookingDetails.setCity(servicesBooking.getCustomer().getBilling().getState());
					customerBookingDetails.setState(servicesBooking.getCustomer().getBilling().getState());
					customerBookingDetails.setPinCode(servicesBooking.getCustomer().getBilling().getPostalCode());
					customerBookingDetails.setEmailAddress(servicesBooking.getCustomer().getEmailAddress());
					}
					else {
						customerBookingDetails.setCustomerName(servicesBooking.getCustomer().getVendorAttrs().getVendorName());
						customerBookingDetails.setContactNumber(servicesBooking.getCustomer().getBilling().getTelephone());
						customerBookingDetails.setStreet(servicesBooking.getCustomer().getVendorAttrs().getVendorOfficeAddress());
						customerBookingDetails.setArea(servicesBooking.getCustomer().getArea());
						customerBookingDetails.setCity(servicesBooking.getCustomer().getBilling().getState());
						customerBookingDetails.setState(servicesBooking.getCustomer().getBilling().getState());
						customerBookingDetails.setPinCode(servicesBooking.getCustomer().getBilling().getPostalCode());
						customerBookingDetails.setEmailAddress(servicesBooking.getCustomer().getEmailAddress());
					}
				Customer serviceProvider = servicesBooking.getService();
				Long serviceProviderId = serviceProvider.getId();
				ServiceProviderDetails serviceProviderDetails = new ServiceProviderDetails();
				serviceProviderDetails.setServiceProviderId(serviceProviderId);
				serviceProviderDetails.setArea(serviceProvider.getArea());
				serviceProviderDetails.setCity(serviceProvider.getBilling().getCity());
				serviceProviderDetails.setState(serviceProvider.getBilling().getState());
				serviceProviderDetails.setPinCode(serviceProvider.getBilling().getPostalCode());
				serviceProviderDetails.setEmailAddress(serviceProvider.getEmailAddress());
				//serviceProviderDetails.setContactNumber(serviceProvider.getVendorAttrs().getVendorTelephone());
				//serviceProviderDetails.setCompanyName(serviceProvider.getVendorAttrs().getVendorName());
				servicesBookingVO.setServiceProviderDetails(serviceProviderDetails);
				servicesBookingVO.setCustomerBookingdetails(customerBookingDetails);
				servicesBookingVOList.add(servicesBookingVO);
			}
		}
		if(adminServicesBookingRequest.getStatus().equals("N")) {
			List<ServicesBooking> servicesBookings = servicesBookingService.getOpenedServicesBookings();
			for(ServicesBooking servicesBooking : servicesBookings) {
				ServicesBookingVO servicesBookingVO = new ServicesBookingVO();
				servicesBookingVO.setBookingDate(servicesBooking.getBookingDate());
				servicesBookingVO.setServicesBookingId(servicesBooking.getId());
				servicesBookingVO.setServiceType(servicesBooking.getServiceType().getServiceType());
				servicesBookingVO.setStatus(servicesBooking.getStatus());
				if(servicesBooking.getCustomer().getCustomerType().equals("0")){
					customerBookingDetails.setCustomerName(servicesBooking.getCustomer().getBilling().getFirstName().concat(" ").concat(servicesBooking.getCustomer().getBilling().getLastName()));
					customerBookingDetails.setContactNumber(servicesBooking.getCustomer().getBilling().getTelephone());
					customerBookingDetails.setStreet(servicesBooking.getCustomer().getBilling().getAddress());
					customerBookingDetails.setArea(servicesBooking.getCustomer().getArea());
					customerBookingDetails.setCity(servicesBooking.getCustomer().getBilling().getState());
					customerBookingDetails.setState(servicesBooking.getCustomer().getBilling().getState());
					customerBookingDetails.setPinCode(servicesBooking.getCustomer().getBilling().getPostalCode());
					customerBookingDetails.setEmailAddress(servicesBooking.getCustomer().getEmailAddress());
					}
					else {
						customerBookingDetails.setCustomerName(servicesBooking.getCustomer().getVendorAttrs().getVendorName());
						customerBookingDetails.setContactNumber(servicesBooking.getCustomer().getBilling().getTelephone());
						customerBookingDetails.setStreet(servicesBooking.getCustomer().getVendorAttrs().getVendorOfficeAddress());
						customerBookingDetails.setArea(servicesBooking.getCustomer().getArea());
						customerBookingDetails.setCity(servicesBooking.getCustomer().getBilling().getState());
						customerBookingDetails.setState(servicesBooking.getCustomer().getBilling().getState());
						customerBookingDetails.setPinCode(servicesBooking.getCustomer().getBilling().getPostalCode());
						customerBookingDetails.setEmailAddress(servicesBooking.getCustomer().getEmailAddress());
					}
				Customer serviceProvider = servicesBooking.getService();
				Long serviceProviderId = serviceProvider.getId();
				ServiceProviderDetails serviceProviderDetails = new ServiceProviderDetails();
				serviceProviderDetails.setServiceProviderId(serviceProviderId);
				serviceProviderDetails.setArea(serviceProvider.getArea());
				serviceProviderDetails.setCity(serviceProvider.getBilling().getCity());
				serviceProviderDetails.setState(serviceProvider.getBilling().getState());
				serviceProviderDetails.setPinCode(serviceProvider.getBilling().getPostalCode());
				serviceProviderDetails.setEmailAddress(serviceProvider.getEmailAddress());
				//serviceProviderDetails.setContactNumber(serviceProvider.getVendorAttrs().getVendorTelephone());
				//serviceProviderDetails.setCompanyName(serviceProvider.getVendorAttrs().getVendorName());
				servicesBookingVO.setServiceProviderDetails(serviceProviderDetails);
				servicesBookingVO.setCustomerBookingdetails(customerBookingDetails);
				servicesBookingVOList.add(servicesBookingVO);
			}
		}
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, servicesBookingVOList.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(servicesBookingVOList == null || servicesBookingVOList.isEmpty() || servicesBookingVOList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(servicesBookingVOList);
			LOGGER.debug("Ended getTestimonials");
			return paginatedResponse;
		}
    	List<ServicesBookingVO> paginatedResponses = servicesBookingVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
		} catch(Exception e) {
			e.printStackTrace();
			paginatedResponse.setErrorMsg("Error while retrieving services bookings"+e.getMessage());
			LOGGER.error("Error while retrieving services bookings");
			return paginatedResponse;
		}
    	return paginatedResponse;
    	
    }
	@RequestMapping(value="/admin/servicesBooking", method = RequestMethod.POST)
	@ResponseBody
	public ServicesBookingStatusResponse servicesBookingStatus(@RequestBody ServicesBookingStatusRequest servicesBookingStatusRequest) throws Exception {
		LOGGER.debug("Entered servicesBookingStatus");
		ServicesBookingStatusResponse servicesBookingStatusResponse = new ServicesBookingStatusResponse();
		try {
			ServicesBooking servicesBooking = servicesBookingService.getById(servicesBookingStatusRequest.getServicesBookingId());
			if(servicesBooking==null) {
				LOGGER.debug("services booking not found");
				servicesBookingStatusResponse.setErrormessage("services booking not found");
				servicesBookingStatusResponse.setStatus("FALSE");
				return servicesBookingStatusResponse;
			}
			if(servicesBookingStatusRequest.getStatus().equals("Y")) {
				LOGGER.debug("Services Booking closed");
				servicesBooking.setStatus(servicesBookingStatusRequest.getStatus());
				servicesBooking.setComment(servicesBookingStatusRequest.getComment());
			} 
			servicesBookingService.update(servicesBooking);
			if(servicesBookingStatusRequest.getStatus().equals("Y")) {
				LOGGER.debug("Services Booking closed");
				servicesBookingStatusResponse.setSuccessMessage("Services Booking closed successfully");
				servicesBookingStatusResponse.setStatus("TRUE");
			}
			if(servicesBookingStatusRequest.getStatus().equals("N")) {
				LOGGER.debug("Services Booking already in open state");
				servicesBookingStatusResponse.setErrormessage("Services Booking is already in open state");
				
			}
		}catch(Exception e) {
			LOGGER.error("Error occured while closing services booking"+e.getMessage());
			servicesBookingStatusResponse.setErrormessage("Error occured while closing services booking");
			servicesBookingStatusResponse.setStatus("FALSE");
			return servicesBookingStatusResponse;
		}
		return servicesBookingStatusResponse;
		
	}
	@RequestMapping(value="/admin/deleteServicesBooking/{servicesBookingId}", method = RequestMethod.GET)
	@ResponseBody
	public DeleteServicesBookingResponse deleteServicesBookingStatus(@PathVariable String servicesBookingId) throws Exception {
		LOGGER.debug("Entered deleteServicesBookingStatus");
		DeleteServicesBookingResponse deleteServicesBookingResponse = new DeleteServicesBookingResponse();
		try {
		Long servicesBookingIdLong = new Long(servicesBookingId);
		ServicesBooking servicesBooking = servicesBookingService.getById(servicesBookingIdLong);
		if(servicesBooking==null) {
			LOGGER.debug("No services booking found for this id  "+servicesBookingIdLong);
			deleteServicesBookingResponse.setErrorMessage("services booking not found");
			deleteServicesBookingResponse.setStatus("FALSE");
			return deleteServicesBookingResponse;
		}
		servicesBookingService.delete(servicesBooking);
		LOGGER.debug("Services booking deleted");
		deleteServicesBookingResponse.setSuccessMessage("Services Booking deleted successfully");
		deleteServicesBookingResponse.setStatus("TRUE");
		}catch(Exception e) {
			LOGGER.error("Error while delting services booking"+e.getMessage());
		}
		return deleteServicesBookingResponse;
		
	}
	
	// Service Providers search based on location 
	@RequestMapping(value="/getServiceProvidersByLocation", method = RequestMethod.POST)
	@ResponseBody
	public PaginatedResponse getServiceProvidersByLocation(
			@RequestBody VendorSearchRequest vendorSearchRequest,
			@RequestParam(value="pageNumber", defaultValue = "1") int page, 
            @RequestParam(value="pageSize", defaultValue="15") int size) {
		
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		
		List<ServicesWorkerVO> servicesWorkerVOList= new ArrayList<ServicesWorkerVO>();
		
		List<Customer> serviceProviders = customerService.getServiceProvidersByLocation(vendorSearchRequest.getCustomerType(),vendorSearchRequest.getSearchString());
		
		for(Customer serviceProvider : serviceProviders) {
			
			Double avgRating = new Double(0);
			int totalRating= 0;
			int totalReviews = 0;
			double totalRate = 0;
			
			ServicesWorkerVO servicesWorkerVO = new ServicesWorkerVO();
			servicesWorkerVO.setId(new Integer(String.valueOf((serviceProvider.getId()))));
			servicesWorkerVO.setCompanyName(serviceProvider.getVendorAttrs().getVendorName());
			/*servicesWorkerVO.setHouseNumber(serviceProvider.getVendorAttrs().getVendorOfficeAddress());
			servicesWorkerVO.setStreet(serviceProvider.getBilling().getAddress());
			servicesWorkerVO.setArea(serviceProvider.getArea());
			servicesWorkerVO.setCity(serviceProvider.getBilling().getCity());
			servicesWorkerVO.setState(serviceProvider.getBilling().getState());
			servicesWorkerVO.setPinCode(serviceProvider.getBilling().getPostalCode());
			servicesWorkerVO.setContactNumber(serviceProvider.getBilling().getTelephone());
			servicesWorkerVO.setImageUrl(serviceProvider.getVendorAttrs().getVendorAuthCert());
			servicesWorkerVO.setCountry(serviceProvider.getBilling().getCountry().getName());*/
			//servicesWorkerVOList.add(servicesWorkerVO);
			//fetching ratings from services rating
			List<ServicesRating> servicesRatingList = servicesRatingService.getServicesReviews(serviceProvider.getId());
			if(servicesRatingList != null) {
				totalReviews = servicesRatingList.size();
				for(ServicesRating servicesRating:servicesRatingList){
					totalRating= totalRating + servicesRating.getRating();
				}
				totalRate = totalRating;
				avgRating = Double.valueOf(totalRate / totalReviews);
				avgRating = Double.valueOf(Math.round(avgRating.doubleValue() * 10D) / 10D);
			}
			servicesWorkerVO.setAvgRating(avgRating);
			//servicesWorkerVO.setTotalRating(totalRating);
			servicesWorkerVO.setTotalRating(totalReviews);
			servicesWorkerVOList.add(servicesWorkerVO);
		}
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, servicesWorkerVOList.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(servicesWorkerVOList == null || servicesWorkerVOList.isEmpty() || servicesWorkerVOList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(servicesWorkerVOList);
			return paginatedResponse;
		}
    	List<ServicesWorkerVO> paginatedResponses = servicesWorkerVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
		return paginatedResponse;
		
	}
	
	// Vendors listing(Architects)
	@RequestMapping(value="/getVendorsList", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResponse getVendorsList(@RequestBody VendorDetailsRequest vendorDetailsRequest, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) {
		
		LOGGER.debug("Entered getVendorsList");
		
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		
		PaginationData paginaionData=createPaginaionData(page,size);
		
		try {
			List<ServicesWorkerVO> paginatedResponses = customerService.getVendorsByCode(vendorDetailsRequest.getCode());
	    	calculatePaginaionData(paginaionData,size, paginatedResponses.size());
	    	paginatedResponse.setPaginationData(paginaionData);
	    	
			if(paginatedResponses == null || paginatedResponses.isEmpty() || paginatedResponses.size() < paginaionData.getCountByPage()){
				paginatedResponse.setResponseData(paginatedResponses);
				return paginatedResponse;
			}
			
	    	paginatedResponses = paginatedResponses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	    	paginatedResponse.setResponseData(paginatedResponses);
	    	
		} catch(Exception e) {
			e.printStackTrace();
			paginatedResponse.setErrorMsg("Error while retrieving getWorkerByService"+e.getMessage());
			LOGGER.error("Error while retrieving getWorkerByService");
			return paginatedResponse;
		}
		
		LOGGER.debug("Ended getVendorsList");
    	return paginatedResponse;
    	
	}
}
