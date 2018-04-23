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
import org.springframework.util.StringUtils;
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
import com.salesmanager.core.business.services.services.ArchitectsPortfolioService;
import com.salesmanager.core.business.services.services.WallPaperPortfolioService;
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
import com.salesmanager.shop.store.controller.customer.VendorResponse;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;

import freemarker.template.utility.StringUtil;

import com.salesmanager.shop.fileupload.services.StorageService;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@CrossOrigin
public class WallPaperController extends AbstractController {

	@Inject
	WallPaperPortfolioService wallPaperPortfolioService;
	
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


	private static final Logger LOGGER = LoggerFactory.getLogger(WallPaperController.class);

	private final static String VENDOR_ADD_PRODUCTS_TPL = "email_template_vendor_add_products.ftl";

	@RequestMapping(value="/addWallPaperPortfolio", method = RequestMethod.POST) 
	@ResponseBody
	public WallPaperResponse addWallPaperPortfolio(@RequestPart("wallPaperRequest") String wallPaperRequestStr,
			@RequestPart("file") MultipartFile uploadedImage) throws Exception {
		LOGGER.debug("Entered addWallPaperPortfolio");
		WallPaperRequest wallPaperRequest = new ObjectMapper().readValue(wallPaperRequestStr, WallPaperRequest.class);
		WallPaperResponse wallPaperResponse = new WallPaperResponse();
		WallPaperPortfolio wallPaperPortfolio = new WallPaperPortfolio();
		
    	String fileName = "";
    	if(uploadedImage.getSize() != 0) {
    		try{
    			fileName = storageService.store(uploadedImage,"wallpaper");
    			LOGGER.debug("wall paper portfolio fileName "+fileName);
    		
	    		Customer customer = customerService.getById(wallPaperRequest.getVendorId());
	    		if(customer == null){
	    			LOGGER.error("customer not found while uploading portfolio for customer id=="+wallPaperRequest.getVendorId());
	    			wallPaperResponse.setErrorMessage("Failed while storing image");
	    			wallPaperResponse.setStatus(false);
	    			return wallPaperResponse;
	    		}
	    		wallPaperPortfolio.setCreateDate(new Date());
	    		wallPaperPortfolio.setImageURL(fileName);
	    		wallPaperPortfolio.setPortfolioName(wallPaperRequest.getPortfolioName());
	    		wallPaperPortfolio.setCustomer(customer);
	    		wallPaperPortfolio.setBrand(wallPaperRequest.getBrand());
	    		wallPaperPortfolio.setPrice(wallPaperRequest.getPrice());
	    		wallPaperPortfolio.setSize(wallPaperRequest.getSize());
	    		wallPaperPortfolio.setThickness(wallPaperRequest.getThickness());
	    		wallPaperPortfolio.setServiceCharges(wallPaperRequest.getServiceCharges());
	    		wallPaperPortfolioService.save(wallPaperPortfolio);
	    		
	    		wallPaperResponse.setStatus(true);
	    		wallPaperResponse.setSuccessMessage("New portfolio details uploaded successfully. Awaiting from admin approval");
	    		
    		}catch(Exception se){
    			LOGGER.error("Failed while uploading portfolio for wall paper=="+se.getMessage());
    			wallPaperResponse.setErrorMessage("Failed while uploading portfolio for wall paper=="+wallPaperRequest.getPortfolioName());
    			wallPaperResponse.setStatus(false);
    			return wallPaperResponse;
    		}
    	}
    	return wallPaperResponse;
	}
	
    @RequestMapping(value="/getWallPaperPortfolio", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public WallPaperResponse getWallPaperPortfolio(@RequestBody WallPaperRequest wallPaperRequest,
  			 @RequestParam(value="pageNumber", defaultValue = "1") int page , 
 			 @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
    	WallPaperResponse wallPaperResponse = new WallPaperResponse();
		
		List<VendorPortfolioData> vendorPortfolioList = new ArrayList<VendorPortfolioData>();
		try {
    		Customer customer = customerService.getById(wallPaperRequest.getVendorId());
			List<WallPaperPortfolio> portfolioList = wallPaperPortfolioService.findByVendorId(wallPaperRequest.getVendorId());
	    	for(WallPaperPortfolio portfolio:portfolioList){
	    		VendorPortfolioData vendorPortfolioData = new VendorPortfolioData();
	    		vendorPortfolioData.setPortfolioId(portfolio.getId());
	    		vendorPortfolioData.setPortfolioName(portfolio.getPortfolioName());
	    		vendorPortfolioData.setVendorId(wallPaperRequest.getVendorId());
	    		vendorPortfolioData.setImageURL(portfolio.getImageURL());
	    		vendorPortfolioData.setBrand(portfolio.getBrand());
	    		vendorPortfolioData.setPrice(portfolio.getPrice());
	    		vendorPortfolioData.setSize(portfolio.getSize());
	    		vendorPortfolioData.setThickness(portfolio.getThickness());
	    		vendorPortfolioData.setServiceCharges(portfolio.getServiceCharges());
	    		vendorPortfolioList.add(vendorPortfolioData);
	    	}
	    	wallPaperResponse.setStatus(true);
	    	wallPaperResponse.setVendorPortfolioList(vendorPortfolioList);
	    	if(customer != null) {
	    		wallPaperResponse.setVendorName(customer.getVendorAttrs().getVendorName());
	    		wallPaperResponse.setVendorImageURL(customer.getUserProfile());
		    	wallPaperResponse.setVendorShortDescription(customer.getVendorAttrs().getVendorShortDescription());
		    	wallPaperResponse.setVendorDescription(customer.getVendorAttrs().getVendorDescription());
	    	}
		}catch(Exception se){
			LOGGER.error("Failed while fetching portfolio list for wall paper=="+se.getMessage());
			wallPaperResponse.setErrorMessage("Failed while fetching portfolio list for wall paper=="+wallPaperRequest.getVendorId());
			wallPaperResponse.setStatus(false);
			return wallPaperResponse;
		}
		return wallPaperResponse;
    }

    @RequestMapping(value="/deleteWallPaperPortfolio", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public WallPaperResponse deleteWallPaperPortfolio(@RequestBody WallPaperRequest wallPaperRequest) throws Exception {
    	WallPaperResponse wallPaperResponse = new WallPaperResponse();
		
		try {
			
			WallPaperPortfolio wallPaperPortfolio = wallPaperPortfolioService.getById(wallPaperRequest.getPortfolioId());
			if(wallPaperPortfolio == null) {
				wallPaperResponse.setErrorMessage("No wall paper exists with portfolio id=="+wallPaperRequest.getPortfolioId());
				wallPaperResponse.setStatus(false);
				return wallPaperResponse;
			} 
			wallPaperPortfolioService.delete(wallPaperPortfolio);
	    	wallPaperResponse.setStatus(true);
	    	wallPaperResponse.setSuccessMessage("Portfolio "+wallPaperPortfolio.getPortfolioName()+" deleted successfully.");
			try {
				//deleting image from the location
				File imageFile = new File(wallPaperPortfolio.getImageURL());
				if(imageFile.exists()){
					imageFile.delete();
				}

			} catch(Exception e){
				//ignore the error while deletion fails. which is not going to impact the flow.
			}
		}catch(Exception se){
			LOGGER.error("Failed while deleting portfolio for wall paper=="+se.getMessage());
			wallPaperResponse.setErrorMessage("Failed while deleting portfolio for wall paper=="+wallPaperRequest.getVendorId());
			wallPaperResponse.setStatus(false);
			return wallPaperResponse;
		}
		return wallPaperResponse;
    }

	@RequestMapping(value="/updateWallPaperPortfolio", method = RequestMethod.POST) 
	@ResponseBody
	public WallPaperResponse updateWallPaperPortfolio(@RequestPart("wallPaperRequest") String wallPaperRequestStr,
			@RequestPart("file") MultipartFile uploadedImage) throws Exception {
		LOGGER.debug("Entered updateWallPaperPortfolio");
		WallPaperRequest wallPaperRequest = new ObjectMapper().readValue(wallPaperRequestStr, WallPaperRequest.class);
		WallPaperResponse wallPaperResponse = new WallPaperResponse();
		
    	String fileName = "";
    		try{
    			WallPaperPortfolio wallPaperPortfolio = wallPaperPortfolioService.getById(wallPaperRequest.getPortfolioId());
    			String existingfile = wallPaperPortfolio.getImageURL();
    	    	if(uploadedImage.getSize() != 0) {
	    			fileName = storageService.store(uploadedImage,"wallpaper");
	    			LOGGER.debug("wall paper portfolio fileName "+fileName);
    	    	}
	    		Customer customer = customerService.getById(wallPaperRequest.getVendorId());
	    		if(customer == null){
	    			LOGGER.error("customer not found while uploading portfolio for customer id=="+wallPaperRequest.getVendorId());
	    			wallPaperResponse.setErrorMessage("Failed while storing image");
	    			wallPaperResponse.setStatus(false);
	    			return wallPaperResponse;
	    		}
	    		if(fileName != null && !fileName.isEmpty()) {
	    			wallPaperPortfolio.setImageURL(fileName);
	    		}
	    		wallPaperPortfolio.setBrand(wallPaperRequest.getBrand());
	    		wallPaperPortfolio.setPrice(wallPaperRequest.getPrice());
	    		wallPaperPortfolio.setSize(wallPaperRequest.getSize());
	    		wallPaperPortfolio.setThickness(wallPaperRequest.getThickness());
	    		wallPaperPortfolio.setPortfolioName(wallPaperRequest.getPortfolioName());
	    		wallPaperPortfolio.setServiceCharges(wallPaperRequest.getServiceCharges());
	    		wallPaperPortfolio.setStatus("N");
	    		//String existingfile = wallPaperPortfolio.getImageURL();
	    		wallPaperPortfolioService.update(wallPaperPortfolio);
	    		
	    		wallPaperResponse.setStatus(true);
	    		wallPaperResponse.setSuccessMessage("Portfolio details updated successfully. Awaiting from admin approval");
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
	    	/*	if(fileName != null) {
					try {
						//deleting image from the location
						File imageFile = new File(wallPaperPortfolio.getImageURL());
						if(imageFile.exists()){
							imageFile.delete();
						}

					} catch(Exception e){
						//ignore the error while deletion fails. which is not going to impact the flow.
					}
	    		}*/
	    		
    		}catch(Exception se){
    			LOGGER.error("Failed while uploading portfolio for wall paper=="+se.getMessage());
    			wallPaperResponse.setErrorMessage("Failed while uploading portfolio for wall paper=="+wallPaperRequest.getPortfolioName());
    			wallPaperResponse.setStatus(false);
    			return wallPaperResponse;
    		}
    		
    		LOGGER.debug("Entered updateWallPaperPortfolio");
    	    return wallPaperResponse;
	}
	@RequestMapping(value="/admin/getAdminWallPaperPortfolio", method=RequestMethod.POST)
  	@ResponseBody
  	public PaginatedResponse getAdminWallPaperPortfolio(@RequestBody AdminWallPaperRequest adminWallPaperRequest,
  			 @RequestParam(value="pageNumber", defaultValue = "1") int page , 
 			 @RequestParam(value="pageSize", defaultValue="15") int size) {
			
		LOGGER.debug("Entered getAdminWallPaperPortfolio");
		
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		
		List<WallPaperPortfolio> wallPaperPortfolios = null;
		
		List<WallPaperPortfolioVO> wallPaperPortfolioList = new ArrayList<WallPaperPortfolioVO>();
		
		try {
			
		if(adminWallPaperRequest.getStatus().equals("ALL")) {
			
			wallPaperPortfolios = wallPaperPortfolioService.getAllPortfolios();
			
			for(WallPaperPortfolio wallPaperPortfolio : wallPaperPortfolios) {
				
				WallPaperPortfolioVO wallPaperPortfolioVO = new WallPaperPortfolioVO();
				
				wallPaperPortfolioVO.setPortfolioId(wallPaperPortfolio.getId());
				wallPaperPortfolioVO.setPortfolioName(wallPaperPortfolio.getPortfolioName());
				wallPaperPortfolioVO.setBrand(wallPaperPortfolio.getBrand());
				wallPaperPortfolioVO.setSize(wallPaperPortfolio.getSize());
				wallPaperPortfolioVO.setThickness(wallPaperPortfolio.getThickness());
				wallPaperPortfolioVO.setPrice(wallPaperPortfolio.getPrice());
				wallPaperPortfolioVO.setStatus(wallPaperPortfolio.getStatus());
				wallPaperPortfolioVO.setImageURL(wallPaperPortfolio.getImageURL());
				wallPaperPortfolioVO.setServiceCharges(wallPaperPortfolio.getServiceCharges());
				
				wallPaperPortfolioVO.setVendorId(wallPaperPortfolio.getCustomer().getId());
				wallPaperPortfolioVO.setVendorName(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorName());
				wallPaperPortfolioVO.setVendorImageURL(wallPaperPortfolio.getCustomer().getUserProfile());
				wallPaperPortfolioVO.setVendorDescription(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
				wallPaperPortfolioVO.setVendorShortDescription(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
				
				wallPaperPortfolioList.add(wallPaperPortfolioVO);
				
			} 
		}else {
				
				wallPaperPortfolios = wallPaperPortfolioService.getPortfoliosBasedOnStatus(adminWallPaperRequest.getStatus());
				
				for(WallPaperPortfolio wallPaperPortfolio : wallPaperPortfolios) {
					
					WallPaperPortfolioVO wallPaperPortfolioVO = new WallPaperPortfolioVO();
					
					wallPaperPortfolioVO.setPortfolioId(wallPaperPortfolio.getId());
					wallPaperPortfolioVO.setPortfolioName(wallPaperPortfolio.getPortfolioName());
					wallPaperPortfolioVO.setBrand(wallPaperPortfolio.getBrand());
					wallPaperPortfolioVO.setSize(wallPaperPortfolio.getSize());
					wallPaperPortfolioVO.setThickness(wallPaperPortfolio.getThickness());
					wallPaperPortfolioVO.setPrice(wallPaperPortfolio.getPrice());
					wallPaperPortfolioVO.setStatus(wallPaperPortfolio.getStatus());
					wallPaperPortfolioVO.setImageURL(wallPaperPortfolio.getImageURL());
					wallPaperPortfolioVO.setServiceCharges(wallPaperPortfolio.getServiceCharges());
					
					wallPaperPortfolioVO.setVendorId(wallPaperPortfolio.getCustomer().getId());
					wallPaperPortfolioVO.setVendorName(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorName());
					wallPaperPortfolioVO.setVendorImageURL(wallPaperPortfolio.getCustomer().getUserProfile());
					wallPaperPortfolioVO.setVendorDescription(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
					wallPaperPortfolioVO.setVendorShortDescription(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
					
					wallPaperPortfolioList.add(wallPaperPortfolioVO);
						
			}
		}
				PaginationData paginaionData=createPaginaionData(page,size);
		    	calculatePaginaionData(paginaionData,size, wallPaperPortfolioList.size());
		    	paginatedResponse.setPaginationData(paginaionData);
		    	
				if(wallPaperPortfolioList == null || wallPaperPortfolioList.isEmpty() || wallPaperPortfolioList.size() < paginaionData.getCountByPage()){
					paginatedResponse.setResponseData(wallPaperPortfolioList);
				
					return paginatedResponse;
				}
				
		    	List<WallPaperPortfolioVO> paginatedResponses = wallPaperPortfolioList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		    	paginatedResponse.setResponseData(paginatedResponses);
		    	
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while retrieving portfolios "+e.getMessage());
			paginatedResponse.setErrorMsg("Error while retrieving portfolios");
			return paginatedResponse;
		}
		
		LOGGER.debug("Ended getAdminWallPaperPortfolio");
		return paginatedResponse;
		
	}
	@RequestMapping(value="/admin/manageAdminWallPaperPortfolios", method=RequestMethod.POST)
  	@ResponseBody
  	public AdminMachineryPortfolioResponse manageAdminWallPaperPortfolios(   
  		            @RequestBody AdminWallPaperRequest adminWallPaperRequest, 
  		            @RequestParam(value="pageNumber", defaultValue = "1") int page, 
  		            @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
			
		LOGGER.debug("Entered manageAdminWallPortfolios");
		
		AdminMachineryPortfolioResponse adminMachineryPortfolioResponse = new AdminMachineryPortfolioResponse();
    	try {
    		WallPaperPortfolio wallPaperPortfolio = 
    				wallPaperPortfolioService.getById(adminWallPaperRequest.getPortfolioId());
    	
    	if (wallPaperPortfolio == null) {
    		
    		adminMachineryPortfolioResponse.setErrorMessgae
    			("Error finding record with portfolio = " + adminWallPaperRequest.getPortfolioId());
    		adminMachineryPortfolioResponse.setStatus(false);
			return adminMachineryPortfolioResponse;
    		
    	}
    	
    	if (adminWallPaperRequest.getStatus().equals("Y")) {
    		
    		// If the request is to Approve, then update table entry with "Y"
    		wallPaperPortfolio.setStatus(adminWallPaperRequest.getStatus());
    		wallPaperPortfolioService.update(wallPaperPortfolio);
    		
    		adminMachineryPortfolioResponse.setSuccessMessage
    			("Approval of portfolio " + adminWallPaperRequest.getPortfolioId() + " is successful");
    	    LOGGER.debug("Approval of portfolio is successful");
    	    
    	} else {
    		
    		// If the request is to Decline, then remove the corresponding entry from table
    		wallPaperPortfolioService.delete(wallPaperPortfolio);
    		
    		adminMachineryPortfolioResponse.setSuccessMessage
				("Portfolio " + adminWallPaperRequest.getPortfolioId() + " is declined and the entry is removed from the table");
    		LOGGER.debug("Approval of portfolio is declined");
    		
    	}
    	
    	adminMachineryPortfolioResponse.setStatus(true);
    	
    	} catch(Exception e) {
    		e.printStackTrace();
    		LOGGER.error("Error while updating portfolio status:" + e.getMessage());
    		adminMachineryPortfolioResponse.setErrorMessgae("Error while updating portfolio status"+e.getMessage());
    		adminMachineryPortfolioResponse.setStatus(false);
    		return adminMachineryPortfolioResponse;
    	}
    	
    	LOGGER.debug("Ended manageAdminWallPortfolios");
    	
    	return adminMachineryPortfolioResponse;
    	
	}
	@RequestMapping(value="/getUserWallPaperPortfolio", method=RequestMethod.POST)
  	@ResponseBody
  	public ArchitectPaginatedResponse getUserWallPaperPortfolio(@RequestBody WallPaperRequest wallPaperRequest,
  			 @RequestParam(value="pageNumber", defaultValue = "1") int page , 
 			 @RequestParam(value="pageSize", defaultValue="15") int size) {
			
		LOGGER.debug("Entered getUserWallPaperPortfolio");
		
		ArchitectPaginatedResponse paginatedResponse = new ArchitectPaginatedResponse();
		
        List<WallPaperPortfolio> wallPaperPortfolios = null;
		
		List<WallPaperPortfolioVO> wallPaperPortfolioList = new ArrayList<WallPaperPortfolioVO>();
		try {
		if(!StringUtils.isEmpty(wallPaperRequest.getStatus()) && wallPaperRequest.getStatus().equals("ALL")) {
			
			wallPaperPortfolios = wallPaperPortfolioService.findByVendorId(wallPaperRequest.getVendorId());
			
			for(WallPaperPortfolio wallPaperPortfolio : wallPaperPortfolios) {
				
				WallPaperPortfolioVO wallPaperPortfolioVO = new WallPaperPortfolioVO();
				
				wallPaperPortfolioVO.setPortfolioId(wallPaperPortfolio.getId());
				wallPaperPortfolioVO.setPortfolioName(wallPaperPortfolio.getPortfolioName());
				wallPaperPortfolioVO.setBrand(wallPaperPortfolio.getBrand());
				wallPaperPortfolioVO.setSize(wallPaperPortfolio.getSize());
				wallPaperPortfolioVO.setThickness(wallPaperPortfolio.getThickness());
				wallPaperPortfolioVO.setPrice(wallPaperPortfolio.getPrice());
				wallPaperPortfolioVO.setStatus(wallPaperPortfolio.getStatus());
				wallPaperPortfolioVO.setImageURL(wallPaperPortfolio.getImageURL());
				wallPaperPortfolioVO.setServiceCharges(wallPaperPortfolio.getServiceCharges());
				
				wallPaperPortfolioVO.setVendorId(wallPaperPortfolio.getCustomer().getId());
				paginatedResponse.setVendorName(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorName());
				paginatedResponse.setVendorImageURL(wallPaperPortfolio.getCustomer().getUserProfile());
				paginatedResponse.setVendorDescription(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
				paginatedResponse.setVendorShortDescription(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
				
				wallPaperPortfolioList.add(wallPaperPortfolioVO);
				
			} 
		}else {
				
				wallPaperPortfolios = wallPaperPortfolioService.getPortfoliosBasedOnStatusAndVendorId(wallPaperRequest.getVendorId(),wallPaperRequest.getStatus());
				
				for(WallPaperPortfolio wallPaperPortfolio : wallPaperPortfolios) {
					
					WallPaperPortfolioVO wallPaperPortfolioVO = new WallPaperPortfolioVO();
					
					wallPaperPortfolioVO.setPortfolioId(wallPaperPortfolio.getId());
					wallPaperPortfolioVO.setPortfolioName(wallPaperPortfolio.getPortfolioName());
					wallPaperPortfolioVO.setBrand(wallPaperPortfolio.getBrand());
					wallPaperPortfolioVO.setSize(wallPaperPortfolio.getSize());
					wallPaperPortfolioVO.setThickness(wallPaperPortfolio.getThickness());
					wallPaperPortfolioVO.setPrice(wallPaperPortfolio.getPrice());
					wallPaperPortfolioVO.setStatus(wallPaperPortfolio.getStatus());
					wallPaperPortfolioVO.setImageURL(wallPaperPortfolio.getImageURL());
					wallPaperPortfolioVO.setServiceCharges(wallPaperPortfolio.getServiceCharges());
					
					wallPaperPortfolioVO.setVendorId(wallPaperPortfolio.getCustomer().getId());
					paginatedResponse.setVendorName(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorName());
					paginatedResponse.setVendorImageURL(wallPaperPortfolio.getCustomer().getUserProfile());
					paginatedResponse.setVendorDescription(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorDescription());
					paginatedResponse.setVendorShortDescription(wallPaperPortfolio.getCustomer().getVendorAttrs().getVendorShortDescription());
					
					wallPaperPortfolioList.add(wallPaperPortfolioVO);
						
			}
		}
				PaginationData paginaionData=createPaginaionData(page,size);
		    	calculatePaginaionData(paginaionData,size, wallPaperPortfolioList.size());
		    	paginatedResponse.setPaginationData(paginaionData);
		    	
				if(wallPaperPortfolioList == null || wallPaperPortfolioList.isEmpty() || wallPaperPortfolioList.size() < paginaionData.getCountByPage()){
					paginatedResponse.setResponseData(wallPaperPortfolioList);
				
					return paginatedResponse;
				}
				
		    	List<WallPaperPortfolioVO> paginatedResponses = wallPaperPortfolioList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		    	paginatedResponse.setResponseData(paginatedResponses);
		    	
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while retrieving portfolios "+e.getMessage());
			paginatedResponse.setErrorMsg("Error while retrieving portfolios");
		}
		
		LOGGER.debug("Ended getUserWallPaperPortfolio");
		
		return paginatedResponse;
		
	}
	@RequestMapping(value="/getWallPaperDetails", method=RequestMethod.POST)
  	@ResponseBody
  	public WallPaperDetailsResponse getWallPaperDetails(@RequestBody WallPaperRequest wallPaperRequest,
  			 @RequestParam(value="pageNumber", defaultValue = "1") int page , 
 			 @RequestParam(value="pageSize", defaultValue="15") int size) {
			
		LOGGER.debug("Entered getWallPaperDetails");
		
		WallPaperDetailsResponse wallPaperDetailsResponse = new WallPaperDetailsResponse();
		
		WallPaperDetails wallPaperDetails = new WallPaperDetails();
		
		try {
			
		WallPaperPortfolio wallPaperPortfolio = wallPaperPortfolioService.getById(wallPaperRequest.getPortfolioId());
		
		wallPaperDetails.setPortfolioId(wallPaperPortfolio.getId());
		wallPaperDetails.setPortfolioName(wallPaperPortfolio.getPortfolioName());
		wallPaperDetails.setBrand(wallPaperPortfolio.getBrand());
		wallPaperDetails.setImageURL(wallPaperPortfolio.getImageURL());
		wallPaperDetails.setSize(wallPaperPortfolio.getSize());
		wallPaperDetails.setThickness(wallPaperPortfolio.getThickness());
		wallPaperDetails.setPrice(wallPaperPortfolio.getPrice());
		wallPaperDetails.setStatus(wallPaperPortfolio.getStatus());
		wallPaperDetails.setServiceCharges(wallPaperPortfolio.getServiceCharges());
		wallPaperDetails.setVendorId(wallPaperPortfolio.getCustomer().getId());
		
		wallPaperDetailsResponse.setWallPaperDetails(wallPaperDetails);
		
		wallPaperDetailsResponse.setSuccessmessage("WallPaperDetials retrieved successfully");
		wallPaperDetailsResponse.setStatus("true");
		
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.debug("Error while retrieving wallpaper details "+e.getMessage());
			wallPaperDetailsResponse.setErrorMessage("Error while retrieving wallpaper details ");
			wallPaperDetailsResponse.setStatus("false");
			return wallPaperDetailsResponse;
		}
		
		LOGGER.debug("Ended getWallPaperDetails");
		return wallPaperDetailsResponse;
		
	}
}
