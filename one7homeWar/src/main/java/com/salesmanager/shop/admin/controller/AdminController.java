package com.salesmanager.shop.admin.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.category.SubCategoryService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.price.ProductPriceService;
import com.salesmanager.core.business.services.catalog.product.type.ProductTypeService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.customer.testmonial.review.CustomerTestmonialService;
import com.salesmanager.core.business.services.image.brand.BrandImageService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.business.utils.ProductPriceUtils;
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
import com.salesmanager.core.model.image.brand.BrandImage;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.country.CountryDescription;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.user.User;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.fileupload.services.StorageException;
import com.salesmanager.shop.fileupload.services.StorageService;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.DateUtil;

@Controller
@CrossOrigin
public class AdminController extends AbstractController {
	
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	
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
		
		//List<TaxClass> taxClasses = taxClassService.listByStore(store);
		
		//List<Language> languages = store.getLanguages();
		
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();


			//Product dbProduct = productService.getById(productId);
			
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
	public AdminVendorProductResponse getAdminVendorProducts(@PathVariable String vendorId) throws Exception {
    	LOGGER.debug("Entered getAdminVendorProducts");
    	AdminVendorProductResponse adminVendorProductResponse = new AdminVendorProductResponse();
    	Long  vId = Long.parseLong(vendorId);
    	List<VendorProduct> vendorProducts = vendorProductService.findProductsByVendor(vId);
    	if(vendorProducts==null) {
    		adminVendorProductResponse.setErrorMsg("Vendor products not found");
    		return adminVendorProductResponse;
    	}
    	
    	List<VendorProductVO> vproductList = new ArrayList<VendorProductVO>();
    	for(VendorProduct vendorProduct : vendorProducts) {
    		VendorProductVO vendorProductVO = new VendorProductVO();
    		vendorProductVO.setVendorId(vendorProduct.getCustomer().getId());
    		vendorProductVO.setVendorName(vendorProduct.getCustomer().getNick());
    		vendorProductVO.setProductId(vendorProduct.getProduct().getId());
    		vendorProductVO.setProductName(vendorProduct.getProduct().getProductDescription().getName());
    		vproductList.add(vendorProductVO);
    	}
    	
    	adminVendorProductResponse.setVendorProducts(vproductList);
    	LOGGER.debug("Ended getAdminVendorProducts");
    	return adminVendorProductResponse;
    	
    }
    
    /*//show vendor products under admin gui so that admin can approve vendor products 
    @RequestMapping(value="/admin/vendor/products", method = RequestMethod.GET)
	@ResponseBody
	public AdminVendorProductResponse getVendorProducts() throws Exception {
    	System.out.println("getVendorProducts() : ");
    	AdminVendorProductResponse adminVendorProductResponse = new AdminVendorProductResponse();
    	List<VendorProduct> vendorProducts = vendorProductService.getVendorProducts();
    	if(vendorProducts==null) {
    		adminVendorProductResponse.setErrorMsg("Vendor products not found");
    		return adminVendorProductResponse;
    	}
    	System.out.println("VendorProducts: "+vendorProducts);
    	List<VendorProductVO> vproductList = new ArrayList<VendorProductVO>();
    	for(VendorProduct vendorProduct : vendorProducts) {
    		System.out.println("vendorProduct"+vendorProduct);
    		VendorProductVO vendorProductVO = new VendorProductVO();
    		vendorProductVO.setVendorProductId(vendorProduct.getId());
    		vendorProductVO.setVendorId(vendorProduct.getCustomer().getId());
    		System.out.println("vendorProduct.getCustomer().getId()"+vendorProduct.getCustomer().getId());
    		if (!(vendorProduct.getCustomer().getVendorAttrs().getVendorName().equals(null))){
    		vendorProductVO.setVendorName(vendorProduct.getCustomer().getVendorAttrs().getVendorName());
    		}
    		vendorProductVO.setProductId(vendorProduct.getProduct().getId());
    		vendorProductVO.setProductName(vendorProduct.getProduct().getProductDescription().getName());
    		//vendorProductVO.setDescription(vendorProduct.getProduct().getProductDescription().getDescription());
    		vproductList.add(vendorProductVO);
    	}
    	adminVendorProductResponse.setVendorProducts(vproductList);
    	return adminVendorProductResponse;
    }*/
    
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
    		System.out.println("vendorProduct :"+vendorProduct);
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
    		return activateProductResponse;
    	}
    	// Approving and updating vendor product
    	vendorProduct.setAdminActivatedDate(new Date());
    	vendorProduct.setAdminActivated(activateProductRequest.isStatus());
    	vendorProductService.update(vendorProduct);
    	if(vendorProduct.isAdminActivated()==true) {
    		activateProductResponse.setSuccessMsg("Activated");
    		activateProductResponse.setStatus("true");
    	} else {
    		activateProductResponse.setErrorMesg("Declined");
    		activateProductResponse.setStatus("false");
    	}
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
    /*@RequestMapping(value="/uploadSubCatImage", method = RequestMethod.POST)
	@ResponseBody
	public SubCatImageResponse uploadSubCatImage(@RequestPart("subCatImageRequest") String subCatImageRequestStr,
			@RequestPart("file") MultipartFile subCatImage) throws Exception {
    	System.out.println("Entered uploadSubCatImage");
    	SubCatImageRequest subCatImageRequest = new ObjectMapper().readValue(subCatImageRequestStr, SubCatImageRequest.class);
    	SubCatImageResponse subCatImageResponse = new SubCatImageResponse();
    	Category subCategory = categoryService.getByCategoryCode(subCatImageRequest.getSubCategoryName());
    	String fileName = "";
    	// Storing uploaded img 
    	if(subCatImage.getSize() != 0) {
    		try{
    			fileName = storageService.store(subCatImage,"subcategoryimg");
    			System.out.println("fileName "+fileName);
    		}catch(StorageException se){
    			System.out.println("StoreException occured, do wee need continue "+se);
    			subCatImageResponse.setErrorMessage("Failed while storing image");
    			subCatImageResponse.setStatus("false");
    			return subCatImageResponse;
    		}
    	}
    		try {	
				SubCategoryImage subCategoryImage = new SubCategoryImage();
				subCategoryImage.setSubCategoryImageURL(fileName);
				subCategoryImage.setCategory(subCategory);
				System.out.println("Sub category image url::"+fileName);
				System.out.println("sub category id::"+subCategory.getId());
				
				subCategoryService.save(subCategoryImage);
				
				subCatImageResponse.setSubCategoryId(subCategory.getId());
				subCatImageResponse.setSubCatImgURL(fileName);
				subCatImageResponse.setSuccessMessage("SubCategory Image uploaded successfully");
				subCatImageResponse.setStatus("true");
					
    		}
		catch(Exception e){
			e.printStackTrace();
			subCatImageResponse.setStatus("false");
			subCatImageResponse.setErrorMessage("Error while storing sub category image");
		}
    
		return subCatImageResponse;
    }*/
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
   /* @RequestMapping(value="/updateSubCatImage", method = RequestMethod.POST)
	@ResponseBody
	public SubCatImageResponse updateSubCatImage(@RequestPart("subCatImageRequest") String subCatImageRequestStr,
			@RequestPart("file") MultipartFile subCatImage) throws Exception {
				
    	SubCatImageRequest subCatImageRequest = new ObjectMapper().readValue(subCatImageRequestStr, SubCatImageRequest.class);
    	SubCatImageResponse subCatImageResponse = new SubCatImageResponse();
    	Category subCategory = categoryService.getByCategoryCode(subCatImageRequest.getSubCategoryName());
    	String fileName = "";
    	// Storing uploaded img 
    	if(subCatImage.getSize() != 0) {
    		try{
    			fileName = storageService.store(subCatImage,"subcategoryimg");
    			System.out.println("fileName "+fileName);
    		}catch(StorageException se){
    			System.out.println("StoreException occured, do wee need continue "+se);
    			subCatImageResponse.setErrorMessage("Failed while storing image");
    			subCatImageResponse.setStatus("false");
    			return subCatImageResponse;
    		}
    	}
    		try {	
    			SubCategoryImage subCategoryImage = subCategoryService.getByCategoryId(subCategory.getId());
    			System.out.println("subCategoryImage id::"+subCategoryImage.getCategory().getId());
				subCategoryImage.setSubCategoryImageURL(fileName);
				subCategoryImage.setCategory(subCategory);
				System.out.println("Sub category image url::"+fileName);
				System.out.println("sub category id::"+subCategory.getId());
				
				subCategoryService.update(subCategoryImage);
				
				subCatImageResponse.setSubCategoryId(subCategory.getId());
				subCatImageResponse.setSubCatImgURL(fileName);
				subCatImageResponse.setSuccessMessage("SubCategory Image updated successfully");
				subCatImageResponse.setStatus("true");
					
    		}
		catch(Exception e){
			e.printStackTrace();
			subCatImageResponse.setStatus("false");
			subCatImageResponse.setErrorMessage("Error while storing updating sub category image");
		}
    
		return subCatImageResponse;
    	
    }*/
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
   
/*    // Save Testimonial
    @RequestMapping(value="/testimonial/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
  	@ResponseBody
  	public TestimonialResponse saveTestimonial(@RequestBody TestimonialRequest testimonialRequest) throws Exception {
    	LOGGER.debug("Entered saveTestimonial");
    	TestimonialResponse testimonialResponse = new TestimonialResponse();
    	if(StringUtils.isEmpty(testimonialRequest.getTestmonialDescription())){
    		testimonialResponse.setErrorMessage("Feedback cannot be empty");
    		testimonialResponse.setStatus(FALSE);
    		return testimonialResponse;
    	}
    	
    	Customer customer = customerService.getById(testimonialRequest.getCustomerId());
    	CustomerTestimonial customerTestimonial = new CustomerTestimonial();
    	customerTestimonial.setCustomer(customer);
    	customerTestimonial.setDescription(testimonialRequest.getTestmonialDescription());
    	customerTestimonial.setEnable(false);
    	customerTestmonialService.save(customerTestimonial);
    	testimonialResponse.setSuccessMessage("Feedback Saved successfully");
    	LOGGER.debug("Testimonial saved");
    	testimonialResponse.setStatus(TRUE);
    	LOGGER.debug("Ended saveTestimonial");
    	return testimonialResponse;
    }
    @RequestMapping(value="/getAllTestimonials", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminTestimonialResponse getAllTestimonials() throws Exception {
    	LOGGER.debug("Entered getAllTestimonials");
    	AdminTestimonialResponse adminTestimonialResponse = new AdminTestimonialResponse();
    	List<CustomerTestimonialVO>  customerTestimonialVOList = new ArrayList<CustomerTestimonialVO>();
    	List<CustomerTestimonial> customerTestimonials = customerTestmonialService.getAllTestimonials();
    	for(CustomerTestimonial testmonial : customerTestimonials) {
    		CustomerTestimonialVO customerTestimonialVO = new CustomerTestimonialVO();
    		customerTestimonialVO.setCustomerId(testmonial.getCustomer().getId());
    		customerTestimonialVO.setCustomerName(testmonial.getCustomer().getBilling().getFirstName());
    		customerTestimonialVO.setEmailAddress(testmonial.getCustomer().getEmailAddress());
    		customerTestimonialVO.setDescription(testmonial.getDescription());
    		customerTestimonialVO.setTestimonialId(testmonial.getId());
    		customerTestimonialVO.setEnable(testmonial.isEnable());
    		customerTestimonialVOList.add(customerTestimonialVO);
    	}
    	adminTestimonialResponse.setCustomerTestimonials(customerTestimonialVOList);
    	LOGGER.debug("Ended getAllTestimonials");
    	return adminTestimonialResponse;
    	
    }
    // Approve customer testimonials
    @RequestMapping(value="/approve/testimonial", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ApproveTestimonialResponse approveTestimonial(@RequestBody ApproveTestimonialRequest approveTestimonialRequest) throws Exception {
		LOGGER.debug("Entered approveTestimonial");
		ApproveTestimonialResponse approveTestimonialResponse = new ApproveTestimonialResponse();
		Long testimonialIdLong = approveTestimonialRequest.getTestimonialId();
		CustomerTestimonial customerTestimonial = customerTestmonialService.getTestimonialById(testimonialIdLong);
		//if(approveTestimonialRequest.isEnable()) {
			customerTestimonial.setEnable(approveTestimonialRequest.isEnable());
		//}
		//else {
			//customerTestimonial.setEnable(false);
		//}
		try {
			customerTestmonialService.update(customerTestimonial);
			LOGGER.debug("Testimonial approved");
			approveTestimonialResponse.setSuccessMessage("Testimonial approved successfully");
			approveTestimonialResponse.setStatus(TRUE);
		} catch (Exception e) {
			LOGGER.error("Error in updating Testimonial");
			approveTestimonialResponse.setErrorMessage("Error in approving Testimonial");
			approveTestimonialResponse.setStatus(FALSE);
			return approveTestimonialResponse;
		}
		LOGGER.debug("Ended approveTestimonial");
    	return approveTestimonialResponse;
    } 
    // Retrieve Admin Approved customer testimonials
    @RequestMapping(value="/getApprovedTestimonials", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminApproveTestimonialResponse getAdminApproveTestimonials() {
    	LOGGER.debug("Entered getAdminApproveTestimonials");
    	AdminApproveTestimonialResponse adminApproveTestimonialResponse = new AdminApproveTestimonialResponse();
    	List<AdminApproveTestimonialVO> adminApproveTestimonialVOList = new ArrayList<AdminApproveTestimonialVO>();
    	List<CustomerTestimonial> approvedTestimonials = customerTestmonialService.getApprovedTestimonial();
    	for(CustomerTestimonial approvedTestimonial : approvedTestimonials) {
    		AdminApproveTestimonialVO adminApproveTestimonialVO = new AdminApproveTestimonialVO();
    		adminApproveTestimonialVO.setCustomerId(approvedTestimonial.getCustomer().getId());
    		adminApproveTestimonialVO.setCustomerName(approvedTestimonial.getCustomer().getBilling().getFirstName());
    		adminApproveTestimonialVO.setEmailAddress(approvedTestimonial.getCustomer().getEmailAddress());
    		adminApproveTestimonialVO.setDescription(approvedTestimonial.getDescription());
    		adminApproveTestimonialVO.setEnable(approvedTestimonial.isEnable());
    		adminApproveTestimonialVO.setTestimonialId(approvedTestimonial.getId());
    		adminApproveTestimonialVOList.add(adminApproveTestimonialVO);
    	}
    	adminApproveTestimonialResponse.setApprovedTestimonials(adminApproveTestimonialVOList);
    	LOGGER.debug("Ended getAdminApproveTestimonials");
    	return adminApproveTestimonialResponse;	
    }*/
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
				LOGGER.debug("Testimonial approved");
			approveTestimonialResponse.setSuccessMessage("Testimonial enabled successfully");
			approveTestimonialResponse.setStatus(TRUE);
			}
			if(approveTestimonialRequest.getStatus().equals("N")) {
				LOGGER.debug("Testimonial declined");
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
    @RequestMapping(value="/getTestimonials", method = RequestMethod.POST)
	@ResponseBody
	public PaginatedResponse getTestimonials(@RequestBody AdminTestimonialsRequest adminTestimonialsRequest,@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) {
    	LOGGER.debug("Entered getTestimonials");
    	AdminTestimonialResponse adminTestimonialResponse = new AdminTestimonialResponse();
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
    		adminTestimonialResponse.setCustomerTestimonials(customerTestimonialVOList);
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
    		adminTestimonialResponse.setCustomerTestimonials(customerTestimonialVOList);
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
    		adminTestimonialResponse.setCustomerTestimonials(customerTestimonialVOList);
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
		//return adminTestimonialResponse;
    }
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
    			brandImageObj.setStatus("N");
    			brandImageService.save(brandImageObj);
    			LOGGER.debug("Brand Image uploaded");
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
		brandImageService.delete(brandImage);
		LOGGER.debug("Brand Image deleted");
		deleteBrandImageResponse.setSuccessMessage("Brand Image deleted successfully");
		deleteBrandImageResponse.setStatus(TRUE);
		}catch(Exception e) {
			LOGGER.error("Error while deleting brand image",e.getMessage());
		}
    	return deleteBrandImageResponse;
    	
    }
    @RequestMapping(value="/getBrandImages", method = RequestMethod.POST)
    @ResponseBody
    public AdminBrandImageResponse getBrandImages(@RequestBody AdminBrandImageRequest adminBrandImageRequest) throws Exception {
		LOGGER.debug("Entered getBrandImages");
		AdminBrandImageResponse adminBrandImageResponse = new AdminBrandImageResponse();
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
    		adminBrandImageResponse.setBrandImages(brandImageVOList);
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
    		adminBrandImageResponse.setBrandImages(brandImageVOList);
    		
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
    		adminBrandImageResponse.setBrandImages(brandImageVOList);
    		
    	}
    	return adminBrandImageResponse;
    	

    }
}
    
 

