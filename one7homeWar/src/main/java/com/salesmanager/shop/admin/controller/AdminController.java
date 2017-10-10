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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.price.ProductPriceService;
import com.salesmanager.core.business.services.catalog.product.type.ProductTypeService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.description.ProductDescription;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.catalog.product.manufacturer.ManufacturerDescription;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.type.ProductType;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.core.model.user.User;
import com.salesmanager.shop.admin.controller.products.PaginatedResponse;
import com.salesmanager.shop.admin.controller.products.ProductResponse;
import com.salesmanager.shop.admin.controller.products.TodaysDeals;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.store.controller.customer.CustomerAccountController;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.DateUtil;

@Controller
@CrossOrigin
public class AdminController extends AbstractController {
	
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
    
    // Admin update store address
	@RequestMapping(value="/admin/updatestore", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminUpdateStoreResponse updateMerchantStore(@RequestBody AdminUpdateStoreRequest adminUpdateStoreRequest) {
	    AdminUpdateStoreResponse adminUpdateStoreResponse=new AdminUpdateStoreResponse();
	    MerchantStore merchantStore=null;
		try {
			merchantStore = merchantStoreService.getByCode(adminUpdateStoreRequest.getStoreCode());
			if(merchantStore==null) {
				adminUpdateStoreResponse.setErrorMessage("Store is not found,unable to update");
				adminUpdateStoreResponse.setStatus("false");
				return adminUpdateStoreResponse;
			}
		} catch (ServiceException e) {
			adminUpdateStoreResponse.setErrorMessage("Error in updating Store");
			adminUpdateStoreResponse.setStatus("false");
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
	    //merchantStore.setCountry(adminUpdateStoreRequest.getStoreCountry());
	    
	    System.out.println("adminUpdateStoreRequest.getStoreName()= "+adminUpdateStoreRequest.getStoreName());
	    System.out.println("adminUpdateStoreRequest.getStoreCode()= "+adminUpdateStoreRequest.getStoreCode());
	    System.out.println("adminUpdateStoreRequest.getStorePhone()= "+adminUpdateStoreRequest.getStorePhone());
	    System.out.println("adminUpdateStoreRequest.getEmailAddress()= "+adminUpdateStoreRequest.getEmailAddress());
	    System.out.println("adminUpdateStoreRequest.getStoreAddress()= "+adminUpdateStoreRequest.getStoreAddress());
	    System.out.println("adminUpdateStoreRequest.getStoreCity()= "+adminUpdateStoreRequest.getStoreCity());
	    System.out.println("adminUpdateStoreRequest.getStoreState()= "+adminUpdateStoreRequest.getStoreState());
	    System.out.println("adminUpdateStoreRequest.getStorePostalCode()= "+adminUpdateStoreRequest.getStorePostalCode());
	    try {
			merchantStoreService.update(merchantStore);
		} catch (ServiceException e) {
			e.printStackTrace();
			adminUpdateStoreResponse.setErrorMessage("Error in updating store");
			adminUpdateStoreResponse.setStatus("false");
			return adminUpdateStoreResponse;
		}
	        adminUpdateStoreResponse.setSuccessMessage("Store updated successfully");
	        adminUpdateStoreResponse.setStatus("true");
	        return adminUpdateStoreResponse;
	    
    }
	
	// Display store address
	@RequestMapping(value="/admin/getStore", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StoreInfoResponse getStoreInfo(HttpServletRequest request) {
		System.out.println("getStoreInfo: ");
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
	        storeInfo.setStoreCountry(merchantStore.getCountry().getIsoCode());
	        storeInfo.setStoreCode(merchantStore.getCode());
	        
	        System.out.println("merchantStore.getStorename()=="+merchantStore.getStorename());
	        System.out.println("merchantStore.getStoreaddress()=="+merchantStore.getStoreaddress());
	        System.out.println("merchantStore.getStoreEmailAddress()=="+merchantStore.getStoreEmailAddress());
	        System.out.println("merchantStore.getStorephone()=="+merchantStore.getStorephone());
	        System.out.println("merchantStore.getStorecity()=="+merchantStore.getStorecity());
	        System.out.println("merchantStore.getStorestateprovince()=="+merchantStore.getStorestateprovince());
	        System.out.println("merchantStore.getStorepostalcode()=="+merchantStore.getStorepostalcode());
	        System.out.println("merchantStore.getCountry().getIsoCode()=="+merchantStore.getCountry().getIsoCode());
	        System.out.println("merchantStore.getCode()=="+merchantStore.getCode());
	        
	        System.out.println("StoreInfo=  "+storeInfo);
	        
	        User user = userService.getById(1l);
	        storeInfo.setAdminName(user.getAdminName());
	        storeInfo.setLastAccess(user.getLastAccess());
	        storeInfoResponse.setStoreInfo(storeInfo);
	        
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		  
		    return storeInfoResponse;
		
	}
	// Admin edit details 
	@RequestMapping(value="/admin/update", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public EditUserAdminResponse updateAdmin(@RequestBody EditUserAdminRequest editUserAdminRequest)
        throws Exception {
		
		    System.out.println("editUserAdmin :");
		    EditUserAdminResponse editUserAdminResponse = new EditUserAdminResponse();
		    String stringId = editUserAdminRequest.getId();
		    Long longId = Long.parseLong(stringId);
		    //Getting admin by id
			User dbUser = userService.getById(longId);
			// Checing admin null
			if(dbUser==null) {
				editUserAdminResponse.setErrorMessage("Admin is null for this id: "+longId);
				editUserAdminResponse.setSucessMessage("false");
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
			
			editUserAdminResponse.setSucessMessage("Admin profile updated successfully");
			editUserAdminResponse.setStatus("true");
			
	        return editUserAdminResponse;
		
		
	}
	// Get list of admin
	@RequestMapping(value="/admin/list", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminListResponse getAdminList() throws Exception {
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
	       System.out.println("userVO: "+userVO);
	       userList.add(userVO);
	    }
	    System.out.println("userList :"+userList);
	    adminListResponse.setAdminList(userList);
		return adminListResponse;
		
	}
	
	// Admin password update
	@RequestMapping(value="/admin/updatepassword", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UpdatePasswordResp changePassword(@RequestBody UpdatePasswordReq updatePasswordReq) throws Exception {
		System.out.println("changePassword: ");
		UpdatePasswordResp updatePasswordResp = new UpdatePasswordResp();
		String stringId = updatePasswordReq.getId();
		Long longId = Long.parseLong(stringId);
		//User dbUser = userService.getById(longId);
		User dbUser = userService.getByEmail(updatePasswordReq.getEmailAddress());
		if(dbUser==null) {
			updatePasswordResp.setErrorMessage("Admin is not exist for this emailaddress: "+updatePasswordReq.getEmailAddress());
			updatePasswordResp.setStatus("false");
			return updatePasswordResp;
		}
		//encoding password and update password
		String pass = passwordEncoder.encode(updatePasswordReq.getNewPassword());
		dbUser.setAdminPassword(pass);
		userService.update(dbUser);	
		updatePasswordResp.setSuccessMessage("Password updated successfully");
		updatePasswordResp.setStatus("true");
		return updatePasswordResp;
	}
	
	// Admin add new products, feature products, recommended and recent bought to his store
	@RequestMapping(value="/admin/addOrRemove", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AddProductResp addProducts(@RequestBody AddProductReq addProductReq) throws Exception {
		System.out.println("addProducts: ");
		AddProductResp addProductResp = new AddProductResp();
		String productId = addProductReq.getProductId();
		Long longId = Long.parseLong(productId);
	    Product dbProduct = productService.getByProductId(longId);
	    if(dbProduct==null) {
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
	public List<AdminProductResponse> getProduct(@PathVariable String categoryCode,@PathVariable String title) throws Exception {
		System.out.println("getProduct : ");
		AdminProductResponse adminProductResponse = new AdminProductResponse();

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
			}
		}
		return responses;
	}

	public List<AdminProductResponse> invokeProductsData(Map<Long,Product> todaysDealsMap,
			String categoryId,List<AdminProductResponse> responses,
			AdminProductResponse adminProductResponse,List<Product> tdProducts,String title) throws Exception {
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
		return responses;
	}
public AdminProductResponse getProductDetails(Product dbProduct,boolean isSpecial,String title) throws Exception {
		
		System.out.println("merchantStoreService =="+merchantStoreService);
		
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
				
			adminProductResponse.setProductName(dbProduct.getProductDescription().getName());
			//productResponse.setVendorName(dbProduct.getManufacturer().getCode());
			
			/*Set<ManufacturerDescription> manufacturerDescription =  dbProduct.getManufacturer().getDescriptions();
			for(ManufacturerDescription description:manufacturerDescription){
				adminProductResponse.setVendorName(description.getName());
				adminProductResponse.setVendorLocation(description.getTitle());
			}*/
			
		return adminProductResponse;
	}

    private String getDiscountPercentage(ProductPrice productPrice){
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
    	System.out.println("Inside getAdminVendorProducts: ");
    	AdminVendorProductResponse adminVendorProductResponse = new AdminVendorProductResponse();
    	Long  vId = Long.parseLong(vendorId);
    	List<VendorProduct> vendorProducts = vendorProductService.findProductsByVendor(vId);
    	if(vendorProducts==null) {
    		adminVendorProductResponse.setErrorMsg("Vendor products not found");
    		return adminVendorProductResponse;
    	}
    	System.out.println("VendorProducts: "+vendorProducts);
    	List<VendorProductVO> vproductList = new ArrayList<VendorProductVO>();
    	for(VendorProduct vendorProduct : vendorProducts) {
    		VendorProductVO vendorProductVO = new VendorProductVO();
    		vendorProductVO.setVendorId(vendorProduct.getCustomer().getId());
    		vendorProductVO.setVendorName(vendorProduct.getCustomer().getNick());
    		vendorProductVO.setProductId(vendorProduct.getProduct().getId());
    		vendorProductVO.setProductName(vendorProduct.getProduct().getProductDescription().getName());
    		vproductList.add(vendorProductVO);
    	}
    	System.out.println("Vendor productList: "+vproductList);
    	adminVendorProductResponse.setVendorProducts(vproductList);
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
	public PaginatedResponse getVendorProductsPagination(@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
    	PaginatedResponse paginatedResponse = new PaginatedResponse();
    	System.out.println("Inside getVendorProducts: ");
    	// Get vendor products are added by vendors to product list
    	List<VendorProduct> vendorProducts = vendorProductService.getVendorProducts();
    	if(vendorProducts==null) {
    		paginatedResponse.setErrorMsg("Vendor products not found");
    		return paginatedResponse;
    	}
    	System.out.println("VendorProducts: "+vendorProducts);
    	List<VendorProductVO> vproductList = new ArrayList<VendorProductVO>();
    	for(VendorProduct vendorProduct : vendorProducts) {
    		System.out.println("vendorProduct :"+vendorProduct);
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
    	System.out.println("Vendor productList: "+vproductList);
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
		return paginatedResponse;
    }
    
    // Admin approve products
    @RequestMapping(value="/admin/products/activate", method = RequestMethod.POST)
	@ResponseBody
	public ActivateProductResponse adminApproveProducts(@RequestBody ActivateProductRequest activateProductRequest) throws Exception {
    	System.out.println("Inside adminApproveProducts:");
    	ActivateProductResponse activateProductResponse = new ActivateProductResponse();
    	Long vendorProductId = activateProductRequest.getVendorProductId();
    	System.out.println("vendorProductId : "+vendorProductId);
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
	    return activateProductResponse;
    	
    }
   
 
    
    // Admin can show a product under deal of day by updating start date,end date
    @RequestMapping(value="/admin/dealOfDay", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminDealOfDayResponse adminDealOfDay(@RequestBody AdminDealOfDayRequest adminDealOfDayReq) throws Exception {
    	System.out.println("adminDealOfDay: ");
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
    	System.out.println("dbProduct : "+dbProduct);
    	if(dbProduct==null) {
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
								System.out.println(dodProducts);
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
		return adminDealOfDayResponse;
  }
    
    // Admin Deal Management
    // Retrieving all deals from current date and for particular date
    @RequestMapping(value="/admin/getDeals", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminTodaysDeals getDeals(@RequestBody AdminDealRequest adminDealRequest, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		
		System.out.println("getDeals ==");
		
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
		System.out.println("dbProducts"+dbProducts);
		tdProducts = dbProducts;
        }else {
        	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        	String cunvertCurrentDate = adminDealRequest.getStatus();
        	Date date = new Date();
        	date = df.parse(cunvertCurrentDate);
        	System.out.println("Date :" +cunvertCurrentDate); 
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
		
        return todaysDeals;
    }   
	
   
public AdminDealProductResponse getProductDetails(Product dbProduct,boolean isSpecial) throws Exception {
		
		System.out.println("merchantStoreService =="+merchantStoreService);
		
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
			
			System.out.println("product id =="+product);
			System.out.println("product id =="+product.getProduct().getId());
			System.out.println("product id =="+dbProduct.getProductDescription().getName());
				
			productResponse.setProductName(dbProduct.getProductDescription().getName());
			
		}catch(Exception e){
			System.out.println("product details ::"+e.getMessage());
		}
		return productResponse;
	}
    /*
     * Admin Deal Management
     * Admin update a deal or remove a deal
     */
    @RequestMapping(value="/admin/deals/updateorremove", method = RequestMethod.POST)
    @ResponseBody
    public DealUpdateOrRemoveResponse adminDealUpdateOrRemove(@RequestBody DealUpdateOrRemoveRequest dealUpdateOrRemoveRequest) throws Exception {
    System.out.println("Entered adminDealUpdateOrRemove===");
	DealUpdateOrRemoveResponse dealUpdateOrRemoveResponse = new DealUpdateOrRemoveResponse();
	Long productId = dealUpdateOrRemoveRequest.getProductId();
	
    // getting product from db
    Product dbProduct = productService.getByProductId(productId);
	System.out.println("dbProduct : "+dbProduct);
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
					System.out.println("Product Id == "+dbProduct.getId());
					System.out.println("ProductPriceSpecialStartDate == "+price.getProductPriceSpecialStartDate());
					System.out.println("ProductPriceSpecialEndDate==="+price.getProductPriceSpecialEndDate());
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
     return dealUpdateOrRemoveResponse;

}
}
