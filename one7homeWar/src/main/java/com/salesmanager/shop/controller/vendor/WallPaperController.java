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
import com.salesmanager.core.business.services.services.WallPaperPortfolioService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.customer.ArchitectsPortfolio;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.WallPaperPortfolio;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.shop.admin.controller.products.ProductImageRequest;
import com.salesmanager.shop.admin.controller.products.ProductImageResponse;
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
    			LOGGER.debug("architect portfolio fileName "+fileName);
    		
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
	    		wallPaperPortfolioService.save(wallPaperPortfolio);
	    		
	    		wallPaperResponse.setStatus(true);
	    		wallPaperResponse.setSuccessMessage("New portfolio details uploaded successfully.");
	    		
    		}catch(StorageException se){
    			LOGGER.error("Failed while uploading portfolio for wall paper=="+wallPaperRequest.getPortfolioName());
    			wallPaperResponse.setErrorMessage("Failed while storing image");
    			wallPaperResponse.setStatus(false);
    			return wallPaperResponse;
    		}
    	}
    	return wallPaperResponse;
	}
}
