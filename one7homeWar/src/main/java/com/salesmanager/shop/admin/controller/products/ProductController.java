package com.salesmanager.shop.admin.controller.products;

import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
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
import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.image.ProductImageService;
import com.salesmanager.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.salesmanager.core.business.services.catalog.product.type.ProductTypeService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.tax.TaxClassService;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.description.ProductDescription;
import com.salesmanager.core.model.catalog.product.filter.FilterType;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.catalog.product.image.ProductImageDescription;
import com.salesmanager.core.model.catalog.product.manufacturer.ManufacturerDescription;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPriceDescription;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.core.model.reference.language.Language;
//import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.fileupload.services.StorageException;
import com.salesmanager.shop.fileupload.services.StorageService;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@CrossOrigin
public class ProductController extends AbstractController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Inject
	private VendorProductService vendorProductService;

    @Inject
    private StorageService storageService;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private ManufacturerService manufacturerService;
	
	@Inject
	private ProductTypeService productTypeService;
	
	@Inject
	private ProductImageService productImageService;
	
	@Inject
	private TaxClassService taxClassService;
	
	@Inject
	private ProductPriceUtils priceUtil;

	@Inject
	LabelUtils messages;
	
	@Inject
	private CoreConfiguration configuration;
	
	@Inject
	CategoryService categoryService;

	@Inject
	MerchantStoreService merchantStoreService;
	
	@Inject
	CustomerService customerService;
	
	@RequestMapping(value="/products/save", method=RequestMethod.POST)
	public String saveProduct(@RequestBody com.salesmanager.shop.admin.model.catalog.Product  product ,HttpServletRequest request, Locale locale) throws Exception {
		
		
		//Language language = (Language)request.getAttribute("LANGUAGE");
		
		//display menu
		//setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.CUSTOMER);
		
		//List<Manufacturer> manufacturers = manufacturerService.listByStore(store, language);
		
		//List<ProductType> productTypes = productTypeService.list();
		
		//List<TaxClass> taxClasses = taxClassService.listByStore(store);
		
		List<Language> languages = store.getLanguages();
				
		//validate price
		BigDecimal submitedPrice = null;
		try {
			submitedPrice = priceUtil.getAmount(product.getProductPrice());
		} catch (Exception e) {
			ObjectError error = new ObjectError("productPrice",messages.getMessage("NotEmpty.product.productPrice", locale));
			//result.addError(error);
		}
		Date date = new Date();
		if(!StringUtils.isBlank(product.getDateAvailable())) {
			try {
				date = DateUtil.getDate(product.getDateAvailable());
				product.getAvailability().setProductDateAvailable(date);
				product.setDateAvailable(DateUtil.formatDate(date));
			} catch (Exception e) {
				ObjectError error = new ObjectError("dateAvailable",messages.getMessage("message.invalid.date", locale));
				//result.addError(error);
			}
		}
		

		
		//validate image
		if(product.getImage()!=null && !product.getImage().isEmpty()) {
			
			try {
				
				String maxHeight = configuration.getProperty("PRODUCT_IMAGE_MAX_HEIGHT_SIZE");
				String maxWidth = configuration.getProperty("PRODUCT_IMAGE_MAX_WIDTH_SIZE");
				String maxSize = configuration.getProperty("PRODUCT_IMAGE_MAX_SIZE");
				
				
				BufferedImage image = ImageIO.read(product.getImage().getInputStream());
				
				
				if(!StringUtils.isBlank(maxHeight)) {
					
					int maxImageHeight = Integer.parseInt(maxHeight);
					if(image.getHeight()>maxImageHeight) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.height", locale) + " {"+maxHeight+"}");
						//result.addError(error);
					}
					
				}
				
				if(!StringUtils.isBlank(maxWidth)) {
					
					int maxImageWidth = Integer.parseInt(maxWidth);
					if(image.getWidth()>maxImageWidth) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.width", locale) + " {"+maxWidth+"}");
						//result.addError(error);
					}
					
				}
				
				if(!StringUtils.isBlank(maxSize)) {
					
					int maxImageSize = Integer.parseInt(maxSize);
					if(product.getImage().getSize()>maxImageSize) {
						ObjectError error = new ObjectError("image",messages.getMessage("message.image.size", locale) + " {"+maxSize+"}");
						//result.addError(error);
					}
					
				}
				

				
			} catch (Exception e) {
				LOGGER.error("Cannot validate product image", e);
			}

		}
		
		
		
/*		if (result.hasErrors()) {
			return "admin-products-edit";
		}
*/		
		Product newProduct = product.getProduct();
		ProductAvailability newProductAvailability = null;
		ProductPrice newProductPrice = null;
		
		Set<ProductPriceDescription> productPriceDescriptions = null;
		
		//get tax class
		//TaxClass taxClass = newProduct.getTaxClass();
		//TaxClass dbTaxClass = taxClassService.getById(taxClass.getId());
		Set<ProductPrice> prices = new HashSet<ProductPrice>();
		Set<ProductAvailability> availabilities = new HashSet<ProductAvailability>();	

		if(product.getProduct().getId()!=null && product.getProduct().getId().longValue()>0) {
		
		
			//get actual product
			newProduct = productService.getById(product.getProduct().getId());
			if(newProduct!=null && newProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "Product already exist";
			}
			
			//copy properties
			newProduct.setSku(product.getProduct().getSku());
			newProduct.setRefSku(product.getProduct().getRefSku());
			newProduct.setAvailable(product.getProduct().isAvailable());
			newProduct.setDateAvailable(date);
			newProduct.setManufacturer(product.getProduct().getManufacturer());
			newProduct.setType(product.getProduct().getType());
			newProduct.setProductHeight(product.getProduct().getProductHeight());
			newProduct.setProductLength(product.getProduct().getProductLength());
			newProduct.setProductWeight(product.getProduct().getProductWeight());
			newProduct.setProductWidth(product.getProduct().getProductWidth());
			newProduct.setProductVirtual(product.getProduct().isProductVirtual());
			newProduct.setProductShipeable(product.getProduct().isProductShipeable());
			newProduct.setTaxClass(product.getProduct().getTaxClass());
			newProduct.setSortOrder(product.getProduct().getSortOrder());

			Set<ProductAvailability> avails = newProduct.getAvailabilities();
			if(avails !=null && avails.size()>0) {
				
				for(ProductAvailability availability : avails) {
					if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {

						
						newProductAvailability = availability;
						Set<ProductPrice> productPrices = availability.getPrices();
						
						for(ProductPrice price : productPrices) {
							if(price.isDefaultPrice()) {
								newProductPrice = price;
								newProductPrice.setProductPriceAmount(submitedPrice);
								productPriceDescriptions = price.getDescriptions();
							} else {
								prices.add(price);
							}	
						}
					} else {
						availabilities.add(availability);
					}
				}
			}
			
			
			for(ProductImage image : newProduct.getImages()) {
				if(image.isDefaultImage()) {
					product.setProductImage(image);
				}
			}
		}
		
		if(newProductPrice==null) {
			newProductPrice = new ProductPrice();
			newProductPrice.setDefaultPrice(true);
			newProductPrice.setProductPriceAmount(submitedPrice);
		}
		
		if(product.getProductImage()!=null && product.getProductImage().getId() == null) {
			product.setProductImage(null);
		}
		
		if(productPriceDescriptions==null) {
			productPriceDescriptions = new HashSet<ProductPriceDescription>();
			for(ProductDescription description : product.getDescriptions()) {
				ProductPriceDescription ppd = new ProductPriceDescription();
				ppd.setProductPrice(newProductPrice);
				ppd.setLanguage(description.getLanguage());
				ppd.setName(ProductPriceDescription.DEFAULT_PRICE_DESCRIPTION);
				productPriceDescriptions.add(ppd);
			}
			newProductPrice.setDescriptions(productPriceDescriptions);
		}
		
		newProduct.setMerchantStore(store);
		
		if(newProductAvailability==null) {
			newProductAvailability = new ProductAvailability();
		}
		

		newProductAvailability.setProductQuantity(product.getAvailability().getProductQuantity());
		newProductAvailability.setProductQuantityOrderMin(product.getAvailability().getProductQuantityOrderMin());
		newProductAvailability.setProductQuantityOrderMax(product.getAvailability().getProductQuantityOrderMax());
		newProductAvailability.setProduct(newProduct);
		newProductAvailability.setPrices(prices);
		availabilities.add(newProductAvailability);
			
		newProductPrice.setProductAvailability(newProductAvailability);
		prices.add(newProductPrice);
			
		newProduct.setAvailabilities(availabilities);

		Set<ProductDescription> descriptions = new HashSet<ProductDescription>();
		if(product.getDescriptions()!=null && product.getDescriptions().size()>0) {
			
			for(ProductDescription description : product.getDescriptions()) {
				description.setProduct(newProduct);
				descriptions.add(description);
				
			}
		}
		
		newProduct.setDescriptions(descriptions);
		product.setDateAvailable(DateUtil.formatDate(date));

		
		
		if(product.getImage()!=null && !product.getImage().isEmpty()) {
			

			
			String imageName = product.getImage().getOriginalFilename();
			

			
			ProductImage productImage = new ProductImage();
			productImage.setDefaultImage(true);
			productImage.setImage(product.getImage().getInputStream());
			productImage.setProductImage(imageName);
			
			
			List<ProductImageDescription> imagesDescriptions = new ArrayList<ProductImageDescription>();

			for(Language l : languages) {
				
				ProductImageDescription imageDescription = new ProductImageDescription();
				imageDescription.setName(imageName);
				imageDescription.setLanguage(l);
				imageDescription.setProductImage(productImage);
				imagesDescriptions.add(imageDescription);
				
			}
			
			productImage.setDescriptions(imagesDescriptions);
			productImage.setProduct(newProduct);
			
			newProduct.getImages().add(productImage);
			
			//productService.saveOrUpdate(newProduct);
			
			//product displayed
			product.setProductImage(productImage);
			
			
		} //else {
			
			//productService.saveOrUpdate(newProduct);
			
		//}
		
		productService.create(newProduct);
		//model.addAttribute("success","success");
		
		return "Product added successfully";
	}
	
	
	
	//REST API
	@RequestMapping(value="/products/{prodId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductDetails getProduct(@PathVariable String prodId) throws Exception {
		LOGGER.debug("Entered getProduct");
		ProductDetails productDetails = new ProductDetails();
		Long productId = new Long(prodId);
		System.out.println("String prodId =="+prodId);
		System.out.println("productId =="+productId);
		Product dbProduct = productService.getByProductId(productId);
		
		List<String> productImages = new ArrayList<String>();
		if(dbProduct != null) {
			System.out.println("product id =="+dbProduct.getId());
			productDetails.setProductId(productId);
			productDetails.setSku(dbProduct.getSku());
			productDetails.setProductName(dbProduct.getProductDescription().getName());
			for(ProductImage image : dbProduct.getImages()) {
				if(image.isDefaultImage()) {
					productDetails.setDefaultImage(image.getProductImageUrl());
				} else {
					productImages.add(image.getProductImageUrl());
				}
			}
			productDetails.setProductImages(productImages);
			
			System.out.println("dbProduct.getProductDescription().getTitle() =="+dbProduct.getProductDescription().getTitle());
			productDetails.setProductId(dbProduct.getId());
			if(dbProduct.getProductDescription() != null) {
				productDetails.setProductTitle(dbProduct.getProductDescription().getTitle());
				productDetails.setProductDescription(dbProduct.getProductDescription().getDescription());
				productDetails.setShortDesc(dbProduct.getProductDescription().getMetatagDescription());
				productDetails.setProductDescTitle(dbProduct.getProductDescription().getMetatagTitle());
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
			
			if(productPrice.getProductPriceAmount() != null)
				productDetails.setProductOriginalPrice(productPrice.getProductPriceAmount());
			if(productPrice.getProductPriceSpecialAmount() != null) {
				if(productPrice.getProductPriceSpecialStartDate() != null && productPrice.getProductPriceSpecialEndDate() != null) {
					if(productPrice.getProductPriceSpecialEndDate().compareTo(productPrice.getProductPriceSpecialStartDate()) > 0){
						productDetails.setProductDiscountPrice(productPrice.getProductPriceSpecialAmount());
						productDetails.setDiscountPercentage(getDiscountPercentage(productPrice));
					}
				}
			}
			
			//adding filters
			Set<FilterType> filters = dbProduct.getFilters();
			//ProductFilterType productFilterType = new ProductFilterType();
			List<ProductFilterType> productFilterTypeList = new ArrayList<ProductFilterType>();
			for(FilterType filter:filters) {
				ProductFilterType productFilterType = new ProductFilterType();
				productFilterType.setFilterId(filter.getId());
				productFilterType.setFilterTypeName(filter.getFilterTypeName());
				productFilterTypeList.add(productFilterType);
			}
			productDetails.setProductFilterTypeList(productFilterTypeList);
		}
		LOGGER.debug("Ended getProduct");
		return productDetails;
	}
	
	public ProductResponse getProductDetails(Product dbProduct,boolean isSpecial) throws Exception {
		LOGGER.debug("Entered getProductDetails ");
		System.out.println("merchantStoreService =="+merchantStoreService);
		
		ProductResponse productResponse = new ProductResponse();
		try {
		productResponse.setProductId(dbProduct.getId());
		MerchantStore store=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		//List<ProductType> productTypes = productTypeService.list();
		
		//List<TaxClass> taxClasses = taxClassService.listByStore(store);
		
		//List<Language> languages = store.getLanguages();
		

		
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();


			//Product dbProduct = productService.getById(productId);
			
			product.setProduct(dbProduct);
			//Set<ProductDescription> productDescriptions = dbProduct.getDescriptions();
			
			/*for(Language l : languages) {
				
				ProductDescription productDesc = null;
				for(ProductDescription desc : productDescriptions) {
					
					Language lang = desc.getLanguage();
					if(lang.getCode().equals(l.getCode())) {
						productDesc = desc;
					}

				}
				
				if(productDesc==null) {
					productDesc = new ProductDescription();
					productDesc.setLanguage(l);
				}

				descriptions.add(productDesc);
				
			}*/
			
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
							/*if(price.getDealOfDay().equals("Y")) {
								productResponse.setDealOfDay(true);
							}
							else if(price.getDealOfDay().equals("N") || price.getDealOfDay().equals(null)){
								productResponse.setDealOfDay(false);
							}*/
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
			
			System.out.println("product id =="+product);
			System.out.println("product id =="+product.getProduct().getId());
			
			System.out.println("product id =="+dbProduct.getProductDescription().getName());
			System.out.println("product id =="+product.getProduct().getProductDescription());
			System.out.println("product id =="+product.getProduct().getProductDescription().getName());
			
			if(product.getProductImage() != null)
				productResponse.setImageURL(product.getProductImage().getProductImageUrl());
			
			if(productPrice.getProductPriceAmount() != null)
				productResponse.setProductPrice(productPrice.getProductPriceAmount());
			if(productPrice.getProductPriceSpecialAmount() != null) {
				if(productPrice.getProductPriceSpecialStartDate() != null && productPrice.getProductPriceSpecialEndDate() != null) {
					if(productPrice.getProductPriceSpecialEndDate().compareTo(productPrice.getProductPriceSpecialStartDate()) > 0){
						productResponse.setProductPrice(productPrice.getProductPriceAmount());
						productResponse.setProductDiscountPrice(productPrice.getProductPriceSpecialAmount());
						productResponse.setDiscountPercentage(getDiscountPercentage(productPrice));
						productResponse.setProductPriceSpecialEndDate(productPrice.getProductPriceSpecialEndDate());
						productResponse.setProductPriceSpecialStartDate(productPrice.getProductPriceSpecialStartDate());
					}
				}
			}
		
/*			if(isSpecial) {
				productResponse.setProductPrice(productPrice.getProductPriceAmount());
				productResponse.setProductDiscountPrice(productPrice.getProductPriceSpecialAmount());
				productResponse.setDiscountPercentage(getDiscountPercentage(productPrice));
				productResponse.setProductPriceSpecialEndDate(productPrice.getProductPriceSpecialEndDate());
				productResponse.setProductPriceSpecialStartDate(productPrice.getProductPriceSpecialStartDate());
				//productResponse.setProductPriceSpecialEndTime(productPrice.getProductPriceSpecialEndDateTime());
			}
			else
				productResponse.setProductPrice(productPrice.getProductPriceAmount());
				*/
			productResponse.setProductDescription(dbProduct.getProductDescription().getDescription());	
			productResponse.setProductName(dbProduct.getProductDescription().getName());
			//productResponse.setVendorName(dbProduct.getManufacturer().getCode());
			
			Set<ManufacturerDescription> manufacturerDescription =  dbProduct.getManufacturer().getDescriptions();
			for(ManufacturerDescription description:manufacturerDescription){
				productResponse.setVendorName(description.getName());
				productResponse.setVendorLocation(description.getTitle());
			}
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("Error while getting product details::"+e.getMessage());
		}
		LOGGER.debug("Ended getProductDetails");
		return productResponse;
	}

	/*@RequestMapping(value="/getTodaysDeals", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public TodaysDeals getTodaysDeals() throws Exception {
		
		System.out.println("getTodaysDeals ==");
		
		TodaysDeals todaysDeals = new TodaysDeals();
		ProductResponse productResponse = new ProductResponse();

		List<Product> dbProducts = productService.getTodaysDeals();
		
		List<ProductResponse> responses = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			Set<Category> dbCategories= product.getCategories();
			List<com.salesmanager.shop.admin.controller.products.Category> categoriesList = new ArrayList<com.salesmanager.shop.admin.controller.products.Category>();
			for(Category category:dbCategories){
				com.salesmanager.shop.admin.controller.products.Category cat = new com.salesmanager.shop.admin.controller.products.Category();
				if(category.getParent() == null)
					cat.setName(category.getCode());
				else {
					System.out.println("parent cat code =="+category.getParent().getCode());
					cat.setName(category.getParent().getCode());	
				}
				
				categoriesList.add(cat);
			}
			productResponse.setCategories(categoriesList);
			responses.add(productResponse);
		}
		
		todaysDeals.setTodaysDealsData(responses);
		return todaysDeals;
	}*/
	
	@RequestMapping(value="/getTodaysDeals", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public TodaysDeals getTodaysDeals(@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size,
			@RequestBody TodaysDealRequest todaysDealRequest) throws Exception {
		
		LOGGER.debug("Entered getTodaysDeals");
		
		TodaysDeals todaysDeals = new TodaysDeals();
		ProductResponse productResponse = new ProductResponse();

		//List<Product> dbProducts = productService.getTodaysDeals();
		List<Product> dbProducts = null;
		
		if(todaysDealRequest.getStatus().equals("0")) {
			final Calendar cal = Calendar.getInstance();
		    cal.add(Calendar.DATE, -1);
		    Date yesterDayDate = cal.getTime();
			dbProducts = productService.getExpiredDeals(yesterDayDate);
			}
		
		if(todaysDealRequest.getStatus().equals("1")) {
			dbProducts = productService.getTodaysDeals();
			}

		if(todaysDealRequest.getStatus().equals("2")) {
		     dbProducts = productService.getUpcomingDeals();
		}
		
		List<ProductResponse> responses = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			Set<Category> dbCategories= product.getCategories();
			List<com.salesmanager.shop.admin.controller.products.Category> categoriesList = new ArrayList<com.salesmanager.shop.admin.controller.products.Category>();
			for(Category category:dbCategories){
				com.salesmanager.shop.admin.controller.products.Category cat = new com.salesmanager.shop.admin.controller.products.Category();
				if(category.getParent() == null)
					cat.setName(category.getCode());
				else {
					System.out.println("parent cat code =="+category.getParent().getCode());
					cat.setName(category.getParent().getCode());	
				}
				
				categoriesList.add(cat);
			}
			productResponse.setCategories(categoriesList);
			responses.add(productResponse);
		}
		if(responses == null || responses.isEmpty() || responses.size() < page){
			todaysDeals.setTodaysDealsData(responses);
			return todaysDeals;
		}
	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	todaysDeals.setPaginationData(paginaionData);
		List<ProductResponse> paginatedProdResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		todaysDeals.setTodaysDealsData(paginatedProdResponses);
		LOGGER.debug("Ended getTodaysDeals");
		return todaysDeals;
	}

	/*@RequestMapping(value="/categories/{categoryId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<ProductResponse> getProductForCat(@PathVariable String categoryId) throws Exception {
		
		System.out.println("getProductForCat ==");
		
		ProductResponse productResponse = new ProductResponse();

		categoryId = categoryId.replaceAll("_", " ");
		Category category = categoryService.getByCategoryCode(categoryId);
		List<ProductResponse> responses = new ArrayList<ProductResponse>();
		List<Product> tdProducts = productService.getTodaysDeals();
		Map<Long,Product> todaysDealsMap = null;
		List<Product> dbProducts = null;
		System.out.println("before invoking product for cat...");
		responses = invokeProductsData(todaysDealsMap, categoryId,responses,productResponse,tdProducts);
		System.out.println("category.getParent() =="+category.getParent());
		if(category.getParent() == null){
			List<Category> childCatList = category.getCategories();
			System.out.println("childCatList =="+childCatList);
			
			for(Category childCat:childCatList){
				System.out.println("1");
				responses = invokeProductsData(todaysDealsMap, childCat.getCode(),responses,productResponse,tdProducts);
				break;
			}
		}
		return responses;
	}*/
	
	@RequestMapping(value="/getProductsByFilters", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FilteredProducts getProductsByFilters(@RequestParam(value="pageNumber", defaultValue = "1") int page ,@RequestParam(value="pageSize", defaultValue="15") int size, @RequestBody FiltersRequest filtersRequest) throws Exception {
		
		LOGGER.debug("Entered getProductsByFilters");
		
		List<Long> filterIds = filtersRequest.getFilterIds();
		
		ProductResponse productResponse = new ProductResponse();
		FilteredProducts filteredProducts = new FilteredProducts();
		List<ProductResponse> responses = new ArrayList<ProductResponse>();

		if(filterIds != null) {
			List<Product> dbProducts = productService.getProductsListByFilters(filterIds);
			List<Product> tdProducts = productService.getTodaysDeals();
			Map<Long,Product> todaysDealsMap = null;
			todaysDealsMap = new HashMap<Long, Product>();
			for(Product tdproduct:tdProducts){
				todaysDealsMap.put(tdproduct.getId(), tdproduct);
			}
			for(Product product:dbProducts) {
				if(todaysDealsMap.containsKey(product.getId())){
					productResponse = getProductDetails(product,true);
				} else {
					productResponse = getProductDetails(product,false);
				}
				responses.add(productResponse);
			}
			
		}
		if(responses == null || responses.isEmpty() || responses.size() < page){
			filteredProducts.setFilteredProducts(responses);
			return filteredProducts;
		}
	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	filteredProducts.setPaginationData(paginaionData);
		List<ProductResponse> paginatedProdResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		filteredProducts.setFilteredProducts(paginatedProdResponses);
		LOGGER.debug("Ended getProductsByFilters");
		return filteredProducts;
	}
	
	@RequestMapping(value="/categories/{categoryId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResponse getProductForCat(@PathVariable String categoryId,
				@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		
		LOGGER.debug("Entered getProductForCat");
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		ProductResponse productResponse = new ProductResponse();
		
		categoryId = categoryId.replaceAll("_", " ");
		Category category = categoryService.getByCategoryCode(categoryId);
		List<ProductResponse> responses = new ArrayList<ProductResponse>();
		List<Product> tdProducts = productService.getTodaysDeals();
		Map<Long,Product> todaysDealsMap = null;
		//List<Product> dbProducts = null;
		responses = invokeProductsData(todaysDealsMap, categoryId,responses,productResponse,tdProducts);
		if(category.getParent() == null){
			List<Category> childCatList = category.getCategories();
			
			for(Category childCat:childCatList){
				responses = invokeProductsData(todaysDealsMap, childCat.getCode(),responses,productResponse,tdProducts);
				break;
			}
		}

	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(responses == null || responses.isEmpty() || responses.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(responses);
			return paginatedResponse;
		}
    	List<ProductResponse> paginatedResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	
    	Long maxProductPrice = getMaxProductPrice(categoryId);
    	paginatedResponse.setMaxProductPrice(maxProductPrice);
    	
    	LOGGER.debug("Ended getProductForCat");
		return paginatedResponse;
		
	} 


	@RequestMapping(value="/vendor/{vendorId}/categories/{categoryId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResponse getVendorProductForCat(HttpServletRequest request,
				@PathVariable String categoryId,
				@PathVariable Long vendorId,
				@RequestParam(value="pageNumber", defaultValue = "1") int page , 
				@RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {

		LOGGER.debug("Entered getProductForCat");
	
		//Customer customer = getSessionAttribute(  Constants.CUSTOMER, request );
		List<VendorProduct> vendorAssociatedProductList = vendorProductService.findProductsByVendor(vendorId);
        List<Long> vendorAssociatedProductIds = vendorAssociatedProductList.stream().map(vp -> vp.getProduct().getId()).collect(Collectors.toList());
        
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		ProductResponse productResponse = new ProductResponse();
		
		categoryId = categoryId.replaceAll("_", " ");
		Category category = categoryService.getByCategoryCode(categoryId);
		List<ProductResponse> responses = new ArrayList<ProductResponse>();
		List<Product> tdProducts = productService.getTodaysDeals();
		Map<Long,Product> todaysDealsMap = null;
		//List<Product> dbProducts = null;
		responses = invokeVendorNotAssociatedProductsData(todaysDealsMap, categoryId,responses,productResponse,tdProducts,
				vendorAssociatedProductIds);
		if(category.getParent() == null){
			List<Category> childCatList = category.getCategories();
			
			for(Category childCat:childCatList){
				responses = invokeVendorNotAssociatedProductsData(todaysDealsMap, childCat.getCode(),responses,productResponse,tdProducts,
						vendorAssociatedProductIds);
				break;
			}
		}

	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(responses == null || responses.isEmpty() || responses.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(responses);
			return paginatedResponse;
		}
    	List<ProductResponse> paginatedResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	LOGGER.debug("Ended getProductForCat");
		return paginatedResponse;
		
	} 
	
	public List<ProductResponse> invokeVendorNotAssociatedProductsData(Map<Long,Product> todaysDealsMap,
			String categoryId,List<ProductResponse> responses,
			ProductResponse productResponse,List<Product> tdProducts,
			List<Long> productIds) throws Exception {
		LOGGER.debug("Entered invokeProductsData");
		todaysDealsMap = new HashMap<Long, Product>();
		List<Product> dbProducts = productService.getVendorNotAssociatedProductsListByCategory(categoryId,productIds);
		for(Product tdproduct:tdProducts){
			todaysDealsMap.put(tdproduct.getId(), tdproduct);
		}
		for(Product product:dbProducts) {
			if(todaysDealsMap.containsKey(product.getId())){
				System.out.println("t1");
				productResponse = getProductDetails(product,true);
			} else {
				System.out.println("p1");
				productResponse = getProductDetails(product,false);
			}
			responses.add(productResponse);
		}
		LOGGER.debug("Ended invokeProductsData");
		return responses;
	}

	
	public List<ProductResponse> invokeProductsData(Map<Long,Product> todaysDealsMap,
			String categoryId,List<ProductResponse> responses,
			ProductResponse productResponse,List<Product> tdProducts) throws Exception {
		LOGGER.debug("Entered invokeProductsData");
		todaysDealsMap = new HashMap<Long, Product>();
		List<Product> dbProducts = productService.getProductsListByCategory(categoryId);
		for(Product tdproduct:tdProducts){
			todaysDealsMap.put(tdproduct.getId(), tdproduct);
		}
		for(Product product:dbProducts) {
			if(todaysDealsMap.containsKey(product.getId())){
				System.out.println("t1");
				productResponse = getProductDetails(product,true);
			} else {
				System.out.println("p1");
				productResponse = getProductDetails(product,false);
			}
			responses.add(productResponse);
		}
		LOGGER.debug("Ended invokeProductsData");
		return responses;
	}
	
	@RequestMapping(value="/getDealOfDay", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public DealOfDay getDealOfDay() throws Exception {
		//Get dealofday 
		LOGGER.debug("Entered getDealOfDay ");
		
		DealOfDay dealOfDay = new DealOfDay();
		ProductResponse productResponse = new ProductResponse();

		//List<Product> dbProducts = productService.getProduct("dealOfDay","Y");
		List<Product> dbProducts = productService.getDealOfDay();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			dealOfDay.setDealOfDay(productResponse);
		}
		LOGGER.debug("Ended getDealOfDay");
		return dealOfDay;
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
			LOGGER.debug("Ended getDiscountPercentage");
			return df.format(discount);
		}
		return df.format(discount);
	
	}

	@RequestMapping(value="/getAllDealOfDay", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResponse getAllDealOfDay(@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		// List all dealofday products
		LOGGER.debug("Entered getAllDealOfDay");
		AdminDealOfDay adminDealOfDay = new AdminDealOfDay();
		PaginatedResponse paginatedResponse = new PaginatedResponse();
		ProductResponse productResponse = new ProductResponse();
		
		List<ProductResponse> prodRespList = new ArrayList<ProductResponse>();
		List<Product> dbProducts = productService.getAllDealOfDay("dealOfDay","Y");
		
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			prodRespList.add(productResponse);
			
		}
		PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, prodRespList.size());
    	paginatedResponse.setPaginationData(paginaionData);
		if(prodRespList == null || prodRespList.isEmpty() || prodRespList.size() < paginaionData.getCountByPage()){
			paginatedResponse.setResponseData(prodRespList);
			return paginatedResponse;
		}
    	List<ProductResponse> paginatedResponses = prodRespList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
    	paginatedResponse.setResponseData(paginatedResponses);
    	LOGGER.debug("Ended getAllDealOfDay");
		return paginatedResponse;
		
	}
	
	@RequestMapping(value="/getNewProduct", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public NewProducts getNewProduct() throws Exception {
		
		LOGGER.debug("Entered getNewProduct ");
		
		NewProducts newProducts = new NewProducts();
		ProductResponse productResponse = new ProductResponse();
		List<Product> dbProducts = productService.getProduct("newProduct","Y");

		List<ProductResponse>  productResponseList = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
		    productResponseList.add(productResponse);   
		}
		newProducts.setNewProducts(productResponseList);
		LOGGER.debug("Ended getNewProduct");
		return newProducts;
	}
	
	@RequestMapping(value="/getFeatureProduct", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeatureProduct getfeatureProduct() throws Exception {
		
		LOGGER.debug("Entered getfeatureProduct");
		
		FeatureProduct featureProduct = new FeatureProduct();
		ProductResponse productResponse = new ProductResponse();

		List<Product> dbProducts = productService.getProduct("featuredProduct","Y");
		List<ProductResponse>  productResponseList = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			productResponseList.add(productResponse);
		}
		featureProduct.setFeaureProducts(productResponseList);
		LOGGER.debug("Ended getfeatureProduct");
		return featureProduct;
	}
	
	@RequestMapping(value="/getRecommendedProduct", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public RecommendedProduct getRecommendedProduct() throws Exception {
		
		LOGGER.debug("Entered getRecommendedProduct");
		
		RecommendedProduct recommendedProduct = new RecommendedProduct();
		ProductResponse productResponse = new ProductResponse();

		List<Product> dbProducts = productService.getProduct("recommendedProduct","Y");
		List<ProductResponse>  productResponseList = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			productResponseList.add(productResponse);
		}
		recommendedProduct.setRecommendedProducts(productResponseList);
		LOGGER.debug("Ended getRecommendedProduct");
		return recommendedProduct;
	}
	
	@RequestMapping(value="/getRecentBought", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public RecentlyBought getRecentBought() throws Exception {
		
		LOGGER.debug("Entered getRecentBought");
		
		RecentlyBought recentlyBought = new RecentlyBought();
		ProductResponse productResponse = new ProductResponse();

		List<Product> dbProducts = productService.getProduct("recentlyBought","Y");
		List<ProductResponse>  productResponseList = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			productResponseList.add(productResponse);
		}
		recentlyBought.setRecentlyBought(productResponseList);
		LOGGER.debug("Ended getRecentBought");
		return recentlyBought;
	}
	
	
	
	
	@RequestMapping(value="/getProductsByFiltersAndPrice", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FilteredProducts getProductsByFiltersAndPrice(@RequestParam(value="pageNumber", defaultValue = "1") int page ,@RequestParam(value="pageSize", defaultValue="15") int size, @RequestBody FiltersRequest filtersRequest) throws Exception {
		
		LOGGER.debug("Entered getProductsByFiltersAndPrice");
		
		List<Long> filterIds = null;
		if(filtersRequest.getFilterIds() != null) {
			filterIds = filtersRequest.getFilterIds();
		}
		
		ProductResponse productResponse = new ProductResponse();
		FilteredProducts filteredProducts = new FilteredProducts();
		try {
		List<ProductResponse> responses = new ArrayList<ProductResponse>();
		BigDecimal minPrice= null;
		System.out.println("filtersRequest.getMinPrice() =="+filtersRequest.getMinPrice());
		if(filtersRequest.getMinPrice() != null && !("").equals(filtersRequest.getMinPrice()))
				minPrice= new BigDecimal(filtersRequest.getMinPrice());
		BigDecimal maxPrice= null;
		if(filtersRequest.getMaxPrice() != null && !("").equals(filtersRequest.getMaxPrice()))
				maxPrice= new BigDecimal(filtersRequest.getMaxPrice());
		Double productRating = null;
		if(filtersRequest.getProductRating() != null && !("").equals(filtersRequest.getProductRating()))
				productRating = filtersRequest.getProductRating();
		
		String categoryCode = filtersRequest.getCategoryCode();
		categoryCode = categoryCode.replaceAll("_", " ");
				
		if((filterIds != null && !filterIds.isEmpty()) || (minPrice!=null && maxPrice!=null) || productRating!=null) {
			List<Product> dbProducts = productService.getProductsListByFiltersAndPrice(categoryCode,filterIds,minPrice,maxPrice,productRating);
			List<Product> tdProducts = productService.getTodaysDeals();
			Map<Long,Product> todaysDealsMap = null;
			todaysDealsMap = new HashMap<Long, Product>();
			for(Product tdproduct:tdProducts){
				todaysDealsMap.put(tdproduct.getId(), tdproduct);
			}
			for(Product product:dbProducts) {
				if(todaysDealsMap.containsKey(product.getId())){
					productResponse = getProductDetails(product,true);
				} else {
					productResponse = getProductDetails(product,false);
				}
				responses.add(productResponse);
			}
			
		}
		if(responses == null || responses.isEmpty() || responses.size() < page){
			filteredProducts.setFilteredProducts(responses);
			return filteredProducts;
		}
	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	filteredProducts.setPaginationData(paginaionData);
		List<ProductResponse> paginatedProdResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		filteredProducts.setFilteredProducts(paginatedProdResponses);
		}catch(Exception e){
			LOGGER.error("Error occured in filtering::"+e.getMessage());
			e.printStackTrace(System.out);
		}
		LOGGER.debug("Ended getProductsByFiltersAndPrice");
		return filteredProducts;
	}
public ProductResponse getProductDetailsAndPrice(Product dbProduct,boolean isSpecial,String minPrice,String maxPrice) throws Exception {
		
		LOGGER.debug("Entered getProductDetailsAndPrice");
		
		ProductResponse productResponse = new ProductResponse();
		try {
		productResponse.setProductId(dbProduct.getId());
		MerchantStore store=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();
			
			product.setProduct(dbProduct);
			
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
			
			System.out.println("product id =="+product);
			System.out.println("product id =="+product.getProduct().getId());
			System.out.println("product id =="+product.getProductImage());
			System.out.println("product id =="+product.getProductImage().getProductImageUrl());
			
			productResponse.setImageURL(product.getProductImage().getProductImageUrl());
			if(isSpecial) {
				productResponse.setProductPrice(productPrice.getProductPriceAmount());
				productResponse.setProductDiscountPrice(productPrice.getProductPriceSpecialAmount());
				productResponse.setDiscountPercentage(getDiscountPercentage(productPrice));
				productResponse.setProductPriceSpecialEndDate(productPrice.getProductPriceSpecialEndDate());
				productResponse.setProductPriceSpecialStartDate(productPrice.getProductPriceSpecialStartDate());
				//productResponse.setProductPriceSpecialEndTime(productPrice.getProductPriceSpecialEndDateTime());
			}
			else
				productResponse.setProductPrice(productPrice.getProductPriceAmount());
				
			productResponse.setProductName(dbProduct.getProductDescription().getName());
			//productResponse.setVendorName(dbProduct.getManufacturer().getCode());
			
			Set<ManufacturerDescription> manufacturerDescription =  dbProduct.getManufacturer().getDescriptions();
			for(ManufacturerDescription description:manufacturerDescription){
				productResponse.setVendorName(description.getName());
				productResponse.setVendorLocation(description.getTitle());
			}
			/*Set<ProductReview> productReviews = dbProduct.getProductReview();
			for(ProductReview productReview : productReviews){
				productResponse.setProductRating(productReview.getReviewRating());
			}*/	
			productResponse.setProductRating((dbProduct.getProductReviewAvg()).doubleValue());
		}catch(Exception e){
			LOGGER.error("Error in getting product details::"+e.getMessage());
		}
		LOGGER.debug("Ended getProductDetailsAndPrice");
		return productResponse;
	}

	@RequestMapping(value="/uploadProductImage", method = RequestMethod.POST)
	@ResponseBody
	public ProductImageResponse uploadProductImage(@RequestPart("productImageRequest") String productRequestStr,
			@RequestPart("file") MultipartFile productUploadedImage) throws Exception {
		LOGGER.debug("Entered uploadProductImage");
		ProductImageRequest productImageRequest = new ObjectMapper().readValue(productRequestStr, ProductImageRequest.class);
		ProductImageResponse productImageResponse = new ProductImageResponse();
		
		if(productImageRequest.getProductId() == null){
			productImageResponse.setErrorMsg("ProductId can not be null");
			productImageResponse.setStatus(false);
			return productImageResponse;
		}
		productImageResponse.setProductId(productImageRequest.getProductId());
    	// Store file into file system
    	String fileName = "";
    	if(productUploadedImage.getSize() != 0) {
    		try{
    			fileName = storageService.store(productUploadedImage,"product");
    			System.out.println("fileName "+fileName);
    		}catch(StorageException se){
    			
    			LOGGER.error("Failed while storing image");
    			productImageResponse.setErrorMsg("Failed while storing image");
    			productImageResponse.setStatus(false);
    			return productImageResponse;
    		}
    		try {
    			boolean isImageExists = false;
    			boolean isDefaultImage = false;
    			if(productImageRequest.getDefaultImage() != null && productImageRequest.getDefaultImage().equals("1"))
    				isDefaultImage = true;

    			Product dbProduct = productService.getById(productImageRequest.getProductId());
    			if(dbProduct == null){
    				LOGGER.debug("No product available with product id : "+productImageRequest.getProductId());
        			productImageResponse.setErrorMsg("No product available with product id : "+productImageRequest.getProductId());
        			productImageResponse.setStatus(false);
        			return productImageResponse;
    			}
    			ProductImage productImage = null;
    			Set<ProductImage> images = new HashSet<ProductImage>();

    			if(dbProduct.getImages() != null)
    			{
    				for(ProductImage image:dbProduct.getImages()) {
    					if(image.getProductImageUrl() != null && productImageRequest.getImageURL() != null && (image.getProductImageUrl().equals(productImageRequest.getImageURL()))){
    						isImageExists = true;
    						productImage = image;
    						break;
    					}
    				}
    			}
				if(!isImageExists){
					productImage = new ProductImage();
				}
    			productImage.setProduct(dbProduct);
    			productImage.setDefaultImage(isDefaultImage);
    			productImage.setProductImageUrl(fileName);
    			images.add(productImage);
    			
    			dbProduct.setImages(images);
    			productService.update(dbProduct);
    			
    		}catch(Exception e){
    			LOGGER.error("Failed while saving image details in database for product id : "+productImageRequest.getProductId());
    			productImageResponse.setErrorMsg("Failed while saving image details in database for product id : "+productImageRequest.getProductId());
    			productImageResponse.setStatus(false);
    			return productImageResponse;
    		}
    		productImageResponse.setStatus(true);
    		productImageResponse.setFileName(fileName);
    	}
		LOGGER.debug("Ended uploadProductImage");
		return productImageResponse;
	}
	
      @RequestMapping(value="/createProduct", method = RequestMethod.POST, 
		               consumes = MediaType.APPLICATION_JSON_VALUE,
		               produces = MediaType.APPLICATION_JSON_VALUE)
      @ResponseBody
      public CreateProductResponse createProduct(@RequestBody CreateProductRequest createProductRequest) throws Exception {
		LOGGER.debug("Entered createProduct");
		CreateProductResponse createProductResponse = new CreateProductResponse();
		try {
		MerchantStore merchantStore=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
		Product newProduct = new Product();
		Set<ProductDescription> descriptions = new HashSet<ProductDescription>();
		ProductDescription productDescription = new ProductDescription();
		Set<ProductAvailability> availabilities = new HashSet<ProductAvailability>();
		Set<ProductPrice> prices = new HashSet<ProductPrice>();
		ProductAvailability productAvailability = new ProductAvailability();
		ProductPrice productPrice = new ProductPrice();
/*		System.out.println("createProductRequest.getSku() =="+createProductRequest.getSku());
		System.out.println("createProductRequest.getDescription() =="+createProductRequest.getDescription());
		System.out.println("createProductRequest.getProductName() =="+createProductRequest.getProductName());
		System.out.println("createProductRequest.getTitle() =="+createProductRequest.getTitle());
		System.out.println("createProductRequest.getShortDesc() =="+createProductRequest.getShortDesc());
		System.out.println("createProductRequest.getProductDescTitle() =="+createProductRequest.getProductDescTitle());
		System.out.println("createProductRequest.getProductPrice() =="+createProductRequest.getProductPrice());
		System.out.println("createProductRequest.getCategory() =="+createProductRequest.getCategory());
*/		
		productDescription.setName(createProductRequest.getProductName());
		productDescription.setDescription(createProductRequest.getDescription());
		productDescription.setTitle(createProductRequest.getTitle());
		productDescription.setMetatagDescription(createProductRequest.getShortDesc());
		productDescription.setMetatagTitle(createProductRequest.getProductDescTitle());
		Language language = new Language();
		language.setId(1);
		productDescription.setLanguage(language);
		productDescription.setProduct(newProduct);
		descriptions.add(productDescription);
		
		//availability
		productAvailability.setRegion("*");
		productAvailability.setProductQuantity(0);
	
		//price
		productPrice.setDefaultPrice(true);
		productPrice.setProductPriceAmount(createProductRequest.getProductPrice());
		productPrice.setCode("base");
		productPrice.setProductAvailability(productAvailability);
		productPrice.setFeaturedProduct("N");
		productPrice.setNewProduct("N");
		productPrice.setRecommendedProduct("N");
		prices.add(productPrice);
		
		productAvailability.setPrices(prices);
		productAvailability.setProduct(newProduct);
		
		availabilities.add(productAvailability);
		
		
		newProduct.setSku(createProductRequest.getSku());
		newProduct.setDescriptions(descriptions);
		newProduct.setMerchantStore(merchantStore);
		newProduct.setAvailabilities(availabilities);
		String categoryId = createProductRequest.getCategory().replaceAll("_", " ");
		Category category = categoryService.getByCategoryCode(categoryId);
		Set<Category> categories = new HashSet<Category>();
		categories.add(category);
		newProduct.setCategories(categories);
		productService.save(newProduct);
		LOGGER.debug("Created product");
		createProductResponse.setStatus(true);
		createProductResponse.setProductId(newProduct.getId());
		}catch (Exception e){
			
			LOGGER.error("failed while creating product::"+e.getMessage());
		}
		LOGGER.debug("Ended createProduct");
		return createProductResponse;
	}
    
  @RequestMapping(value="/updateProduct", method = RequestMethod.POST, 
           consumes = MediaType.APPLICATION_JSON_VALUE,
           produces = MediaType.APPLICATION_JSON_VALUE)
 @ResponseBody
 public CreateProductResponse updateProduct(@RequestBody CreateProductRequest createProductRequest) throws Exception {

	LOGGER.debug("Entered updateProduct");
	CreateProductResponse createProductResponse = new CreateProductResponse();
	createProductResponse.setStatus(false);
	try {
			Product dbProduct = productService.getById(createProductRequest.getProductId());
			if(dbProduct == null){
				LOGGER.debug("No product available with product id : "+createProductRequest.getProductId());
				createProductResponse.setErrorMsg("No product available with product id : "+createProductRequest.getProductId());
				return createProductResponse;
			}
			Product newProduct = new Product();
			Set<ProductDescription> descriptions = new HashSet<ProductDescription>();
			ProductDescription productDescription = null;
			if(dbProduct.getProductDescription() != null){
				productDescription = dbProduct.getProductDescription();
			} else {
				productDescription = new ProductDescription();
			}
			
			Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
			ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
			Set<ProductPrice> prices = null;
			
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
						productAvailability = availability;
						prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
							}
						}
					}
				}
			}
			else {
				productAvailability = new ProductAvailability();
			}
			if(productPrice == null) {
				productPrice = new ProductPrice();
				prices = new HashSet<ProductPrice>();
			}
			
			System.out.println("createProductRequest.getSku() =="+createProductRequest.getSku());
			System.out.println("createProductRequest.getDescription() =="+createProductRequest.getDescription());
			System.out.println("createProductRequest.getProductName() =="+createProductRequest.getProductName());
			System.out.println("createProductRequest.getTitle() =="+createProductRequest.getTitle());
			System.out.println("createProductRequest.getShortDesc() =="+createProductRequest.getShortDesc());
			System.out.println("createProductRequest.getProductDescTitle() =="+createProductRequest.getProductDescTitle());
			System.out.println("createProductRequest.getProductPrice() =="+createProductRequest.getProductPrice());
			
			productDescription.setName(createProductRequest.getProductName());
			productDescription.setDescription(createProductRequest.getDescription());
			productDescription.setTitle(createProductRequest.getTitle());
			productDescription.setMetatagDescription(createProductRequest.getShortDesc());
			productDescription.setMetatagTitle(createProductRequest.getProductDescTitle());
			Language language = new Language();
			language.setId(1);
			productDescription.setLanguage(language);
			productDescription.setProduct(dbProduct);
			descriptions.add(productDescription);
			
			//availability
			productAvailability.setRegion("*");
			productAvailability.setProductQuantity(0);
		
			//price
			productPrice.setDefaultPrice(true);
			productPrice.setProductPriceAmount(createProductRequest.getProductPrice());
			productPrice.setProductAvailability(productAvailability);
			prices.add(productPrice);
			
			productAvailability.setPrices(prices);
			productAvailability.setProduct(dbProduct);
			
			availabilities.add(productAvailability);
			
			
			dbProduct.setSku(createProductRequest.getSku());
			dbProduct.setDescriptions(descriptions);
			dbProduct.setAvailabilities(availabilities);
			productService.update(dbProduct);
			LOGGER.debug("Product Updated");
			createProductResponse.setStatus(true);
			createProductResponse.setProductId(dbProduct.getId());

	}catch (Exception e){
		LOGGER.error("failed while updating product discount==="+e.getMessage());
		createProductResponse.setErrorMsg("failed while updating product discount==="+e.getMessage());
		return createProductResponse;
	}
	LOGGER.debug("Ended updateProduct");
	return createProductResponse;
	
	}
  @RequestMapping(value="/deleteProduct", method = RequestMethod.POST, 
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public CreateProductResponse deleteProduct(@RequestBody ProductDiscountRequest productDiscountRequest) throws Exception {

	LOGGER.debug("Entered deleteProduct");
	CreateProductResponse createProductResponse = new CreateProductResponse();
	createProductResponse.setStatus(false);
	try {
			Product dbProduct = productService.getById(productDiscountRequest.getProductId());
			if(dbProduct == null){
				LOGGER.debug("No product available with product id : "+productDiscountRequest.getProductId());
				createProductResponse.setErrorMsg("No product available with product id : "+productDiscountRequest.getProductId());
				return createProductResponse;
			}
			productService.delete(dbProduct);
			createProductResponse.setStatus(true);
			createProductResponse.setProductId(productDiscountRequest.getProductId());
	}catch (Exception e){
		LOGGER.error("failed while deleting product==="+e.getMessage());
		createProductResponse.setErrorMsg("failed while deleting product==="+e.getMessage());
		return createProductResponse;
	}
	LOGGER.debug("Ended deleteProduct");
	return createProductResponse;
	
	}
  @RequestMapping(value="/updateProductDiscount", method = RequestMethod.POST, 
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public CreateProductResponse updateProductDiscount(@RequestBody ProductDiscountRequest productDiscountRequest) throws Exception {

	LOGGER.debug("Entered updateProductDiscount");
	CreateProductResponse createProductResponse = new CreateProductResponse();
	createProductResponse.setStatus(false);
	try {
			Product dbProduct = productService.getById(productDiscountRequest.getProductId());
			if(dbProduct == null){
				LOGGER.debug("No product available with product id : "+productDiscountRequest.getProductId());
				createProductResponse.setErrorMsg("No product available with product id : "+productDiscountRequest.getProductId());
				return createProductResponse;
			}
			Set<ProductAvailability> availabilities = dbProduct.getAvailabilities();
			ProductAvailability productAvailability = null;
			ProductPrice productPrice = null;
			Set<ProductPrice> prices = null;
			
			if(availabilities!=null && availabilities.size()>0) {
				
				for(ProductAvailability availability : availabilities) {
					if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
						productAvailability = availability;
						prices = availability.getPrices();
						for(ProductPrice price : prices) {
							if(price.isDefaultPrice()) {
								productPrice = price;
							}
						}
					}
				}
				productPrice.setProductPriceSpecialAmount(productDiscountRequest.getProductDiscountPrice());
				if(productDiscountRequest.getProductPriceSpecialStartDate() != null){
					productPrice.setProductPriceSpecialStartDate(productDiscountRequest.getProductPriceSpecialStartDate());
				}
				if(productDiscountRequest.getProductPriceSpecialEndDate() != null){
					productPrice.setProductPriceSpecialEndDate(productDiscountRequest.getProductPriceSpecialEndDate());
				}
				if(prices == null)
				{
					LOGGER.debug("Price object not found for this product id");
					createProductResponse.setErrorMsg("Price object not found for this product id");
					return createProductResponse;
				}
				prices.add(productPrice);
				productAvailability.setPrices(prices);
				availabilities.add(productAvailability);
				dbProduct.setAvailabilities(availabilities);
				productService.update(dbProduct);
				LOGGER.debug("Updated Product discount");
				createProductResponse.setStatus(true);
				createProductResponse.setProductId(productDiscountRequest.getProductId());

			}
		

	}catch (Exception e){
		LOGGER.debug("failed while updating product discount==="+e.getMessage());
		createProductResponse.setErrorMsg("failed while updating product discount==="+e.getMessage());
		return createProductResponse;
	}
	LOGGER.debug("Ended updateProductDiscount");
	return createProductResponse;
	
	}
  
	 @RequestMapping(value="/deleteProductImage", method = RequestMethod.POST, 
     consumes = MediaType.APPLICATION_JSON_VALUE,
     produces = MediaType.APPLICATION_JSON_VALUE)
	 @ResponseBody
	public ProductImageResponse deleteProductImage(@RequestBody ProductImageRequest productImageRequest) throws Exception {
		
		 LOGGER.debug("Entered deleteProductImage");
		 ProductImageResponse productImageResponse = new ProductImageResponse();
		
		if(productImageRequest.getProductId() == null){
			LOGGER.debug("ProductId can not be null");
			productImageResponse.setErrorMsg("ProductId can not be null");
			productImageResponse.setStatus(false);
			return productImageResponse;
		}
		productImageResponse.setProductId(productImageRequest.getProductId());
  		try {
  			boolean isImageExists = false;

  			Product dbProduct = productService.getById(productImageRequest.getProductId());
  			if(dbProduct == null){
  				LOGGER.debug("No product available with product id : "+productImageRequest.getProductId());
      			productImageResponse.setErrorMsg("No product available with product id : "+productImageRequest.getProductId());
      			productImageResponse.setStatus(false);
      			return productImageResponse;
  			}
  			ProductImage productImage = null;
  			Set<ProductImage> images = new HashSet<ProductImage>();

  			if(dbProduct.getImages() != null)
  			{
  				for(ProductImage image:dbProduct.getImages()) {
  					if(image.getProductImageUrl() != null && productImageRequest.getImageURL() != null && (image.getProductImageUrl().equals(productImageRequest.getImageURL()))){
  						isImageExists = true;
  						productImage = image;
  						break;
  					}
  				}
  			}
			if(!isImageExists){
      			productImageResponse.setErrorMsg("Image URL :"+productImageRequest.getImageURL()+" provided is not available with product id : "+productImageRequest.getProductId());
      			productImageResponse.setStatus(false);
      			return productImageResponse;
			} else {
				//deleting image from the location
				File imageFile = new File(productImageRequest.getImageURL());
				if(imageFile.exists()){
					imageFile.delete();
				}
	  			productImage.setProduct(dbProduct);
	  			productImageService.delete(productImage);
			}
  			
  		}catch(Exception e){
  			LOGGER.error("Failed while deleting image details in database for product id : "+productImageRequest.getProductId());
  			productImageResponse.setErrorMsg("Failed while deleting image details in database for product id : "+productImageRequest.getProductId());
  			productImageResponse.setStatus(false);
  			return productImageResponse;
  		}
  		LOGGER.debug("Deleted Product Image");
  		productImageResponse.setStatus(true);
  		productImageResponse.setFileName(productImageRequest.getImageURL());
  		LOGGER.debug("Ended deleteProductImage");
  		return productImageResponse;
  	}

	@RequestMapping(value="/getProductsBySearch", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FilteredProducts getProductsBySearch(@RequestParam(value="pageNumber", defaultValue = "1") int page ,@RequestParam(value="pageSize", defaultValue="15") int size, @RequestBody SearchRequest searchRequest) throws Exception {
		
		LOGGER.debug("Entered getProductsBySearch");
		
		ProductResponse productResponse = new ProductResponse();
		FilteredProducts filteredProducts = new FilteredProducts();
		List<ProductResponse> responses = new ArrayList<ProductResponse>();

		if(searchRequest.getSearchString() != null) {
			List<Product> dbProducts = productService.getProductsListBySearch(searchRequest.getSearchString());

			for(Product product:dbProducts) {
				productResponse = getProductDetails(product,false);
				responses.add(productResponse);
			}
			
		}
		if(responses == null || responses.isEmpty() || responses.size() < page){
			filteredProducts.setFilteredProducts(responses);
			return filteredProducts;
		}
	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	filteredProducts.setPaginationData(paginaionData);
		List<ProductResponse> paginatedProdResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		filteredProducts.setFilteredProducts(paginatedProdResponses);
		LOGGER.debug("Ended getProductsBySearch");
		return filteredProducts;
	}

	public Long getMaxProductPrice(String categoryCode) throws Exception {
		return productService.getMaxProductPrice(categoryCode);
	}


	@RequestMapping(value="/getTodaysDealsForCat", method = RequestMethod.POST)
	@ResponseBody
	public TodaysDeals getTodaysDealsForCat(@RequestParam(value="pageNumber", defaultValue = "1") int page , 
			@RequestParam(value="pageSize", defaultValue="15") int size,
			@RequestBody TodaysDealRequest todaysDealRequest) throws Exception {
		
		LOGGER.debug("Entered getUpcomingsDeals");
		

		TodaysDeals todaysDeals = new TodaysDeals();
		ProductResponse productResponse = new ProductResponse();
		
		String catCode = todaysDealRequest.getCategoryCode();
		
		List<Product> dbProducts = null;
		
		if(todaysDealRequest.getStatus().equals("0")) {
			final Calendar cal = Calendar.getInstance();
		    cal.add(Calendar.DATE, -1);
		    Date yesterDayDate = cal.getTime();
			dbProducts = productService.getExpiredDeals(yesterDayDate);
			}
		
		if(todaysDealRequest.getStatus().equals("1")) {
			dbProducts = productService.getTodaysDeals();
			}

		if(todaysDealRequest.getStatus().equals("2")) {
		     dbProducts = productService.getUpcomingDeals();
		}
		
		List<ProductResponse> responses = new ArrayList<ProductResponse>();
		
		for(Product product:dbProducts) {
			
			List<com.salesmanager.shop.admin.controller.products.Category> categoriesList = new ArrayList<com.salesmanager.shop.admin.controller.products.Category>();
			Set<Category> productCategories = product.getCategories();
			
			for(Category productCategory : productCategories) {
				
			Category searchCategory = categoryService.getByCategoryCode(catCode); 
			List<Category> searchCategories = searchCategory.getCategories();
			
			for(Category searchCat : searchCategories) {
				
				if(searchCat.getCode().equals(productCategory.getCode())) {
					
					productResponse=getProductDetails(product,true);
					
					com.salesmanager.shop.admin.controller.products.Category cat = new com.salesmanager.shop.admin.controller.products.Category();
					
					if(productCategory.getParent() == null)
						cat.setName(productCategory.getCode());
					else {
						System.out.println("parent cat code =="+productCategory.getParent().getCode());
						cat.setName(productCategory.getParent().getCode());	
					}
					
					categoriesList.add(cat);
					productResponse.setCategories(categoriesList);
					responses.add(productResponse);
				}
			}
			}
		}
		
		if(responses == null || responses.isEmpty() || responses.size() < page){
			todaysDeals.setTodaysDealsData(responses);
			return todaysDeals;
		}
		
	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	todaysDeals.setPaginationData(paginaionData);
		List<ProductResponse> paginatedProdResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		todaysDeals.setTodaysDealsData(paginatedProdResponses);
		
		LOGGER.debug("Ended getTodaysDeals");
		return todaysDeals;
		
	}
		
	/*public ProductResponse getTodaysProductDetails(Product dbProduct,boolean isSpecial) throws Exception {
		LOGGER.debug("Entered getProductDetails ");
		System.out.println("merchantStoreService =="+merchantStoreService);
		
		ProductResponse productResponse = new ProductResponse();
		
		try {
			
		productResponse.setProductId(dbProduct.getId());
		MerchantStore store=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
	
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();


			//Product dbProduct = productService.getById(productId);
			
			product.setProduct(dbProduct);
			
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
									if(price.getDealOfDay().equals("Y")) {
										productResponse.setDealOfDay(true);
									}
									else if(price.getDealOfDay().equals("N") || price.getDealOfDay().equals(null)){
										productResponse.setDealOfDay(false);
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
					
					System.out.println("product id =="+product);
					System.out.println("product id =="+product.getProduct().getId());
					
					System.out.println("product id =="+dbProduct.getProductDescription().getName());
					System.out.println("product id =="+product.getProduct().getProductDescription());
					System.out.println("product id =="+product.getProduct().getProductDescription().getName());
					
					if(product.getProductImage() != null)
						productResponse.setImageURL(product.getProductImage().getProductImageUrl());
					
					if(productPrice.getProductPriceAmount() != null)
						productResponse.setProductPrice(productPrice.getProductPriceAmount());
					if(productPrice.getProductPriceSpecialAmount() != null) {
						if(productPrice.getProductPriceSpecialStartDate() != null && productPrice.getProductPriceSpecialEndDate() != null) {
							if(productPrice.getProductPriceSpecialEndDate().compareTo(productPrice.getProductPriceSpecialStartDate()) > 0){
								productResponse.setProductPrice(productPrice.getProductPriceAmount());
								productResponse.setProductDiscountPrice(productPrice.getProductPriceSpecialAmount());
								productResponse.setDiscountPercentage(getDiscountPercentage(productPrice));
								productResponse.setProductPriceSpecialEndDate(productPrice.getProductPriceSpecialEndDate());
								productResponse.setProductPriceSpecialStartDate(productPrice.getProductPriceSpecialStartDate());
							}
						}
					}
				
					if(isSpecial) {
						productResponse.setProductPrice(productPrice.getProductPriceAmount());
						productResponse.setProductDiscountPrice(productPrice.getProductPriceSpecialAmount());
						productResponse.setDiscountPercentage(getDiscountPercentage(productPrice));
						productResponse.setProductPriceSpecialEndDate(productPrice.getProductPriceSpecialEndDate());
						productResponse.setProductPriceSpecialStartDate(productPrice.getProductPriceSpecialStartDate());
						//productResponse.setProductPriceSpecialEndTime(productPrice.getProductPriceSpecialEndDateTime());
					}
					else
						productResponse.setProductPrice(productPrice.getProductPriceAmount());
						
					productResponse.setProductDescription(dbProduct.getProductDescription().getDescription());	
					productResponse.setProductName(dbProduct.getProductDescription().getName());
					//productResponse.setVendorName(dbProduct.getManufacturer().getCode());
					
					Set<ManufacturerDescription> manufacturerDescription =  dbProduct.getManufacturer().getDescriptions();
					for(ManufacturerDescription description:manufacturerDescription){
						productResponse.setVendorName(description.getName());
						productResponse.setVendorLocation(description.getTitle());
					}
					
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("Error while getting product details::"+e.getMessage());
		}
		
			LOGGER.debug("Ended getProductDetails");
			return productResponse;
	}*/
				
			
}
