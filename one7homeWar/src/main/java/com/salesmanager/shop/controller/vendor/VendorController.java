package com.salesmanager.shop.controller.vendor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.services.MachineryPortfolioService;
import com.salesmanager.core.business.services.services.ServicesBookingService;
import com.salesmanager.core.business.services.services.ServicesRatingService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.vendor.VendorBookingService;
import com.salesmanager.core.business.vendor.VendorRatingService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.catalog.product.review.ProductReview;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.MachineryPortfolio;
import com.salesmanager.core.model.customer.ServicesBooking;
import com.salesmanager.core.model.customer.ServicesRating;
import com.salesmanager.core.model.customer.VendorBooking;
import com.salesmanager.core.model.customer.VendorRating;
import com.salesmanager.core.model.customer.WallPaperPortfolio;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.core.model.services.Services;
import com.salesmanager.shop.admin.controller.products.PaginatedReviewResponse;
import com.salesmanager.shop.admin.controller.products.ProductDetails;
import com.salesmanager.shop.admin.controller.products.ProductImageRequest;
import com.salesmanager.shop.admin.controller.products.ProductImageResponse;
import com.salesmanager.shop.admin.controller.products.ProductReviewVO;
import com.salesmanager.shop.admin.controller.services.ServicesBookingRequest;
import com.salesmanager.shop.admin.controller.services.ServicesRatingRequest;
import com.salesmanager.shop.admin.controller.services.ServicesRatingResponse;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.fileupload.services.StorageException;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.controller.customer.VendorRequest;
import com.salesmanager.shop.store.controller.customer.VendorResponse;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;
import com.salesmanager.shop.fileupload.services.StorageService;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
        templateTokens.put(EmailConstants.EMAIL_SERVICE_TYPE, Constants.customerTypes.get(customer.getCustomerType()));
        templateTokens.put(EmailConstants.EMAIL_VENDOR_NAME, vendorBooking.getVendor().getVendorAttrs().getVendorName());
        templateTokens.put(EmailConstants.EMAIL_VENDOR_IMAGE, vendorBooking.getVendor().getVendorAttrs().getVendorAuthCert());
		
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

		emailService.sendHtmlEmail(merchantStore, email);
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
	
}
