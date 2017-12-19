package com.salesmanager.shop.controller.vendor;

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

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.services.ArchitectsPortfolioService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.shop.admin.controller.products.ProductImageRequest;
import com.salesmanager.shop.admin.controller.products.ProductImageResponse;
import com.salesmanager.shop.admin.controller.services.ServicesRatingRequest;
import com.salesmanager.shop.admin.controller.services.ServicesRatingResponse;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.fileupload.services.StorageException;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.controller.customer.VendorResponse;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;
import com.salesmanager.shop.fileupload.services.StorageService;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@Controller
@CrossOrigin
public class ArchitectsController extends AbstractController {

	@Inject
	ArchitectsPortfolioService architectsPortfolioService;
	
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
    private StorageService storageService;


	private static final Logger LOGGER = LoggerFactory.getLogger(ArchitectsController.class);

	private final static String VENDOR_ADD_PRODUCTS_TPL = "email_template_vendor_add_products.ftl";

	@RequestMapping(value="/addArchitectsPortfolio", method = RequestMethod.POST) 
	@ResponseBody
	public ArchitectsResponse addArchitectsPortfolio(@RequestPart("architectsRequest") String architectsRequestStr,
			@RequestPart("file") MultipartFile uploadedImage) throws Exception {
		LOGGER.debug("Entered addArchitectsPortfolio");
		ArchitectsRequest architectsRequest = new ObjectMapper().readValue(architectsRequestStr, ArchitectsRequest.class);
		ArchitectsResponse architectsResponse = new ArchitectsResponse();
		ArchitectsPortfolio architectsPortfolio = new ArchitectsPortfolio();
		
    	String fileName = "";
    	if(uploadedImage.getSize() != 0) {
    		try{
    			fileName = storageService.store(uploadedImage,"architect");
    			LOGGER.debug("architect portfolio fileName "+fileName);
    		
	    		Customer customer = customerService.getById(architectsRequest.getVendorId());
	    		if(customer == null){
	    			LOGGER.error("customer not found while uploading portfolio for customer id=="+architectsRequest.getVendorId());
	    			architectsResponse.setErrorMessage("Failed while storing image");
	    			architectsResponse.setStatus(false);
	    			return architectsResponse;
	    		}
	    		architectsPortfolio.setCreateDate(new Date());
	    		architectsPortfolio.setImageURL(fileName);
	    		architectsPortfolio.setPortfolioName(architectsRequest.getPortfolioName());
	    		architectsPortfolio.setCustomer(customer);
	    		architectsPortfolioService.save(architectsPortfolio);
	    		
	    		architectsResponse.setStatus(true);
	    		architectsResponse.setSuccessMessage("New portfolio details uploaded successfully.");
	    		
    		}catch(StorageException se){
    			LOGGER.error("Failed while uploading portfolio for architect=="+se.getMessage());
    			architectsResponse.setErrorMessage("Failed while uploading portfolio for architect=="+architectsRequest.getPortfolioName());
    			architectsResponse.setStatus(false);
    			return architectsResponse;
    		}
    	}
    	return architectsResponse;
	}
    @RequestMapping(value="/getArchitectsPortfolio", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public ArchitectsResponse getArchitectsPortfolio(@RequestBody ArchitectsRequest architectsRequest) throws Exception {
		ArchitectsResponse architectsResponse = new ArchitectsResponse();
		ArchitectsPortfolio architectsPortfolio = new ArchitectsPortfolio();
		List<VendorPortfolioData> vendorPortfolioList = new ArrayList<VendorPortfolioData>();
		try {
			
			List<ArchitectsPortfolio> portfolioList = architectsPortfolioService.findByVendorId(architectsRequest.getVendorId());
	    	for(ArchitectsPortfolio portfolio:portfolioList){
	    		VendorPortfolioData vendorPortfolioData = new VendorPortfolioData();
	    		vendorPortfolioData.setPortfolioId(portfolio.getId());
	    		vendorPortfolioData.setPortfolioName(portfolio.getPortfolioName());
	    		vendorPortfolioData.setVendorId(architectsRequest.getVendorId());
	    		vendorPortfolioList.add(vendorPortfolioData);
	    	}
			architectsResponse.setStatus(true);
			architectsResponse.setVendorPortfolioList(vendorPortfolioList);
		}catch(StorageException se){
			LOGGER.error("Failed while fetching portfolio list for architect=="+se.getMessage());
			architectsResponse.setErrorMessage("Failed while fetching portfolio list for architect=="+architectsRequest.getVendorId());
			architectsResponse.setStatus(false);
			return architectsResponse;
		}
		return architectsResponse;
    }
	
}
