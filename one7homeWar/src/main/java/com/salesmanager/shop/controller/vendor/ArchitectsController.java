package com.salesmanager.shop.controller.vendor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
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
import com.salesmanager.core.model.customer.MachineryPortfolio;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.shop.admin.controller.CustomerTestimonialVO;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.admin.controller.products.ProductImageRequest;
import com.salesmanager.shop.admin.controller.products.ProductImageResponse;
import com.salesmanager.shop.admin.controller.services.ServicesRatingRequest;
import com.salesmanager.shop.admin.controller.services.ServicesRatingResponse;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.fileupload.services.StorageException;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.controller.customer.VendorResponse;
import com.salesmanager.shop.store.model.paging.PaginationData;
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
	    		architectsResponse.setSuccessMessage("Portfolio added successfully. Awaiting for Admin approval.");
	    		
    		}catch(Exception se){
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
		List<VendorPortfolioData> vendorPortfolioList = new ArrayList<VendorPortfolioData>();
		try {
			
    		Customer customer = customerService.getById(architectsRequest.getVendorId());
			List<ArchitectsPortfolio> portfolioList = 
					architectsPortfolioService.getApprovedVendor(architectsRequest.getVendorId());
	    	
			for(ArchitectsPortfolio portfolio:portfolioList) {

	    		VendorPortfolioData vendorPortfolioData = new VendorPortfolioData();
	    		
	    		if (portfolio.getImageURL() != null) {
		    		
		    		// Check whether there are any PDF/DOC portfolios uploaded by Architect
	    			// If exists, add portfolio name and document under attribute
	    			
	    			if((FilenameUtils.isExtension(portfolio.getImageURL(),"pdf")) || 
	    					(FilenameUtils.isExtension(portfolio.getImageURL(),"doc"))) {
	    				
	    				architectsResponse.setVendorPortfolioName(portfolio.getPortfolioName());
	    				architectsResponse.setVendorPortfolioDocument(portfolio.getImageURL());
	    				
	    			} else {
	    				
	    				// If doesn't exists, add images to the vendor portfolio list

	    				vendorPortfolioData.setImageURL(portfolio.getImageURL());
	    	    		vendorPortfolioData.setPortfolioId(portfolio.getId());
	    	    		vendorPortfolioData.setPortfolioName(portfolio.getPortfolioName());
	    	    		vendorPortfolioData.setStatus(portfolio.getStatus());
	    	    		vendorPortfolioData.setVendorId(architectsRequest.getVendorId());
	    	    		vendorPortfolioList.add(vendorPortfolioData);

	    			}
	    			
	    		}
	    		
	    	}
			architectsResponse.setStatus(true);
			architectsResponse.setVendorPortfolioList(vendorPortfolioList);
	    	if(customer != null) {
	    		architectsResponse.setVendorName(customer.getVendorAttrs().getVendorName());
	    		architectsResponse.setVendorImageURL(customer.getUserProfile());
	    		architectsResponse.setVendorShortDescription(customer.getVendorAttrs().getVendorShortDescription());
	    		architectsResponse.setVendorDescription(customer.getVendorAttrs().getVendorDescription());
	    		
	    		// Set common return attributes to NULL if any of PDF/DOC portfolios are not uploaded by Architect
	    		
	    		if (architectsResponse.getVendorPortfolioDocument() == null) {
	    			
	    			architectsResponse.setVendorPortfolioName(null);
    				architectsResponse.setVendorPortfolioDocument(null);
    				
	    		}
	    		
	    	}
	    	
		}catch(Exception se){
			System.out.println("Failed while fetching portfolio list for architect=="+se.getMessage());
			LOGGER.error("Failed while fetching portfolio list for architect=="+se.getMessage());
			architectsResponse.setErrorMessage("Failed while fetching portfolio list for architect=="+architectsRequest.getVendorId());
			architectsResponse.setStatus(false);
			return architectsResponse;
		}
		return architectsResponse;
    }
    
    @RequestMapping(value="/deleteArchitectsPortfolio", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public ArchitectsResponse deleteArchitectsPortfolio(@RequestBody ArchitectsRequest architectsRequest) throws Exception {
    	ArchitectsResponse architectsResponse = new ArchitectsResponse();
		
		try {
			
			ArchitectsPortfolio architectsPortfolio = architectsPortfolioService.getById(architectsRequest.getPortfolioId());
			if(architectsPortfolio == null) {
				architectsResponse.setErrorMessage("No machinery exists with portfolio id=="+architectsRequest.getPortfolioId());
				architectsResponse.setStatus(false);
				return architectsResponse;
			} 
			architectsPortfolioService.delete(architectsPortfolio);
			architectsResponse.setStatus(true);
			architectsResponse.setSuccessMessage("Portfolio "+architectsPortfolio.getPortfolioName()+" deleted successfully.");
			try {
				//deleting image from the location
				File imageFile = new File(architectsPortfolio.getImageURL());
				if(imageFile.exists()){
					imageFile.delete();
				}

			} catch(Exception e){
				//ignore the error while deletion fails. which is not going to impact the flow.
			}
		}catch(Exception se){
			LOGGER.error("Failed while deleting portfolio for machinery=="+se.getMessage());
			architectsResponse.setErrorMessage("Failed while deleting portfolio for machinery=="+architectsRequest.getVendorId());
			architectsResponse.setStatus(false);
			return architectsResponse;
		}
		return architectsResponse;
    }
    
	@RequestMapping(value="/updateArchitectsPortfolio", method = RequestMethod.POST) 
	@ResponseBody
	public ArchitectsResponse updateArchitectsPortfolio(@RequestPart("architectsRequest") String architectsRequestStr,
			@RequestPart("file") MultipartFile uploadedImage) throws Exception {
		LOGGER.debug("Entered updateArchitectsPortfolio");
		ArchitectsRequest architectsRequest = new ObjectMapper().readValue(architectsRequestStr, ArchitectsRequest.class);
		ArchitectsResponse architectsResponse = new ArchitectsResponse();
		
    	String fileName = "";
    		try{
    			ArchitectsPortfolio architectsPortfolio = architectsPortfolioService.getById(architectsRequest.getPortfolioId());
    			String existingfile = architectsPortfolio.getImageURL();
    			if(uploadedImage.getSize() != 0) {
	    			fileName = storageService.store(uploadedImage,"architect");
	    			LOGGER.debug("architect portfolio fileName "+fileName);
    	    	}
	    		Customer customer = customerService.getById(architectsRequest.getVendorId());
	    		if(customer == null){
	    			LOGGER.error("customer not found while uploading portfolio for customer id=="+architectsRequest.getVendorId());
	    			architectsResponse.setErrorMessage("Failed while storing image");
	    			architectsResponse.setStatus(false);
	    			return architectsResponse;
	    		}
	    		if(fileName != null) {
		    		architectsPortfolio.setImageURL(fileName);
	    		}
	    		architectsPortfolio.setPortfolioName(architectsRequest.getPortfolioName());
	    		
	    		// Since there is an update of Image or Description, setting the status to "N" for Admin approval
	    		architectsPortfolio.setStatus("N");
	    		
	    		architectsPortfolioService.update(architectsPortfolio);
	    		
	    		architectsResponse.setStatus(true);
	    		architectsResponse.setSuccessMessage("Portfolio updated successfully. Awaiting for Admin approval.");
	    		
	    		if(existingfile != null) {
					try {
						//deleting image from the location
						File imageFile = new File(existingfile);
						if(imageFile.exists()){
							imageFile.delete();
						}

					} catch(Exception e){
						//ignore the error while deletion fails. which is not going to impact the flow.
					}
	    		}
    		}catch(Exception se){
    			LOGGER.error("Failed while uploading portfolio for architect=="+se.getMessage());
    			architectsResponse.setErrorMessage("Failed while uploading portfolio for architect=="+architectsRequest.getPortfolioName());
    			architectsResponse.setStatus(false);
    			return architectsResponse;
    		}
    	return architectsResponse;
	}
	    // Admin Architects Portfolios
	    @RequestMapping(value="/admin/getAdminArchitectsPortfolio", method=RequestMethod.POST)
	  	@ResponseBody
	  	public PaginatedResponse getAdminArchitectsPortfolio(@RequestBody AdminArchitectsRequest adminArchitectsRequest, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
	    	LOGGER.debug("Entered getAdminArchitectsPortfolio");
	    	PaginatedResponse paginatedResponse = new PaginatedResponse();
			List<ArchitectsPortfolioVO> architectsPortfolioList = new ArrayList<ArchitectsPortfolioVO>();
			List<ArchitectsPortfolio> architectsPortfolios = null;
			
			// By default, retrieve all the vendor related portfolio details
			
			if(adminArchitectsRequest.getStatus().equals("ALL")) {
				
				architectsPortfolios = architectsPortfolioService.getAllPortfolios();
				
				for(ArchitectsPortfolio architectsPortfolio : architectsPortfolios) {
				
					ArchitectsPortfolioVO architectsPortfolioVO = new ArchitectsPortfolioVO();
					architectsPortfolioVO.setArchitectPortfolioId(architectsPortfolio.getId());
					architectsPortfolioVO.setCreatedate(architectsPortfolio.getCreateDate());
					architectsPortfolioVO.setImageURL(architectsPortfolio.getImageURL());
					architectsPortfolioVO.setPortfolioName(architectsPortfolio.getPortfolioName());
					architectsPortfolioVO.setVendorName(architectsPortfolio.getCustomer().getVendorAttrs().getVendorName());
					architectsPortfolioVO.setVendorDescription(architectsPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
					architectsPortfolioVO.setVendorShortDescription(architectsPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
					architectsPortfolioVO.setVendorImageURL(architectsPortfolio.getCustomer().getUserProfile());
					architectsPortfolioVO.setStatus(architectsPortfolio.getStatus());
					architectsPortfolioList.add(architectsPortfolioVO);
					
				}
				
			} else {
				
				architectsPortfolios = architectsPortfolioService.getPortfoliosBasedOnStatus(adminArchitectsRequest.getStatus());
				
				for(ArchitectsPortfolio architectsPortfolio : architectsPortfolios) {
				
					ArchitectsPortfolioVO architectsPortfolioVO = new ArchitectsPortfolioVO();
					architectsPortfolioVO.setArchitectPortfolioId(architectsPortfolio.getId());
					architectsPortfolioVO.setCreatedate(architectsPortfolio.getCreateDate());
					architectsPortfolioVO.setImageURL(architectsPortfolio.getImageURL());
					architectsPortfolioVO.setPortfolioName(architectsPortfolio.getPortfolioName());
					architectsPortfolioVO.setVendorName(architectsPortfolio.getCustomer().getVendorAttrs().getVendorName());
					architectsPortfolioVO.setVendorDescription(architectsPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
					architectsPortfolioVO.setVendorShortDescription(architectsPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
					architectsPortfolioVO.setVendorImageURL(architectsPortfolio.getCustomer().getVendorAttrs().getVendorAuthCert());
					architectsPortfolioVO.setStatus(architectsPortfolio.getStatus());
					architectsPortfolioList.add(architectsPortfolioVO);
					
				}
				
			}
			
			PaginationData paginaionData=createPaginaionData(page,size);
	    	calculatePaginaionData(paginaionData,size, architectsPortfolioList.size());
	    	paginatedResponse.setPaginationData(paginaionData);
	    	
			if(architectsPortfolioList == null || architectsPortfolioList.isEmpty() || architectsPortfolioList.size() < paginaionData.getCountByPage()){
				paginatedResponse.setResponseData(architectsPortfolioList);
				LOGGER.debug("Ended getAdminArchitectsPortfolio");
				return paginatedResponse;
			}
			
	    	List<ArchitectsPortfolioVO> paginatedResponses = architectsPortfolioList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	    	paginatedResponse.setResponseData(paginatedResponses);
			
			return paginatedResponse;
	    }
	    
	    // Manage admin architect portfolio
	    @RequestMapping(value="/admin/manageAdminArchitectPortfolios", method=RequestMethod.POST)
	  	@ResponseBody
	  	public AdminArchitectsPortfolioResponse manageAdminArchitectPortfolios
	  		(@RequestBody AdminArchitectsRequest adminArchitectsRequest, 
	  		@RequestParam(value="pageNumber", defaultValue = "1") int page, 
	  		@RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
	    	LOGGER.debug("Entered manageAdminArchitectPortfolios");
	    	AdminArchitectsPortfolioResponse adminArchitectsPortfolioResponse = new AdminArchitectsPortfolioResponse();
	    	try {
	    	ArchitectsPortfolio architectsPortfolio = 
	    				architectsPortfolioService.getById(adminArchitectsRequest.getPortfolioId());
	    	
	    	if (architectsPortfolio == null) {
	    		
	    		adminArchitectsPortfolioResponse.setErrorMessgae
	    			("Error finding record with portfolio = " + adminArchitectsRequest.getPortfolioId());
	    		adminArchitectsPortfolioResponse.setStatus(false);
    			return adminArchitectsPortfolioResponse;
	    		
	    	}
	    	
	    	if (adminArchitectsRequest.getStatus().equals("Y")) {
	    		
	    		// If the request is to Approve, then update table entry with "Y"
	    		architectsPortfolio.setStatus(adminArchitectsRequest.getStatus());
	    		architectsPortfolioService.update(architectsPortfolio);
	    		
	    		adminArchitectsPortfolioResponse.setSuccessMessage
	    			("Approval of portfolio " + adminArchitectsRequest.getPortfolioId() + " is successful");
	    	    LOGGER.debug("Approval of portfolio is successful");
	    	    
	    	} else {
	    		
	    		// If the request is to Decline, then remove the corresponding entry from table
	    		architectsPortfolioService.delete(architectsPortfolio);
	    		
	    		adminArchitectsPortfolioResponse.setSuccessMessage
    				("Portfolio " + adminArchitectsRequest.getPortfolioId() + " is declined and the entry is removed from the table");
	    		LOGGER.debug("Approval of portfolio is declined");
	    		
	    	}
	    	
	    	adminArchitectsPortfolioResponse.setStatus(true);
	    	
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    		LOGGER.error("Error while updating portfolio status:" + e.getMessage());
	    		
	    	}
	    	LOGGER.debug("Ended manageAdminArchitectPortfolios");
	    	return adminArchitectsPortfolioResponse;
	    	
	    }
	    @RequestMapping(value="/getUserArchitectsPortfolio", method=RequestMethod.POST)
	  	@ResponseBody
	  	public ArchitectPaginatedResponse getUserArchitectsPortfolio(@RequestBody UserArchitectsRequest userArchitectsRequest, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
	    	LOGGER.debug("Entered getAdminArchitectsPortfolio");
	    	ArchitectPaginatedResponse architectPaginatedResponse = new ArchitectPaginatedResponse();
			List<ArchitectsPortfolioVO> architectsPortfolioList = new ArrayList<ArchitectsPortfolioVO>();
			List<ArchitectsPortfolio> architectsPortfolios = null;
			
			// By default, retrieve all the vendor related portfolio details
			
			if(userArchitectsRequest.getStatus().equals("ALL")) {
				
				architectsPortfolios = architectsPortfolioService.getPortFoliosByVendorId(userArchitectsRequest.getVendorId());
				if(architectsPortfolios.isEmpty()){
					Customer vendor = customerService.getById(userArchitectsRequest.getVendorId());
					
					architectPaginatedResponse.setVendorName(vendor.getVendorAttrs().getVendorName());
					architectPaginatedResponse.setVendorDescription(vendor.getVendorAttrs().getVendorDescription());
					architectPaginatedResponse.setVendorShortDescription(vendor.getVendorAttrs().getVendorShortDescription());
					architectPaginatedResponse.setVendorImageURL(vendor.getUserProfile());
					return architectPaginatedResponse;
				}
				for(ArchitectsPortfolio architectsPortfolio : architectsPortfolios) {
				
					ArchitectsPortfolioVO architectsPortfolioVO = new ArchitectsPortfolioVO();
					architectsPortfolioVO.setArchitectPortfolioId(architectsPortfolio.getId());
					architectsPortfolioVO.setCreatedate(architectsPortfolio.getCreateDate());
					if((!FilenameUtils.isExtension(architectsPortfolio.getImageURL(),"pdf")) || 
	    					(!FilenameUtils.isExtension(architectsPortfolio.getImageURL(),"doc"))) {
	    				
						architectsPortfolioVO.setImageURL(architectsPortfolio.getImageURL());
	    				
	    			}
					//architectsPortfolioVO.setImageURL(architectsPortfolio.getImageURL());
					architectsPortfolioVO.setPortfolioName(architectsPortfolio.getPortfolioName());
					architectPaginatedResponse.setVendorName(architectsPortfolio.getCustomer().getVendorAttrs().getVendorName());
					architectPaginatedResponse.setVendorDescription(architectsPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
					architectPaginatedResponse.setVendorShortDescription(architectsPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
					architectPaginatedResponse.setVendorImageURL(architectsPortfolio.getCustomer().getUserProfile());
					architectsPortfolioVO.setStatus(architectsPortfolio.getStatus());
					architectsPortfolioList.add(architectsPortfolioVO);
					
				}
				
			} else {
				
				architectsPortfolios = architectsPortfolioService.getPortfoliosBasedOnStatusAndVendorId(userArchitectsRequest.getStatus(),userArchitectsRequest.getVendorId());
				if(architectsPortfolios.isEmpty()){
					Customer vendor = customerService.getById(userArchitectsRequest.getVendorId());
					
					architectPaginatedResponse.setVendorName(vendor.getVendorAttrs().getVendorName());
					architectPaginatedResponse.setVendorDescription(vendor.getVendorAttrs().getVendorDescription());
					architectPaginatedResponse.setVendorShortDescription(vendor.getVendorAttrs().getVendorShortDescription());
					architectPaginatedResponse.setVendorImageURL(vendor.getUserProfile());
					return architectPaginatedResponse;
				}
				for(ArchitectsPortfolio architectsPortfolio : architectsPortfolios) {
				
					ArchitectsPortfolioVO architectsPortfolioVO = new ArchitectsPortfolioVO();
					architectsPortfolioVO.setArchitectPortfolioId(architectsPortfolio.getId());
					architectsPortfolioVO.setCreatedate(architectsPortfolio.getCreateDate());
					if((!FilenameUtils.isExtension(architectsPortfolio.getImageURL(),"pdf")) || 
	    					(!FilenameUtils.isExtension(architectsPortfolio.getImageURL(),"doc"))) {
	    				
						architectsPortfolioVO.setImageURL(architectsPortfolio.getImageURL());
	    				
	    			}
					//architectsPortfolioVO.setImageURL(architectsPortfolio.getImageURL());
					architectsPortfolioVO.setPortfolioName(architectsPortfolio.getPortfolioName());
					architectPaginatedResponse.setVendorName(architectsPortfolio.getCustomer().getVendorAttrs().getVendorName());
					architectPaginatedResponse.setVendorDescription(architectsPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
					architectPaginatedResponse.setVendorShortDescription(architectsPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
					architectPaginatedResponse.setVendorImageURL(architectsPortfolio.getCustomer().getUserProfile());
					architectsPortfolioVO.setStatus(architectsPortfolio.getStatus());
					architectsPortfolioList.add(architectsPortfolioVO);
					
				}
				
			}
			
			PaginationData paginaionData=createPaginaionData(page,size);
	    	calculatePaginaionData(paginaionData,size, architectsPortfolioList.size());
	    	architectPaginatedResponse.setPaginationData(paginaionData);
	    	
			if(architectsPortfolioList == null || architectsPortfolioList.isEmpty() || architectsPortfolioList.size() < paginaionData.getCountByPage()){
				architectPaginatedResponse.setResponseData(architectsPortfolioList);
				LOGGER.debug("Ended getAdminArchitectsPortfolio");
				return architectPaginatedResponse;
			}
			
	    	List<ArchitectsPortfolioVO> paginatedResponses = architectsPortfolioList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	    	architectPaginatedResponse.setResponseData(paginatedResponses);
			
			return architectPaginatedResponse;
	    }
}
