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
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.MachineryPortfolio;
import com.salesmanager.core.model.customer.WallPaperPortfolio;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.admin.controller.products.ProductImageRequest;
import com.salesmanager.shop.admin.controller.products.ProductImageResponse;
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
public class MachineryController extends AbstractController {

	@Inject
	MachineryPortfolioService machineryPortfolioService;
	
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


	private static final Logger LOGGER = LoggerFactory.getLogger(MachineryController.class);

	private final static String VENDOR_ADD_PRODUCTS_TPL = "email_template_vendor_add_products.ftl";

	@RequestMapping(value="/addMachineryPortfolio", method = RequestMethod.POST) 
	@ResponseBody
	public MachineryResponse addMachineryPortfolio(@RequestPart("machineryRequest") String machineryRequestStr,
			@RequestPart("file") MultipartFile uploadedImage) throws Exception {
		LOGGER.debug("Entered addMachineryPortfolio");
		MachineryRequest machineryRequest = new ObjectMapper().readValue(machineryRequestStr, MachineryRequest.class);
		MachineryResponse machineryResponse = new MachineryResponse();
		MachineryPortfolio machineryPortfolio = new MachineryPortfolio();
		
    	String fileName = "";
    	if(uploadedImage.getSize() != 0) {
    		try{
    			fileName = storageService.store(uploadedImage,"machinery");
    			LOGGER.debug("architect portfolio fileName "+fileName);
    		
	    		Customer customer = customerService.getById(machineryRequest.getVendorId());
	    		if(customer == null){
	    			LOGGER.error("customer not found while uploading portfolio for customer id=="+machineryRequest.getVendorId());
	    			machineryResponse.setErrorMessage("Failed while storing image");
	    			machineryResponse.setStatus(false);
	    			return machineryResponse;
	    		}
	    		machineryPortfolio.setCreateDate(new Date());
	    		machineryPortfolio.setImageURL(fileName);
	    		machineryPortfolio.setPortfolioName(machineryRequest.getPortfolioName());
	    		machineryPortfolio.setCustomer(customer);
	    		machineryPortfolio.setEquipmentName(machineryRequest.getEquipmentName());
	    		machineryPortfolio.setEquipmentPrice(machineryRequest.getEquipmentPrice());
	    		machineryPortfolio.setHiringType(machineryRequest.getHiringType());
	    		machineryPortfolioService.save(machineryPortfolio);
	    		
	    		machineryResponse.setStatus(true);
	    		machineryResponse.setSuccessMessage("New portfolio details uploaded successfully. Awaiting from admin approval");
	    		
    		}catch(Exception se){
    			LOGGER.error("Failed while uploading portfolio for machinery=="+se.getMessage());
    			machineryResponse.setErrorMessage("Failed while uploading portfolio for machinery=="+machineryRequest.getPortfolioName());
    			machineryResponse.setStatus(false);
    			return machineryResponse;
    		}
    	}
    	return machineryResponse;
	}

   @RequestMapping(value="/getMachineryPortfolio", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public MachineryResponse getMachineryPortfolio(@RequestBody MachineryRequest machineryRequest) throws Exception {
		MachineryResponse machineryResponse = new MachineryResponse();
		
		List<VendorPortfolioData> vendorPortfolioList = new ArrayList<VendorPortfolioData>();
		try {
    		Customer customer = customerService.getById(machineryRequest.getVendorId());
			List<MachineryPortfolio> portfolioList = machineryPortfolioService.findByVendorId(machineryRequest.getVendorId());
	    	for(MachineryPortfolio portfolio:portfolioList){
	    		VendorPortfolioData vendorPortfolioData = new VendorPortfolioData();
	    		vendorPortfolioData.setPortfolioId(portfolio.getId());
	    		vendorPortfolioData.setPortfolioName(portfolio.getPortfolioName());
	    		vendorPortfolioData.setVendorId(machineryRequest.getVendorId());
	    		vendorPortfolioData.setImageURL(portfolio.getImageURL());
	    		vendorPortfolioData.setStatus(portfolio.getStatus());
	    		vendorPortfolioData.setEquipmentName(portfolio.getEquipmentName());
	    		vendorPortfolioData.setEquipmentPrice(portfolio.getEquipmentPrice());
	    		vendorPortfolioData.setHiringType(portfolio.getHiringType());
	    		vendorPortfolioList.add(vendorPortfolioData);
	    	}
	    	machineryResponse.setStatus(true);
	    	machineryResponse.setVendorPortfolioList(vendorPortfolioList);
	    	if(customer != null) {
	    		machineryResponse.setVendorName(customer.getVendorAttrs().getVendorName());
	    		machineryResponse.setVendorImageURL(customer.getUserProfile());
	    		machineryResponse.setVendorShortDescription(customer.getVendorAttrs().getVendorShortDescription());
	    		machineryResponse.setVendorDescription(customer.getVendorAttrs().getVendorDescription());
	    	}
		}catch(Exception se){
			LOGGER.error("Failed while fetching portfolio list for machinery=="+se.getMessage());
			machineryResponse.setErrorMessage("Failed while fetching portfolio list for machinery=="+machineryRequest.getVendorId());
			machineryResponse.setStatus(false);
			return machineryResponse;
		}
		return machineryResponse;
    }
    
    @RequestMapping(value="/deleteMachineryPortfolio", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public MachineryResponse deleteMachineryPortfolio(@RequestBody MachineryRequest machineryRequest) throws Exception {
    	MachineryResponse machineryResponse = new MachineryResponse();
		
		try {
			
			MachineryPortfolio machineryPortfolio = machineryPortfolioService.getById(machineryRequest.getPortfolioId());
			if(machineryPortfolio == null) {
				machineryResponse.setErrorMessage("No machinery exists with portfolio id=="+machineryRequest.getPortfolioId());
				machineryResponse.setStatus(false);
				return machineryResponse;
			} 
			machineryPortfolioService.delete(machineryPortfolio);
			machineryResponse.setStatus(true);
			machineryResponse.setSuccessMessage("Portfolio "+machineryPortfolio.getPortfolioName()+" deleted successfully.");
			try {
				//deleting image from the location
				File imageFile = new File(machineryPortfolio.getImageURL());
				if(imageFile.exists()){
					imageFile.delete();
				}

			} catch(Exception e){
				//ignore the error while deletion fails. which is not going to impact the flow.
			}
		}catch(Exception se){
			LOGGER.error("Failed while deleting portfolio for machinery=="+se.getMessage());
			machineryResponse.setErrorMessage("Failed while deleting portfolio for machinery=="+machineryRequest.getVendorId());
			machineryResponse.setStatus(false);
			return machineryResponse;
		}
		return machineryResponse;
    }
    
	@RequestMapping(value="/updateMachineryPortfolio", method = RequestMethod.POST) 
	@ResponseBody
	public MachineryResponse updateMachineryPortfolio(@RequestPart("machineryRequest") String machineryRequestStr,
			@RequestPart("file") MultipartFile uploadedImage) throws Exception {
		LOGGER.debug("Entered addMachineryPortfolio");
		MachineryRequest machineryRequest = new ObjectMapper().readValue(machineryRequestStr, MachineryRequest.class);
		MachineryResponse machineryResponse = new MachineryResponse();
		
    	String fileName = "";
    		try{
    			MachineryPortfolio machineryPortfolio = machineryPortfolioService.getById(machineryRequest.getPortfolioId());
    			String existingfile = machineryPortfolio.getImageURL();
    			if(uploadedImage.getSize() != 0) {
	    			fileName = storageService.store(uploadedImage,"architect");
	    			LOGGER.debug("machinery portfolio fileName "+fileName);
    	    	}
	    		Customer customer = customerService.getById(machineryRequest.getVendorId());
	    		if(customer == null){
	    			LOGGER.error("customer not found while uploading portfolio for customer id=="+machineryRequest.getVendorId());
	    			machineryResponse.setErrorMessage("Failed while storing image");
	    			machineryResponse.setStatus(false);
	    			return machineryResponse;
	    		}
	    		if(fileName != null) {
	    			machineryPortfolio.setImageURL(fileName);
	    		}
	    		machineryPortfolio.setPortfolioName(machineryRequest.getPortfolioName());
	    		machineryPortfolio.setEquipmentName(machineryRequest.getEquipmentName());
	    		machineryPortfolio.setEquipmentPrice(machineryRequest.getEquipmentPrice());
	    		machineryPortfolio.setHiringType(machineryRequest.getHiringType());
	    		machineryPortfolio.setStatus("N");
	    		machineryPortfolioService.update(machineryPortfolio);
	    		
	    		machineryResponse.setStatus(true);
	    		machineryResponse.setSuccessMessage("Portfolio details updated successfully. Awaiting from admin approval");
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
    			LOGGER.error("Failed while uploading portfolio for machinery=="+se.getMessage());
    			machineryResponse.setErrorMessage("Failed while uploading portfolio for machinery=="+machineryRequest.getPortfolioName());
    			machineryResponse.setStatus(false);
    			return machineryResponse;
    		}
    	return machineryResponse;
	}
	// Admin Machinery Portfolio
	@RequestMapping(value="/admin/getAdminMachineryPortfolio", method=RequestMethod.POST)
  	@ResponseBody
  	public PaginatedResponse getAdminMachineryPortfolio(
  			 @RequestBody AdminMachineryRequest adminMachineryRequest,
  			 @RequestParam(value="pageNumber", defaultValue = "1") int page , 
  			 @RequestParam(value="pageSize", defaultValue="15") int size) {
		
		LOGGER.debug("Entered getAdminMachineryPortfolio");
		
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		
		List<MachineryPortfolioVO> machineryPortfolioList = new ArrayList<MachineryPortfolioVO>();
		
		List<MachineryPortfolio> machineryPortfolios = null;
		
		if(adminMachineryRequest.getStatus().equals("ALL")) {
			
			machineryPortfolios = machineryPortfolioService.getAllPortfoios();
			
			for(MachineryPortfolio machineryPortfolio : machineryPortfolios) {
				
				MachineryPortfolioVO machineryPortfolioVO = new MachineryPortfolioVO();
				machineryPortfolioVO.setMachineryPortfolioId(machineryPortfolio.getId());
				machineryPortfolioVO.setCreatedate(machineryPortfolio.getCreateDate());
				machineryPortfolioVO.setImageURL(machineryPortfolio.getImageURL());
				machineryPortfolioVO.setPortfolioName(machineryPortfolio.getPortfolioName());
				machineryPortfolioVO.setStatus(machineryPortfolio.getStatus());
				machineryPortfolioVO.setEquipmentName(machineryPortfolio.getEquipmentName());
				machineryPortfolioVO.setEquipmentPrice(machineryPortfolio.getEquipmentPrice());
				machineryPortfolioVO.setHiringType(machineryPortfolio.getHiringType());
				machineryPortfolioVO.setVendorName(machineryPortfolio.getCustomer().getVendorAttrs().getVendorName());
				machineryPortfolioVO.setVendorImageURL(machineryPortfolio.getCustomer().getUserProfile());
				machineryPortfolioVO.setVendorDescription(machineryPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
				machineryPortfolioVO.setVendorShortDescription(machineryPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
				machineryPortfolioList.add(machineryPortfolioVO);
			}
		} else {
			
			machineryPortfolios = machineryPortfolioService.getPortfoliosBasedOnStatus(adminMachineryRequest.getStatus());
			
               for(MachineryPortfolio machineryPortfolio : machineryPortfolios) {
				
				MachineryPortfolioVO machineryPortfolioVO = new MachineryPortfolioVO();
				machineryPortfolioVO.setMachineryPortfolioId(machineryPortfolio.getId());
				machineryPortfolioVO.setCreatedate(machineryPortfolio.getCreateDate());
				machineryPortfolioVO.setImageURL(machineryPortfolio.getImageURL());
				machineryPortfolioVO.setPortfolioName(machineryPortfolio.getPortfolioName());
				machineryPortfolioVO.setStatus(machineryPortfolio.getStatus());
				machineryPortfolioVO.setEquipmentName(machineryPortfolio.getEquipmentName());
				machineryPortfolioVO.setEquipmentPrice(machineryPortfolio.getEquipmentPrice());
				machineryPortfolioVO.setHiringType(machineryPortfolio.getHiringType());
				machineryPortfolioVO.setVendorName(machineryPortfolio.getCustomer().getVendorAttrs().getVendorName());
				machineryPortfolioVO.setVendorImageURL(machineryPortfolio.getCustomer().getUserProfile());
				machineryPortfolioVO.setVendorDescription(machineryPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
				machineryPortfolioVO.setVendorShortDescription(machineryPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
				machineryPortfolioList.add(machineryPortfolioVO);
			}
		}
		
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, machineryPortfolioList.size());
    	paginatedResponse.setPaginationData(paginaionData);
    	
		if(machineryPortfolioList == null || machineryPortfolioList.isEmpty() || machineryPortfolioList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(machineryPortfolioList);
		
			return paginatedResponse;
		}
		
    	List<MachineryPortfolioVO> paginatedResponses = machineryPortfolioList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	
    	LOGGER.debug("Ended getAdminMachineryPortfolio");
		return paginatedResponse;
		
	}
	
	@RequestMapping(value="/admin/manageAdminMachineryPortfolios", method=RequestMethod.POST)
  	@ResponseBody
  	public AdminMachineryPortfolioResponse manageAdminMachineryPortfolios
  		(@RequestBody AdminMachineryRequest adminMachineryRequest, 
  		@RequestParam(value="pageNumber", defaultValue = "1") int page, 
  		@RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
    	LOGGER.debug("Entered manageAdminMachineryPortfolios");
    	AdminMachineryPortfolioResponse adminMachineryPortfolioResponse = new AdminMachineryPortfolioResponse();
    	try {
    		MachineryPortfolio machineryPortfolio = 
    				machineryPortfolioService.getById(adminMachineryRequest.getPortfolioId());
    	
    	if (machineryPortfolio == null) {
    		
    		adminMachineryPortfolioResponse.setErrorMessgae
    			("Error finding record with portfolio = " + adminMachineryRequest.getPortfolioId());
    		adminMachineryPortfolioResponse.setStatus(false);
			return adminMachineryPortfolioResponse;
    		
    	}
    	
    	if (adminMachineryRequest.getStatus().equals("Y")) {
    		
    		// If the request is to Approve, then update table entry with "Y"
    		machineryPortfolio.setStatus(adminMachineryRequest.getStatus());
    		machineryPortfolioService.update(machineryPortfolio);
    		
    		adminMachineryPortfolioResponse.setSuccessMessage
    			("Approval of portfolio " + adminMachineryRequest.getPortfolioId() + " is successful");
    	    LOGGER.debug("Approval of portfolio is successful");
    	    
    	} else {
    		
    		// If the request is to Decline, then remove the corresponding entry from table
    		machineryPortfolioService.delete(machineryPortfolio);
    		
    		adminMachineryPortfolioResponse.setSuccessMessage
				("Portfolio " + adminMachineryRequest.getPortfolioId() + " is declined and the entry is removed from the table");
    		LOGGER.debug("Approval of portfolio is declined");
    		
    	}
    	
    	adminMachineryPortfolioResponse.setStatus(true);
    	
    	} catch(Exception e) {
    		e.printStackTrace();
    		LOGGER.error("Error while updating portfolio status:" + e.getMessage());
    		
    	}
    	LOGGER.debug("Ended manageAdminMachineryPortfolios");
    	return adminMachineryPortfolioResponse;
    	
    }
	// Machinery portfolios Retrieval vendorwise and status 
	@RequestMapping(value="/getUserMachineryPortfolio", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public ArchitectPaginatedResponse getUserMachineryPortfolio(@RequestBody MachineryRequest machineryRequest,
  			@RequestParam(value="pageNumber", defaultValue = "1") int page , 
 			@RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		
		LOGGER.debug("Entered getUserMachineryPortfolio");
		ArchitectPaginatedResponse paginatedResponse = new ArchitectPaginatedResponse();
	
		List<MachineryPortfolioVO> machineryPortfolioList = new ArrayList<MachineryPortfolioVO>();
		List<MachineryPortfolio> portfolioList = null;
		
		try {
    		
			if(machineryRequest.getStatus().equals("ALL")) {
			portfolioList = machineryPortfolioService.findByVendorId(machineryRequest.getVendorId());
	    	for(MachineryPortfolio machineryPortfolio:portfolioList){
	    		
	    		MachineryPortfolioVO machineryPortfolioVO = new MachineryPortfolioVO();
				machineryPortfolioVO.setMachineryPortfolioId(machineryPortfolio.getId());
				machineryPortfolioVO.setCreatedate(machineryPortfolio.getCreateDate());
				machineryPortfolioVO.setImageURL(machineryPortfolio.getImageURL());
				machineryPortfolioVO.setPortfolioName(machineryPortfolio.getPortfolioName());
				machineryPortfolioVO.setStatus(machineryPortfolio.getStatus());
				machineryPortfolioVO.setEquipmentName(machineryPortfolio.getEquipmentName());
				machineryPortfolioVO.setEquipmentPrice(machineryPortfolio.getEquipmentPrice());
				machineryPortfolioVO.setHiringType(machineryPortfolio.getHiringType());
				paginatedResponse.setVendorName(machineryPortfolio.getCustomer().getVendorAttrs().getVendorName());
				paginatedResponse.setVendorImageURL(machineryPortfolio.getCustomer().getUserProfile());
				paginatedResponse.setVendorDescription(machineryPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
				paginatedResponse.setVendorShortDescription(machineryPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
				machineryPortfolioList.add(machineryPortfolioVO);
	    	}
			} else {
	    		
				portfolioList = machineryPortfolioService.getMachineryPortfolioBasedonStatusAndVendorId(machineryRequest.getVendorId(),machineryRequest.getStatus());
		    	for(MachineryPortfolio machineryPortfolio:portfolioList){
		    		MachineryPortfolioVO machineryPortfolioVO = new MachineryPortfolioVO();
					machineryPortfolioVO.setMachineryPortfolioId(machineryPortfolio.getId());
					machineryPortfolioVO.setCreatedate(machineryPortfolio.getCreateDate());
					machineryPortfolioVO.setImageURL(machineryPortfolio.getImageURL());
					machineryPortfolioVO.setPortfolioName(machineryPortfolio.getPortfolioName());
					machineryPortfolioVO.setStatus(machineryPortfolio.getStatus());
					machineryPortfolioVO.setEquipmentName(machineryPortfolio.getEquipmentName());
					machineryPortfolioVO.setEquipmentPrice(machineryPortfolio.getEquipmentPrice());
					machineryPortfolioVO.setHiringType(machineryPortfolio.getHiringType());
					paginatedResponse.setVendorName(machineryPortfolio.getCustomer().getVendorAttrs().getVendorName());
					paginatedResponse.setVendorImageURL(machineryPortfolio.getCustomer().getUserProfile());
					paginatedResponse.setVendorDescription(machineryPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
					paginatedResponse.setVendorShortDescription(machineryPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
					machineryPortfolioList.add(machineryPortfolioVO);
		    	}
	    	}
			
			PaginationData paginaionData=createPaginaionData(page,size);
	    	calculatePaginaionData(paginaionData,size, machineryPortfolioList.size());
	    	paginatedResponse.setPaginationData(paginaionData);
	    	
			if(machineryPortfolioList == null || machineryPortfolioList.isEmpty() || machineryPortfolioList.size() < paginaionData.getCountByPage()){
				paginatedResponse.setResponseData(machineryPortfolioList);
				LOGGER.debug("Ended getAdminArchitectsPortfolio");
				return paginatedResponse;
			}
			
	    	List<MachineryPortfolioVO> paginatedResponses = machineryPortfolioList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	    	paginatedResponse.setResponseData(paginatedResponses);
	    	
		}catch(Exception se){
			LOGGER.error("Failed while fetching portfolio list for machinery=="+se.getMessage());
			paginatedResponse.setErrorMsg("Failed while fetching portfolio list for machinery=="+machineryRequest.getVendorId());
			return paginatedResponse;
		}
		
		LOGGER.debug("Ended getUserMachineryPortfolio");
		return paginatedResponse;
    }
}
