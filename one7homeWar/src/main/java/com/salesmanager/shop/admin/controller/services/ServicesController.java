package com.salesmanager.shop.admin.controller.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.modules.services.ServicesResponse;
import com.salesmanager.core.business.modules.services.WorkerRatingResponse;
import com.salesmanager.core.business.modules.services.WorkerServiceResponse;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.services.ServicesBookingService;
import com.salesmanager.core.business.services.services.ServicesRatingService;
import com.salesmanager.core.business.services.services.ServicesService;
import com.salesmanager.core.business.services.services.WorkerService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.CustomerTestimonial;
import com.salesmanager.core.model.customer.ServicesBooking;
import com.salesmanager.core.model.customer.ServicesRating;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.services.Services;
import com.salesmanager.core.model.user.User;
import com.salesmanager.core.model.services.CompanyService;
import org.springframework.web.bind.annotation.RequestBody;

import com.salesmanager.shop.admin.controller.CustomerTestimonialVO;
import com.salesmanager.shop.admin.controller.TestimonialRequest;
import com.salesmanager.shop.admin.controller.TestimonialResponse;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.controller.AbstractController;
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

	@RequestMapping(value="/services/{type}/workers", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody	
	//public List<Customer> getWorkerByService(@PathVariable String type) {		
	public WorkerServiceResponse getWorkerByService(@PathVariable String type) {
		LOGGER.debug("Entered getWorkerByService by type");
			//return workerService.getWorkerByServiceType(type);
		return customerService.findByServiceType(type);
			//return null;
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
    	
    	servicesBookingService.save(servicesBooking);
    	servicesRatingResponse.setSuccessMessage("Service booking accepted.");
    	LOGGER.debug("bookServices saved");
    	servicesRatingResponse.setStatus(true);
    	
    	MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");
    	final Locale locale  = new Locale("en");
    	
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
        templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getBilling().getFirstName());
        templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, customer.getBilling().getLastName());
        templateTokens.put(EmailConstants.EMAIL_USER_NAME, customer.getEmailAddress());
        
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("eamil.customer.service.booking",locale));
		//email.setTo(customer.getEmailAddress());
		email.setTo("surendervarmac@gmail.com");
		email.setTemplateName(SERVICE_BOOKING_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		LOGGER.debug("Email sent to customer");
		sendEmailToServiceProvider(merchantStore,customer,services,customer);
		//sendEmailToAdmin(customer,services,merchantStore);
		LOGGER.debug("Email sent successful");
    	LOGGER.debug("Ended bookServices");
    	return servicesRatingResponse;
    }

  /*  private void sendEmailToAdmin(Customer customer, Services services, MerchantStore merchantStore) throws Exception {
    	User user = userService.getById(1l);
    	final Locale locale  = new Locale("en");
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
        templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, user.getAdminName());
        templateTokens.put(EmailConstants.EMAIL_PRODUCT_LABEL, messages.getMessage("email.vendor.add.request.product", locale));
        
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("eamil.customer.service.booking",locale));
		email.setTo("surendervarmac@gmail.com");
		email.setTemplateName(ADMIN_SERVICE_BOOKING_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		
		LOGGER.debug("Email sent to admin");
		
	}*/

	private void sendEmailToServiceProvider(MerchantStore merchantStore, Customer customer, Services services,Customer service) throws Exception{
		final Locale locale  = new Locale("en");
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
        templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, service.getBilling().getFirstName());
       
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("eamil.customer.service.booking",locale));
		email.setTo("surendervarmac@gmail.com");
		email.setTemplateName(SERVICEPROVIDER_SERVICE_BOOKING_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		
		LOGGER.debug("Email sent to Service provider");
		
	}

	@RequestMapping(value="/admin/getServicesBooking", method = RequestMethod.GET)
	@ResponseBody
	public PaginatedResponse getServicesBooking(@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		LOGGER.debug("Entered getServicesBooking");
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		try {
		List<ServicesBooking> servicesBookings = servicesBookingService.getAllServicesBookings();
		if(servicesBookings==null) {
			paginatedResponse.setErrorMsg("Services booking not found");
			return paginatedResponse;
		}
		List<ServicesBookingVO> servicesBookingVOList = new ArrayList<ServicesBookingVO>();
		for(ServicesBooking servicesBooking : servicesBookings) {
			ServicesBookingVO servicesBookingVO = new ServicesBookingVO();
			servicesBookingVO.setBookingDate(servicesBooking.getBookingDate());
			servicesBookingVO.setServicesBookingId(servicesBooking.getId());
			servicesBookingVO.setCustomerName(servicesBooking.getCustomer().getBilling().getFirstName());
			servicesBookingVO.setServiceType(servicesBooking.getServiceType().getServiceType());
			servicesBookingVOList.add(servicesBookingVO);
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
			paginatedResponse.setErrorMsg("Error while retrieving services bookings"+e.getMessage());
			LOGGER.error("Error while retrieving services bookings");
			return paginatedResponse;
		}
    	return paginatedResponse;
    	
    }
}
