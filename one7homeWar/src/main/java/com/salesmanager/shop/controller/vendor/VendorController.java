package com.salesmanager.shop.controller.vendor;

import java.math.BigDecimal;
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
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.vendor.VendorBookingService;
import com.salesmanager.core.business.vendor.VendorRatingService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.VendorBooking;
import com.salesmanager.core.model.customer.VendorRating;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.admin.controller.products.PaginatedReviewResponse;
import com.salesmanager.shop.admin.controller.products.ProductReviewVO;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@CrossOrigin
public class VendorController extends AbstractController {

	@Inject
	CustomerService customerService;

	@Inject
	EmailService emailService;

	@Inject
	private LabelUtils messages;

	@Inject
	MerchantStoreService merchantStoreService ;

	@Inject
	private EmailUtils emailUtils;
	
	@Inject
	VendorRatingService vendorRatingService;
	
	@Inject
	VendorBookingService vendorBookingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(VendorController.class);

	private final static String VENDOR_ADD_PRODUCTS_TPL = "email_template_vendor_add_products.ftl";
	private final static String SERVICE_BOOKING_TMPL = "email_template_service_booking.ftl";
	private final static String VENDOR_BOOKING_TMPL = "email_template_vendor_booking.ftl";
	private final static String ADMIN_VENDOR_BOOKING_CLOSE_TMPL = "email_template_vendor_booking_close.ftl";
    @RequestMapping(value="/updateVendorDescription", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public MachineryResponse updateVendorDescription(@RequestBody MachineryRequest machineryRequest) throws Exception {
		LOGGER.debug("Entered updateVendorDescription");
		MachineryResponse machineryResponse = new MachineryResponse();
		
		try{
	
	    		Customer customer = customerService.getById(machineryRequest.getVendorId());
	    		if(customer == null){
	    			LOGGER.error("customer not found while uploading portfolio for customer id=="+machineryRequest.getVendorId());
	    			machineryResponse.setErrorMessage("Failed while storing image");
	    			machineryResponse.setStatus(false);
	    			return machineryResponse;
	    		}
	    		customer.getVendorAttrs().setVendorShortDescription(machineryRequest.getVendorShortDescription());
	    		customer.getVendorAttrs().setVendorDescription(machineryRequest.getVendorDescription());
	    		customerService.update(customer);
	    		
	    		machineryResponse.setStatus(true);
	    		machineryResponse.setSuccessMessage("Vendor description updated successfully.");
	    		
    		}catch(Exception se){
    			LOGGER.error("Failed while updating Vendor description=="+se.getMessage());
    			machineryResponse.setErrorMessage("Failed while updating Vendor description=="+machineryRequest.getVendorId());
    			machineryResponse.setStatus(false);
    			return machineryResponse;
    		}
    	return machineryResponse;
	}
    
    @RequestMapping(value="/vendor/booking", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public VendorBookingResponse vendorBooking(@RequestBody VendorBookingRequest vendorBookingRequest) throws Exception {
    	LOGGER.debug("Entered vendorBooking");
    	VendorBookingResponse vendorBookingResponse = new VendorBookingResponse();
    	
    	Customer customer = customerService.getById(vendorBookingRequest.getCustomerId());
    	Customer vendor = customerService.getById(vendorBookingRequest.getVendorId());
    	
    	
    	if(customer == null || vendor == null){
    		vendorBookingResponse.setErrorMessage("Either customer/vendor invalid.");
    		vendorBookingResponse.setStatus(false);
        	LOGGER.debug("Ended vendorBooking with errors");
        	return vendorBookingResponse;
    	}
    	
    	VendorBooking vendorBooking = new VendorBooking();
    	vendorBooking.setCustomer(customer);
    	vendorBooking.setVendor(vendor);
    	vendorBooking.setBookingDate(new Date());
    	vendorBooking.setStatus("N");
    	vendorBooking.setAppointmentDate(vendorBookingRequest.getAppointmentDate());
    	vendorBooking.setDescription(vendorBookingRequest.getDescription());
    	vendorBooking.setAddress(vendorBookingRequest.getAddress());
    	
    	if(vendor.getCustomerType().equals("5"))
    	vendorBooking.setPortfolioId(vendorBookingRequest.getPortfolioId());
    	
    	vendorBookingService.save(vendorBooking);
    	vendorBookingResponse.setSuccessMessage("Vendor booking accepted.");
    	LOGGER.debug("bookServices saved");
    	vendorBookingResponse.setStatus(true);
    	
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
        vendorBooking = vendorBookingService.getById(vendorBooking.getId());
        templateTokens.put(EmailConstants.EMAIL_SERVICE_TYPE, Constants.customerTypes.get(vendor.getCustomerType()));
        templateTokens.put(EmailConstants.EMAIL_VENDOR_NAME, vendorBooking.getVendor().getVendorAttrs().getVendorName());
        templateTokens.put(EmailConstants.EMAIL_VENDOR_IMAGE, vendorBooking.getVendor().getVendorAttrs().getVendorAuthCert());
        templateTokens.put(EmailConstants.EMAIL_URL_LINK, messages.getMessage("email.url.link",locale));
		
        Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.customer.vendor.booking",locale));
		if(customer.getCustomerType().equals("0")) {
		email.setTo(customer.getEmailAddress());
		}else {
			email.setTo(vendorBooking.getVendor().getEmailAddress());
		}
		//email.setTo("sm19811130@gmail.com");
		email.setTemplateName(VENDOR_BOOKING_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(EmailConstants.ADMIN_EMAIL_SENDER, email);
		LOGGER.debug("Email sent to customer");
		//sendEmailToAdmin(customer,services,merchantStore);
		LOGGER.debug("Email sent successful");
    	LOGGER.debug("Ended bookServices");
    	return vendorBookingResponse;
    }
    @RequestMapping(value="/vendor/rating", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public VendorBookingResponse vendorRating(@RequestBody VendorBookingRequest vendorBookingRequest) throws Exception {
    	LOGGER.debug("Entered vendorRating");
    	VendorBookingResponse vendorBookingResponse = new VendorBookingResponse();
    	try {
    	Customer customer = customerService.getById(vendorBookingRequest.getCustomerId());
    	Customer vendor = customerService.getById(vendorBookingRequest.getVendorId());
    	
    	if(customer == null || vendor == null){
    		vendorBookingResponse.setErrorMessage("Either customer/vendor invalid.");
    		vendorBookingResponse.setStatus(false);
        	LOGGER.debug("Ended vendorRating with errors");
        	return vendorBookingResponse;
    	}
    	VendorRating vendorRating = new VendorRating();
    	vendorRating.setCreateDate(new Date());
    	vendorRating.setCustomer(customer);
    	vendorRating.setVendor(vendor);
    	
    	vendorRating.setRating(vendorBookingRequest.getRating());
    	vendorRating.setReviewTitle(vendorBookingRequest.getReviewTitle());
    	vendorRating.setReviewDescription(vendorBookingRequest.getReviewDescription());
    	vendorRatingService.save(vendorRating);
    	vendorBookingResponse.setSuccessMessage("Rating Saved successfully");
    	LOGGER.debug("vendorRating saved");
    	vendorBookingResponse.setStatus(true);
    	}catch(Exception e) {
    		e.printStackTrace();
    		LOGGER.error("Error while saving vendor rating");
    	}
    	LOGGER.debug("Ended vendorRating");
    	return vendorBookingResponse;
    }
    
	@RequestMapping(value="/vendor/{vendorId}/reviews", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
  	public PaginatedReviewResponse vendorReviews(@PathVariable Long vendorId, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		PaginatedReviewResponse paginatedReviewResponse = new PaginatedReviewResponse();
		LOGGER.debug("Entered productReviews method");
		List<ProductReviewVO> productReviewResponseList =new ArrayList<ProductReviewVO>();		
		
		try {

	    	Customer vendor = customerService.getById(vendorId);
	    	
	    	if(vendor == null){
	    		paginatedReviewResponse.setErrorMessage("Vendor is invalid.");
	    		paginatedReviewResponse.setStatus(false);
	        	LOGGER.debug("Ended vendorRating with errors");
	        	return paginatedReviewResponse;
	    	}
	    	
	    	List<VendorRating> vendorRatingList = vendorRatingService.getVendorReviews(vendor.getId());
	    	Double avgRating = new Double(0);
	    	int totalRating = 0;
	    	int totalReviews = vendorRatingList.size();
	    	double totalRate = 0;
	    	for(VendorRating vendorRating:vendorRatingList) {
	    		ProductReviewVO productReviewVO = new ProductReviewVO();
	    		productReviewVO.setReviewRating(Double.parseDouble(String.valueOf(vendorRating.getRating())));
				productReviewVO.setReviewDate(vendorRating.getCreateDate());
				productReviewVO.setName(vendorRating.getCustomer().getBilling().getFirstName());
				productReviewVO.setDescription(vendorRating.getReviewDescription());
				productReviewResponseList.add(productReviewVO);
	    		totalRating = totalRating + vendorRating.getRating();
	    	}
	    	
			totalRate = totalRating;
			avgRating = Double.valueOf(totalRate / totalReviews);
			avgRating = Double.valueOf(Math.round(avgRating.doubleValue() * 10D) / 10D);
			
			vendor.setAvgReview(new BigDecimal(avgRating));
			customerService.saveOrUpdate(vendor);
			
			paginatedReviewResponse.setAvgReview(avgRating);
			paginatedReviewResponse.setTotalratingCount(Long.parseLong(String.valueOf(totalReviews)));
			PaginationData paginaionData=createPaginaionData(page,size);
	    	calculatePaginaionData(paginaionData,size, productReviewResponseList.size());
	    	paginatedReviewResponse.setPaginationData(paginaionData);
			if(productReviewResponseList == null || productReviewResponseList.isEmpty() || productReviewResponseList.size() < paginaionData.getCountByPage()){
				paginatedReviewResponse.setReviewList(productReviewResponseList);
				return paginatedReviewResponse;
			}
	    	List<ProductReviewVO> paginatedResponses = productReviewResponseList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	    	paginatedReviewResponse.setReviewList(paginatedResponses);
	    	LOGGER.debug("Ended getVendorProducts");
		} catch (Exception e) {			
		    LOGGER.error("Error in retrieving Product Reviews",e.getMessage());
    		paginatedReviewResponse.setErrorMessage("Error in retrieving Product Reviews =="+e.getMessage());
    		paginatedReviewResponse.setStatus(false);
        	return paginatedReviewResponse;
		}	
		LOGGER.debug("Ended productReviews method");
		paginatedReviewResponse.setStatus(true);
		return paginatedReviewResponse;
	}
	@RequestMapping(value="/adminVendorBookingClose", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminVendorBookingResponse adminVendorBookingClose(@RequestBody AdminVendorBookingRequest adminVendorBookingRequest) throws Exception {
		
		LOGGER.debug("Entered adminVendorBookingClose");
		
		AdminVendorBookingResponse adminVendorBookingResponse = new AdminVendorBookingResponse();
		
		try {
		
		VendorBooking vendorBooking = vendorBookingService.getById(adminVendorBookingRequest.getBookingId());
		
		if(vendorBooking==null) {
			
			adminVendorBookingResponse.setSuccessMessage("Vendor booking not found");
			adminVendorBookingResponse.setStatus("true");
			return adminVendorBookingResponse;
			
		}
		
		vendorBooking.setComment(adminVendorBookingRequest.getComment());
		vendorBooking.setClosingDate(new Date());
		vendorBooking.setStatus(adminVendorBookingRequest.getStatus());
		
		vendorBookingService.update(vendorBooking);
		
		adminVendorBookingResponse.setSuccessMessage("Vendor booking closed successfully");
		adminVendorBookingResponse.setStatus("true");
		
		MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");
		final Locale locale  = new Locale("en");
		
	    Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
	    if(vendorBooking.getCustomer().getCustomerType().equals("0")) {
	    templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, vendorBooking.getCustomer().getBilling().getFirstName());
	    templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, vendorBooking.getCustomer().getBilling().getLastName());
	    } else {
	    	templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, vendorBooking.getCustomer().getVendorAttrs().getVendorName());
	    	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, "");
	    }
	    templateTokens.put(EmailConstants.EMAIL_USER_NAME, vendorBooking.getCustomer().getEmailAddress());
	
	    templateTokens.put(EmailConstants.EMAIL_SERVICE_TYPE, Constants.customerTypes.get(vendorBooking.getCustomer().getCustomerType()));
	    templateTokens.put(EmailConstants.EMAIL_VENDOR_NAME, vendorBooking.getVendor().getVendorAttrs().getVendorName());
	    templateTokens.put(EmailConstants.EMAIL_VENDOR_IMAGE, vendorBooking.getVendor().getVendorAttrs().getVendorAuthCert());
	    templateTokens.put(EmailConstants.ADMIN_VENDOR_BOOKING_COMMENT, vendorBooking.getComment());
	    templateTokens.put(EmailConstants.EMAIL_URL_LINK, messages.getMessage("email.url.link",locale));
		
	    Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.customer.vendor.booking",locale));
		
		email.setTo(vendorBooking.getCustomer().getEmailAddress());
		
		
		email.setTemplateName(ADMIN_VENDOR_BOOKING_CLOSE_TMPL);
		email.setTemplateTokens(templateTokens);
	
		emailService.sendHtmlEmail(EmailConstants.ADMIN_EMAIL_SENDER, email);
		LOGGER.debug("Email sent to customer");
		sendEmailToVendor(vendorBooking,merchantStore);
		LOGGER.debug("Email sent to vendor");
		
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("Error while closing vendor booking "+e.getMessage());
			adminVendorBookingResponse.setErrorMessage("Error while closing vendor booking");
			adminVendorBookingResponse.setStatus("false");
			return adminVendorBookingResponse;
		}
		LOGGER.debug("Ended adminVendorBookingClose");
		return adminVendorBookingResponse;
		
		
	}

	private void sendEmailToVendor(VendorBooking vendorBooking, MerchantStore merchantStore) throws Exception{

        final Locale locale  = new Locale("en");
    	
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
        if(vendorBooking.getCustomer().getCustomerType().equals("0")) {
        templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, vendorBooking.getCustomer().getBilling().getFirstName());
        templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, vendorBooking.getCustomer().getBilling().getLastName());
        } else {
        	templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, vendorBooking.getVendor().getVendorAttrs().getVendorName());
        	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, "");
        }
        templateTokens.put(EmailConstants.EMAIL_USER_NAME, vendorBooking.getCustomer().getEmailAddress());
        
        templateTokens.put(EmailConstants.EMAIL_SERVICE_TYPE, Constants.customerTypes.get(vendorBooking.getCustomer().getCustomerType()));
        templateTokens.put(EmailConstants.EMAIL_VENDOR_NAME, vendorBooking.getVendor().getVendorAttrs().getVendorName());
        templateTokens.put(EmailConstants.EMAIL_VENDOR_IMAGE, vendorBooking.getVendor().getVendorAttrs().getVendorAuthCert());
        templateTokens.put(EmailConstants.ADMIN_VENDOR_BOOKING_COMMENT, vendorBooking.getComment());
        templateTokens.put(EmailConstants.EMAIL_URL_LINK, messages.getMessage("email.url.link",locale));
		
        Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.customer.vendor.booking",locale));
		
		email.setTo(vendorBooking.getVendor().getEmailAddress());
		
		
		email.setTemplateName(ADMIN_VENDOR_BOOKING_CLOSE_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(EmailConstants.INFORMATION_EMAIL_SENDER, email);
	}
	
	// Search vendors by location
	@RequestMapping(value="/getVendorsByLocation", method=RequestMethod.POST)
	@ResponseBody
	public PaginatedResponse getVendorsByLocation(@RequestBody VendorSearchRequest vendorSearchRequest,
			@RequestParam(value="pageNumber", defaultValue = "1") int page , 
			@RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		
		LOGGER.debug("Entered getVendorsByLocation");
		
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		
		List<VendorSearchDetails> vendorSearchDetailsList = new ArrayList<VendorSearchDetails>();
		
		try {
			
		if(vendorSearchRequest.getSearchString() != null) {
			List<Customer> vendors = null;
			if(vendorSearchRequest.getSearchSubCategory().equals(null))
			vendors = customerService.getVendorsByLocation(vendorSearchRequest.getCustomerType(),vendorSearchRequest.getSearchString());
			else
				vendors = customerService.getVendorsByLocationAndSubCategory(vendorSearchRequest.getCustomerType(),vendorSearchRequest.getSearchString(),vendorSearchRequest.getSearchSubCategory());
			for(Customer vendor : vendors) {

				VendorSearchDetails vendorSearchDetails = new VendorSearchDetails();
				
				vendorSearchDetails.setId(vendor.getId().intValue());
				vendorSearchDetails.setCompanyName(vendor.getVendorAttrs().getVendorName());
				/*
				 * commenting following code as it is not needed as a part of response
				 */
				/*vendorSearchDetails.setHouseNumber(vendor.getVendorAttrs().getVendorOfficeAddress());
				vendorSearchDetails.setStreet(vendor.getBilling().getAddress());
				vendorSearchDetails.setArea(vendor.getArea());
				vendorSearchDetails.setCity(vendor.getBilling().getCity());
				vendorSearchDetails.setState(vendor.getBilling().getState());
				vendorSearchDetails.setPinCode(vendor.getBilling().getPostalCode());
				vendorSearchDetails.setContactNumber(vendor.getBilling().getTelephone());
				vendorSearchDetails.setCountry(vendor.getBilling().getCountry().getName());*/
				vendorSearchDetails.setImageUrl(vendor.getUserProfile());
				vendorSearchDetails.setAvgRating(vendor.getAvgReview());
				
				List<VendorRating> vendorRatings = vendorRatingService.getVendorReviews(vendor.getId());
				vendorSearchDetails.setTotalRating(vendorRatings.size());
				
				vendorSearchDetails.setDescription(vendor.getVendorAttrs().getVendorDescription());
				vendorSearchDetails.setShortDescription(vendor.getVendorAttrs().getVendorShortDescription());
				vendorSearchDetailsList.add(vendorSearchDetails);
			}
		}
		
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, vendorSearchDetailsList.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(vendorSearchDetailsList == null || vendorSearchDetailsList.isEmpty() || vendorSearchDetailsList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(vendorSearchDetailsList);
			return paginatedResponse;
		}
    	List<VendorSearchDetails> paginatedResponses = vendorSearchDetailsList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.debug("Error while searching vendors by location "+e.getMessage());
			paginatedResponse.setErrorMsg("Error while searching vendors by location");
		}
		
		LOGGER.debug("Ended getVendorsByLocation");
		return paginatedResponse;
		
	}
	// Search vendors(Archtects/wallpaper/machinery) by rating
	@RequestMapping(value="/getVendorPortFoliosByRating", method = RequestMethod.POST) 
	@ResponseBody
	public PaginatedResponse getVendorPortFoliosByRating(@RequestBody VendorFilterRequest vendorFilterRequest,
 			 @RequestParam(value="pageNumber", defaultValue = "1") int page , 
			 @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
				
		LOGGER.debug("Entered getVendorPortFoliosByRating");
		
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		
		List<FilteredVendors> filteredVendorsList = new ArrayList<FilteredVendors>();
		
		try {
			
			List<Customer> vendors =null;
			
		if(vendorFilterRequest.getRating() != null && (vendorFilterRequest.getRating().doubleValue() >=1 && vendorFilterRequest.getRating().doubleValue() <= 5)){
			
		if(vendorFilterRequest.getSearchSubCategory()==null)	
		   vendors = customerService.getWallPaperVendorsByRating(vendorFilterRequest.getRating(),vendorFilterRequest.getVendorType());
		else
			vendors = customerService.getVendorsBasedOnSubCategoryByRating(vendorFilterRequest.getRating(),vendorFilterRequest.getVendorType(),vendorFilterRequest.getSearchSubCategory());
		
		for(Customer vendor : vendors) {
			
			FilteredVendors filteredVendors = new FilteredVendors();
			
			filteredVendors.setId(vendor.getId());
			filteredVendors.setCompanyName(vendor.getVendorAttrs().getVendorName());
			/*
			 * commenting following code as it is not needed as a part of response
			 */
			/*filteredVendors.setHouseNumber(vendor.getVendorAttrs().getVendorOfficeAddress());
			filteredVendors.setStreet(vendor.getBilling().getAddress());
			filteredVendors.setArea(vendor.getArea());
			filteredVendors.setCity(vendor.getBilling().getCity());
			filteredVendors.setState(vendor.getBilling().getState());
			filteredVendors.setPinCode(vendor.getBilling().getPostalCode());
			filteredVendors.setCountry(vendor.getBilling().getCountry().getName());
			filteredVendors.setContactNumber(vendor.getBilling().getTelephone());*/
			filteredVendors.setImageUrl(vendor.getUserProfile());
			filteredVendors.setAvgRating(vendor.getAvgReview());
			
			List<VendorRating> vendorRatings = vendorRatingService.getVendorReviews(vendor.getId());
			filteredVendors.setTotalRating(vendorRatings.size());
			
			filteredVendors.setDescription(vendor.getVendorAttrs().getVendorDescription());
			filteredVendors.setShortDescription(vendor.getVendorAttrs().getVendorShortDescription());
			filteredVendorsList.add(filteredVendors);
		}
	}
		
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, filteredVendorsList.size());
    	paginatedResponse.setPaginationData(paginaionData);
    	
		if(filteredVendorsList == null || filteredVendorsList.isEmpty() || filteredVendorsList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(filteredVendorsList);
		
			return paginatedResponse;
		}
		
    	List<FilteredVendors> paginatedResponses = filteredVendorsList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while retrieving VendorPortFoliosByRating "+e.getMessage());
			paginatedResponse.setErrorMsg("Error while retrieving VendorPortFoliosByRating");
		}
		
		LOGGER.debug("Ended getVendorPortFoliosByRating");
		return paginatedResponse;
		
	}
}
