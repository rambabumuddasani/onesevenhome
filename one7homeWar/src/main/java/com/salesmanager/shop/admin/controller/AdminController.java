package com.salesmanager.shop.admin.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.category.SubCategoryService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.price.ProductPriceService;
import com.salesmanager.core.business.services.catalog.product.type.ProductTypeService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.customer.testmonial.review.CustomerTestmonialService;
import com.salesmanager.core.business.services.historymanage.HistoryManagementService;
import com.salesmanager.core.business.services.homepage.offers.HomePageOffersService;
import com.salesmanager.core.business.services.image.brand.BrandImageService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.postrequirement.PostRequirementService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.services.MachineryPortfolioService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.business.vendor.VendorBookingService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.SubCategoryImage;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.description.ProductDescription;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.type.ProductType;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.CustomerTestimonial;
import com.salesmanager.core.model.customer.MachineryPortfolio;
import com.salesmanager.core.model.customer.VendorBooking;
import com.salesmanager.core.model.history.HistoryManagement;
import com.salesmanager.core.model.homepage.offers.HomePageOffers;
import com.salesmanager.core.model.image.brand.BrandImage;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.OrderTotal;
import com.salesmanager.core.model.order.orderproduct.OrderProduct;
import com.salesmanager.core.model.postrequirement.PostRequirement;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.country.CountryDescription;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.user.User;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.fileupload.services.StorageException;
import com.salesmanager.shop.fileupload.services.StorageService;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@CrossOrigin
public class AdminController extends AbstractController {
	
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	private static final String ADIMIN_APPROVE_PRODUCT_TMPL = "email_template_vendor_approve_products.ftl";
	private static final String ADIMIN_ADD_PRODUCT_TMPL = "email_template_admin_vendor_postrequirement.ftl";
	private static final String VENDOR_REGISTRATION_ADMIN_APPROVE_TMPL = "email_template_vendor_registration_admin_approve.ftl";
	@Inject
	private MerchantStoreService merchantStoreService;
	
	@Inject
	private UserService userService;

    @Inject
    private LanguageService  languageService;
    
    @Inject
    private CountryService countryService;
    
    @Inject
	@Named("passwordEncoder")
	private PasswordEncoder passwordEncoder;
    
    @Inject
	private ProductService productService;
    
    @Inject
    private ProductPriceService productPrice;
    
    @Inject
	CategoryService categoryService;
    
    @Inject
	private ProductTypeService productTypeService;
    
    /*@Inject
	private TaxClassService taxClassService;*/
    
    @Inject
	private ProductPriceUtils priceUtil;
    
    @Inject
	VendorProductService vendorProductService;
    
    @Inject
    private CustomerService customerService;
    
    @Inject
    private StorageService storageService;
    
    @Inject
    private SubCategoryService subCategoryService;
    
    @Inject
    private CustomerTestmonialService customerTestmonialService;
    
    @Inject
    private BrandImageService brandImageService;
    
    @Inject
	private EmailUtils emailUtils;
    
	@Inject
	private LabelUtils messages;
	
	@Inject
	EmailService emailService;
	
	@Inject
	PostRequirementService postRequirementService;
	
	@Inject
	HistoryManagementService historyManagementService;
    
	@Inject
	VendorBookingService vendorBookingService;
	
	@Inject
	MachineryPortfolioService machineryPortfolioService;
	
	@Inject
	private OrderService orderService;
	
	@Inject
	private HomePageOffersService homePageOffersService;
	
    // Admin update store address
	@RequestMapping(value="/admin/updatestore", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminUpdateStoreResponse updateMerchantStore(@RequestBody AdminUpdateStoreRequest adminUpdateStoreRequest) {
	    
		LOGGER.debug("Entered updateMerchantStore method");
		AdminUpdateStoreResponse adminUpdateStoreResponse=new AdminUpdateStoreResponse();
	    MerchantStore merchantStore=null;
		try {
			merchantStore = merchantStoreService.getByCode(adminUpdateStoreRequest.getStoreCode());
			if(merchantStore==null) {
				LOGGER.debug("Store is not found,unable to update");
				adminUpdateStoreResponse.setErrorMessage("Store is not found,unable to update");
				adminUpdateStoreResponse.setStatus(FALSE);
				return adminUpdateStoreResponse;
			}
		} catch (ServiceException e) {
			LOGGER.debug("Error in updating Store");
			adminUpdateStoreResponse.setErrorMessage("Error in updating Store");
			adminUpdateStoreResponse.setStatus(FALSE);
			return adminUpdateStoreResponse;
		}
		//update store
	    merchantStore.setStorename(adminUpdateStoreRequest.getStoreName());
	    merchantStore.setCode(adminUpdateStoreRequest.getStoreCode());
	    merchantStore.setStorephone(adminUpdateStoreRequest.getStorePhone());
	    merchantStore.setStoreEmailAddress(adminUpdateStoreRequest.getEmailAddress());
	    merchantStore.setStoreaddress(adminUpdateStoreRequest.getStoreAddress());
	    merchantStore.setStorecity(adminUpdateStoreRequest.getStoreCity());
	    merchantStore.setStorestateprovince(adminUpdateStoreRequest.getStoreState());
	    merchantStore.setStorepostalcode(adminUpdateStoreRequest.getStorePostalCode());
	   
	    Country storeCountry = null;
	    try {
	    	Language language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
			Country country = countryService.getCountryByCodeAndLang(adminUpdateStoreRequest.getStoreCountry(), language.getId());
			storeCountry = country;
		} catch (ServiceException e1) {
			LOGGER.debug("Error in updating Store");
			adminUpdateStoreResponse.setErrorMessage("Error in updating Store");
			adminUpdateStoreResponse.setStatus(FALSE);
			return adminUpdateStoreResponse;
		}
	    merchantStore.setCountry(storeCountry);
	  
	    try {
			merchantStoreService.update(merchantStore);
		} catch (ServiceException e) {
			LOGGER.debug("Error in updating store");
			e.printStackTrace();
			adminUpdateStoreResponse.setErrorMessage("Error in updating store");
			adminUpdateStoreResponse.setStatus(FALSE);
			return adminUpdateStoreResponse;
		}
	        LOGGER.debug("Store updated");;
	        adminUpdateStoreResponse.setSuccessMessage("Store updated successfully");
	        adminUpdateStoreResponse.setStatus(TRUE);
	        LOGGER.debug("Ended updateMerchantStore method");
	        return adminUpdateStoreResponse;
	    
    }
	
	// Display store address
	@RequestMapping(value="/admin/getStore", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StoreInfoResponse getStoreInfo(HttpServletRequest request) {
		LOGGER.debug("Entered getStoreInfo method");
		StoreInfoResponse storeInfoResponse=new StoreInfoResponse();
		try {
			//getting store
			MerchantStore merchantStore=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
	        System.out.println("merchantStore="+merchantStore);
	       
	        StoreInfo storeInfo = new StoreInfo();
	        storeInfo.setStoreName(merchantStore.getStorename());
	        storeInfo.setStoreAddress(merchantStore.getStoreaddress());
	        storeInfo.setEmailAddress(merchantStore.getStoreEmailAddress());
	        storeInfo.setStorePhone(merchantStore.getStorephone());
	        storeInfo.setStoreCity(merchantStore.getStorecity());
	        storeInfo.setStoreState(merchantStore.getStorestateprovince());
	        storeInfo.setStorePostalCode(merchantStore.getStorepostalcode());
	        
	        storeInfo.setStoreCode(merchantStore.getCode());
	        Language language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
	        Country country = countryService.getCountryByCodeAndLang(merchantStore.getCountry().getIsoCode(), language.getId() );
		    
	        List<CountryDescription> countryDescription = country.getDescriptions();
	        
	        for(CountryDescription countryDesc:countryDescription){
	        	String countryName = countryDesc.getName();
	        	storeInfo.setStoreCountry(countryName);
	        }
	        
	        User user = userService.getById(1l);
	        storeInfo.setAdminName(user.getAdminName());
	        storeInfo.setLastAccess(user.getLastAccess());
	        storeInfoResponse.setStoreInfo(storeInfo);
	        
		} catch (ServiceException e) {
			LOGGER.error("Error while getting store info"+e.getMessage());
		
		}
		LOGGER.info("Ended getStoreInfo method");
		    return storeInfoResponse;
		
	}
	// Admin edit details 
	@RequestMapping(value="/admin/update", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public EditUserAdminResponse updateAdmin(@RequestBody EditUserAdminRequest editUserAdminRequest)
        throws Exception {
		    LOGGER.debug("Entered updateAdmin");
		    EditUserAdminResponse editUserAdminResponse = new EditUserAdminResponse();
		    String stringId = editUserAdminRequest.getId();
		    Long longId = Long.parseLong(stringId);
		    //Getting admin by id
			User dbUser = userService.getById(longId);
			// Checking admin null
			if(dbUser==null) {
				LOGGER.debug("Admin is null for this id: "+longId);
				editUserAdminResponse.setErrorMessage("Admin is null for this id: "+longId);
				editUserAdminResponse.setSucessMessage(FALSE);
				return editUserAdminResponse;
			}
			// Update admin details
			dbUser.setFirstName(editUserAdminRequest.getFirstName());
			dbUser.setLastName(editUserAdminRequest.getLastName());
			//Language  language = languageService.getByCode(editUserAdminRequest.getDefaultLang());
			//dbUser.setDefaultLanguage(language);
			dbUser.setAdminName(editUserAdminRequest.getUserName());
			dbUser.setAdminEmail(editUserAdminRequest.getEmail());
			MerchantStore store = merchantStoreService.getByCode(editUserAdminRequest.getStoreCode());
			dbUser.setMerchantStore(store);
			// Saving admin details
			userService.saveOrUpdate(dbUser);
			LOGGER.debug("Admin profile updated");
			editUserAdminResponse.setSucessMessage("Admin profile updated successfully");
			editUserAdminResponse.setStatus(TRUE);
			
	        return editUserAdminResponse;
		
		
	}
	// Get list of admin
	@RequestMapping(value="/admin/list", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminListResponse getAdminList() throws Exception {
		LOGGER.debug("Entered getAdminList");
		AdminListResponse  adminListResponse = new AdminListResponse();
		List<UserVO> userList = new ArrayList<UserVO>();
		MerchantStore store = merchantStoreService.getByCode(MerchantStore.DEFAULT_STORE);
		//Getting list of admin for the store
	    List<User> users = userService.listByStore(store);
	    for(User user:users) {
	       UserVO userVO = new UserVO();
	       userVO.setId(user.getId());
	       userVO.setAdminName(user.getAdminName());
	       userVO.setStoreCode(user.getMerchantStore().getCode());
	       userVO.setEmail(user.getAdminEmail());
	       userVO.setFirstName(user.getFirstName());
	       userVO.setLastName(user.getLastName());
	       //userVO.setDefaultLang(user.getDefaultLanguage());
	     
	       userList.add(userVO);
	    }
	   
	    adminListResponse.setAdminList(userList);
	    LOGGER.debug("Ended getAdminList");
		return adminListResponse;
		
	}
	
	// Admin password update
	@RequestMapping(value="/admin/updatepassword", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UpdatePasswordResp changePassword(@RequestBody UpdatePasswordReq updatePasswordReq) throws Exception {
		LOGGER.info("Entered changePassword");
		UpdatePasswordResp updatePasswordResp = new UpdatePasswordResp();
		String stringId = updatePasswordReq.getId();
		Long longId = Long.parseLong(stringId);
		//User dbUser = userService.getById(longId);
		User dbUser = userService.getByEmail(updatePasswordReq.getEmailAddress());
		if(dbUser==null) {
			LOGGER.debug("Admin is not exist for this emailaddress: "+updatePasswordReq.getEmailAddress());
			updatePasswordResp.setErrorMessage("Admin is not exist for this emailaddress: "+updatePasswordReq.getEmailAddress());
			updatePasswordResp.setStatus(FALSE);
			return updatePasswordResp;
		}
		//encoding password and update password
		String pass = passwordEncoder.encode(updatePasswordReq.getNewPassword());
		dbUser.setAdminPassword(pass);
		userService.update(dbUser);	
		LOGGER.debug("Password updated");
		updatePasswordResp.setSuccessMessage("Password updated successfully");
		updatePasswordResp.setStatus(TRUE);
		return updatePasswordResp;
	}
	
	// Admin add new products, feature products, recommended and recent bought to his store
	@RequestMapping(value="/admin/addOrRemove", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AddProductResp addOrRemoveProducts(@RequestBody AddProductReq addProductReq) throws Exception {
		LOGGER.debug("Entered addOrRemoveProducts");
		AddProductResp addProductResp = new AddProductResp();
		String productId = addProductReq.getProductId();
		Long longId = Long.parseLong(productId);
	    Product dbProduct = productService.getByProductId(longId);
	    if(dbProduct==null) {
	    	LOGGER.debug("Product not found");
	    	addProductResp.setErrorMessage("Product is not present");
	    }
	    MerchantStore store=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
	    com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		//List<ProductDescription> descriptions = new ArrayList<ProductDescription>();

			//Product dbProduct = productService.getById(productId);
			product.setProduct(dbProduct);
			//Set<ProductDescription> productDescriptions = dbProduct.getDescriptions();
			
			for(ProductImage image : dbProduct.getImages()) {
				if(image.isDefaultImage()) {
					product.setProductImage(image);
					break;
				}

			}
	    // Getting product availabilities
	    ProductAvailability productAvailability = null;
		ProductPrice pPrice = null;
	    Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
	    if(availabilities!=null && availabilities.size()>0) {
			
			for(ProductAvailability availability : availabilities) {
				if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
					productAvailability = availability;
					Set<ProductPrice> prices = availability.getPrices();
					for(ProductPrice price : prices) {
						if(price.isDefaultPrice()) {
							pPrice = price;
							product.setProductPrice(priceUtil.getAdminFormatedAmount(store, pPrice.getProductPriceAmount()));
						}
						//checking for featureproduct
						if(addProductReq.getTitle().equals("featureProduct")) {
							if(addProductReq.getStatus().equals("Y")) { 
								price.setFeaturedProduct("Y");
								productPrice.saveOrUpdate(price);
								addProductResp.setStatusMessage("Product is added to feature product");
							}
							else {
								price.setFeaturedProduct("N");
								productPrice.saveOrUpdate(price);
								addProductResp.setStatusMessage("Product is removed from feature product");
						   }
						}
						//checking new product
						if(addProductReq.getTitle().equals("newProduct")) {
							if(addProductReq.getStatus().equals("Y")) { 
								price.setNewProduct("Y");
								productPrice.saveOrUpdate(price);
								addProductResp.setStatusMessage("Product is added to New Product");
							}
							else {
								price.setNewProduct("N");
								productPrice.saveOrUpdate(price);
								addProductResp.setStatusMessage("Product is removed from New Product");
						   }
						}
						//checking recommended product
						if(addProductReq.getTitle().equals("recommendedProduct")) {
							if(addProductReq.getStatus().equals("Y")) { 
								price.setRecommendedProduct("Y");
								productPrice.saveOrUpdate(price);
								addProductResp.setStatusMessage("Product is added to Recommended Product");
							}
							else {
								price.setRecommendedProduct("N");
								productPrice.saveOrUpdate(price);
								addProductResp.setStatusMessage("Product is removed Recommended Product");
						   }
						}
						//checking recent bought
						if(addProductReq.getTitle().equals("recentBought")) {
							if(addProductReq.getStatus().equals("Y")) { 
								price.setRecentlyBought("Y");
								productPrice.saveOrUpdate(price);
								addProductResp.setStatusMessage("Product is added to Recent Product");
							}
							else {
								price.setRecentlyBought("N");
								productPrice.saveOrUpdate(price);
								addProductResp.setStatusMessage("Product is removed Recent Product");
						   }
						}
				}
			}
		}
		
	  }
	    LOGGER.debug("Ended addOrRemoveProducts");
	    return addProductResp;
	}
	
/*
	private boolean isFeatureProduct(String isfeatureProduct) {
		if(isfeatureProduct.equals("Y")) {
			return true;
		}
		return false;
	}*/
	
    // Show new products, feature products, recommended and recent bought products in admin GUI
	@RequestMapping(value="/admin/categories/{categoryCode}/{title}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResponse getProductForCatAndTitle(@PathVariable String categoryCode,@PathVariable String title,
			@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		LOGGER.debug("Entered getProductForCatAndTitle method ");
		
		AdminProductResponse adminProductResponse = new AdminProductResponse();
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		categoryCode = categoryCode.replaceAll("_", " ");
		Category category = categoryService.getByCategoryCode(categoryCode);
		List<AdminProductResponse> responses = new ArrayList<AdminProductResponse>();
		List<Product> tdProducts = productService.getTodaysDeals();
		Map<Long,Product> todaysDealsMap = null;
		List<Product> dbProducts = null;
		responses = invokeProductsData(todaysDealsMap, categoryCode,responses,adminProductResponse,tdProducts,title);
		if(category.getParent() == null){
			List<Category> childCatList = category.getCategories();
			
			for(Category childCat:childCatList){
				responses = invokeProductsData(todaysDealsMap, childCat.getCode(),responses,adminProductResponse,tdProducts,title);
				break;
			}
		}
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(responses == null || responses.isEmpty() || responses.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(responses);
			LOGGER.debug("Ended getProductForCatAndTitle method ");
			return paginatedResponse;
		}
    	List<AdminProductResponse> paginatedResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
		return paginatedResponse;
		
	}

	public List<AdminProductResponse> invokeProductsData(Map<Long,Product> todaysDealsMap,
			String categoryId,List<AdminProductResponse> responses,
			AdminProductResponse adminProductResponse,List<Product> tdProducts,String title) throws Exception {
		LOGGER.debug("Entered invokeProductsData");
		todaysDealsMap = new HashMap<Long, Product>();
		List<Product> dbProducts = productService.getProductsListByCategory(categoryId);
		for(Product tdproduct:tdProducts){
			todaysDealsMap.put(tdproduct.getId(), tdproduct);
		}
		for(Product product:dbProducts) {
			if(todaysDealsMap.containsKey(product.getId())){
				adminProductResponse = getProductDetails(product,true, title);
			} else {
				adminProductResponse = getProductDetails(product,false,title);
			}
			responses.add(adminProductResponse);
		}
		LOGGER.debug("Ended invokeProductsData");
		return responses;
	}
public AdminProductResponse getProductDetails(Product dbProduct,boolean isSpecial,String title) throws Exception {
		LOGGER.debug("Entered getProductDetails");
		AdminProductResponse adminProductResponse = new AdminProductResponse();
		adminProductResponse.setProductId(dbProduct.getId());
		MerchantStore store=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		List<ProductType> productTypes = productTypeService.list();
		
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();
			
			product.setProduct(dbProduct);
			Set<ProductDescription> productDescriptions = dbProduct.getDescriptions();
			
			for(ProductImage image : dbProduct.getImages()) {
				if(image.isDefaultImage()) {
					product.setProductImage(image);
					break;
				}

			}
			
			ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
			
			Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
						productAvailability = availability;
						Set<ProductPrice> prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
								product.setProductPrice(priceUtil.getAdminFormatedAmount(store, productPrice.getProductPriceAmount()));
							}
							if(title.equals("featureProduct")) {
								if(price.getFeaturedProduct().equals("Y")) { 
									adminProductResponse.setFeatureProduct("Y");
								}
								else {
									adminProductResponse.setFeatureProduct("N");
								}
							}
							if(title.equals("newProduct")) {
								if(price.getNewProduct().equals("Y")) { 
									adminProductResponse.setNewProduct("Y");
								}
								else {
									adminProductResponse.setNewProduct("N");
								}
							}
							if(title.equals("recommendedProduct")) {
								if(price.getRecommendedProduct().equals("Y")) { 
									adminProductResponse.setRecommendedProduct("Y");
								}
								else {
									adminProductResponse.setRecommendedProduct("N");
								}
							}
							if(title.equals("recentBought")) {
								if(price.getRecentlyBought().equals("Y")) { 
									adminProductResponse.setRecentBought("Y");
								}
								else {
									adminProductResponse.setRecentBought("N");
								}
							
							}
						}
					}
				}
			}
			
			if(productAvailability==null) {
				productAvailability = new ProductAvailability();
			}
			
			if(productPrice==null) {
				productPrice = new ProductPrice();
			}
			
			product.setAvailability(productAvailability);
			product.setPrice(productPrice);
			product.setDescriptions(descriptions);
			
			
			product.setDateAvailable(DateUtil.formatDate(dbProduct.getDateAvailable()));
			if(product.getProductImage() != null)
			adminProductResponse.setImageURL(product.getProductImage().getProductImageUrl());
			if(isSpecial) {
				adminProductResponse.setProductPrice(productPrice.getProductPriceAmount());
				adminProductResponse.setProductDiscountPrice(productPrice.getProductPriceSpecialAmount());
				adminProductResponse.setDiscountPercentage(getDiscountPercentage(productPrice));
				adminProductResponse.setProductPriceSpecialEndDate(productPrice.getProductPriceSpecialEndDate());
				//productResponse.setProductPriceSpecialEndTime(productPrice.getProductPriceSpecialEndDateTime());
			}
			else
				adminProductResponse.setProductPrice(productPrice.getProductPriceAmount());
			
			adminProductResponse.setProductDescription(dbProduct.getProductDescription().getDescription());
			adminProductResponse.setProductName(dbProduct.getProductDescription().getName());
			//productResponse.setVendorName(dbProduct.getManufacturer().getCode());
			
			/*Set<ManufacturerDescription> manufacturerDescription =  dbProduct.getManufacturer().getDescriptions();
			for(ManufacturerDescription description:manufacturerDescription){
				adminProductResponse.setVendorName(description.getName());
				adminProductResponse.setVendorLocation(description.getTitle());
			}*/
		LOGGER.debug("Ended getProductDetails");	
		return adminProductResponse;
	}

    private String getDiscountPercentage(ProductPrice productPrice){
    LOGGER.debug("Entered getDiscountPercentage");
	BigDecimal discount = new BigDecimal(0);
	DecimalFormat df = new DecimalFormat();
	df.setMaximumFractionDigits(2); //Sets the maximum number of digits after the decimal point
	df.setMinimumFractionDigits(0); //Sets the minimum number of digits after the decimal point
	df.setGroupingUsed(false); //If false thousands separator such ad 1,000 wont work so it will display 1000
	if(productPrice.getProductPriceAmount().intValue() > 0 && productPrice.getProductPriceSpecialAmount().intValue() > 0) {
		discount = productPrice.getProductPriceAmount().subtract(productPrice.getProductPriceSpecialAmount());
		discount = discount.multiply(new BigDecimal(100));
		discount = discount.divide(productPrice.getProductPriceAmount(),2,4);
		return df.format(discount);
	}
	return df.format(discount);

}
    // Get products for vendor who added products to product list
    @RequestMapping(value="/admin/vendor/products/{vendorId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResponse getAdminVendorProducts(
			@PathVariable String vendorId, 
			@RequestParam(value="pageNumber", defaultValue = "1") int page ,
            @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
    	
    	LOGGER.debug("Entered getAdminVendorProducts");
    	
    	PaginatedResponse paginatedResponse = new PaginatedResponse();
    	
    	Long  vId = Long.parseLong(vendorId);
    	
    	try {
    	List<VendorProduct> vendorProducts = vendorProductService.findProductsByVendor(vId);
    	if(vendorProducts==null) {
    		paginatedResponse.setErrorMsg("Vendor products not found");
    		return paginatedResponse;
    	}
    	
    	List<VendorProductVO> vproductList = new ArrayList<VendorProductVO>();
    	for(VendorProduct vendorProduct : vendorProducts) {
    		VendorProductVO vendorProductVO = new VendorProductVO();
    		
    		vendorProductVO.setVendorProductId(vendorProduct.getId());
    		vendorProductVO.setVendorId(vendorProduct.getCustomer().getId());
    		vendorProductVO.setStatus(vendorProduct.isAdminActivated());
    		
    		if (!(vendorProduct.getCustomer().getVendorAttrs().getVendorName().equals(null))){
    		vendorProductVO.setVendorName(vendorProduct.getCustomer().getVendorAttrs().getVendorName());
    		}
    		vendorProductVO.setProductId(vendorProduct.getProduct().getId());
    		vendorProductVO.setProductName(vendorProduct.getProduct().getProductDescription().getName());
    		vendorProductVO.setProductDescription(vendorProduct.getProduct().getProductDescription().getDescription());
    		vendorProductVO.setImageURL(vendorProduct.getProduct().getProductImage().getProductImageUrl());
    		//vendorProductVO.setVendorMobile(vendorProduct.getCustomer().getVendorAttrs().getVendorMobile());
    		vendorProductVO.setVendorTelephone(vendorProduct.getCustomer().getVendorAttrs().getVendorTelephone());
    		vendorProductVO.setHouseNumber(vendorProduct.getCustomer().getVendorAttrs().getVendorOfficeAddress());
    		vendorProductVO.setStreet(vendorProduct.getCustomer().getBilling().getAddress());
    		vendorProductVO.setCity(vendorProduct.getCustomer().getBilling().getCity());
    		vendorProductVO.setArea(vendorProduct.getCustomer().getArea());
    		vendorProductVO.setState(vendorProduct.getCustomer().getBilling().getState());
    		vendorProductVO.setPinCode(vendorProduct.getCustomer().getBilling().getPostalCode());
    		vproductList.add(vendorProductVO);
    	}
    	
    	PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, vproductList.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(vproductList == null || vproductList.isEmpty() || vproductList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(vproductList);
			return paginatedResponse;
		}
		
    	List<VendorProductVO> paginatedResponses = vproductList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	
    	}catch(Exception e) {
    		e.printStackTrace();
    		LOGGER.error("Error while listing vendor products "+e.getMessage());
    		paginatedResponse.setErrorMsg("Error while listing vendor products");
    	}
    	LOGGER.debug("Ended getAdminVendorProducts");
    	
    	return paginatedResponse;
    	
    }
    
    // get vendor products with pagination
    @RequestMapping(value="/admin/vendor/products", method = RequestMethod.GET)
	@ResponseBody
	public PaginatedResponse getVendorProducts(@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
    	LOGGER.debug("Entered getVendorProducts");
    	PaginatedResponse paginatedResponse = new PaginatedResponse();

    	// Get vendor products are added by vendors to product list
    	List<VendorProduct> vendorProducts = vendorProductService.getVendorProducts();
    	if(vendorProducts==null) {
    		paginatedResponse.setErrorMsg("Vendor products not found");
    		return paginatedResponse;
    	}
    	
    	List<VendorProductVO> vproductList = new ArrayList<VendorProductVO>();
    	for(VendorProduct vendorProduct : vendorProducts) {
    		
    		VendorProductVO vendorProductVO = new VendorProductVO();
    		vendorProductVO.setVendorProductId(vendorProduct.getId());
    		vendorProductVO.setVendorId(vendorProduct.getCustomer().getId());
    		
    		if (!(vendorProduct.getCustomer().getVendorAttrs().getVendorName().equals(null))){
    		vendorProductVO.setVendorName(vendorProduct.getCustomer().getVendorAttrs().getVendorName());
    		}
    		vendorProductVO.setProductId(vendorProduct.getProduct().getId());
    		vendorProductVO.setProductName(vendorProduct.getProduct().getProductDescription().getName());
    		vendorProductVO.setProductDescription(vendorProduct.getProduct().getProductDescription().getDescription());
    		vendorProductVO.setImageURL(vendorProduct.getProduct().getProductImage().getProductImageUrl());
    		//vendorProductVO.setVendorMobile(vendorProduct.getCustomer().getVendorAttrs().getVendorMobile());
    		vendorProductVO.setVendorTelephone(vendorProduct.getCustomer().getVendorAttrs().getVendorTelephone());
    		vendorProductVO.setHouseNumber(vendorProduct.getCustomer().getVendorAttrs().getVendorOfficeAddress());
    		vendorProductVO.setStreet(vendorProduct.getCustomer().getBilling().getAddress());
    		vendorProductVO.setCity(vendorProduct.getCustomer().getBilling().getCity());
    		vendorProductVO.setArea(vendorProduct.getCustomer().getArea());
    		vendorProductVO.setState(vendorProduct.getCustomer().getBilling().getState());
    		vendorProductVO.setPinCode(vendorProduct.getCustomer().getBilling().getPostalCode());
    		//vendorProductVO.setDescription(vendorProduct.getProduct().getProductDescription().getDescription());
    		vproductList.add(vendorProductVO);
    	}
    	
    	//Pagination
    	PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, vproductList.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(vproductList == null || vproductList.isEmpty() || vproductList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(vproductList);
			return paginatedResponse;
		}
    	List<VendorProductVO> paginatedResponses = vproductList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	LOGGER.debug("Ended getVendorProducts");
		return paginatedResponse;
    }
    
    // Admin approve products
    @RequestMapping(value="/admin/products/activate", method = RequestMethod.POST)
	@ResponseBody
	public ActivateProductResponse adminApproveProducts(@RequestBody ActivateProductRequest activateProductRequest) throws Exception {
    	LOGGER.debug("Entered adminApproveProducts");
    	ActivateProductResponse activateProductResponse = new ActivateProductResponse();
    	Long vendorProductId = activateProductRequest.getVendorProductId();
    	
    	//getting vendor product to be approved by by admin
	    VendorProduct vendorProduct = vendorProductService.getVendorProductById(vendorProductId);
    	if(vendorProduct==null) {
    		activateProductResponse.setErrorMesg("Vendor product not found");
    		activateProductResponse.setStatus(FALSE);
    		return activateProductResponse;
    	}
    	// Approving and updating vendor product
    	vendorProduct.setAdminActivatedDate(new Date());
    	vendorProduct.setAdminActivated(activateProductRequest.isStatus());
    	vendorProductService.update(vendorProduct);
    	if(vendorProduct.isAdminActivated()==true) {
    		activateProductResponse.setSuccessMsg("Approved");
    		activateProductResponse.setStatus(TRUE);
    	} else {
    		activateProductResponse.setSuccessMsg("Declined");
    		activateProductResponse.setStatus(TRUE);
    	}
    	final Locale locale  = new Locale("en");
    	MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");
    	String url ="http://rainiersoft.com/clients/onesevenhome/";
    	String imageURL = url.concat(vendorProduct.getProduct().getProductImage().getProductImageUrl());
    	ProductAvailability productAvailability = null;
		ProductPrice productPrice = null;
		Set<ProductAvailability> availabilities = vendorProduct.getProduct().getAvailabilities();
		if(availabilities!=null && availabilities.size()>0) {
			
			for(ProductAvailability availability : availabilities) {
				if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
					productAvailability = availability;
					Set<ProductPrice> prices = availability.getPrices();
					for(ProductPrice price : prices) {
						if(price.isDefaultPrice()) {
							productPrice = price;
						}
					}
				}
			}
		}
		
    	Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, vendorProduct.getCustomer().getVendorAttrs().getVendorName());
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_NAME, vendorProduct.getProduct().getProductDescription().getName());
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_APPROVED_STATUS, activateProductResponse.getSuccessMsg() );
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_IMAGE_URL, imageURL);
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_PRICE, productPrice.getProductPriceAmount().toString());
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_DESCRIPTION, vendorProduct.getProduct().getProductDescription().getDescription());
		
		
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.vendor.product.approve.status",locale));
		email.setTo(vendorProduct.getCustomer().getEmailAddress());
		email.setTemplateName(ADIMIN_APPROVE_PRODUCT_TMPL);
		email.setTemplateTokens(templateTokens);


		
		emailService.sendHtmlEmail(merchantStore, email);
		//vendorProductService.delete(vendorProduct);
    	LOGGER.debug("Ended adminApproveProducts");
    	
	    return activateProductResponse;
    	
    }
   
 
    
    // Admin can show a product under deal of day by updating start date,end date
    @RequestMapping(value="/admin/dealOfDay", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminDealOfDayResponse adminDealOfDay(@RequestBody AdminDealOfDayRequest adminDealOfDayReq) throws Exception {
    	LOGGER.debug("Entered adminDealOfDay");
    	AdminDealOfDayResponse adminDealOfDayResponse = new AdminDealOfDayResponse();
    	
    	// Get the product details based on the AdminDeal of the day request 
    	// This will get the product details, which need to be further updated with prices.
    	Long productId = adminDealOfDayReq.getProductId();
    	Date startDate = adminDealOfDayReq.getProductPriceSpecialStartDate();
    	Date endDate = adminDealOfDayReq.getProductPriceSpecialEndDate();
    	String status = adminDealOfDayReq.getStatus();
    	/*String status = adminDealOfDayReq.getStatus();
    	String title = adminDealOfDayReq.getTitle();*/
    	
    	//Get the complete product details.
    	Product dbProduct = productService.getByProductId(productId);
    	
    	if(dbProduct==null) {
    		LOGGER.debug("Deal Of Day product is not found");
    		adminDealOfDayResponse.setErrorMesg("Deal Of Day product is not found");
    		adminDealOfDayResponse.setStatus("false");
    		return adminDealOfDayResponse;
	    }
    	
    	MerchantStore store=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
	    
    	com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
    	
     	product.setProduct(dbProduct);
    	
	    ProductAvailability productAvailability = null;
		ProductPrice pPrice = null;
	    Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
	    
	    if(availabilities!=null && availabilities.size()>0)
	    {
			
			for(ProductAvailability availability : availabilities)
			{
				if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS))
				 {
					productAvailability = availability;
					
					Set<ProductPrice> prices = availability.getPrices();
					for(ProductPrice price : prices)
					{
						if(price.isDefaultPrice()) {
							pPrice = price;
							product.setProductPrice(priceUtil.getAdminFormatedAmount(store, pPrice.getProductPriceAmount()));
						}
						
							if(adminDealOfDayReq.getStatus().equals("Y")) {
								//Checking DealOfDay product which is available in the given date  
								List<Product> dodProducts = productService.getDealOfDay(startDate,endDate,adminDealOfDayReq.getStatus());
								
								if(dodProducts!=null && !(dodProducts.isEmpty())) {
									adminDealOfDayResponse.setErrorMesg("Please provide different date to set Deal Of Day");
									adminDealOfDayResponse.setStatus("false");
									return adminDealOfDayResponse;
								}
								else {
							    // if no product is available in the specified date, then update the product in dealofday 
								price.setDealOfDay("Y");
								price.setProductPriceSpecialStartDate(adminDealOfDayReq.getProductPriceSpecialStartDate());
								price.setProductPriceSpecialEndDate(adminDealOfDayReq.getProductPriceSpecialEndDate());
								productPrice.saveOrUpdate(price);
								adminDealOfDayResponse.setSuccessMsg("Deal Of Day is set successfully");
								adminDealOfDayResponse.setStatus("true");
								try {
								HistoryManagement historyManagement = new HistoryManagement();
								historyManagement.setProductId(dbProduct.getId());
								historyManagement.setProductName(dbProduct.getProductDescription().getName());
								historyManagement.setProductPrice(price.getProductPriceAmount());
								historyManagement.setProductDiscountPrice(price.getProductPriceSpecialAmount());
								historyManagement.setProductPriceStartDate(price.getProductPriceSpecialStartDate());
								historyManagement.setProductPriceEndDate(price.getProductPriceSpecialEndDate());
								historyManagement.setEnableFor("DOD");
								historyManagementService.save(historyManagement);
								}catch(Exception e) {
									LOGGER.error("Error in history management "+e.getMessage());
								}
								}
							}
							if(adminDealOfDayReq.getStatus().equals("N")) {
								price.setDealOfDay("N");
								productPrice.saveOrUpdate(price);
								adminDealOfDayResponse.setSuccessMsg("Product is disabled from Deal Of Day");
								adminDealOfDayResponse.setStatus("true");
							}		
				}
			}
		}
			
     }
	    LOGGER.debug("Ended adminDealOfDay");
		return adminDealOfDayResponse;
  }
    
    // Admin Deal Management
    // Retrieving all deals from current date and for particular date
    @RequestMapping(value="/admin/getDeals", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminTodaysDeals getDeals(@RequestBody AdminDealRequest adminDealRequest, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		
		LOGGER.debug("Entered getDeals");
		
		AdminTodaysDeals todaysDeals = new AdminTodaysDeals();
		AdminDealProductResponse productResponse = new AdminDealProductResponse();
		List<Product> tdProducts = null; 
        if(adminDealRequest.getStatus().equals("ALL")) {
        //Retrieves all Deals starting from current date
		List<Product> dbProducts = productService.getAdminTodaysDeals();
        // If no products found from db returning messsage
		if(dbProducts==null) {
    		todaysDeals.setErrorMesg("No Deals found");
    		return todaysDeals;
    	}
		
		tdProducts = dbProducts;
        }else {
        	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        	String cunvertCurrentDate = adminDealRequest.getStatus();
        	Date date = new Date();
        	date = df.parse(cunvertCurrentDate);
        	
        	//Retrieves Deals for the particular date
        	List<Product> dbProducts = productService.getTodaysDeals(date);
        	// If no products found from db for particular date returning messsage
        	if(dbProducts==null) {
        		todaysDeals.setErrorMesg("No Deals found");
        		return todaysDeals;
        	}
        	tdProducts = dbProducts;
        }
       
        // Getting product details
		List<AdminDealProductResponse> responses = new ArrayList<AdminDealProductResponse>();
		for(Product product:tdProducts) {
			productResponse = getProductDetails(product,true);
			responses.add(productResponse);
		}
		if(responses == null || responses.isEmpty() || responses.size() < page){
			todaysDeals.setTodaysDealsData(responses);
			return todaysDeals;
		}
	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	todaysDeals.setPaginationData(paginaionData);
		List<AdminDealProductResponse> paginatedProdResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		todaysDeals.setTodaysDealsData(paginatedProdResponses);
		LOGGER.debug("Ended getDeals");
        return todaysDeals;
    }   
	
   
public AdminDealProductResponse getProductDetails(Product dbProduct,boolean isSpecial) throws Exception {
		
		LOGGER.debug("Entered getProductDetails");
		
		AdminDealProductResponse productResponse = new AdminDealProductResponse();
		try {
		productResponse.setProductId(dbProduct.getId());
		MerchantStore store=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();
			
			product.setProduct(dbProduct);
			
			ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
			//Getting Product Availabilities for the product retrieved from which we will get product price
			Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
						productAvailability = availability;
						Set<ProductPrice> prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
								product.setProductPrice(priceUtil.getAdminFormatedAmount(store, productPrice.getProductPriceAmount()));
							}
							productResponse.setProductPriceSpecialStartDate(price.getProductPriceSpecialStartDate());
							productResponse.setProductPriceSpecialEndDate(price.getProductPriceSpecialEndDate());
						}
					}
				}
			}
			
			if(productAvailability==null) {
				//productAvailability = new ProductAvailability();
				productResponse.setErrorMesg("Product is not available");
			}
					
			product.setAvailability(productAvailability);
			product.setPrice(productPrice);
			product.setDescriptions(descriptions);
			
			product.setDateAvailable(DateUtil.formatDate(dbProduct.getDateAvailable()));	
			productResponse.setProductName(dbProduct.getProductDescription().getName());
			
		}catch(Exception e){
			LOGGER.error("Error while getting product details"+e.getMessage());
			productResponse.setErrorMesg("Error while getting product details"+e.getMessage());
		}
		LOGGER.debug("Ended getProductDetails");
		return productResponse;
	}
    /*
     * Admin Deal Management
     * Admin update a deal or remove a deal
     */
    @RequestMapping(value="/admin/deals/updateorremove", method = RequestMethod.POST)
    @ResponseBody
    public DealUpdateOrRemoveResponse adminDealUpdateOrRemove(@RequestBody DealUpdateOrRemoveRequest dealUpdateOrRemoveRequest) throws Exception {
    LOGGER.debug("Entered adminDealUpdateOrRemove");
	DealUpdateOrRemoveResponse dealUpdateOrRemoveResponse = new DealUpdateOrRemoveResponse();
	Long productId = dealUpdateOrRemoveRequest.getProductId();
	
    // getting product from db
    Product dbProduct = productService.getByProductId(productId);
	
	if(dbProduct==null) {
		dealUpdateOrRemoveResponse.setErrorMesg("Product is not found");
		dealUpdateOrRemoveResponse.setStatus("false");
		return dealUpdateOrRemoveResponse;
    }
	
	com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
	
 	product.setProduct(dbProduct);
	
    ProductAvailability productAvailability = null;
	ProductPrice pPrice = null;
    Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
    
    if(availabilities!=null && availabilities.size()>0)
    {
		
		for(ProductAvailability availability : availabilities)
		{
			if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS))
			 {
				productAvailability = availability;
				
				Set<ProductPrice> prices = availability.getPrices();
				for(ProductPrice price : prices)
				{
					// if status is true, updating the product 
					if(dealUpdateOrRemoveRequest.isStatus()) {
					Date startDate = dealUpdateOrRemoveRequest.getProductPriceSpecialStartDate();
				    Date endDate = dealUpdateOrRemoveRequest.getProductPriceSpecialEndDate();
					price.setProductPriceSpecialStartDate(startDate);
					price.setProductPriceSpecialEndDate(endDate);
					productPrice.saveOrUpdate(price);
					
					dealUpdateOrRemoveResponse.setSuccessMsg("Deal Updated successfully");
					dealUpdateOrRemoveResponse.setStatus("true");
					try {
					HistoryManagement historyManagement = new HistoryManagement();
					historyManagement.setProductId(dbProduct.getId());
					historyManagement.setProductName(dbProduct.getProductDescription().getName());
					historyManagement.setProductPrice(price.getProductPriceAmount());
					historyManagement.setProductDiscountPrice(price.getProductPriceSpecialAmount());
					historyManagement.setProductPriceStartDate(price.getProductPriceSpecialStartDate());
					historyManagement.setProductPriceEndDate(price.getProductPriceSpecialEndDate());
					historyManagement.setEnableFor("TD");
					historyManagementService.save(historyManagement);
					}catch(Exception e) {
						LOGGER.error("Error in history management "+e.getMessage());
					}
					}
					// if status is false removing the product from todaysDeals 
					else {
						price.setProductPriceSpecialStartDate(null);
						price.setProductPriceSpecialEndDate(null);
						productPrice.saveOrUpdate(price);
						dealUpdateOrRemoveResponse.setSuccessMsg("Deal Removed");
						dealUpdateOrRemoveResponse.setStatus("true");
					}
			}
		}
	}
		
 }
    LOGGER.debug("Ended adminDealUpdateOrRemove");
     return dealUpdateOrRemoveResponse;

}
   //Retrieval of subcategory images
    @RequestMapping(value="/getAllSubCatImages", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminSubCatImgResponse getAllSubCatImages() throws Exception {
		LOGGER.debug("Entered getAllSubCatImages");
    	AdminSubCatImgResponse adminSubCatImgResponse = new AdminSubCatImgResponse();    	
		Map<String,List<SubCategoryImageVO>> parentMap = new HashMap<String, List<SubCategoryImageVO>>();

    	//Getting subCategoryImages
        List<SubCategoryImage> subCategoryImages = subCategoryService.getAllSubCategoryImage();
        for(SubCategoryImage subCategoryImage:subCategoryImages) {
        	String name = subCategoryImage.getCategory().getDescription().getName();
        	List<SubCategoryImageVO> subCategoryImageVOList = null;
        	// Checking subcatimg contains parent, if it contains parent then adding  the parent name
        	// and subcat name, imageurl to list and pushing the info into parentMap
        	if(subCategoryImage.getCategory().getParent() != null) {
        		 Category parentCategoryFullObj = categoryService.getById(subCategoryImage.getCategory().getParent().getId());
        		 String parentCatName = parentCategoryFullObj.getDescription().getName();
				if(parentMap.containsKey(parentCatName)) {
					subCategoryImageVOList = parentMap.get(parentCatName);
	            	SubCategoryImageVO subcategoryImageVO = new SubCategoryImageVO();
		        	subcategoryImageVO.setSubCategoryName(name);
		        	subcategoryImageVO.setSubCategoryId(subCategoryImage.getCategory().getId());
		        	subcategoryImageVO.setSubCategoryImageURL(subCategoryImage.getSubCategoryImageURL());
		        	subCategoryImageVOList.add(subcategoryImageVO);
				}
				else
				{
					subCategoryImageVOList = new ArrayList<SubCategoryImageVO>();
	            	SubCategoryImageVO subcategoryImageVO = new SubCategoryImageVO();
		        	subcategoryImageVO.setSubCategoryName(name);
		        	subcategoryImageVO.setSubCategoryId(subCategoryImage.getCategory().getId());
		        	subcategoryImageVO.setSubCategoryImageURL(subCategoryImage.getSubCategoryImageURL());
		        	subCategoryImageVOList.add(subcategoryImageVO);

				}
				parentMap.put(parentCatName, subCategoryImageVOList);	
        	}	
 	    }
        // setting subcatimges to response
        adminSubCatImgResponse.setSubCatagoryImgsObjByCatagory(parentMap);
        LOGGER.debug("Entered getAllSubCatImages");
    	return adminSubCatImgResponse;
    }
   
    // Remove sub category image
    @RequestMapping(value="/deleteSubCatImage/{subCategoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DeleteSubCatImgResponse deleteSubCatImage(@PathVariable String subCategoryId) throws Exception {
		LOGGER.debug("Entered deleteSubCatImage");
		DeleteSubCatImgResponse deleteSubCatImgResponse = new DeleteSubCatImgResponse();
    	Long subCategoryIdLong = new Long(subCategoryId);
    	SubCategoryImage subCategoryImage = subCategoryService.getByCategoryId(subCategoryIdLong);
    	if(subCategoryImage ==null) {
    		deleteSubCatImgResponse.setErrorMesssage("Subcategory image not exist");
    		deleteSubCatImgResponse.setStatus(FALSE);
    		return deleteSubCatImgResponse;
    	}
    	storageService.deleteFile(subCategoryImage.getSubCategoryImageURL()); // delete image
    	subCategoryService.delete(subCategoryImage);
    	LOGGER.debug("Sub category image deleted");
    	deleteSubCatImgResponse.setSuccessMessage("SubCategoryImage with id "+subCategoryIdLong+" deleted successfully");
    	deleteSubCatImgResponse.setStatus(TRUE);
    	LOGGER.debug("Ended deleteSubCatImage");
    	return deleteSubCatImgResponse;
    	
    }
    // Upload or update sub category image
    @RequestMapping(value="/uploadOrUpdateSubCatImage", method = RequestMethod.POST)
	@ResponseBody
	public SubCatImageResponse uploadOrUpdateSubCatImage(@RequestPart("subCatImageRequest") String subCatImageRequestStr,
			@RequestPart("file") MultipartFile subCatImage) throws Exception {
    	LOGGER.debug("Entered uploadOrUpdateSubCatImage ");
    	SubCatImageRequest subCatImageRequest = new ObjectMapper().readValue(subCatImageRequestStr, SubCatImageRequest.class);
    	SubCatImageResponse subCatImageResponse = new SubCatImageResponse();
    	Category subCategory = categoryService.getByCategoryCode(subCatImageRequest.getSubCategoryName());
    	String fileName = "";
    	// Storing uploaded or updated image 
    	if(subCatImage.getSize() != 0) {
    		try{
    			LOGGER.debug("Storing Sub category image");
    			fileName = storageService.store(subCatImage,"subcategoryimg");
    			System.out.println("fileName "+fileName);
    		}catch(StorageException se){
    			LOGGER.error("StoreException occured"+se.getMessage());
    			subCatImageResponse.setErrorMessage("Failed while storing image");
    			subCatImageResponse.setStatus(FALSE);
    			return subCatImageResponse;
    		}
    	}
    	  /**  If we update the sub category image that already exist,then updating
           *  the sub category image with newly added image otherwise saving the
           *  sub category image
           */ 
    		try { 
    			SubCategoryImage subCategoryImage = subCategoryService.getByCategoryId(subCategory.getId());
    			if(subCategoryImage==null) {
				SubCategoryImage subCategoryImageObj = new SubCategoryImage();
				subCategoryImageObj.setSubCategoryImageURL(fileName);
				subCategoryImageObj.setCategory(subCategory);
			
				subCategoryService.save(subCategoryImageObj);
				LOGGER.debug("SubCategory Image uploaded");
				subCatImageResponse.setSubCategoryId(subCategory.getId());
				subCatImageResponse.setSubCatImgURL(fileName);
				subCatImageResponse.setSuccessMessage("SubCategory Image uploaded successfully");
			    subCatImageResponse.setStatus(TRUE);
    			}else {
    				subCategoryImage.setSubCategoryImageURL(fileName);
    				subCategoryImage.setCategory(subCategory);
    				
    				subCategoryService.update(subCategoryImage);
    			    LOGGER.debug("SubCategory Image updated");
				    subCatImageResponse.setSubCategoryId(subCategory.getId());
				    subCatImageResponse.setSubCatImgURL(fileName);
				    subCatImageResponse.setSuccessMessage("SubCategory Image updated successfully");
				    subCatImageResponse.setStatus(TRUE);
    			}	
    		}
		catch(Exception e){
			LOGGER.error("Error while storing sub category image");
			if(StringUtils.isEmpty(fileName)){
				storageService.deleteFile(fileName); // delete image
			}
			subCatImageResponse.setStatus(FALSE);
			subCatImageResponse.setErrorMessage("Error while storing sub category image");
		}
        LOGGER.debug("Ended uploadOrUpdateSubCatImage");
		return subCatImageResponse;
    }
   
    // Approve testimonial by id
    @RequestMapping(value="/testimonial/{testimonialId}", method = RequestMethod.GET)
	@ResponseBody
	public TesimonialResponse getTestimonialById(@PathVariable String testimonialId) {
    	TesimonialResponse tesimonialResponse = new TesimonialResponse();
    	Long testimonialIdLong = new Long(testimonialId);
    	CustomerTestimonial customerTestimonial = customerTestmonialService.getTestimonialById(testimonialIdLong);
    	tesimonialResponse.setCustomerId(customerTestimonial.getCustomer().getId());
    	tesimonialResponse.setCustomerName(customerTestimonial.getCustomer().getBilling().getFirstName());
    	tesimonialResponse.setEmailAddress(customerTestimonial.getCustomer().getEmailAddress());
    	tesimonialResponse.setDescription(customerTestimonial.getDescription());
    	tesimonialResponse.setStatus(customerTestimonial.getStatus());
    	tesimonialResponse.setTestimonialId(customerTestimonial.getId());
    	return tesimonialResponse;
    }
    // Approve testimonial
    @RequestMapping(value="/approve/testimonial", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApproveTestimonialResponse approveOrDeclineTestimonial(@RequestBody ApproveTestimonialRequest approveTestimonialRequest) throws Exception {
		LOGGER.debug("Entered approveOrDeclineTestimonial");
		ApproveTestimonialResponse approveTestimonialResponse = new ApproveTestimonialResponse();
		Long testimonialIdLong = approveTestimonialRequest.getTestimonialId();
		CustomerTestimonial customerTestimonial = customerTestmonialService.getTestimonialById(testimonialIdLong);
		if(approveTestimonialRequest.getStatus().equals("Y")) {
			customerTestimonial.setStatus("Y");
		}
		else {
			customerTestimonial.setStatus("N");
		}
		try {
			customerTestmonialService.update(customerTestimonial);
			
			if(approveTestimonialRequest.getStatus().equals("Y")){
				LOGGER.debug("Testimonial enabled");
			approveTestimonialResponse.setSuccessMessage("Testimonial enabled successfully");
			approveTestimonialResponse.setStatus(TRUE);
			}
			if(approveTestimonialRequest.getStatus().equals("N")) {
				LOGGER.debug("Testimonial disabled");
				approveTestimonialResponse.setSuccessMessage("Testimonial is disabled successfully");
				approveTestimonialResponse.setStatus(TRUE);
			}
		} catch (Exception e) {
			LOGGER.error("Error in updating Testimonial");
			approveTestimonialResponse.setErrorMessage("Error in approving Testimonial");
			approveTestimonialResponse.setStatus(FALSE);
			return approveTestimonialResponse;
		}
		LOGGER.debug("Ended approveOrDeclineTestimonial");
    	return approveTestimonialResponse;
    } 
    // Retrieval of testimonials
    @RequestMapping(value="/getTestimonials", method = RequestMethod.POST)
	@ResponseBody
	public PaginatedResponse getTestimonials(@RequestBody AdminTestimonialsRequest adminTestimonialsRequest,@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) {
    	LOGGER.debug("Entered getTestimonials");
    	
    	List<CustomerTestimonialVO>  customerTestimonialVOList = new ArrayList<CustomerTestimonialVO>();
    	PaginatedResponse paginatedResponse = new PaginatedResponse();
    	if(adminTestimonialsRequest.getStatus().equals("ALL")){
    		List<CustomerTestimonial> customerTestimonials = customerTestmonialService.getAllTestimonials();  
    		
    		for(CustomerTestimonial testmonial : customerTestimonials) {
        		CustomerTestimonialVO customerTestimonialVO = new CustomerTestimonialVO();
        		customerTestimonialVO.setCustomerId(testmonial.getCustomer().getId());
        		customerTestimonialVO.setCustomerName(testmonial.getCustomer().getBilling().getFirstName());
        		customerTestimonialVO.setEmailAddress(testmonial.getCustomer().getEmailAddress());
        		customerTestimonialVO.setDescription(testmonial.getDescription());
        		customerTestimonialVO.setTestimonialId(testmonial.getId());
        		customerTestimonialVO.setStatus(testmonial.getStatus());
        		customerTestimonialVOList.add(customerTestimonialVO);
        	}
    		
    	}
    	if(adminTestimonialsRequest.getStatus().equals("Y")) {
    		List<CustomerTestimonial> approvedTestimonials = customerTestmonialService.getApprovedTestimonial();
    		
    		for(CustomerTestimonial testmonial : approvedTestimonials) {
        		CustomerTestimonialVO customerTestimonialVO = new CustomerTestimonialVO();
        		customerTestimonialVO.setCustomerId(testmonial.getCustomer().getId());
        		customerTestimonialVO.setCustomerName(testmonial.getCustomer().getBilling().getFirstName());
        		customerTestimonialVO.setEmailAddress(testmonial.getCustomer().getEmailAddress());
        		customerTestimonialVO.setDescription(testmonial.getDescription());
        		customerTestimonialVO.setTestimonialId(testmonial.getId());
        		customerTestimonialVO.setStatus(testmonial.getStatus());
        		customerTestimonialVOList.add(customerTestimonialVO);
        	}
    		
    	}
    	if(adminTestimonialsRequest.getStatus().equals("N")) {
    		List<CustomerTestimonial> declinedTestimonials = customerTestmonialService.getDeclinedtestimonials();
    		
    		for(CustomerTestimonial testmonial : declinedTestimonials) {
        		CustomerTestimonialVO customerTestimonialVO = new CustomerTestimonialVO();
        		customerTestimonialVO.setCustomerId(testmonial.getCustomer().getId());
        		customerTestimonialVO.setCustomerName(testmonial.getCustomer().getBilling().getFirstName());
        		customerTestimonialVO.setEmailAddress(testmonial.getCustomer().getEmailAddress());
        		customerTestimonialVO.setDescription(testmonial.getDescription());
        		customerTestimonialVO.setTestimonialId(testmonial.getId());
        		customerTestimonialVO.setStatus(testmonial.getStatus());
        		customerTestimonialVOList.add(customerTestimonialVO);
        	}
    		
    	}
    	PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, customerTestimonialVOList.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(customerTestimonialVOList == null || customerTestimonialVOList.isEmpty() || customerTestimonialVOList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(customerTestimonialVOList);
			LOGGER.debug("Ended getTestimonials");
			return paginatedResponse;
		}
    	List<CustomerTestimonialVO> paginatedResponses = customerTestimonialVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
		return paginatedResponse;
		
    }
    // Save testimonial
    @RequestMapping(value="/testimonial/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public TestimonialResponse saveTestimonial(@RequestBody TestimonialRequest testimonialRequest) throws Exception {
    	LOGGER.debug("Entered saveTestimonial");
    	TestimonialResponse testimonialResponse = new TestimonialResponse();
    	if(StringUtils.isEmpty(testimonialRequest.getTestmonialDescription())){
    		testimonialResponse.setErrorMessage("Testimonial cannot be empty");
    		testimonialResponse.setStatus(FALSE);
    		return testimonialResponse;
    	}
    	
    	Customer customer = customerService.getById(testimonialRequest.getCustomerId());
    	CustomerTestimonial customerTestimonial = new CustomerTestimonial();
    	customerTestimonial.setCustomer(customer);
    	customerTestimonial.setDescription(testimonialRequest.getTestmonialDescription());
    	customerTestimonial.setStatus("N");
    	customerTestmonialService.save(customerTestimonial);
    	testimonialResponse.setSuccessMessage("Testimonial saved successfully");
    	LOGGER.debug("Testimonial saved");
    	testimonialResponse.setStatus(TRUE);
    	LOGGER.debug("Ended saveTestimonial");
    	return testimonialResponse;
    }
    // delete testimonial
    @RequestMapping(value="/deleteTestimonial/{testimonialId}", method = RequestMethod.GET)
    @ResponseBody
    public DeleteTesimonialResponse deleteTestimonial(@PathVariable String testimonialId) throws Exception {
    	LOGGER.debug("Entered deleteTestimonial");
    	DeleteTesimonialResponse deleteTesimonialResponse = new DeleteTesimonialResponse();
    	Long testimonialIdLong = new Long(testimonialId);
    	try {
    	CustomerTestimonial customerTestimonial = customerTestmonialService.getTestimonialById(testimonialIdLong);
    	if(customerTestimonial==null) {
    		deleteTesimonialResponse.setErrormessage("Testimonial not found for id "+testimonialIdLong);
    		deleteTesimonialResponse.setStatus(FALSE);
    		return deleteTesimonialResponse;
    	}
    	customerTestmonialService.delete(customerTestimonial);
    	LOGGER.debug("Testimonial deleted");
    	deleteTesimonialResponse.setSuccessMessage("Testimonial deleted successfully");
    	deleteTesimonialResponse.setStatus(TRUE);
    	}catch(Exception e) {
    		LOGGER.error("Error while deleting testimonial",e.getMessage());
    	}
    	LOGGER.debug("Ended deleteTestimonial");
    	return deleteTesimonialResponse;
    	
    }
    // upload brand image
    @RequestMapping(value="/uploadBrandImage", method = RequestMethod.POST)
	@ResponseBody
	public BrandImageResponse uploadBrandImage(@RequestPart("brandImageRequest") String brandImageStr,
			@RequestPart("file") MultipartFile brandImage) throws Exception {
    	LOGGER.debug("Entered uploadBrandImage");
    	BrandImageRequest brandImageRequest = new ObjectMapper().readValue(brandImageStr, BrandImageRequest.class);
    	BrandImageResponse brandImageResponse = new BrandImageResponse();
    	
    	String fileName = "";
    	// Storing uploaded or updated image 
    	if(brandImage.getSize() != 0) {
    		try{
    			LOGGER.debug("Storing brandImage");
    			fileName = storageService.store(brandImage,"brandimg");
    			System.out.println("fileName "+fileName);
    		}catch(StorageException se){
    			LOGGER.error("StoreException occured"+se.getMessage());
    			brandImageResponse.setErrorMessage("Failed while storing brand image");
    			brandImageResponse.setStatus(FALSE);
    			return brandImageResponse;
    		}
    	}
    		try { 
    			BrandImage brandImageObj = new BrandImage();
    			brandImageObj.setImage(fileName);
    			brandImageObj.setName(brandImageRequest.getBrandName());
    			brandImageObj.setStatus(brandImageRequest.getStatus());
    			brandImageService.save(brandImageObj);
    			LOGGER.debug("Brand Image uploaded");
    			brandImageResponse.setStatus(TRUE);
    			brandImageResponse.setSuccessmessge("Brand Image uploaded successfully");
    		}
		catch(Exception e){
			LOGGER.error("Error while storing brand image");
			if(StringUtils.isEmpty(fileName)){
				storageService.deleteFile(fileName); // delete image
			}
			brandImageResponse.setStatus(FALSE);
			brandImageResponse.setErrorMessage("Error while storing brand image");
		}
        LOGGER.debug("Ended uploadBrandImage");
		return brandImageResponse;
    	
    }
    // delete brand image
    @RequestMapping(value="/deleteBrandImage/{brandImageId}", method = RequestMethod.GET)
    @ResponseBody
    public DeleteBrandImageResponse deleteBrandImage(@PathVariable String brandImageId) throws Exception {
		LOGGER.debug("Entered deleteBrandImage");
		DeleteBrandImageResponse deleteBrandImageResponse = new DeleteBrandImageResponse();
		Long brandImageIdLong = new Long(brandImageId);
		try {
		BrandImage brandImage = brandImageService.getById(brandImageIdLong);
		if(brandImage==null){
			deleteBrandImageResponse.setErrorMessage("Brand Image cannot be found");
			deleteBrandImageResponse.setStatus(FALSE);
			return deleteBrandImageResponse;
		}
		storageService.deleteFile(brandImage.getImage());
		brandImageService.delete(brandImage);
		LOGGER.debug("Brand Image deleted");
		deleteBrandImageResponse.setSuccessMessage("Brand Image deleted successfully");
		deleteBrandImageResponse.setStatus(TRUE);
		}catch(Exception e) {
			LOGGER.error("Error while deleting brand image",e.getMessage());
		}
    	return deleteBrandImageResponse;
    	
    }
    //Retrieval of brand images
    @RequestMapping(value="/getBrandImages", method = RequestMethod.POST)
    @ResponseBody
    public PaginatedResponse getBrandImages(@RequestBody AdminBrandImageRequest adminBrandImageRequest, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		LOGGER.debug("Entered getBrandImages");
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		
    	List<BrandImageVO> brandImageVOList = new ArrayList<BrandImageVO>();
    	if(adminBrandImageRequest.getStatus().equals("ALL")) {
    		List<BrandImage> brandImages = brandImageService.getAllBrandImages();
    		for(BrandImage brandImage : brandImages) {
    			BrandImageVO brandImageVO = new BrandImageVO();
    			brandImageVO.setBrandId(brandImage.getId());
    			brandImageVO.setBrandName(brandImage.getName());
    			brandImageVO.setBrangImage(brandImage.getImage());
    			brandImageVO.setStatus(brandImage.getStatus());
    			brandImageVOList.add(brandImageVO);
    		}
    		
    	}
    	if(adminBrandImageRequest.getStatus().equals("Y")) {
    		List<BrandImage> brandImages = brandImageService.getEnableBrandImages();
    		for(BrandImage brandImage : brandImages) {
    			BrandImageVO brandImageVO = new BrandImageVO();
    			brandImageVO.setBrandId(brandImage.getId());
    			brandImageVO.setBrandName(brandImage.getName());
    			brandImageVO.setBrangImage(brandImage.getImage());
    			brandImageVO.setStatus(brandImage.getStatus());
    			brandImageVOList.add(brandImageVO);
    		}
    		
    		
    	}
    	if(adminBrandImageRequest.getStatus().equals("N")) {
    		List<BrandImage> brandImages = brandImageService.getDisableBrandImages();
    		for(BrandImage brandImage : brandImages) {
    			BrandImageVO brandImageVO = new BrandImageVO();
    			brandImageVO.setBrandId(brandImage.getId());
    			brandImageVO.setBrandName(brandImage.getName());
    			brandImageVO.setBrangImage(brandImage.getImage());
    			brandImageVO.setStatus(brandImage.getStatus());
    			brandImageVOList.add(brandImageVO);
    		}
    		
    		
    	}
    	PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, brandImageVOList.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(brandImageVOList == null || brandImageVOList.isEmpty() || brandImageVOList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(brandImageVOList);
			
			return paginatedResponse;
		}
    	List<BrandImageVO> paginatedResponses = brandImageVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	LOGGER.debug("Ended BrandImages");
		return paginatedResponse;
    	
    }
    //Enable brand image
    @RequestMapping(value="/enable/brandImage", method = RequestMethod.POST)
	@ResponseBody
	public EnableBrandImageResponse enableOrDisableBrandImage(@RequestBody EnableBrandImageRequest enableBrandImageRequest) throws Exception {
		LOGGER.debug("Entered enableOrDisableBrandImage");
		EnableBrandImageResponse enableBrandImageResponse = new EnableBrandImageResponse();
		try {
			BrandImage brandImage = brandImageService.getById(enableBrandImageRequest.getBrandImageId());
			if(brandImage==null){
				enableBrandImageResponse.setErrorMessage("Brand Image cannot be found");
				enableBrandImageResponse.setStatus(FALSE);
				return enableBrandImageResponse;
			}
			if(enableBrandImageRequest.getStatus().equals("Y")){
				brandImage.setStatus(enableBrandImageRequest.getStatus());
			} else {
				brandImage.setStatus(enableBrandImageRequest.getStatus());
			}
			brandImageService.update(brandImage);
			
			if(enableBrandImageRequest.getStatus().equals("Y")) {
			LOGGER.debug("Brand Image enabled");
			enableBrandImageResponse.setSuccessMessage("Brand Image enabled successfully");
			enableBrandImageResponse.setStatus(TRUE);
			}
			if(enableBrandImageRequest.getStatus().equals("N")) {
				LOGGER.debug("Brand Image disabled");
				enableBrandImageResponse.setSuccessMessage("Brand Image disabled successfully");
				enableBrandImageResponse.setStatus(TRUE);
				}
			}catch(Exception e) {
				LOGGER.error("Error while enabling/disabling brand image",e.getMessage());
			}
		LOGGER.debug("Ended enableOrDisableBrandImage");
    	return enableBrandImageResponse;
    	
    }
    @RequestMapping(value="/postrequirement/save", method=RequestMethod.POST)
  	@ResponseBody
  	public PostRequirementResponse postRequirement(@RequestBody PostRequirementRequest postRequirementRequest) throws Exception {
    	LOGGER.debug("Entered postReqirement");
    	PostRequirementResponse postReqirementResponse = new PostRequirementResponse();
    	if(StringUtils.isEmpty(postRequirementRequest.getQuery())){
    		postReqirementResponse.setErrorMessage("Query cannot be empty..Please provide your query");
    		postReqirementResponse.setStatus(FALSE);
    		return postReqirementResponse;
    	}
    	try {
    	Customer customer = customerService.getById(postRequirementRequest.getCustomerId());
    	Category category = categoryService.getByCategoryCode(postRequirementRequest.getCategory());
    	PostRequirement postRequirement = new PostRequirement();
    	postRequirement.setCustomerId(customer.getId());
    	postRequirement.setQuery(postRequirementRequest.getQuery());
    	postRequirement.setCategoryId(category.getId());
    	postRequirement.setPostedDate(new Date());
    	postRequirementService.save(postRequirement);
    	LOGGER.debug("Query saved");
    	final Locale locale  = new Locale("en");
    	MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");
    	
    	Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
    	if(customer.getCustomerType().equals("0")) {
		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getBilling().getFirstName());
    	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, customer.getBilling().getLastName());
    	} else {
    		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getVendorAttrs().getVendorName());
        	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, "");
    	}
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_LABEL, messages.getMessage("email.vendor.add.request.product", locale));
		templateTokens.put(EmailConstants.EMAIL_CATEGORY, category.getDescription().getName());
		
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.vendor.request.postrequirement",locale));
		email.setTo(customer.getEmailAddress());
		email.setTemplateName(ADIMIN_ADD_PRODUCT_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		sendEmailToCustomer(customer,category);
    	postReqirementResponse.setSuccessMessage("Query sent successfully");
    	postReqirementResponse.setStatus(TRUE);
    	} catch (Exception e){
    		e.printStackTrace();
    		LOGGER.error("Error while saving query"+e.getMessage());
    		postReqirementResponse.setErrorMessage("Error while saving query");
    		postReqirementResponse.setStatus(FALSE);
    		return postReqirementResponse;
    	}
    	LOGGER.debug("Ended postReqirement");
		return postReqirementResponse;
    	
    }
    private void sendEmailToCustomer(Customer customer, Category category) throws Exception {
    	final Locale locale  = new Locale("en");
    	MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");
    	
    	Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
    	if(customer.getCustomerType().equals("0")) {
		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getBilling().getFirstName());
    	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, customer.getBilling().getLastName());
    	} else {
    		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getVendorAttrs().getVendorName());
        	templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, "");
    	}
    		
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_LABEL, messages.getMessage("email.vendor.add.request.product", locale));
		templateTokens.put(EmailConstants.EMAIL_CATEGORY, category.getDescription().getName());
		
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.vendor.request.postrequirement",locale));
		email.setTo(customer.getEmailAddress());
		email.setTemplateName(ADIMIN_ADD_PRODUCT_TMPL);
		email.setTemplateTokens(templateTokens);
		
		emailService.sendHtmlEmail(merchantStore, email);
		LOGGER.debug("Email sent to customer");
		
	}

	@RequestMapping(value="/getPostRequirements", method = RequestMethod.GET)
	@ResponseBody
	public PaginatedResponse getPostRequirements(@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) {
    	LOGGER.debug("Entered getPostRequirements");
    	PaginatedResponse paginatedResponse = new PaginatedResponse();
    	List<PostRequirementVO> postRequirementVOList = new ArrayList<PostRequirementVO>();
    	try {
    		List<PostRequirement> postRequirements = postRequirementService.getAllPostRequirements();
    		for(PostRequirement postRequirement: postRequirements) {
    			PostRequirementVO postRequirementVO = new PostRequirementVO();
    			postRequirementVO.setPostRequirementId(postRequirement.getId());
    			postRequirementVO.setQuery(postRequirement.getQuery());
    			postRequirementVO.setDateAndTime(postRequirement.getPostedDate());
    			Customer customer = customerService.getById(postRequirement.getCustomerId());
    			postRequirementVO.setCustomerId(customer.getId());
    			Category category = categoryService.getById(postRequirement.getCategoryId());
    			postRequirementVO.setCategory(category.getDescription().getName());
    			if(customer.getCustomerType().equals("0")) {
    				postRequirementVO.setCustomerName(customer.getBilling().getFirstName().concat(" ").concat(customer.getBilling().getLastName()));
    			}else {
    				postRequirementVO.setCustomerName(customer.getVendorAttrs().getVendorName());
    			}
    			postRequirementVOList.add(postRequirementVO);
    		}
    		PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, postRequirementVOList.size());
        	paginatedResponse.setPaginationData(paginaionData);
    		if(postRequirementVOList == null || postRequirementVOList.isEmpty() || postRequirementVOList.size() < paginaionData.getCountByPage()){
    			paginatedResponse.setResponseData(postRequirementVOList);
    			LOGGER.debug("Ended getProductForCatAndTitle method ");
    			return paginatedResponse;
    		}
        	List<PostRequirementVO> paginatedResponses = postRequirementVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedResponse.setResponseData(paginatedResponses);
    		return paginatedResponse;
    	}catch (Exception e) {
    		e.printStackTrace();
    		LOGGER.error("Error while retrieving queries "+e.getMessage());	
    	}
    	LOGGER.debug("Ended getPostRequirements");
		return paginatedResponse;
    	
    }
    @RequestMapping(value="/postrequirement", method=RequestMethod.POST)
  	@ResponseBody
  	public PostRequirementResponse sendMail(@RequestBody RequirementRequest requirementRequest) throws Exception {
		LOGGER.debug("Entered sendMail");
		PostRequirementResponse postRequirementResponse = new PostRequirementResponse();
		//Long postRequirementIdLong = new Long(postRequirementId);
		try {
		PostRequirement postRequirement = postRequirementService.getById(requirementRequest.getPostRequirementId());
		postRequirement.setResponseMessage(requirementRequest.getResponseMessage());
		postRequirementService.save(postRequirement);
		
		final Locale locale  = new Locale("en");
    	MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");
    	Customer customer = customerService.getById(postRequirement.getCustomerId());
    	Category category = categoryService.getById(postRequirement.getCategoryId());
    	
    	Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getVendorAttrs().getVendorName());
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_LABEL, messages.getMessage("email.vendor.add.request.product", locale));
		templateTokens.put(EmailConstants.EMAIL_CATEGORY, category.getDescription().getName());
		templateTokens.put(EmailConstants.EMAIL_AMIN_RESPONSE_MESSAGE, postRequirement.getResponseMessage());
		
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.vendor.add.request.product",locale));
		email.setTo(customer.getEmailAddress());
		email.setTemplateName(ADIMIN_ADD_PRODUCT_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		sendEmailToAdmin(merchantStore);
	    LOGGER.debug("Email sent successful");
	    postRequirementResponse.setSuccessMessage("Email sent sucessful");
		postRequirementResponse.setStatus(TRUE);
		} catch(Exception e) {
			LOGGER.error("Error while sending email");
			postRequirementResponse.setErrorMessage("Error while sending email");
			postRequirementResponse.setStatus(FALSE);
			return postRequirementResponse;
		}
		
    	return postRequirementResponse;
    	
    }

	private void sendEmailToAdmin(MerchantStore merchantStore) throws Exception{
		User user = userService.getById(1l);
		final Locale locale  = new Locale("en");
		
    	Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, user.getAdminName());
		templateTokens.put(EmailConstants.EMAIL_PRODUCT_LABEL, messages.getMessage("email.vendor.add.request.product", locale));
		
		
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.vendor.add.request.product",locale));
		email.setTo("surendervarmac@gmail.com");
		email.setTemplateName(ADIMIN_ADD_PRODUCT_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(merchantStore, email);
		LOGGER.debug("Email sent to Admin");
	}
	// History management retrieval
	@RequestMapping(value="/getHistoryOfDeals", method = RequestMethod.GET)
	@ResponseBody
	public PaginatedResponse getHistoryOfDeals(@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) {
    	LOGGER.debug("Entered getHistoryOfDeals");
    	PaginatedResponse paginatedResponse = new PaginatedResponse();
    	List<HistoryManagementVO>	historyManagementVOList = new ArrayList<HistoryManagementVO>();
    	try {
    	List<HistoryManagement> historyOfDeals = historyManagementService.getHistoryOfDeals();  
    	
    		for(HistoryManagement historyOfDeal : historyOfDeals) {
    			HistoryManagementVO historyManagementVO = new HistoryManagementVO();
    			historyManagementVO.setHistoryManagementId(historyOfDeal.getId());
    			historyManagementVO.setProductId(historyOfDeal.getProductId());
    			historyManagementVO.setProductName(historyOfDeal.getProductName());
    			historyManagementVO.setProductPrice(historyOfDeal.getProductPrice());
    			historyManagementVO.setProductDiscountPrice(historyOfDeal.getProductDiscountPrice());
    			historyManagementVO.setProductPriceStartDate(historyOfDeal.getProductPriceStartDate());
    			historyManagementVO.setProductPriceEndDate(historyOfDeal.getProductPriceEndDate());
    			historyManagementVO.setEnableFor(historyOfDeal.getEnableFor());
    			historyManagementVOList.add(historyManagementVO);
        	}
    		PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, historyManagementVOList.size());
        	paginatedResponse.setPaginationData(paginaionData);
    		if(historyManagementVOList == null || historyManagementVOList.isEmpty() || historyManagementVOList.size() < paginaionData.getCountByPage()){
    			paginatedResponse.setResponseData(historyManagementVOList);
    			return paginatedResponse;
    		}
        	List<HistoryManagementVO> paginatedResponses = historyManagementVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedResponse.setResponseData(paginatedResponses);
    		return paginatedResponse;
    	}catch(Exception e) {
    		e.printStackTrace();
    		LOGGER.error("Error while retrieving history of deals "+e.getMessage());
    	}
    	LOGGER.debug("Ended getHistoryOfDeals");
		return paginatedResponse;
    	
	}
	@RequestMapping(value="/getHistoryOfDealsByDate", method = RequestMethod.POST)
	@ResponseBody
	public PaginatedResponse getHistoryOfDealsByDate(
	            @RequestParam(value="pageNumber", defaultValue = "1") int page , 
	            @RequestParam(value="pageSize", defaultValue="15") int size,
	            @RequestParam("fromDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
				@RequestParam("toDate")   @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {
		LOGGER.debug("Entered getHistoryOfDealsByDate");
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		
		List<HistoryManagementVO>	historyManagementVOList = new ArrayList<HistoryManagementVO>();
		
		try {
			// retrieving history of deals by start date and end date
	    	List<HistoryManagement> historyOfDeals = historyManagementService.getHistoryOfDealsByDate(fromDate,toDate);  
	    	
	    		for(HistoryManagement historyOfDeal : historyOfDeals) {
	    			HistoryManagementVO historyManagementVO = new HistoryManagementVO();
	    			historyManagementVO.setHistoryManagementId(historyOfDeal.getId());
	    			historyManagementVO.setProductId(historyOfDeal.getProductId());
	    			historyManagementVO.setProductName(historyOfDeal.getProductName());
	    			historyManagementVO.setProductPrice(historyOfDeal.getProductPrice());
	    			historyManagementVO.setProductDiscountPrice(historyOfDeal.getProductDiscountPrice());
	    			historyManagementVO.setProductPriceStartDate(historyOfDeal.getProductPriceStartDate());
	    			historyManagementVO.setProductPriceEndDate(historyOfDeal.getProductPriceEndDate());
	    			historyManagementVO.setEnableFor(historyOfDeal.getEnableFor());
	    			historyManagementVOList.add(historyManagementVO);
	        	}
	    		PaginationData paginaionData=createPaginaionData(page,size);
	        	calculatePaginaionData(paginaionData,size, historyManagementVOList.size());
	        	paginatedResponse.setPaginationData(paginaionData);
	    		if(historyManagementVOList == null || historyManagementVOList.isEmpty() || historyManagementVOList.size() < paginaionData.getCountByPage()){
	    			paginatedResponse.setResponseData(historyManagementVOList);
	    			return paginatedResponse;
	    		}
	        	List<HistoryManagementVO> paginatedResponses = historyManagementVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	        	paginatedResponse.setResponseData(paginatedResponses);
	    		return paginatedResponse;
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		LOGGER.error("Error while retrieving history of deals "+e.getMessage());
	    	}
		LOGGER.debug("Ended getHistoryOfDealsByDate");
		return paginatedResponse;
		
	}
	    @RequestMapping(value="/admin/vendorproducts/activate", method = RequestMethod.POST)
		@ResponseBody
		public ActivateProductResponse adminApproveProductsByVendor(@RequestBody ApproveVendorProductRequest approveVendorProductRequest) throws Exception {
			LOGGER.debug("Entered adminApproveProductsByVendor");
			
			ActivateProductResponse activateProductResponse = new ActivateProductResponse();
			
			try {
				
			List<Long> vendorProductIds = approveVendorProductRequest.getVendorProductIds();
			
			//List<VendorProduct> vendorProductsList = vendorProductService.findProductsByVendor(approveVendorProductRequest.getVendorId());
			
			for(Long vendorProductId : vendorProductIds) {
				 VendorProduct vendorProduct = vendorProductService.getVendorProductById(vendorProductId);
			    	if(vendorProduct==null) {
			    		activateProductResponse.setErrorMesg("Vendor product not found");
			    		activateProductResponse.setStatus(FALSE);
			    		return activateProductResponse;
			    	}
			    	// Approving and updating vendor product
			    	vendorProduct.setAdminActivatedDate(new Date());
			    	vendorProduct.setAdminActivated(approveVendorProductRequest.isStatus());
			    	vendorProductService.update(vendorProduct);
	
			}
			
			if(approveVendorProductRequest.isStatus()==true) {
	    		activateProductResponse.setSuccessMsg("Product(s) Approved");
	    		activateProductResponse.setStatus(TRUE);
	    	} else {
	    		activateProductResponse.setSuccessMsg("Product(s) Declined");
	    		activateProductResponse.setStatus(TRUE);
	    	}
		
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("Error while approving/declining products"+e.getMessage());
			}
	    	return activateProductResponse;
		 
	 }
	    // Registered vendors listing who are going to be approved by admin
	    @RequestMapping(value="/getVendorForAdmin", method = RequestMethod.POST)
		@ResponseBody
		public PaginatedResponse getVendorForAdmin(@RequestParam(value="pageNumber", defaultValue = "1") int page ,
				                  @RequestParam(value="pageSize", defaultValue="15") int size,
				                  @RequestBody AdminApprovedVendorsRequest adminApprovedVendorsRequest) throws Exception{
			LOGGER.debug("Entered getVendorForAdmin");
			
			PaginatedResponse paginatedResponse = new PaginatedResponse();
			
			List<VendorDetailsVO> vendorDetailsVOList = new ArrayList<VendorDetailsVO>();
			
			List<Customer> vendorsList = null;
			
			try {
				
			if(adminApprovedVendorsRequest.getStatus().equals("ALL")) {
				
				if(adminApprovedVendorsRequest.getCustomerType().equals("ALL")){
				vendorsList = customerService.getAllVendors();
				}
				else {
					vendorsList = customerService.getVendorsByCustomerType(adminApprovedVendorsRequest.getCustomerType());
				}
				
				for(Customer vendor : vendorsList) {
					
					VendorDetailsVO vendorDetailsVO = new VendorDetailsVO();
					vendorDetailsVO.setVendorId(vendor.getId());
					
					if(vendor.getCustomerType().equals("0"))
						vendorDetailsVO.setCustomerName(vendor.getBilling().getFirstName().concat(" ").concat(vendor.getBilling().getLastName()));
					else
					vendorDetailsVO.setVendorName(vendor.getVendorAttrs().getVendorName());
					
					vendorDetailsVO.setVendorUserProfile(vendor.getUserProfile());
					if(vendor.getActivated().equals("0")) {
					vendorDetailsVO.setStatus(Constants.PENDING_FOR_APPROVAL);
					}else {
						vendorDetailsVO.setStatus(Constants.APPROVED);
					}
					if(vendor.getCustomerType().equals("0"))
						vendorDetailsVO.setVendorType(Constants.CUSTOMERS);
					if(vendor.getCustomerType().equals("1"))
						vendorDetailsVO.setVendorType(Constants.PRODUCT_VENDORS);
					if(vendor.getCustomerType().equals("2")) 
						vendorDetailsVO.setVendorType(Constants.SERVICE_PROVIDER);
					if(vendor.getCustomerType().equals("3")) 
						vendorDetailsVO.setVendorType(Constants.ARCHITECTS);
					if(vendor.getCustomerType().equals("4")) 
						vendorDetailsVO.setVendorType(Constants.WALLPAPER);
					if(vendor.getCustomerType().equals("5"))
					    vendorDetailsVO.setVendorType(Constants.MACHINERY_EQUIPMENT);
					
					vendorDetailsVOList.add(vendorDetailsVO);
				}
			} else {
				
				if(adminApprovedVendorsRequest.getCustomerType().equals("ALL")) {
				vendorsList = customerService.getVendorsBasedOnStatus(adminApprovedVendorsRequest.getStatus());
				}
				else{
					vendorsList = customerService.getVendorsBasedOnStatusAndCustomerType(adminApprovedVendorsRequest.getStatus(),adminApprovedVendorsRequest.getCustomerType());
				}
				
				for(Customer vendor : vendorsList) {
					
					VendorDetailsVO vendorDetailsVO = new VendorDetailsVO();
					vendorDetailsVO.setVendorId(vendor.getId());
					
					if(vendor.getCustomerType().equals("0"))
						vendorDetailsVO.setCustomerName(vendor.getBilling().getFirstName().concat(" ").concat(vendor.getBilling().getLastName()));
					else
					vendorDetailsVO.setVendorName(vendor.getVendorAttrs().getVendorName());
					
					vendorDetailsVO.setVendorUserProfile(vendor.getUserProfile());
					
					if(vendor.getActivated().equals("0")) {
						vendorDetailsVO.setStatus(Constants.PENDING_FOR_APPROVAL);
						}else {
							vendorDetailsVO.setStatus(Constants.APPROVED);
						}
					
					if(vendor.getCustomerType().equals("0"))
						vendorDetailsVO.setVendorType(Constants.CUSTOMERS);
					if(vendor.getCustomerType().equals("1"))
						vendorDetailsVO.setVendorType(Constants.PRODUCT_VENDORS);
					if(vendor.getCustomerType().equals("2")) 
						vendorDetailsVO.setVendorType(Constants.SERVICE_PROVIDER);
					if(vendor.getCustomerType().equals("3")) 
						vendorDetailsVO.setVendorType(Constants.ARCHITECTS);
					if(vendor.getCustomerType().equals("4")) 
						vendorDetailsVO.setVendorType(Constants.WALLPAPER);
					if(vendor.getCustomerType().equals("5"))
					    vendorDetailsVO.setVendorType(Constants.MACHINERY_EQUIPMENT);
					
					vendorDetailsVOList.add(vendorDetailsVO);
			   }
			}
				PaginationData paginaionData=createPaginaionData(page,size);
	        	calculatePaginaionData(paginaionData,size, vendorDetailsVOList.size());
	        	paginatedResponse.setPaginationData(paginaionData);
	    		if(vendorDetailsVOList == null || vendorDetailsVOList.isEmpty() || vendorDetailsVOList.size() < paginaionData.getCountByPage()){
	    			paginatedResponse.setResponseData(vendorDetailsVOList);
	    			return paginatedResponse;
	    		}
	        	List<VendorDetailsVO> paginatedResponses = vendorDetailsVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	        	paginatedResponse.setResponseData(paginatedResponses);
			}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("error while retrieving vendors"+e.getMessage());
				paginatedResponse.setErrorMsg("error while retrieving vendors");
			}
			LOGGER.debug("Ended getVendorForAdmin");
			return paginatedResponse;
			
	    }
	    // Listing vendors who are requested for products
	    @RequestMapping(value="/getRequestedVendorForAdmin", method = RequestMethod.GET)
		@ResponseBody
		public PaginatedResponse getRequestedVendorForAdmin(@RequestParam(value="pageNumber", defaultValue = "1") int page ,
				                  @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception{
			
	    	LOGGER.debug("Entered getRequestedVendorForAdmin");
	    	PaginatedResponse paginatedResponse = new PaginatedResponse();
	    	
	    	List<AdminVendorDetailsVO> adminVendorDetailsVOList = new ArrayList<AdminVendorDetailsVO>();
	    	try {
	    	List<Customer> requestedVendorList = vendorProductService.getRequestedVendors();
	    	
	    	for(Customer vendorProduct:requestedVendorList) {
	    		
	    		AdminVendorDetailsVO adminvendorDetailsVO = new AdminVendorDetailsVO();
	    		adminvendorDetailsVO.setVendorId(vendorProduct.getId());
	    		LOGGER.debug("vendor id :: "+vendorProduct.getId());
	    		int totalCount = getVendorAddedProductsCount(vendorProduct.getId());
	    		int approvedCount = getVendorAprovedProductsCount(vendorProduct.getId());
	    		adminvendorDetailsVO.setCount("Total:"+totalCount+"  "+"Approved:"+approvedCount);
	    		if(!vendorProduct.getCustomerType().equals("0"))
				adminvendorDetailsVO.setVendorName(vendorProduct.getVendorAttrs().getVendorName());
				if(vendorProduct.getUserProfile()!=null)
				adminvendorDetailsVO.setVendorUserProfile(vendorProduct.getUserProfile());
				adminVendorDetailsVOList.add(adminvendorDetailsVO);
	    		
	    	}
	    	
	    	PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, adminVendorDetailsVOList.size());
        	paginatedResponse.setPaginationData(paginaionData);
    		if(adminVendorDetailsVOList == null || adminVendorDetailsVOList.isEmpty() || adminVendorDetailsVOList.size() < paginaionData.getCountByPage()){
    			paginatedResponse.setResponseData(adminVendorDetailsVOList);
    			return paginatedResponse;
    		}
    		
        	List<AdminVendorDetailsVO> paginatedResponses = adminVendorDetailsVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedResponse.setResponseData(paginatedResponses);
        	
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.error("error while retrieving vendors "+e.getMessage());
			paginatedResponse.setErrorMsg("Error while retrieving vendors");
		}
	    	LOGGER.debug("Ended getRequestedVendorForAdmin");
	    	return paginatedResponse;
	    	
	    }
	    
	    public int getVendorAprovedProductsCount(Long vendorId) throws Exception{
			
	    	int count =0;
	  
	    	List<VendorProduct> approvedProducts = vendorProductService.getVendorApprovedProductsByVendorId(vendorId);
	    	
	    	count = approvedProducts.size();
	    	return count;
	    	
	    }
	    
		public int getVendorAddedProductsCount(Long vendorId) throws Exception{
			
	    	int count =0;
	    	
	    	List<VendorProduct>  vendorProducts = vendorProductService.getVendorAddedProductsByVendorId(vendorId);
	    	
	    	count = vendorProducts.size();
	    	return count;
	    	
	    }
	    // Admin Approving registered vendors 
	    @RequestMapping(value="/approveVendorByAdmin", method = RequestMethod.POST)
		@ResponseBody
		public AdminApprovedVendorsResponse approveVendorByAdmin(@RequestParam(value="pageNumber", defaultValue = "1") int page ,
				                  @RequestParam(value="pageSize", defaultValue="15") int size,
				                  @RequestBody AdminApprovedVendorsRequest adminApprovedVendorsRequest) throws Exception {
			LOGGER.debug("Entered approveVendorByAdmin");
			String status = null;
			AdminApprovedVendorsResponse adminApprovedVendorsResponse = new AdminApprovedVendorsResponse();
			Customer vendor =null;
			try {
			vendor = customerService.getById(adminApprovedVendorsRequest.getVendorId());
			
			if(vendor==null) {
				adminApprovedVendorsResponse.setErrorMessage("vendor not found for id "+adminApprovedVendorsRequest.getVendorId());
				adminApprovedVendorsResponse.setStatus(FALSE);
				return adminApprovedVendorsResponse;
			}
			if(adminApprovedVendorsRequest.getStatus().equals(Constants.APPROVED)) {
				status = "1";
			}else {
				status = "0";
			}
			vendor.setActivated(status);
			customerService.update(vendor);
			LOGGER.debug("Updated vendor status");
			
			if(adminApprovedVendorsRequest.getStatus().equals(Constants.APPROVED)) {
				LOGGER.debug("Vendor activated");
				adminApprovedVendorsResponse.setSuccessMessage("Vendor activated");
				adminApprovedVendorsResponse.setStatus(TRUE);
			}
			else {
				LOGGER.debug("Vendor declined");
				adminApprovedVendorsResponse.setSuccessMessage("Vendor declined");
				adminApprovedVendorsResponse.setStatus(TRUE);
			}
			}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("Error while approving vendor"+e.getMessage());
				adminApprovedVendorsResponse.setErrorMessage("Error while approving vendor");
				adminApprovedVendorsResponse.setStatus(TRUE);
				return adminApprovedVendorsResponse;
			}
			
			MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");  //i will come back here
	    	final Locale locale  = new Locale("en");
	    	
	    	//String activationURL = userRequest.getActivationURL()+"?email="+userRequest.getEmail();
	        //sending email
	        //String[] activationURLArg = {activationURL};
	        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
			templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, vendor.getVendorAttrs().getVendorName());
			templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, "");
			templateTokens.put(EmailConstants.EMAIL_USER_NAME, vendor.getEmailAddress());
			templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username",locale));
			templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_ACTIVATION, messages.getMessage("email.newuser.text.activation",locale));
			//templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_ACTIVATION_LINK, messages.getMessage("email.newuser.text.activation.link",activationURLArg,locale));
			//templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_ACTIVATION_LINK, activationURL);
			templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password",locale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));
			templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));

			
			Email email = new Email();
			email.setFrom(merchantStore.getStorename());
			email.setFromEmail(merchantStore.getStoreEmailAddress());
			email.setSubject(messages.getMessage("email.newuser.text.activation",locale));
			email.setTo(vendor.getEmailAddress());
			email.setTemplateName(VENDOR_REGISTRATION_ADMIN_APPROVE_TMPL);
			email.setTemplateTokens(templateTokens);


			
			emailService.sendHtmlEmail(merchantStore, email);
			LOGGER.debug("Email sent successful");
	    	
			LOGGER.debug("Ended approveVendorByAdmin");
	    	return adminApprovedVendorsResponse;
	    	
	    }
	    // Vendor booking for admin (Archtects and Machinery & Equipment)
	    @RequestMapping(value="/getVendorBookingsForAdmin", method = RequestMethod.POST)
		@ResponseBody
		public PaginatedResponse getVendorBookingsForAdmin(@RequestBody AdminVendorBokingRequest adminVendorBokingRequest, 
				                  @RequestParam(value="pageNumber", defaultValue = "1") int page ,
				                  @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception{
			
	    	LOGGER.debug("Entered getVendorBookingsForAdmin");
	    	
	    	PaginatedResponse paginatedResponse = new PaginatedResponse();
	    	
	    	List<VendorBooking> vendorBookingList = null;
	    	
	    	List<VendorBookingVO> vendorBookingVOList = new ArrayList<VendorBookingVO>();
	    	
	    	try {
	    	
	    	if(adminVendorBokingRequest.getStatus().equals("ALL")) { 
	    		
	    	
	    	vendorBookingList = vendorBookingService.getVendorBookingsByVendorType(adminVendorBokingRequest.getVendorType());
	    		
	    	for(VendorBooking vendorBooking : vendorBookingList) {
	    		
	    		    VendorBookingVO vendorBookingVO = new VendorBookingVO();
	    			
	    			vendorBookingVO.setId(vendorBooking.getId());
	    			vendorBookingVO.setCustomerName(vendorBooking.getCustomer().getBilling().getFirstName().concat(" ").concat(vendorBooking.getCustomer().getBilling().getLastName()));
	    			vendorBookingVO.setVendorName(vendorBooking.getVendor().getVendorAttrs().getVendorName());
	    			vendorBookingVO.setVendorId(vendorBooking.getVendor().getId());
	    			vendorBookingVO.setBookingDate(vendorBooking.getBookingDate());
	    			vendorBookingVO.setAppointmentDate(vendorBooking.getAppointmentDate());
	    			vendorBookingVO.setClosingDate(vendorBooking.getClosingDate());
	    			vendorBookingVO.setAddress(vendorBooking.getAddress());
	    			vendorBookingVO.setDescription(vendorBooking.getDescription());
	    			vendorBookingVO.setComment(vendorBooking.getComment());
	    			vendorBookingVO.setStatus(vendorBooking.getStatus());
	    			vendorBookingVO.setCustomerEmailId(vendorBooking.getCustomer().getEmailAddress());
	    			vendorBookingVO.setCustomerMobileNumber(vendorBooking.getCustomer().getBilling().getTelephone());
	    			vendorBookingVO.setVendorEmailId(vendorBooking.getVendor().getEmailAddress());
	    			vendorBookingVO.setVendorMobileNumber(vendorBooking.getVendor().getVendorAttrs().getVendorMobile());
	    			
	    			if(vendorBooking.getVendor().getCustomerType().equals("5") && vendorBooking.getPortfolioId() != null) {
	    			MachineryPortfolio  machineryPortfoilio = machineryPortfolioService.getById(vendorBooking.getPortfolioId());
	    			vendorBookingVO.setEquipmentName(machineryPortfoilio.getEquipmentName());
	    			vendorBookingVO.setEquipmentPrice(machineryPortfoilio.getEquipmentPrice());
	    			vendorBookingVO.setHiringtype(machineryPortfoilio.getHiringType());
	    			vendorBookingVO.setImageURL(machineryPortfoilio.getImageURL());
	    			vendorBookingVO.setPortfolioName(machineryPortfoilio.getPortfolioName());
	    			vendorBookingVO.setMachineryPortfolioId(machineryPortfoilio.getId());
	    			}
	    			
	    			if(vendorBooking.getVendor().getCustomerType().equals("1"))
	    			vendorBookingVO.setBookingType(Constants.PRODUCT_VENDORS);
	    			if(vendorBooking.getVendor().getCustomerType().equals("2"))
	    				vendorBookingVO.setBookingType(Constants.SERVICE_PROVIDER);
	    			if(vendorBooking.getVendor().getCustomerType().equals("3"))
	    				vendorBookingVO.setBookingType(Constants.ARCHITECTS);
	    			if(vendorBooking.getVendor().getCustomerType().equals("4"))
	    				vendorBookingVO.setBookingType(Constants.WALLPAPER);
	    			if(vendorBooking.getVendor().getCustomerType().equals("5")) 
	    				vendorBookingVO.setBookingType(Constants.MACHINERY_EQUIPMENT);
	    		
	    			vendorBookingVOList.add(vendorBookingVO);
	    			
	    	 } 
	    	
	    	} else {
	    				vendorBookingList = vendorBookingService.getVendorBookingBasedOnStatus(adminVendorBokingRequest.getStatus(),adminVendorBokingRequest.getVendorType());
	    				
	    				for(VendorBooking vendorBooking : vendorBookingList) {
	    					
	    				VendorBookingVO vendorBookingVO = new VendorBookingVO();
	    				
	    				vendorBookingVO.setId(vendorBooking.getId());
		    			vendorBookingVO.setCustomerName(vendorBooking.getCustomer().getBilling().getFirstName().concat(" ").concat(vendorBooking.getCustomer().getBilling().getLastName()));
		    			vendorBookingVO.setVendorName(vendorBooking.getVendor().getVendorAttrs().getVendorName());
		    			vendorBookingVO.setVendorId(vendorBooking.getVendor().getId());
		    			vendorBookingVO.setBookingDate(vendorBooking.getBookingDate());
		    			vendorBookingVO.setAppointmentDate(vendorBooking.getAppointmentDate());
		    			vendorBookingVO.setClosingDate(vendorBooking.getClosingDate());
		    			vendorBookingVO.setAddress(vendorBooking.getAddress());
		    			vendorBookingVO.setDescription(vendorBooking.getDescription());
		    			vendorBookingVO.setComment(vendorBooking.getComment());
		    			vendorBookingVO.setStatus(vendorBooking.getStatus());
		    			vendorBookingVO.setCustomerEmailId(vendorBooking.getCustomer().getEmailAddress());
		    			vendorBookingVO.setCustomerMobileNumber(vendorBooking.getCustomer().getBilling().getTelephone());
		    			vendorBookingVO.setVendorEmailId(vendorBooking.getVendor().getEmailAddress());
		    			vendorBookingVO.setVendorMobileNumber(vendorBooking.getVendor().getVendorAttrs().getVendorMobile());
		    			
		    			if(vendorBooking.getVendor().getCustomerType().equals("5") && vendorBooking.getPortfolioId() != null) {
		    			MachineryPortfolio  machineryPortfoilio = machineryPortfolioService.getById(vendorBooking.getPortfolioId());
		    			vendorBookingVO.setEquipmentName(machineryPortfoilio.getEquipmentName());
		    			vendorBookingVO.setEquipmentPrice(machineryPortfoilio.getEquipmentPrice());
		    			vendorBookingVO.setHiringtype(machineryPortfoilio.getHiringType());
		    			vendorBookingVO.setImageURL(machineryPortfoilio.getImageURL());
		    			vendorBookingVO.setPortfolioName(machineryPortfoilio.getPortfolioName());
		    			vendorBookingVO.setMachineryPortfolioId(machineryPortfoilio.getId());
		    			}
		    			
		    			if(vendorBooking.getVendor().getCustomerType().equals("1"))
			    			vendorBookingVO.setBookingType(Constants.PRODUCT_VENDORS);
			    		if(vendorBooking.getVendor().getCustomerType().equals("2"))
			    			vendorBookingVO.setBookingType(Constants.SERVICE_PROVIDER);
			    		if(vendorBooking.getVendor().getCustomerType().equals("3"))
			    			vendorBookingVO.setBookingType(Constants.ARCHITECTS);
			    		if(vendorBooking.getVendor().getCustomerType().equals("4"))
			    			vendorBookingVO.setBookingType(Constants.WALLPAPER);
			    		if(vendorBooking.getVendor().getCustomerType().equals("5")) 
			    			vendorBookingVO.setBookingType(Constants.MACHINERY_EQUIPMENT);;
		    		
		    			vendorBookingVOList.add(vendorBookingVO);
	    			}
	    	
	    		}
	    	
	    	PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, vendorBookingVOList.size());
        	paginatedResponse.setPaginationData(paginaionData);
    		if(vendorBookingVOList == null || vendorBookingVOList.isEmpty() || vendorBookingVOList.size() < paginaionData.getCountByPage()){
    			paginatedResponse.setResponseData(vendorBookingVOList);
    			return paginatedResponse;
    		}
    		
        	List<VendorBookingVO> paginatedResponses = vendorBookingVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedResponse.setResponseData(paginatedResponses);
	    	
	    }catch(Exception e) {
	    	e.printStackTrace();
	    	LOGGER.error("Error while retrieving vendor bookings "+e.getMessage());
	    }
	    	LOGGER.debug("Ended getVendorBookingsForAdmin");
	    	return paginatedResponse;
	    }
	    
	    // Calculate discount price or discount percentage
	    @RequestMapping(value="/getDiscountPriceOrPercentage", method = RequestMethod.POST)
		@ResponseBody
		public PriceResponse getDiscountPriceOrPercentage(@RequestBody PriceRequest priceRequest) {
			
	    	PriceResponse priceResponse = new PriceResponse();
	    
	    	Product dbProduct = productService.getById(priceRequest.getProductId());
	    	
	    	ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
	    	
	    	Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
						productAvailability = availability;
						Set<ProductPrice> prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
							} 
						}
					}
				}
			}
			
			String discountedValue = getDiscountedvalue(productPrice.getProductPriceAmount(),priceRequest.getDiscountValue(),priceRequest.isStatus());
			
			priceResponse.setDiscountedvalue(discountedValue);
			
	    	return priceResponse;
	    	
	    }

	    private String getDiscountedvalue(BigDecimal productPriceAmount, BigDecimal discountValue, boolean status) {

			BigDecimal discount = new BigDecimal(0);
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2); //Sets the maximum number of digits after the decimal point
			df.setMinimumFractionDigits(0); //Sets the minimum number of digits after the decimal point
			df.setGroupingUsed(false);
			
			if(productPriceAmount.intValue() > 0 && discountValue.intValue() > 0) {
				
               if(status==true) {
                //discountPrice=(100-discountValue)*productPriceAmount/100;
            	discount = new BigDecimal(100).subtract(discountValue);
            	discount = discount.multiply(productPriceAmount);
            	discount = discount.divide(new BigDecimal(100));
                }
                else {
             	//discountPrice = 100*(productPriceAmount-discountValue)/productPriceAmount;
            	discount = productPriceAmount.subtract(discountValue);
            	discount = discount.multiply(new BigDecimal(100));
            	discount = discount.divide(productPriceAmount,2,4);
               }
            
			}
	         return df.format(discount);
		}
	    @RequestMapping(value="/admin/modifyDealOfDay", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public AdminDealOfDayResponse adminModifyDealOfDay(@RequestBody AdminDealOfDayRequest adminDealOfDayReq) throws Exception {
	    	LOGGER.debug("Entered adminDealOfDay");
	    	AdminDealOfDayResponse adminDealOfDayResponse = new AdminDealOfDayResponse();
	    
	    	Long productId = adminDealOfDayReq.getProductId();
	    	Date startDate = adminDealOfDayReq.getProductPriceSpecialStartDate();
	    	Date endDate = adminDealOfDayReq.getProductPriceSpecialEndDate();
	    
	    	Product dbProduct = productService.getByProductId(productId);
	    	
	    	if(dbProduct==null) {
	    		LOGGER.debug("Deal Of Day product is not found");
	    		adminDealOfDayResponse.setErrorMesg("Deal Of Day product is not found");
	    		adminDealOfDayResponse.setStatus("false");
	    		return adminDealOfDayResponse;
		    }
	    	
	    	MerchantStore store=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		    
	    	com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
	    	
	     	product.setProduct(dbProduct);
	    	
		    ProductAvailability productAvailability = null;
			ProductPrice pPrice = null;
		    Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
		    
		    if(availabilities!=null && availabilities.size()>0)
		    {
				
				for(ProductAvailability availability : availabilities)
				{
					if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS))
					 {
						productAvailability = availability;
						
						Set<ProductPrice> prices = availability.getPrices();
						for(ProductPrice price : prices)
						{
							if(price.isDefaultPrice()) {
								pPrice = price;
								product.setProductPrice(priceUtil.getAdminFormatedAmount(store, pPrice.getProductPriceAmount()));
							}
							
								if(adminDealOfDayReq.getStatus().equals("Y")) {
									//Checking DealOfDay product which is available in the given date  
									List<Product> dodProducts = productService.modifyDealOfDay(startDate,endDate,adminDealOfDayReq.getStatus());
								if(dodProducts!=null && !dodProducts.isEmpty()) {
									for(Product dodProduct : dodProducts){
										/*if(dodProduct.getId()==adminDealOfDayReq.getProductId()) {
											ProductPrice existingDodProductPrice = getProductPrice(dodProduct);
											existingDodProductPrice.setProductPriceSpecialStartDate(adminDealOfDayReq.getProductPriceSpecialStartDate());
											existingDodProductPrice.setProductPriceSpecialEndDate(adminDealOfDayReq.getProductPriceSpecialEndDate());
											existingDodProductPrice.setDealOfDay("Y");
											productPrice.saveOrUpdate(existingDodProductPrice);
											//break;
										}
										ProductPrice dodproductPrice = getProductPrice(dodProduct);
										dodproductPrice.setDealOfDay("N");
										productPrice.saveOrUpdate(dodproductPrice);*/
									/*	if(!(dodProduct.getId()==adminDealOfDayReq.getProductId())) {
											ProductPrice dodproductPrice = getProductPrice(dodProduct);
											dodproductPrice.setDealOfDay("N");
											productPrice.saveOrUpdate(dodproductPrice);
											
										}*//*else {
										ProductPrice existingDodProductPrice = getProductPrice(dodProduct);
										existingDodProductPrice.setProductPriceSpecialStartDate(adminDealOfDayReq.getProductPriceSpecialStartDate());
										existingDodProductPrice.setProductPriceSpecialEndDate(adminDealOfDayReq.getProductPriceSpecialEndDate());
										existingDodProductPrice.setDealOfDay("Y");
										productPrice.saveOrUpdate(existingDodProductPrice);
										//break;
										}*/
										if(dodProduct.getId()==adminDealOfDayReq.getProductId()) {
											ProductPrice dodproductPrice = getProductPrice(dodProduct);
											dodproductPrice.setDealOfDay("Y");
											dodproductPrice.setProductPriceSpecialStartDate(startDate);
											dodproductPrice.setProductPriceSpecialEndDate(endDate);
											productPrice.saveOrUpdate(dodproductPrice);
											
										}else{
											ProductPrice dodproductPrice = getProductPrice(dodProduct);
											dodproductPrice.setProductPriceSpecialStartDate(endDate);
											productPrice.saveOrUpdate(dodproductPrice);
										}
									}
									
								}
										price.setDealOfDay("Y");
										price.setProductPriceSpecialStartDate(adminDealOfDayReq.getProductPriceSpecialStartDate());
										price.setProductPriceSpecialEndDate(adminDealOfDayReq.getProductPriceSpecialEndDate());
										productPrice.saveOrUpdate(price);
										adminDealOfDayResponse.setSuccessMsg("Deal Of Day is set successfully");
										adminDealOfDayResponse.setStatus("true");
								
									}
									else {
								    // if no product is available in the specified date, then update the product in dealofday 
									price.setDealOfDay("Y");
									price.setProductPriceSpecialStartDate(adminDealOfDayReq.getProductPriceSpecialStartDate());
									price.setProductPriceSpecialEndDate(adminDealOfDayReq.getProductPriceSpecialEndDate());
									productPrice.saveOrUpdate(price);
									adminDealOfDayResponse.setSuccessMsg("Deal Of Day is set successfully");
									adminDealOfDayResponse.setStatus("true");
									try {
									HistoryManagement historyManagement = new HistoryManagement();
									historyManagement.setProductId(dbProduct.getId());
									historyManagement.setProductName(dbProduct.getProductDescription().getName());
									historyManagement.setProductPrice(price.getProductPriceAmount());
									historyManagement.setProductDiscountPrice(price.getProductPriceSpecialAmount());
									historyManagement.setProductPriceStartDate(price.getProductPriceSpecialStartDate());
									historyManagement.setProductPriceEndDate(price.getProductPriceSpecialEndDate());
									historyManagement.setEnableFor("DOD");
									historyManagementService.save(historyManagement);
									}catch(Exception e) {
										LOGGER.error("Error in history management "+e.getMessage());
									}
									}
								}
								if(adminDealOfDayReq.getStatus().equals("N")) {
									pPrice.setDealOfDay("N");
									productPrice.saveOrUpdate(pPrice);
									adminDealOfDayResponse.setSuccessMsg("Product is disabled from Deal Of Day");
									adminDealOfDayResponse.setStatus("true");
								}		
					}
				}
			}
				
	     
		    LOGGER.debug("Ended adminDealOfDay");
			return adminDealOfDayResponse;
	  }
	    public ProductPrice getProductPrice(Product dodProduct) {
	    	
	    	ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
			
			Set<ProductAvailability> availabilities = dodProduct.getAvailabilities();
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
						productAvailability = availability;
						Set<ProductPrice> prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
							}
						}
					}
				}
			}
	    	
			return productPrice;
	    	
	    }
	    
	    @RequestMapping(value="/getVendorRevenues", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public PaginatedRevenueResponse getVendorRevenues(@RequestBody VendorRevenueRequest vendorRevenueRequest,
				@RequestParam(value="pageNumber", defaultValue = "1") int page ,
                @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
			
	    	LOGGER.debug("Entered getVendorRevenues");
	    	
	    	PaginatedRevenueResponse paginatedRevenueResponse = new PaginatedRevenueResponse();
	    	
	    	List<VendorRevenueVO> vendorRevenueList = new ArrayList<VendorRevenueVO>();
	    	List<BigInteger> vendorIds = null;
	    	
	    	try {
	    	Date startDate = vendorRevenueRequest.getStartDate();
	    	Date endDate = vendorRevenueRequest.getEndDate();
	    	
	    	if(vendorRevenueRequest.getVendorIds().isEmpty()) 
	    		vendorIds = orderService.findVendorIds(startDate,endDate);
	    	else	
	    		vendorIds = vendorRevenueRequest.getVendorIds();
	    	
	    	int vendorsTotalRevenue = 0;
	    	for(BigInteger vendorId : vendorIds){
	    		
	    		VendorRevenueVO vendorRevenueVO = new VendorRevenueVO();	
	    		LOGGER.debug("Vendor Id: "+vendorId);
	    		vendorRevenueVO.setVendorId(vendorId.longValue());
	    		
	    		Customer vendor = customerService.getById(vendorId.longValue());
	    		
	    		if(vendor==null) {
	    			paginatedRevenueResponse.setErrorMsg("Vendor with vendor Id : "+vendorId+" does not exist");
	    			paginatedRevenueResponse.setStatus("false");
	    			return paginatedRevenueResponse;
	    		}
	    		
	    	vendorRevenueVO.setVendorName(vendor.getVendorAttrs().getVendorName());
	    		
	    	List<Order> vendorAssociatedOrders =  orderService.findOrdersByVendor(startDate,endDate,vendorId.longValue());
	    	
	    	int total = 0;
	    	for(Order order : vendorAssociatedOrders) {
	    		
	    		Set<OrderProduct> orderProducts = order.getOrderProducts();
	    		
	    		for(OrderProduct orderProduct : orderProducts){
	    		  
	    			 BigDecimal  productTotal = orderProduct.getOneTimeCharge().multiply(new BigDecimal(orderProduct.getProductQuantity()));
	    			 total = total+productTotal.intValue();
	    	
	    		}
	    	}
	    	
	    	vendorsTotalRevenue = vendorsTotalRevenue+total;
	    	vendorRevenueVO.setTotalRevenue(total);
	    	vendorRevenueList.add(vendorRevenueVO);
	    	
	    	if(vendorRevenueRequest.getSortBy().equals("ASC"))
	    	Collections.sort(vendorRevenueList, new TotalRevenueComparator());
	    	
	    	if(vendorRevenueRequest.getSortBy().equals("DESC"))
	    	Collections.sort(vendorRevenueList, Collections.reverseOrder(new TotalRevenueDescComparator()));
	    }
	    
	    	PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, vendorRevenueList.size());
        	paginatedRevenueResponse.setPaginationData(paginaionData);
    		if(vendorRevenueList == null || vendorRevenueList.isEmpty() || vendorRevenueList.size() < paginaionData.getCountByPage()){
    			paginatedRevenueResponse.setVendorRevenues(vendorRevenueList);
    			return paginatedRevenueResponse;
    		}
    		
        	List<VendorRevenueVO> paginatedResponses = vendorRevenueList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedRevenueResponse.setVendorRevenues(paginatedResponses);
        	paginatedRevenueResponse.setTotal(vendorsTotalRevenue);
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		LOGGER.error("Error while retrieving vendor revenues "+e.getMessage());
	    		paginatedRevenueResponse.setErrorMsg("Error while retrieving vendor revenues");
	    		paginatedRevenueResponse.setStatus("false");
	    		return paginatedRevenueResponse;
	    	}
	    	LOGGER.debug("Ended getVendorRevenues");
	    	return paginatedRevenueResponse;
	 }
	    
	    @RequestMapping(value="/getProductRevenues", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public PaginatedRevenueResponse getProductRevenues(@RequestBody VendorRevenueRequest vendorRevenueRequest,
				@RequestParam(value="pageNumber", defaultValue = "1") int page ,
                @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
			
	    	LOGGER.debug("Entered getProductRevenues");
	    	
	    	PaginatedRevenueResponse paginatedRevenueResponse = new PaginatedRevenueResponse();
	    	
	    	List<ProductRevenueVO> productRevenueList = new ArrayList<ProductRevenueVO>();
	    	
	    	List<String> productSkus = null;
	    	
	    	try {
	    	Date startDate = vendorRevenueRequest.getStartDate();
	    	Date endDate = vendorRevenueRequest.getEndDate();
	    	
	    	if(vendorRevenueRequest.getProductSkus().isEmpty())
	    	   productSkus = orderService.findProductSkus(startDate,endDate);
	    	else
	    	   productSkus= vendorRevenueRequest.getProductSkus();
	    	
	    	int productsTotalRevenue = 0;
	    	for(String productSku : productSkus){
	    		
	    		ProductRevenueVO productRevenueVO = new ProductRevenueVO();	
	    		LOGGER.debug("Product Sku "+productSku);
	    		productRevenueVO.setProductId(productSku);
	    		
	    		Language language = languageService.getById(1);
	    		
	    		Product product = productService.getByCode(productSku,language);
	    		
	    		if(product==null) {
	    			LOGGER.debug("Product with product sku : "+productSku+" does not exist");
	    			paginatedRevenueResponse.setErrorMsg("Product with product sku : "+productSku+" does not exist");
	    			paginatedRevenueResponse.setStatus("false");
	    			return paginatedRevenueResponse;
	    		}
	    		
	    		LOGGER.debug("Product Name "+product.getProductDescription().getName());
	    		
	    		productRevenueVO.setProductName(product.getProductDescription().getName());
	    		
	    	List<Order> vendorAssociatedOrders =  orderService.findOrdersByProduct(startDate,endDate,productSku);
	    	
	    	int total = 0;
	    	for(Order order : vendorAssociatedOrders) {
	    		
	    		Set<OrderProduct> orderProducts = order.getOrderProducts();
	    		
	    		for(OrderProduct orderProduct : orderProducts){
	    		 
	    			 BigDecimal  productTotal = orderProduct.getOneTimeCharge().multiply(new BigDecimal(orderProduct.getProductQuantity()));
	    			 total = total+productTotal.intValue();
	    
	    		}
	    	}
	    	productsTotalRevenue = productsTotalRevenue+total;
	    	productRevenueVO.setTotalRevenue(total);
	    	productRevenueList.add(productRevenueVO);
	    }
	    	
	    	PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, productRevenueList.size());
        	paginatedRevenueResponse.setPaginationData(paginaionData);
    		if(productRevenueList == null || productRevenueList.isEmpty() || productRevenueList.size() < paginaionData.getCountByPage()){
    			paginatedRevenueResponse.setProductRevenues(productRevenueList);
    			return paginatedRevenueResponse;
    		}
    		
        	List<ProductRevenueVO> paginatedResponses = productRevenueList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedRevenueResponse.setProductRevenues(paginatedResponses);
        	paginatedRevenueResponse.setTotal(productsTotalRevenue);
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		LOGGER.error("Error while retrieving product revenues "+e.getMessage());
	    		paginatedRevenueResponse.setErrorMsg("Error while retrieving product revenues");
	    		paginatedRevenueResponse.setStatus("false");
	    		return paginatedRevenueResponse;
	    	}
	    	
	    	LOGGER.debug("Ended getProductRevenues");
	    	return paginatedRevenueResponse;
	 }
	    
	    @RequestMapping(value="/getProductRevenuesByVendor", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public PaginatedRevenueResponse getVendorProductRevenues(@RequestBody VendorProductRevenueRequest vendorProductRevenueRequest,
				@RequestParam(value="pageNumber", defaultValue = "1") int page ,
                @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
					
            LOGGER.debug("Entered getProductRevenuesByVendor");
	    	
	    	PaginatedRevenueResponse paginatedRevenueResponse = new PaginatedRevenueResponse();
	    	
	    	List<VendorProductRevenueVO> vendorProductRevenueList = new ArrayList<VendorProductRevenueVO>();
	    	List<VendorProducts> vendorProductList = new ArrayList<VendorProducts>();
	    	
	    	try {
	    		
	    	Date startDate = vendorProductRevenueRequest.getStartDate();
	    	Date endDate   = vendorProductRevenueRequest.getEndDate();
	
	    	VendorProductRevenueVO vendorProductRevenueVO = new VendorProductRevenueVO();
	    	
	    	LOGGER.debug("Vendor Id: "+vendorProductRevenueRequest.getVendorId());
	    		
	    	vendorProductRevenueVO.setVendorId(vendorProductRevenueRequest.getVendorId());
	    		
	    	Customer vendor = customerService.getById(vendorProductRevenueRequest.getVendorId());
	    			
	    	vendorProductRevenueVO.setVendorName(vendor.getVendorAttrs().getVendorName());
	    		
	    	List<Order> vendorAssociatedOrders =  orderService.findOrdersByVendor(startDate,endDate,vendorProductRevenueRequest.getVendorId());
	    	
	    	int total = 0;
	    	for(Order order : vendorAssociatedOrders) {
	    		
	    		Set<OrderProduct> orderProducts = order.getOrderProducts();
	    		
	    		int  productRevenueTotal = 0;
	    		
	    		for(OrderProduct orderProduct : orderProducts){
	    		  
	    			   VendorProducts vendorProducts = new VendorProducts();
	    			   
	    			   BigDecimal  productRevenue =  orderProduct.getOneTimeCharge().multiply(new BigDecimal(orderProduct.getProductQuantity()));
	    			   productRevenueTotal = productRevenueTotal+productRevenue.intValue();
	    			   
	    			   Language language = languageService.getById(1);
	   	    		
	   	    		   Product product = productService.getByCode(orderProduct.getSku(),language);
	   	    		   vendorProducts.setProductId(product.getId());
	   	    		   
	    			   vendorProducts.setProductRevenue(productRevenueTotal);
	    			   vendorProducts.setProductName(orderProduct.getProductName());
	    			   vendorProducts.setOrderId(orderProduct.getOrder().getId());
	    			   vendorProducts.setPurchasedDate(orderProduct.getOrder().getDatePurchased());
	    			   vendorProducts.setProductquantity(orderProduct.getProductQuantity());
	    			   vendorProducts.setProductPrice(orderProduct.getOneTimeCharge());
	    			   
	    			   vendorProductList.add(vendorProducts);
	    			   vendorProductRevenueVO.setVendorProducts(vendorProductList);
	    			 
	    			   total = total+productRevenueTotal;
	    			   vendorProductRevenueVO.setTotalRevenue(total);    	
	    		}
	    	}
	    	
	    	vendorProductRevenueVO.setVendorProducts(vendorProductList);
	    	vendorProductRevenueList.add(vendorProductRevenueVO);
	    	
	    	LOGGER.debug("VendorProductRevenue list size :"+vendorProductRevenueList.size());
	    	
	    	PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, vendorProductRevenueList.size());
        	paginatedRevenueResponse.setPaginationData(paginaionData);
    		if(vendorProductRevenueList == null || vendorProductRevenueList.isEmpty() || vendorProductRevenueList.size() < paginaionData.getCountByPage()){
    			paginatedRevenueResponse.setVendorProductRevenueData(vendorProductRevenueList);
    			return paginatedRevenueResponse;
    		}
        	List<VendorProductRevenueVO> paginatedResponses = vendorProductRevenueList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedRevenueResponse.setVendorProductRevenueData(paginatedResponses);
	    	}catch(Exception ex) {
	    		LOGGER.error("Error while retrieving vendor product revenues "+ex.getMessage());
	    		paginatedRevenueResponse.setErrorMsg("Error while vendor product revenues");
	    		paginatedRevenueResponse.setStatus("false");
	    		return paginatedRevenueResponse;
	    	}
	    	
	    	LOGGER.debug("Ended getProductRevenuesByVendor");
	    	return paginatedRevenueResponse;
	   
	    	
	    }
	    // retrieval of vendor Ids for vendor revenues
	    @RequestMapping(value="/getRevenueVendors", method=RequestMethod.POST)
		@ResponseBody
		public List<Long> getRevenueVendors(@RequestBody VendorProductRevenueRequest vendorProductRevenueRequest) throws Exception {
					
	    	LOGGER.debug("Entered getRevenueVendors");
	    	
	    	Date startDate = vendorProductRevenueRequest.getStartDate();
	    	Date endDate   = vendorProductRevenueRequest.getEndDate();
	    	
	    	List<BigInteger> revenueVendorIds = orderService.findRevenueVendors(startDate,endDate);
	    	
	    	LOGGER.debug("RevenueVendorIds size "+revenueVendorIds.size());
	    	
	    	List<Long> revVendorIds = new ArrayList<Long>();
	    	for(BigInteger revenueVendor : revenueVendorIds) {
	    		
	    		revVendorIds.add(revenueVendor.longValue());
	    	}
	    	
	    	LOGGER.debug("Entered getRevenueVendors");
	    	return revVendorIds;
	    	
	    }
	    
	    @RequestMapping(value="/getProductVendors", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		public PaginatedRevenueResponse getProductVendors(@RequestBody VendorProductRevenueRequest vendorProductRevenueRequest,
				@RequestParam(value="pageNumber", defaultValue = "1") int page ,
                @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
			
	    	LOGGER.debug("Entered getProductVendors");		
	    	
	    	PaginatedRevenueResponse paginatedRevenueResponse = new PaginatedRevenueResponse(); 
	    	
	    	try {
	    	Date startDate = vendorProductRevenueRequest.getStartDate();
	    	Date endDate   = vendorProductRevenueRequest.getEndDate();
	    	
	    	
	    	List<ProductVendorRevenueVO> productVendorRevenueVOList  = new ArrayList<ProductVendorRevenueVO>();
	    	List<ProductVendors> productVendorsList = new ArrayList<ProductVendors>();
	    	
	    	ProductVendorRevenueVO productVendorRevenueVO = new ProductVendorRevenueVO();
	    	
	    	productVendorRevenueVO.setProductSku(vendorProductRevenueRequest.getProductSku());
	    	
	    	Language language = languageService.getById(1);
    		
    		Product product = productService.getByCode(vendorProductRevenueRequest.getProductSku(),language);
	    	
    		productVendorRevenueVO.setProductName(product.getProductDescription().getName());
	    	
	    	LOGGER.debug("Product Sku: "+vendorProductRevenueRequest.getProductSku());
	    	
	    	List<Order> vendorAssociatedOrders =  orderService.findOrdersByProduct(startDate,endDate,vendorProductRevenueRequest.getProductSku());
	    	
	    	int total = 0;
	    	for(Order order : vendorAssociatedOrders) {
	    		
	    		Set<OrderProduct> orderProducts = order.getOrderProducts();
	    		
	    		for(OrderProduct orderProduct : orderProducts){
	    		 
	    			 ProductVendors  productVendors = new ProductVendors();
	    			 BigDecimal  productTotal = orderProduct.getOneTimeCharge().multiply(new BigDecimal(orderProduct.getProductQuantity()));
	    			 total = total+productTotal.intValue();
	    			 
	    			 Customer vendor = customerService.getById(orderProduct.getVendorId()); 
	    			 
	    			 productVendors.setVendorId(vendor.getId());
	    			 productVendors.setVendorName(vendor.getVendorAttrs().getVendorName());
	    			 productVendors.setOrderId(orderProduct.getOrder().getId());
	    			 
	    			 productVendorsList.add(productVendors);
	    			 productVendorRevenueVO.setProductVendors(productVendorsList);
	    			 productVendorRevenueVO.setTotalRevenue(total);
	    
	    		}
	    	}
	    	
	    	productVendorRevenueVOList.add(productVendorRevenueVO);
	    	
	    	LOGGER.debug("ProductVendorRevenueVOList size "+productVendorRevenueVOList.size());
	    	
	    	PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, productVendorRevenueVOList.size());
        	paginatedRevenueResponse.setPaginationData(paginaionData);
    		if(productVendorRevenueVOList == null || productVendorRevenueVOList.isEmpty() || productVendorRevenueVOList.size() < paginaionData.getCountByPage()){
    			paginatedRevenueResponse.setProductVendorRevenueData(productVendorRevenueVOList);
    			return paginatedRevenueResponse;
    		}
        	List<ProductVendorRevenueVO> paginatedResponses = productVendorRevenueVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedRevenueResponse.setProductVendorRevenueData(paginatedResponses);
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		LOGGER.error("Error while retrieving product vendors "+e.getMessage());
	    		paginatedRevenueResponse.setErrorMsg("Error while retrieving product vendors");
	    		paginatedRevenueResponse.setStatus("false");
	    		return paginatedRevenueResponse;
	    		
	    	}
	    	return paginatedRevenueResponse;
	    	
	    }
	    // retrieval of product ids for product revenues
	    @RequestMapping(value="/getRevenueProducts", method=RequestMethod.POST)
		@ResponseBody
		public List<String> getRevenueProducts(@RequestBody VendorProductRevenueRequest vendorProductRevenueRequest) throws Exception {
			
	    	LOGGER.debug("Entered getOrderProducts");
	    	
	    	Date startDate = vendorProductRevenueRequest.getStartDate();
	    	Date endDate   = vendorProductRevenueRequest.getEndDate();
	    	
	    	//List<Long> productIdsList = new ArrayList<Long>();
	    	
	    	List<String> productSkus = orderService.findProductSkus(startDate, endDate);
	    	/*for(String productSku : productSkus) {
	    		Language language = languageService.getById(1);
	    		
	    		Product product = productService.getByCode(productSku,language);
	    		Long productId = product.getId();
	    		productIdsList.add(productId);
	    		
	    	}*/
	    	//return productIdsList;
	    	return productSkus;
	    	
	    }
	    
	    // Activating vendor means that the vendors  are listing for a products
	    // relating to the vendor
	    @RequestMapping(value="/activateVendor", method=RequestMethod.POST)
		@ResponseBody
		public ActivateVendorResponse activateVendor(@RequestBody ActivateVendorRequest activateVendorRequest) throws Exception {
			
	    	LOGGER.debug("Entered activateVendor");
	    	
	    	ActivateVendorResponse activateVendorResponse = new ActivateVendorResponse();
	    	
	    	Long vendorId = activateVendorRequest.getVendorId();
	    	
	    	try {
	    		
	    	Customer vendor = customerService.getById(vendorId);
	    	
	    	vendor.setActivated("1");
	    	vendor.setIsVendorActivated("Y");
	    	
	    	customerService.saveOrUpdate(vendor);
	    	
	    	LOGGER.debug("Vendor Activated");
	    	
	    	activateVendorResponse.setSuccessMessage("Vendor approved successfully");
	    	activateVendorResponse.setStatus(TRUE);
	    	
	    	}catch(Exception e) {
	    		LOGGER.debug("Error while activating vendor "+e.getMessage());
	    		activateVendorResponse.setErrorMessage("Error while activating vendor");
	    		activateVendorResponse.setStatus(FALSE);
	    		return activateVendorResponse;
	    	}
	    	
	    	return activateVendorResponse;
	    	
	    }
	
	    // In-activating vendor means that the vendors are not listing for a products
	    // relating to the vendor
	    @RequestMapping(value="/inActivateVendor", method=RequestMethod.POST)
		@ResponseBody
		public ActivateVendorResponse inActivateVendor(@RequestBody ActivateVendorRequest activateVendorRequest) throws Exception {
			
	    	LOGGER.debug("Entered activateVendor");
	    	
	    	ActivateVendorResponse activateVendorResponse = new ActivateVendorResponse();
	    	
	    	Long vendorId = activateVendorRequest.getVendorId();
	    	
	    	try {
	    		
	    	Customer vendor = customerService.getById(vendorId);
	    	
	    	vendor.setActivated("1");
	    	vendor.setIsVendorActivated("N");
	    	
	    	customerService.saveOrUpdate(vendor);
	    	
	    	LOGGER.debug("Vendor Activated");
	    	
	    	activateVendorResponse.setSuccessMessage("Vendor inactivated successfully");
	    	activateVendorResponse.setStatus(TRUE);
	    	
	    	}catch(Exception e) {
	    		LOGGER.debug("Error while deactivating vendor "+e.getMessage());
	    		activateVendorResponse.setErrorMessage("Error while inactivating vendor");
	    		activateVendorResponse.setStatus(FALSE);
	    		return activateVendorResponse;
	    	}
	    	
	    	return activateVendorResponse;
	    	
	    }
	    
	    // Retrieval of paid and unpaid vendors based on status and customer type
	    // here status may be N/Y/ALL and customer type may be 1/2/3/4/5/ALL
	    @RequestMapping(value="/getPaidOrUnpaidVendors", method = RequestMethod.POST)
		@ResponseBody
		public PaginatedResponse getPaidOrUnpaidVendors(@RequestParam(value="pageNumber", defaultValue = "1") int page ,
				                  @RequestParam(value="pageSize", defaultValue="15") int size,
				                  @RequestBody AdminApprovedVendorsRequest adminApprovedVendorsRequest) throws Exception{
									
	    	LOGGER.debug("Entered getPaidOrUnpaidVendors");
	    	
            PaginatedResponse paginatedResponse = new PaginatedResponse();
			
			List<VendorDetailsVO> vendorDetailsVOList = new ArrayList<VendorDetailsVO>();
			
			List<Customer> vendorsList = null;
			
			try {
				
			if(adminApprovedVendorsRequest.getStatus().equals("ALL")) {
				
				if(adminApprovedVendorsRequest.getCustomerType().equals("ALL")){
				vendorsList = customerService.getAllPaidOrUnPaidVendors();
				}
				else {
					vendorsList = customerService.getPaidOrUnPaidtVendorsByCustomerType(adminApprovedVendorsRequest.getCustomerType());
				}
				
				for(Customer vendor : vendorsList) {
					
					VendorDetailsVO vendorDetailsVO = new VendorDetailsVO();
					vendorDetailsVO.setVendorId(vendor.getId());
					
					vendorDetailsVO.setVendorName(vendor.getVendorAttrs().getVendorName());
					vendorDetailsVO.setStatus(vendor.getIsVendorActivated());
					
					vendorDetailsVO.setVendorUserProfile(vendor.getUserProfile());
					
					if(vendor.getCustomerType().equals("1"))
						vendorDetailsVO.setVendorType(Constants.PRODUCT_VENDORS);
					if(vendor.getCustomerType().equals("2")) 
						vendorDetailsVO.setVendorType(Constants.SERVICE_PROVIDER);
					if(vendor.getCustomerType().equals("3")) 
						vendorDetailsVO.setVendorType(Constants.ARCHITECTS);
					if(vendor.getCustomerType().equals("4")) 
						vendorDetailsVO.setVendorType(Constants.WALLPAPER);
					if(vendor.getCustomerType().equals("5"))
					    vendorDetailsVO.setVendorType(Constants.MACHINERY_EQUIPMENT);
					
					vendorDetailsVOList.add(vendorDetailsVO);
				}
			} else {
				
				if(adminApprovedVendorsRequest.getCustomerType().equals("ALL")) {
				vendorsList = customerService.getPaidOrUnPaidVendorsBasedOnStatus(adminApprovedVendorsRequest.getStatus());
				}
				else{
					vendorsList = customerService.getPaidOrUnPaidVendorsBasedOnStatusAndCustomerType(adminApprovedVendorsRequest.getStatus(),adminApprovedVendorsRequest.getCustomerType());
				}
				
				for(Customer vendor : vendorsList) {
					
					VendorDetailsVO vendorDetailsVO = new VendorDetailsVO();
					vendorDetailsVO.setVendorId(vendor.getId());
					
					vendorDetailsVO.setVendorName(vendor.getVendorAttrs().getVendorName());
					vendorDetailsVO.setStatus(vendor.getIsVendorActivated());
					
					vendorDetailsVO.setVendorUserProfile(vendor.getUserProfile());
					
					if(vendor.getCustomerType().equals("1"))
						vendorDetailsVO.setVendorType(Constants.PRODUCT_VENDORS);
					if(vendor.getCustomerType().equals("2")) 
						vendorDetailsVO.setVendorType(Constants.SERVICE_PROVIDER);
					if(vendor.getCustomerType().equals("3")) 
						vendorDetailsVO.setVendorType(Constants.ARCHITECTS);
					if(vendor.getCustomerType().equals("4")) 
						vendorDetailsVO.setVendorType(Constants.WALLPAPER);
					if(vendor.getCustomerType().equals("5"))
					    vendorDetailsVO.setVendorType(Constants.MACHINERY_EQUIPMENT);
					
					vendorDetailsVOList.add(vendorDetailsVO);
			   }
			}
				PaginationData paginaionData=createPaginaionData(page,size);
	        	calculatePaginaionData(paginaionData,size, vendorDetailsVOList.size());
	        	paginatedResponse.setPaginationData(paginaionData);
	    		if(vendorDetailsVOList == null || vendorDetailsVOList.isEmpty() || vendorDetailsVOList.size() < paginaionData.getCountByPage()){
	    			paginatedResponse.setResponseData(vendorDetailsVOList);
	    			return paginatedResponse;
	    		}
	        	List<VendorDetailsVO> paginatedResponses = vendorDetailsVOList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	        	paginatedResponse.setResponseData(paginatedResponses);
			}catch(Exception e) {
				e.printStackTrace();
				LOGGER.error("error while retrieving paid or unpaid vendors"+e.getMessage());
				paginatedResponse.setErrorMsg("error while retrieving paid or unpaid vendors");
			}
			
			LOGGER.debug("Ended getPaidOrUnpaidVendors");
			return paginatedResponse;
	
	    }
	    // Home page offers
	    @RequestMapping(value="/homePageOffers", method = RequestMethod.POST)
		@ResponseBody
		public HomePageOffersResponse homePageOffers(@RequestPart("homePageOffersRequest") String homePageOffersRequestStr,
				@RequestPart("file") MultipartFile catImage) throws Exception {
	    	
	    	LOGGER.debug("Entered uploadOrUpdateSubCatImage ");
	    	
	    	HomePageOffersRequest homePageOffersRequest = new ObjectMapper().readValue(homePageOffersRequestStr, HomePageOffersRequest.class);
	    	HomePageOffersResponse homePageOffersResponse = new HomePageOffersResponse();
	    	
	    	
	    	String fileName = "";
	    	
	    	try { 
	    		
	    		List<HomePageOffers> dbHomePageOffers = homePageOffersService.getAllHomePageOffers();
	    		
	    		for(HomePageOffers homePageOffer : dbHomePageOffers) {
	    			
	    			if(homePageOffer.getSectionName().equals(homePageOffersRequest.getSectionName())) {
	    				
	    				// Storing image
	    		    	if(catImage.getSize() != 0) {
	    		    		try{
	    		    			LOGGER.debug("Storing category image");
	    		    			fileName = storageService.store(catImage,"homecatimg");
	    		    			System.out.println("fileName "+fileName);
	    		    		}catch(StorageException se){
	    		    			LOGGER.error("StoreException occured"+se.getMessage());
	    		    			homePageOffersResponse.setErrorMessage("Failed while storing image");
	    		    			homePageOffersResponse.setStatus(FALSE);
	    		    			return homePageOffersResponse;
	    		    		}
	    		    	}
	    				homePageOffer.setCategoryImageURL(fileName);
	    	    		
	    	    		if(!homePageOffersRequest.getCategoryName().equals(null))
	    	    			homePageOffer.setCategoryName(homePageOffersRequest.getCategoryName());
	    	    		
	    	    		if(!homePageOffersRequest.getSubCategoryName().equals(null))
	    	    			homePageOffer.setSubCategoryName(homePageOffersRequest.getSubCategoryName());
	    	    		
	    	    		homePageOffer.setDescription(homePageOffersRequest.getDescription());
	    	    		homePageOffer.setDiscount(homePageOffersRequest.getDiscount());
	    	    		homePageOffer.setCategoryTitle(homePageOffersRequest.getCategoryTitle());
	    	    		homePageOffer.setSectionName(homePageOffersRequest.getSectionName());
	    	    		
	    	    		homePageOffersService.update(homePageOffer);
	    	    		
	    	    		LOGGER.debug("Image uploaded");
	    				homePageOffersResponse.setCategoryName(homePageOffersRequest.getCategoryName());
	    				homePageOffersResponse.setCategoryImageURL(fileName);
	    				homePageOffersResponse.setSuccessMessage("Category Image updated successfully");
	    				homePageOffersResponse.setStatus(TRUE);
	    				
	    				return homePageOffersResponse;
	    				
	    			}
	    		
	    	}	
	    		// Storing image
		    	if(catImage.getSize() != 0) {
		    		try{
		    			LOGGER.debug("Storing category image");
		    			fileName = storageService.store(catImage,"homecatimg");
		    			System.out.println("fileName "+fileName);
		    		}catch(StorageException se){
		    			LOGGER.error("StoreException occured"+se.getMessage());
		    			homePageOffersResponse.setErrorMessage("Failed while storing image");
		    			homePageOffersResponse.setStatus(FALSE);
		    			return homePageOffersResponse;
		    		}
		    	}
		          HomePageOffers homePageOffers = new HomePageOffers();
		
		          homePageOffers.setCategoryImageURL(fileName);
		
		         if(!homePageOffersRequest.getCategoryName().equals(null))
		            homePageOffers.setCategoryName(homePageOffersRequest.getCategoryName());
		
		         if(!homePageOffersRequest.getSubCategoryName().equals(null))
		            homePageOffers.setSubCategoryName(homePageOffersRequest.getSubCategoryName());
		
		            homePageOffers.setDescription(homePageOffersRequest.getDescription());
		            homePageOffers.setDiscount(homePageOffersRequest.getDiscount());
		            homePageOffers.setCategoryTitle(homePageOffersRequest.getCategoryTitle());
		            homePageOffers.setSectionName(homePageOffersRequest.getSectionName());
		
	
		            homePageOffersService.save(homePageOffers);
		            LOGGER.debug("Image uploaded");
		            homePageOffersResponse.setCategoryName(homePageOffersRequest.getCategoryName());
		            homePageOffersResponse.setCategoryImageURL(fileName);
		            homePageOffersResponse.setSuccessMessage("Category Image uploaded successfully");
		            homePageOffersResponse.setStatus(TRUE);
    		}
		    catch(Exception e){ 
			LOGGER.error("Error while storing category image");
			if(StringUtils.isEmpty(fileName)){
				storageService.deleteFile(fileName); // delete image
			}
			homePageOffersResponse.setStatus(FALSE);
			homePageOffersResponse.setErrorMessage("Error while storing category image");
		    }
			return homePageOffersResponse;
	    }
	    
	    @RequestMapping(value="/getHomePageOffers", method = RequestMethod.GET)
		@ResponseBody
		public PaginatedResponse getHomePageOffers(@RequestParam(value="pageNumber", defaultValue = "1") int page ,
				                  @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception{
									
	    	LOGGER.debug("Entered getHomePageOffers");
	    	
	    	PaginatedResponse paginatedResponse = new PaginatedResponse();
	    	
	    	try{
	    		
	    	List<HomePageOffers> homePageOffers = homePageOffersService.getAllHomePageOffers();
	    	
	    	List<HomePageOffersVO> homePageOffersList = new ArrayList<HomePageOffersVO>();
	    	
	    	for(HomePageOffers homePageOffer : homePageOffers ) {
	    		
	    		HomePageOffersVO homePageOffersVO = new HomePageOffersVO();
	    		
	    		homePageOffersVO.setId(homePageOffer.getId());
	    		
	    		if(!homePageOffer.getCategoryName().equals(null))
	    		homePageOffersVO.setCategoryName(homePageOffer.getCategoryName());
	    		
	    		if(!homePageOffer.getSubCategoryName().equals(null))
	    		homePageOffersVO.setSubCategoryName(homePageOffer.getSubCategoryName());
	    		
	    		homePageOffersVO.setDiscount(homePageOffer.getDiscount());
	    		homePageOffersVO.setDescription(homePageOffer.getDescription());
	    		homePageOffersVO.setSectionName(homePageOffer.getSectionName());
	    		homePageOffersVO.setCategoryImageURL(homePageOffer.getCategoryImageURL());
	    		homePageOffersVO.setCategoryTitle(homePageOffer.getCategoryTitle());
	    		
	    		homePageOffersList.add(homePageOffersVO);
	    	}
	    	
	    	PaginationData paginaionData=createPaginaionData(page,size);
        	calculatePaginaionData(paginaionData,size, homePageOffersList.size());
        	paginatedResponse.setPaginationData(paginaionData);
    		if(homePageOffersList == null || homePageOffersList.isEmpty() || homePageOffersList.size() < paginaionData.getCountByPage()){
    			paginatedResponse.setResponseData(homePageOffersList);
    			return paginatedResponse;
    		}
        	List<HomePageOffersVO> paginatedResponses = homePageOffersList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
        	paginatedResponse.setResponseData(paginatedResponses);
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		LOGGER.debug("Error while retrieving home page offers "+e.getMessage());
	    		paginatedResponse.setErrorMsg("Error while retrieving home page offers");
	    		return paginatedResponse;
	    	}
	    	
	    	LOGGER.debug("Ended getHomePageOffers");
	    	return paginatedResponse;
	    	
	    	
	    }
}
    
 

