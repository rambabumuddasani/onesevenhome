package com.salesmanager.shop.controller.vendor.product;

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

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.shop.admin.controller.VendorBookingVO;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.populator.order.VendorPopulator;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.controller.customer.CustomerResponse;
import com.salesmanager.shop.store.controller.customer.VendorResponse;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@CrossOrigin
public class VendorProductController extends AbstractController {

	@Inject
	CustomerService customerService;

	@Inject
	ProductService productService;

	@Inject
	VendorProductService vendorProductService;

	@Inject
	EmailService emailService;

	@Inject
	private LabelUtils messages;

	@Inject
	MerchantStoreService merchantStoreService ;

	@Inject
	private EmailUtils emailUtils;

	@Inject
	private PricingService pricingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(VendorProductController.class);

	private final static String VENDOR_ADD_PRODUCTS_TPL = "email_template_vendor_add_products.ftl";


	@RequestMapping(value="/addVendorProducts", method = RequestMethod.POST) 
	@ResponseBody
	public VendorProductResponse addVendorProducts(@RequestBody VendorProductRequest vendorProductRequest ) throws Exception {

		LOGGER.debug("Entered addVendorProducts:");
		
		VendorProductResponse vendorProductResponse = new VendorProductResponse(); 
		
		try {
		String vendorId = vendorProductRequest.getVendorId();
		
		Customer customer = customerService.getById(Long.parseLong(vendorId));

		List<String> productIds = vendorProductRequest.getProductId();

		List<VendorProduct> vpList = new ArrayList<VendorProduct>();
		List<ProductsInfo> vList = new ArrayList<ProductsInfo>();

		for(String productId : productIds){
			Product dbProduct = productService.getById(Long.parseLong(productId));
			VendorProduct vendorProduct = new VendorProduct();
			ProductsInfo productsInfo = new ProductsInfo();
			vendorProduct.setProduct(dbProduct);
			vendorProduct.setCustomer(customer);
			vendorProduct.setCreatedDate(new Date());
			vendorProduct.setVendorWishListed(Boolean.FALSE);
			productsInfo.setProductId(dbProduct.getId());
			productsInfo.setProductName(dbProduct.getProductDescription().getName());
			vpList.add(vendorProduct);
			vList.add(productsInfo);
		}
		LOGGER.debug("vpList:"+vpList.size());
		vendorProductService.save(vpList);
		LOGGER.debug("Added products");
		vendorProductResponse.setVenderId(vendorId);
		vendorProductResponse.setVendorProducts(vList);
		vendorProductResponse.setSuccessMessage("Successfully added products to ProductList");
		vendorProductResponse.setStatus("true");

		//sending email
		MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");  
		final Locale locale  = new Locale("en");
		String[] vendorName = {customer.getVendorAttrs().getVendorName()};
		Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username",locale));
		templateTokens.put(EmailConstants.EMAIL_VENDOR_ADD_PRODUCTS_TXT, messages.getMessage("email.vendor.addproducts.text",vendorName,locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));


		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.vendor.addproducts.text.subject",locale));
		email.setTo(merchantStore.getStoreEmailAddress());
		email.setTemplateName(VENDOR_ADD_PRODUCTS_TPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);

		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("Error while adding product(s) to Product List "+e.getMessage());
			vendorProductResponse.setErrorMessage("Error while adding product(s) to Product List");
			vendorProductResponse.setStatus("false");
		}
		LOGGER.debug("Ended addVendorProducts");
		return vendorProductResponse;
	}

	@RequestMapping(value="/addVendorWishListProducts", method = RequestMethod.POST) 
	@ResponseBody
	public VendorProductResponse addVendorWishListProducts(@RequestBody VendorProductRequest vendorProductRequest ) throws Exception {

		LOGGER.debug("Entered addVendorWishListProducts:");
		VendorProductResponse vendorProductResponse = new VendorProductResponse();
		
		try {
		String vendorId = vendorProductRequest.getVendorId();
		
		Customer customer = customerService.getById(Long.parseLong(vendorId));

		List<String> productIds = vendorProductRequest.getProductId(); 

		List<VendorProduct> vpList = new ArrayList<VendorProduct>();
		List<ProductsInfo> vList = new ArrayList<ProductsInfo>();

		for(String productId : productIds){
			Product dbProduct = productService.getById(Long.parseLong(productId));
			VendorProduct vendorProduct = new VendorProduct();
			ProductsInfo productsInfo = new ProductsInfo();
			vendorProduct.setProduct(dbProduct);
			vendorProduct.setCustomer(customer);
			vendorProduct.setCreatedDate(new Date());
			vendorProduct.setVendorWishListed(true);
			productsInfo.setProductId(dbProduct.getId());
			productsInfo.setProductName(dbProduct.getProductDescription().getName());			
			vpList.add(vendorProduct);
			vList.add(productsInfo);
		}

		LOGGER.debug("product list size : "+vpList.size());
		vendorProductService.save(vpList);
		LOGGER.debug("Products added to wishList");
		vendorProductResponse.setVenderId(vendorId);
		vendorProductResponse.setVendorProducts(vList);
		vendorProductResponse.setSuccessMessage("Successfully added product(s) to WishList");
		vendorProductResponse.setStatus("true");

		/* Commenting code to send email which is not required for adding products into wish list*/
		
		//sending email
		/* MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");  
		final Locale locale  = new Locale("en");
		String[] vendorName = {customer.getVendorAttrs().getVendorName()};
		Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username",locale));
		//templateTokens.put(EmailConstants.EMAIL_VENDOR_ADD_PRODUCTS_TXT, messages.getMessage("email.vendor.addproducts.text",vendorName,locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));

		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.vendor.addproducts.text.subject",locale));
		email.setTo(merchantStore.getStoreEmailAddress());
		email.setTemplateName(VENDOR_ADD_PRODUCTS_TPL);
		email.setTemplateTokens(templateTokens);
		emailService.sendHtmlEmail(merchantStore, email); */

		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("Error while adding product(s) to WishList "+e.getMessage());
			vendorProductResponse.setErrorMessage("Error while adding product(s) to WishLis");
			vendorProductResponse.setStatus("false");
		}
		LOGGER.debug("Ended addVendorWishListProducts:");
		return vendorProductResponse;
	}

	@RequestMapping(value={"/wishlist/{vendorId}"},  method = { RequestMethod.GET })
	@ResponseBody	
	public PaginatedResponse getWishListProducts(@PathVariable Long vendorId,
			@RequestParam(value="pageNumber", defaultValue = "1") int page ,
            @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		LOGGER.debug("Entered getWishListProducts");
		//VendortProductList vendorProductList = new VendortProductList();
		List<VendorProductData> vendorProductData = new ArrayList<VendorProductData>();
		List<VendorProduct> dbVendorProductList = vendorProductService.findProductWishListByVendor(vendorId);
		for(VendorProduct vendorProduct : dbVendorProductList){
			VendorProductData vpData = new VendorProductData();
			Product product = vendorProduct.getProduct();
			if(product != null) {
				ProductImage image = product.getProductImage();
				if(image != null) {
					String imagePath =  image.getProductImageUrl();
					vpData.setProductImg(imagePath);
				}
				vpData.setProductCode(product.getSku());
				vpData.setProductId(product.getId());
				vpData.setProductName(product.getProductDescription().getName());
				vpData.setVendorProuctId(vendorProduct.getId());
				try {
					vpData.setProductPrice(pricingService.calculateProductPrice(product).getOriginalPrice().toString());
				} catch (ServiceException e) {
					LOGGER.error("Error while getting WishListProducts");
				}
				vendorProductData.add(vpData);
			}
		}
		//vendorProductList.setVendorProductData(vendorProductData);
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, vendorProductData.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(vendorProductData == null || vendorProductData.isEmpty() || vendorProductData.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(vendorProductData);
			return paginatedResponse;
		}
		
    	List<VendorProductData> paginatedResponses = vendorProductData.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
		LOGGER.debug("Ended getWishListProducts");
		return paginatedResponse;
	}

	@RequestMapping(value={"/productList/{vendorId}"},  method = { RequestMethod.GET })
	@ResponseBody	
	public PaginatedResponse getVendorProductList(@PathVariable Long vendorId,
			@RequestParam(value="pageNumber", defaultValue = "1") int page ,
            @RequestParam(value="pageSize", defaultValue="15") int size){
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		LOGGER.debug("Entered getVendorProductList");
		//VendortProductList vendorProductList = new VendortProductList();
		List<VendorProductData> vendorProductData = new ArrayList<VendorProductData>();
		List<VendorProduct> dbVendorProductList = vendorProductService.findProductsByVendor(vendorId);
		for(VendorProduct vendorProduct : dbVendorProductList){
			VendorProductData vpData = new VendorProductData();
			Product product = vendorProduct.getProduct();
			if(product != null) {
				ProductImage image = product.getProductImage();
				if(image != null) {
					String imagePath =  image.getProductImageUrl();
					vpData.setProductImg(imagePath);
				}
				vpData.setProductCode(product.getSku());
				vpData.setProductId(product.getId());
				vpData.setProductName(product.getProductDescription().getName());
				try {
					vpData.setProductPrice(pricingService.calculateProductPrice(product).getOriginalPrice().toString());
				} catch (ServiceException e) {
					LOGGER.error("Error while getting Productlist");
				}
				vendorProductData.add(vpData);
			}
		}
		//vendorProductList.setVendorProductData(vendorProductData);
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, vendorProductData.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(vendorProductData == null || vendorProductData.isEmpty() || vendorProductData.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(vendorProductData);
			return paginatedResponse;
		}
		
    	List<VendorProductData> paginatedResponses = vendorProductData.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
		LOGGER.debug("Ended getVendorProductList");
		return paginatedResponse;
	}


	@RequestMapping(value={"/vendors/{productId}"},  method = { RequestMethod.GET })
	@ResponseBody	
	public VendorsList getProductVendors(@PathVariable Long productId){
		LOGGER.debug("Entered getProductVendors");
		VendorsList vendorsList = new VendorsList();
		List<VendorResponse> vendorsDataForProduct = new ArrayList<VendorResponse>();
		try {
			List<VendorProduct> dbVendorProductList = vendorProductService.findProductVendors(productId);
			LOGGER.debug("dbVendorProductList size=="+dbVendorProductList.size());
			for(VendorProduct vendorProduct : dbVendorProductList){
				VendorResponse vendorResponse = new VendorResponse();
				LOGGER.debug("customer --vendor=="+vendorProduct.getCustomer().getEmailAddress());
				Customer vendor = vendorProduct.getCustomer();
				if(vendor != null) {
					VendorPopulator vendorPopulator = new VendorPopulator();
					vendorPopulator.populate(vendor, vendorResponse, null, null);
					vendorsDataForProduct.add(vendorResponse);
				}
			}
			vendorsList.setVendorsDataForProduct(vendorsDataForProduct);
		}catch(Exception e){
			LOGGER.error("error occured while retrieving vendors based on product id ="+productId+"---"+e.getMessage());
			//e.printStackTrace(System.out);
		}
		LOGGER.error("getProductVendors");
		return vendorsList;
	}

	@RequestMapping(value={"/pincodeWiseVendors/{productId}"},  method = { RequestMethod.GET })
	@ResponseBody	
	public VendorsList getProductVendorsByProductIdAndCustomer(@PathVariable Long productId,HttpServletRequest request){
		LOGGER.debug("Entered getProductVendors");
		VendorsList vendorsList = new VendorsList();
		List<VendorResponse> vendorsDataForProduct = new ArrayList<VendorResponse>();
		Customer customer = getSessionAttribute(  Constants.CUSTOMER, request );
		String billingPostalCode = customer.getBilling().getPostalCode();
		try {
			List<VendorProduct> dbVendorProductList = vendorProductService.findProductVendorsByProductIdAndCustomerPinCode(productId, billingPostalCode);
			if(dbVendorProductList == null || dbVendorProductList.isEmpty()){
				vendorsList.setStatus("Couldn't locate vendor(s) under this pincode");
				return vendorsList;
			}
			LOGGER.debug("dbVendorProductList size=="+dbVendorProductList.size());
			for(VendorProduct vendorProduct : dbVendorProductList){
				VendorResponse vendorResponse = new VendorResponse();
				LOGGER.debug("customer --vendor=="+vendorProduct.getCustomer().getEmailAddress());
				Customer vendor = vendorProduct.getCustomer();
				if(vendor != null) {
					VendorPopulator vendorPopulator = new VendorPopulator();
					vendorPopulator.populate(vendor, vendorResponse, null, null);
					vendorsDataForProduct.add(vendorResponse);
				}
			}
			vendorsList.setVendorsDataForProduct(vendorsDataForProduct);
		}catch(Exception e){
			LOGGER.error("error occured while retrieving vendors based on product id ="+productId+"---"+e.getMessage());
			//e.printStackTrace(System.out);
		}
		vendorsList.setStatus("OK");
		LOGGER.error("getProductVendors");
		return vendorsList;
	}

	@RequestMapping(value={"/seachPincodeWiseVendors/{productId}"},  method = { RequestMethod.GET })
	@ResponseBody	
	public VendorsList getProductVendorsByProductIdAndCustomerPinCode(@PathVariable Long productId,@RequestParam(name = "postalCode",required=true)  String billingPostalCode ,HttpServletRequest request){
		LOGGER.debug("Entered getProductVendors");
		VendorsList vendorsList = new VendorsList();
		List<VendorResponse> vendorsDataForProduct = new ArrayList<VendorResponse>();
		//Customer customer = getSessionAttribute(  Constants.CUSTOMER, request );
		//String billingPostalCode = customer.getBilling().getPostalCode();
		try {
			List<VendorProduct> dbVendorProductList = vendorProductService.findProductVendorsByProductIdAndCustomerPinCode(productId, billingPostalCode);
			if(dbVendorProductList == null || dbVendorProductList.isEmpty()){
				vendorsList.setStatus("Couldn't locate this product in your pincode, Sorry!");
				return vendorsList;
			}
			LOGGER.debug("dbVendorProductList size=="+dbVendorProductList.size());
			for(VendorProduct vendorProduct : dbVendorProductList){
				VendorResponse vendorResponse = new VendorResponse();
				LOGGER.debug("customer --vendor=="+vendorProduct.getCustomer().getEmailAddress());
				Customer vendor = vendorProduct.getCustomer();
				if(vendor != null) {
					VendorPopulator vendorPopulator = new VendorPopulator();
					vendorPopulator.populate(vendor, vendorResponse, null, null);
					vendorsDataForProduct.add(vendorResponse);
				}
			}
			vendorsList.setVendorsDataForProduct(vendorsDataForProduct);
		}catch(Exception e){
			LOGGER.error("error occured while retrieving vendors based on product id ="+productId+"---"+e.getMessage());
			//e.printStackTrace(System.out);
		}
		vendorsList.setStatus("OK");
		LOGGER.error("getProductVendors");
		return vendorsList;
	}
	@RequestMapping(value="/addWishListProductsToProductList", method = RequestMethod.POST) 
	@ResponseBody
	public VendorProductResponse addWishListProductsToProductList(@RequestBody VendorProductRequest vendorProductRequest ) throws Exception {

		LOGGER.debug("Entered addWishListProductsToProductList:");
		String vendorId = vendorProductRequest.getVendorId();
	
		Customer customer = customerService.getById(Long.parseLong(vendorId));

		List<String> productIds = vendorProductRequest.getProductId();

		VendorProductResponse vendorProductResponse = new VendorProductResponse(); 

		List<VendorProduct> vpList = new ArrayList<VendorProduct>();
		List<ProductsInfo> vList = new ArrayList<ProductsInfo>();

		for(String productId : productIds){
			
			VendorProduct vendorProduct = vendorProductService.getById(Long.parseLong(productId));

			
			ProductsInfo productsInfo = new ProductsInfo();
			
			vendorProduct.setCreatedDate(new Date());
			vendorProduct.setVendorWishListed(Boolean.FALSE);
			vendorProductService.update(vendorProduct);
			
			productsInfo.setProductId(vendorProduct.getId());
			productsInfo.setProductName(vendorProduct.getProduct().getProductDescription().getName());
			vpList.add(vendorProduct);
			vList.add(productsInfo);
		}
		LOGGER.debug("vpList:"+vpList.size());
		vendorProductService.save(vpList);
		LOGGER.debug("Added products");
		vendorProductResponse.setVenderId(vendorId);
		vendorProductResponse.setVendorProducts(vList);

		//sending email
		/*MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");  
		final Locale locale  = new Locale("en");
		String[] vendorName = {customer.getVendorAttrs().getVendorName()};
		Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username",locale));
		templateTokens.put(EmailConstants.EMAIL_VENDOR_ADD_PRODUCTS_TXT, messages.getMessage("email.vendor.addproducts.text",vendorName,locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));


		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.vendor.addproducts.text.subject",locale));
		email.setTo(merchantStore.getStoreEmailAddress());
		email.setTemplateName(VENDOR_ADD_PRODUCTS_TPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
*/
		LOGGER.debug("Ended addWishListProductsToProductList");
		return vendorProductResponse;
	}
	// Delete wishlisted products for both customer and vendor
	@RequestMapping(value="/deleteWishListProducts", method = RequestMethod.POST) 
	@ResponseBody
	public DeleteProductResponse deleteWishListProducts(@RequestBody VendorProductRequest vendorProductRequest ) throws Exception {
		
		LOGGER.debug("Entered deleteWishListProducts");
		
		DeleteProductResponse deleteProductResponse = new DeleteProductResponse(); 
		
		String vendorId = vendorProductRequest.getVendorId();
		
		try {
			
		Long longVendorId = new Long(vendorProductRequest.getVendorId());

		List<String> productIds = vendorProductRequest.getProductId();

		List<VendorProduct> vpList = new ArrayList<VendorProduct>();
		List<ProductsInfo> vList = new ArrayList<ProductsInfo>();

		for(String productId : productIds){
			
			Long longProductId = new Long(productId);
			
			List<VendorProduct> vendorProducts = vendorProductService.getByProductIdAndVendorId(longProductId,longVendorId);
			
			for(VendorProduct vendorProduct : vendorProducts){

			ProductsInfo productsInfo = new ProductsInfo();
			
			vendorProductService.delete(vendorProduct);
			
			productsInfo.setProductId(vendorProduct.getId());
			productsInfo.setProductName(vendorProduct.getProduct().getProductDescription().getName());
			vpList.add(vendorProduct);
			vList.add(productsInfo);
			}
		}
		
		LOGGER.debug("vpList:"+vpList.size());
		LOGGER.debug("Deleted wishlisted products");
		deleteProductResponse.setVenderId(vendorId);
		deleteProductResponse.setVendorProducts(vList);
		deleteProductResponse.setSuccessMessage("Successfully deleted product(s) from Wishlist");
		deleteProductResponse.setStatus("true");
		}catch(Exception e) {
			LOGGER.error("Error while deleting product(s) from Wishlist "+e.getMessage());
			deleteProductResponse.setErrorMessage("Error while deleting product(s) from Wishlist");
			deleteProductResponse.setStatus("false");
		}
		
		LOGGER.debug("Ended deleteWishListProducts");
		return deleteProductResponse;
	}
	// Method to delete products from product list
	@RequestMapping(value="/deleteProductsFromProductList", method = RequestMethod.POST) 
	@ResponseBody
	public DeleteProductResponse deleteProductsFromProductList(@RequestBody VendorProductRequest vendorProductRequest ) throws Exception {
		
		LOGGER.debug("Entered deleteProductsFromProductList");
	
		DeleteProductResponse deleteProductResponse = new DeleteProductResponse();
		
		String vendorId = vendorProductRequest.getVendorId();
		
		try {
			
		Long longVendorId = new Long(vendorProductRequest.getVendorId());

		List<String> productIds = vendorProductRequest.getProductId();

		List<VendorProduct> vpList = new ArrayList<VendorProduct>();
		List<ProductsInfo> vList = new ArrayList<ProductsInfo>();

		for(String productId : productIds){
			
			Long longProductId = new Long(productId);
			
			List<VendorProduct> vendorProducts = vendorProductService.getVendoProductsProductIdAndVendorId(longProductId,longVendorId);
			
			for(VendorProduct vendorProduct : vendorProducts){

			ProductsInfo productsInfo = new ProductsInfo();
			
			vendorProductService.delete(vendorProduct);
			
			productsInfo.setProductId(vendorProduct.getId());
			productsInfo.setProductName(vendorProduct.getProduct().getProductDescription().getName());
			vpList.add(vendorProduct);
			vList.add(productsInfo);
			}
		}
		LOGGER.debug("vpList:"+vpList.size());
		LOGGER.debug("Deleted products");
		
		deleteProductResponse.setVenderId(vendorId);
		deleteProductResponse.setVendorProducts(vList);
		deleteProductResponse.setSuccessMessage("Successfully deleted product(s) from ProductList");
		deleteProductResponse.setStatus("true");
		}catch(Exception e) {
			LOGGER.error("Error while deleting products from ProductList "+e.getMessage());
			deleteProductResponse.setErrorMessage("Error while deleting products from ProductList");
			deleteProductResponse.setStatus("false");
		}
		
		LOGGER.debug("Ended deleteProductsFromProductList");
		return deleteProductResponse;
	}
}
