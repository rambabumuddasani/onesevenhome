package com.salesmanager.shop.admin.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

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
import com.salesmanager.shop.utils.DateUtil;

@Controller
@CrossOrigin
public class AdminController {
	
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
	    merchantStore.setStorename(adminUpdateStoreRequest.getStoreName());
	    merchantStore.setCode(adminUpdateStoreRequest.getStoreCode());
	    merchantStore.setStorephone(adminUpdateStoreRequest.getStorePhone());
	    merchantStore.setStoreEmailAddress(adminUpdateStoreRequest.getEmailAddress());
	    merchantStore.setStoreaddress(adminUpdateStoreRequest.getStoreAddress());
	    merchantStore.setStorecity(adminUpdateStoreRequest.getStoreCity());
	    merchantStore.setStorestateprovince(adminUpdateStoreRequest.getStoreState());
	    merchantStore.setStorepostalcode(adminUpdateStoreRequest.getStorePostalCode());
	    //merchantStore.setCountry(adminUpdateStoreRequest.getStoreCountry());
	    
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
	
	@RequestMapping(value="/admin/getStore", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StoreInfoResponse getStoreInfo(HttpServletRequest request) {
		
		StoreInfoResponse storeInfoResponse=new StoreInfoResponse();
		try {
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
	        
	        User user = userService.getById(1l);
	        storeInfo.setAdminName(user.getAdminName());
	        storeInfo.setLastAccess(user.getLastAccess());
	        storeInfoResponse.setStoreInfo(storeInfo);
	        
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		  
		    return storeInfoResponse;
		
	}
	
	@RequestMapping(value="/admin/update", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public EditUserAdminResponse updateAdmin(@RequestBody EditUserAdminRequest editUserAdminRequest)
        throws Exception {
		
		    System.out.println("editUserAdmin :");
		    EditUserAdminResponse editUserAdminResponse = new EditUserAdminResponse();
		    String stringId = editUserAdminRequest.getId();
		    Long longId = Long.parseLong(stringId);
			User dbUser = userService.getById(longId);
			
			if(dbUser==null) {
				editUserAdminResponse.setErrorMessage("Admin is null for this id: "+longId);
				editUserAdminResponse.setSucessMessage("false");
				return editUserAdminResponse;
			}
			
			dbUser.setFirstName(editUserAdminRequest.getFirstName());
			dbUser.setLastName(editUserAdminRequest.getLastName());
			//Language  language = languageService.getByCode(editUserAdminRequest.getDefaultLang());
			//dbUser.setDefaultLanguage(language);
			dbUser.setAdminName(editUserAdminRequest.getUserName());
			dbUser.setAdminEmail(editUserAdminRequest.getEmail());
			MerchantStore store = merchantStoreService.getByCode(editUserAdminRequest.getStoreCode());
			dbUser.setMerchantStore(store);
			
			userService.saveOrUpdate(dbUser);
			
			editUserAdminResponse.setSucessMessage("Admin profile updated successfully");
			editUserAdminResponse.setStatus("true");
			
	        return editUserAdminResponse;
		
		
	}
	
	@RequestMapping(value="/admin/list", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminListResponse getAdminList() throws Exception {
		AdminListResponse  adminListResponse = new AdminListResponse();
		List<UserVO> userList = new ArrayList<UserVO>();
		MerchantStore store = merchantStoreService.getByCode(MerchantStore.DEFAULT_STORE);
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
		return adminListResponse;
		
	}
	
	@RequestMapping(value="/admin/updatepassword", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UpdatePasswordResp changePassword(@RequestBody UpdatePasswordReq updatePasswordReq) throws Exception {
		
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
		String pass = passwordEncoder.encode(updatePasswordReq.getNewPassword());
		dbUser.setAdminPassword(pass);
		userService.update(dbUser);	
		updatePasswordResp.setSuccessMessage("Password updated successfully");
		updatePasswordResp.setStatus("true");
		return updatePasswordResp;
	}
	
	/*@RequestMapping(value="/admin/addProducts", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AddProductResp addProducts(@RequestBody AddProductReq addProductReq) throws Exception {
		AddProductResp addProductResp = new AddProductResp();
		String productId = addProductReq.getProductId();
		Long longId = Long.parseLong(productId);
	    Product dbProduct = productService.getByProductId(longId);
	    if(dbProduct==null) {
	    	addProductResp.setErrormessage("Product not exist");
	    	return addProductResp;
	    }
	    
		return null;
	
	}
	*/
	@RequestMapping(value="/admin/addOrRemove", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AddProductResp addProducts(@RequestBody AddProductReq addProductReq) throws Exception {
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
						/*if(isFeatureProduct(addProductReq.getIsfeatureProduct())) {
						if(addProductReq.getStatus().equals("Y")) { 
							price.setFeaturedProduct("N");
							addProductResp.setStatusMessage("Product is added to feature product");
						}
						else {
							price.setFeaturedProduct("Y");
							addProductResp.setStatusMessage("Product is removed from feature product");
					   }
				      }*/
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

	@RequestMapping(value="/admin/categories/{categoryCode}/{title}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<AdminProductResponse> getProduct(@PathVariable String categoryCode,@PathVariable String title) throws Exception {
		
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
			
			Set<ManufacturerDescription> manufacturerDescription =  dbProduct.getManufacturer().getDescriptions();
			for(ManufacturerDescription description:manufacturerDescription){
				adminProductResponse.setVendorName(description.getName());
				adminProductResponse.setVendorLocation(description.getTitle());
			}
			
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
    	adminVendorProductResponse.setVendorProducts(vproductList);
    	return adminVendorProductResponse;
    	
    }
    
    @RequestMapping(value="/admin/vendor/products", method = RequestMethod.GET)
	@ResponseBody
	public AdminVendorProductResponse getVendorProducts() throws Exception {
    	System.out.println("Inside getVendorProducts: ");
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
    }
    
    @RequestMapping(value="/admin/products/activate", method = RequestMethod.POST)
	@ResponseBody
	public ActivateProductResponse adminApproveProducts(@RequestBody ActivateProductRequest activateProductRequest) throws Exception {
    	System.out.println("Inside adminApproveProducts:");
    	ActivateProductResponse activateProductResponse = new ActivateProductResponse();
    	Long vendorProductId = activateProductRequest.getVendorProductId();
    	System.out.println("vendorProductId : "+vendorProductId);
	    VendorProduct vendorProduct = vendorProductService.getVendorProductById(vendorProductId);
    	if(vendorProduct==null) {
    		activateProductResponse.setErrorMesg("Vendor product not found");
    		return activateProductResponse;
    	}
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
   
}
