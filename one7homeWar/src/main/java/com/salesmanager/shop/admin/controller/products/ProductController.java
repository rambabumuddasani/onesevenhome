package com.salesmanager.shop.admin.controller.products;

import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.image.ProductImageService;
import com.salesmanager.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.salesmanager.core.business.services.catalog.product.type.ProductTypeService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.tax.TaxClassService;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.CategoryDescription;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.attribute.ProductAttribute;
import com.salesmanager.core.model.catalog.product.availability.ProductAvailability;
import com.salesmanager.core.model.catalog.product.description.ProductDescription;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.catalog.product.image.ProductImageDescription;
import com.salesmanager.core.model.catalog.product.manufacturer.Manufacturer;
import com.salesmanager.core.model.catalog.product.manufacturer.ManufacturerDescription;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.price.ProductPriceDescription;
import com.salesmanager.core.model.catalog.product.relationship.ProductRelationship;
import com.salesmanager.core.model.catalog.product.type.ProductType;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.tax.taxclass.TaxClass;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.PathVariable;
import com.salesmanager.shop.admin.controller.products.ProductResponse;
import com.salesmanager.shop.admin.controller.products.TodaysDeals;
//import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Controller
@CrossOrigin
public class ProductController extends AbstractController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
	

	
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

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/editProduct.html", method=RequestMethod.GET)
	public String displayProductEdit(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(productId,model,request,response);

	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/viewEditProduct.html", method=RequestMethod.GET)
	public String displayProductEdit(@RequestParam("sku") String sku, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		Product dbProduct = productService.getByCode(sku, language);
		
		long productId = -1;//non existent
		if(dbProduct!=null) {
			productId = dbProduct.getId();
		}
		
		return displayProduct(productId,model,request,response);
	}
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/createProduct.html", method=RequestMethod.GET)
	public String displayProductCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(null,model,request,response);

	}
	
	
	
	private String displayProduct(Long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		

		//display menu
		setMenu(model,request);
		
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		

		List<Manufacturer> manufacturers = manufacturerService.listByStore(store, language);
		
		List<ProductType> productTypes = productTypeService.list();
		
		List<TaxClass> taxClasses = taxClassService.listByStore(store);
		
		List<Language> languages = store.getLanguages();
		

		
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		List<ProductDescription> descriptions = new ArrayList<ProductDescription>();

		if(productId!=null && productId!=0) {//edit mode
			

			Product dbProduct = productService.getById(productId);
			
			if(dbProduct==null || dbProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/products/products.html";
			}
			
			product.setProduct(dbProduct);
			Set<ProductDescription> productDescriptions = dbProduct.getDescriptions();
			
			for(Language l : languages) {
				
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
				
			}
			
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


		} else {


			for(Language l : languages) {
				
				ProductDescription desc = new ProductDescription();
				desc.setLanguage(l);
				descriptions.add(desc);
				
			}
			
			Product prod = new Product();
			
			prod.setAvailable(true);
			
			ProductAvailability productAvailability = new ProductAvailability();
			ProductPrice price = new ProductPrice();
			product.setPrice(price);
			product.setAvailability(productAvailability);
			product.setProduct(prod);
			product.setDescriptions(descriptions);
			product.setDateAvailable(DateUtil.formatDate(new Date()));


		}
		
		
		
		
		
		model.addAttribute("product",product);
		model.addAttribute("manufacturers", manufacturers);
		model.addAttribute("productTypes", productTypes);
		model.addAttribute("taxClasses", taxClasses);
		return "admin-products-edit";
	}
	
	@RequestMapping(value="/products/dummy", method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public com.salesmanager.shop.admin.model.catalog.Product getDummyProduct(){
		
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		product.setProductPrice("400");
		product.setDateAvailable("2017-08-02");
		ProductAvailability productAvailabilty = new ProductAvailability();
		productAvailabilty.setProductQuantity(10);
		productAvailabilty.setProductQuantityOrderMin(1);
		productAvailabilty.setProductQuantityOrderMax(5);
		product.setAvailability(productAvailabilty);
		Product dbProduct = productService.getById(100l) ;
	    product.setProduct(dbProduct);
		return product;
	    
	}

	//@PreAuthorize("hasRole('PRODUCTS')")
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
	
	
	/**
	 * Creates a duplicate product with the same inner object graph
	 * Will ignore SKU, reviews and images
	 * @param id
	 * @param result
	 * @param model
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/product/duplicate.html", method=RequestMethod.POST)
	public String duplicateProduct(@ModelAttribute("productId") Long  id, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		

		Language language = (Language)request.getAttribute("LANGUAGE");
		
		//display menu
		setMenu(model,request);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		List<Manufacturer> manufacturers = manufacturerService.listByStore(store, language);
		List<ProductType> productTypes = productTypeService.list();
		List<TaxClass> taxClasses = taxClassService.listByStore(store);

		model.addAttribute("manufacturers", manufacturers);
		model.addAttribute("productTypes", productTypes);
		model.addAttribute("taxClasses", taxClasses);
		
		Product dbProduct = productService.getById(id);
		Product newProduct = new Product();
		
		if(dbProduct==null || dbProduct.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}

		//Make a copy of the product
		com.salesmanager.shop.admin.model.catalog.Product product = new com.salesmanager.shop.admin.model.catalog.Product();
		
		Set<ProductAvailability> availabilities = new HashSet<ProductAvailability>();
		//availability - price
		for(ProductAvailability pAvailability : dbProduct.getAvailabilities()) {
			
			ProductAvailability availability = new ProductAvailability();
			availability.setProductDateAvailable(pAvailability.getProductDateAvailable());
			availability.setProductIsAlwaysFreeShipping(pAvailability.getProductIsAlwaysFreeShipping());
			availability.setProductQuantity(pAvailability.getProductQuantity());
			availability.setProductQuantityOrderMax(pAvailability.getProductQuantityOrderMax());
			availability.setProductQuantityOrderMin(pAvailability.getProductQuantityOrderMin());
			availability.setProductStatus(pAvailability.getProductStatus());
			availability.setRegion(pAvailability.getRegion());
			availability.setRegionVariant(pAvailability.getRegionVariant());


			
			Set<ProductPrice> prices = pAvailability.getPrices();
			for(ProductPrice pPrice : prices) {
				
				ProductPrice price = new ProductPrice();
				price.setDefaultPrice(pPrice.isDefaultPrice());
				price.setProductPriceAmount(pPrice.getProductPriceAmount());
				price.setProductAvailability(availability);
				price.setProductPriceSpecialAmount(pPrice.getProductPriceSpecialAmount());
				price.setProductPriceSpecialEndDate(pPrice.getProductPriceSpecialEndDate());
				price.setProductPriceSpecialStartDate(pPrice.getProductPriceSpecialStartDate());
				price.setProductPriceType(pPrice.getProductPriceType());
				
				Set<ProductPriceDescription> priceDescriptions = new HashSet<ProductPriceDescription>();
				//price descriptions
				for(ProductPriceDescription pPriceDescription : pPrice.getDescriptions()) {
					
					ProductPriceDescription productPriceDescription = new ProductPriceDescription();
					productPriceDescription.setAuditSection(pPriceDescription.getAuditSection());
					productPriceDescription.setDescription(pPriceDescription.getDescription());
					productPriceDescription.setName(pPriceDescription.getName());
					productPriceDescription.setLanguage(pPriceDescription.getLanguage());
					productPriceDescription.setProductPrice(price);
					priceDescriptions.add(productPriceDescription);
					
				}
				price.setDescriptions(priceDescriptions);
				if(price.isDefaultPrice()) {
					product.setPrice(price);
					product.setProductPrice(priceUtil.getAdminFormatedAmount(store, price.getProductPriceAmount()));
				}
				
				availability.getPrices().add(price);
			}
			
			

			if(availability.getRegion().equals(com.salesmanager.core.business.constants.Constants.ALL_REGIONS)) {
				product.setAvailability(availability);
			}
			
			availabilities.add(availability);
		}
		
		newProduct.setAvailabilities(availabilities);
		
		
		
		//attributes
		Set<ProductAttribute> attributes = new HashSet<ProductAttribute>();
		for(ProductAttribute pAttribute : dbProduct.getAttributes()) {
			
			ProductAttribute attribute = new ProductAttribute();
			attribute.setAttributeDefault(pAttribute.getAttributeDefault());
			attribute.setAttributeDiscounted(pAttribute.getAttributeDiscounted());
			attribute.setAttributeDisplayOnly(pAttribute.getAttributeDisplayOnly());
			attribute.setAttributeRequired(pAttribute.getAttributeRequired());
			attribute.setProductAttributePrice(pAttribute.getProductAttributePrice());
			attribute.setProductAttributeIsFree(pAttribute.getProductAttributeIsFree());
			attribute.setProductAttributeWeight(pAttribute.getProductAttributeWeight());
			attribute.setProductOption(pAttribute.getProductOption());
			attribute.setProductOptionSortOrder(pAttribute.getProductOptionSortOrder());
			attribute.setProductOptionValue(pAttribute.getProductOptionValue());
			attributes.add(attribute);
						
		}
		newProduct.setAttributes(attributes);
		
		//relationships
		Set<ProductRelationship> relationships = new HashSet<ProductRelationship>();
		for(ProductRelationship pRelationship : dbProduct.getRelationships()) {
			
			ProductRelationship relationship = new ProductRelationship();
			relationship.setActive(pRelationship.isActive());
			relationship.setCode(pRelationship.getCode());
			relationship.setRelatedProduct(pRelationship.getRelatedProduct());
			relationship.setStore(store);
			relationships.add(relationship);

		}
		
		newProduct.setRelationships(relationships);
		
		//product description
		Set<ProductDescription> descsset = new HashSet<ProductDescription>();
		List<ProductDescription> desclist = new ArrayList<ProductDescription>();
		Set<ProductDescription> descriptions = dbProduct.getDescriptions();
		for(ProductDescription pDescription : descriptions) {
			
			ProductDescription description = new ProductDescription();
			description.setAuditSection(pDescription.getAuditSection());
			description.setName(pDescription.getName());
			description.setDescription(pDescription.getDescription());
			description.setLanguage(pDescription.getLanguage());
			description.setMetatagDescription(pDescription.getMetatagDescription());
			description.setMetatagKeywords(pDescription.getMetatagKeywords());
			description.setMetatagTitle(pDescription.getMetatagTitle());
			descsset.add(description);
			desclist.add(description);
		}
		newProduct.setDescriptions(descsset);
		product.setDescriptions(desclist);
		
		//product
		newProduct.setAuditSection(dbProduct.getAuditSection());
		newProduct.setAvailable(dbProduct.isAvailable());
		
		

		//copy
		// newProduct.setCategories(dbProduct.getCategories());
		newProduct.setDateAvailable(dbProduct.getDateAvailable());
		newProduct.setManufacturer(dbProduct.getManufacturer());
		newProduct.setMerchantStore(store);
		newProduct.setProductHeight(dbProduct.getProductHeight());
		newProduct.setProductIsFree(dbProduct.getProductIsFree());
		newProduct.setProductLength(dbProduct.getProductLength());
		newProduct.setProductOrdered(dbProduct.getProductOrdered());
		newProduct.setProductWeight(dbProduct.getProductWeight());
		newProduct.setProductWidth(dbProduct.getProductWidth());
		newProduct.setSortOrder(dbProduct.getSortOrder());
		newProduct.setTaxClass(dbProduct.getTaxClass());
		newProduct.setType(dbProduct.getType());
		newProduct.setSku(UUID.randomUUID().toString().replace("-",""));
		newProduct.setProductVirtual(dbProduct.isProductVirtual());
		newProduct.setProductShipeable(dbProduct.isProductShipeable());
		
		productService.update(newProduct);
		
		Set<Category> categories = dbProduct.getCategories();
		for(Category category : categories) {
			Category categoryCopy = categoryService.getById(category.getId());
			newProduct.getCategories().add(categoryCopy);
			productService.update(newProduct);
		}
		
		product.setProduct(newProduct);
		model.addAttribute("product", product);
		model.addAttribute("success","success");
		
		return "redirect:/admin/products/editProduct.html?id=" + newProduct.getId();
	}

	
	/**
	 * Removes a product image based on the productimage id
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/product/removeImage.html")
	public @ResponseBody ResponseEntity<String> removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String iid = request.getParameter("imageId");

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();

		
		try {
			
			Long id = Long.parseLong(iid);
			ProductImage productImage = productImageService.getById(id);
			if(productImage==null || productImage.getProduct().getMerchantStore().getId().intValue()!=store.getId().intValue()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				
			} else {
				
				productImageService.removeProductImage(productImage);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
			}
		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	
	/**
	 * List all categories and let the merchant associate the product to a category
	 * @param productId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/displayProductToCategories.html", method=RequestMethod.GET)
	public String displayAddProductToCategories(@RequestParam("id") long productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		
		setMenu(model,request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		
		//get the product and validate it belongs to the current merchant
		Product product = productService.getById(productId);
		
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		

		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		
		model.addAttribute("product", product);
		model.addAttribute("categories", categories);
		return "catalogue-product-categories";
		
	}
	
	/**
	 * List all categories associated to a Product
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/product-categories/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageProductCategories(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		AjaxResponse resp = new AjaxResponse();
		
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		Long productId;
		Product product = null;
		
		try {
			productId = Long.parseLong(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		}

		
		try {

			product = productService.getById(productId);

			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			
			Language language = (Language)request.getAttribute("LANGUAGE");

			
			Set<Category> categories = product.getCategories();
			

			for(Category category : categories) {
				Map entry = new HashMap();
				entry.put("categoryId", category.getId());
				
				List<CategoryDescription> descriptions = category.getDescriptions();
				String categoryName = category.getDescriptions().get(0).getName();
				for(CategoryDescription description : descriptions){
					if(description.getLanguage().getCode().equals(language.getCode())) {
						categoryName = description.getName();
					}
				}
				entry.put("name", categoryName);
				resp.addDataEntry(entry);
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);


	}
	
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/product-categories/remove.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteProductFromCategory(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sCategoryid = request.getParameter("categoryId");
		String sProductId = request.getParameter("productId");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();
		
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		
		try {
			
			Long categoryId = Long.parseLong(sCategoryid);
			Long productId = Long.parseLong(sProductId);
			
			Category category = categoryService.getById(categoryId);
			Product product = productService.getById(productId);
			
			if(category==null || category.getMerchantStore().getId()!=store.getId()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			} 
			
			if(product==null || product.getMerchantStore().getId()!=store.getId()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			} 
			
			product.getCategories().remove(category);
			productService.update(product);	
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/addProductToCategories.html", method=RequestMethod.POST)
	public String addProductToCategory(@RequestParam("productId") long productId, @RequestParam("id") long categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Language language = (Language)request.getAttribute("LANGUAGE");
		
		
		//get the product and validate it belongs to the current merchant
		Product product = productService.getById(productId);
		
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		

		//get parent categories
		List<Category> categories = categoryService.listByStore(store,language);
		
		Category category = categoryService.getById(categoryId);
		
		if(category==null) {
			return "redirect:/admin/products/products.html";
		}
		
		if(category.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		product.getCategories().add(category);
		
		productService.update(product);
		
		model.addAttribute("product", product);
		model.addAttribute("categories", categories);
		
		return "catalogue-product-categories";
		
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
/*		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products", "catalogue-products");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//	
*/	}
	
	//REST API
	@RequestMapping(value="/products/{prodId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductDetails getProduct(@PathVariable String prodId) throws Exception {
		ProductDetails productDetails = new ProductDetails();
		Long productId = new Long(prodId);
		System.out.println("String prodId =="+prodId);
		System.out.println("productId =="+productId);
		Product dbProduct = productService.getByProductId(productId);
		System.out.println("dbProduct =="+dbProduct);
		List<String> productImages = new ArrayList<String>();
		if(dbProduct != null) {
			System.out.println("product id =="+dbProduct.getId());
			for(ProductImage image : dbProduct.getImages()) {
				if(image.isDefaultImage()) {
					productDetails.setDefaultImage(image.getProductImageUrl());
				} else {
					productImages.add(image.getProductImageUrl());
				}
			}
			productDetails.setProductImages(productImages);
			
			System.out.println("dbProduct.getProductDescription().getTitle() =="+dbProduct.getProductDescription().getTitle());

			productDetails.setProductTitle(dbProduct.getProductDescription().getTitle());
			productDetails.setProductDescription(dbProduct.getProductDescription().getDescription());
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
			
			productDetails.setProductOriginalPrice(productPrice.getProductPriceAmount());
			productDetails.setProductDiscountPrice(productPrice.getProductPriceSpecialAmount());
			productDetails.setDiscountPercentage(getDiscountPercentage(productPrice));
		}
		return productDetails;
	}
	
	public ProductResponse getProductDetails(Product dbProduct,boolean isSpecial) throws Exception {
		
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
		}catch(Exception e){
			System.out.println("product details ::"+e.getMessage());
		}
		return productResponse;
	}

	@RequestMapping(value="/getTodaysDeals", method = RequestMethod.GET, 
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
	}
	
/*	@RequestMapping(value="/getTodaysDeals", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public TodaysDeals getTodaysDeals(@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		
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
		if(responses == null || responses.isEmpty() || responses.size() < page){
			todaysDeals.setTodaysDealsData(responses);
			return todaysDeals;
		}
	    PaginationData paginaionData=createPaginaionData(page,size);
    	calculatePaginaionData(paginaionData,size, responses.size());
    	todaysDeals.setPaginationData(paginaionData);
		List<ProductResponse> paginatedProdResponses = responses.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
		todaysDeals.setTodaysDealsData(paginatedProdResponses);
		return todaysDeals;
	}*/

	@RequestMapping(value="/categories/{categoryId}", method = RequestMethod.GET, 
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
	}
	
/*	@RequestMapping(value="/categories/{categoryId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedResponse getProductForCat(@PathVariable String categoryId) throws Exception {
	//public PaginatedResponse getProductForCat(@PathVariable String categoryId,
	//			@RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) throws Exception {
		
		System.out.println("getProductForCat ==");
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
		return paginatedResponse;
		
	} */

	public List<ProductResponse> invokeProductsData(Map<Long,Product> todaysDealsMap,
			String categoryId,List<ProductResponse> responses,
			ProductResponse productResponse,List<Product> tdProducts) throws Exception {
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
		return responses;
	}
	
	@RequestMapping(value="/getDealOfDay", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public DealOfDay getDealOfDay() throws Exception {
		
		System.out.println("getDealOfDay ==");
		
		DealOfDay dealOfDay = new DealOfDay();
		ProductResponse productResponse = new ProductResponse();

		List<Product> dbProducts = productService.getProduct("dealOfDay","Y");

		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			dealOfDay.setDealOfDay(productResponse);
		}
		return dealOfDay;
	}
	
	@RequestMapping(value="/getNewProduct", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public NewProducts getNewProduct() throws Exception {
		
		System.out.println("getNewProduct:");
		
		NewProducts newProducts = new NewProducts();
		ProductResponse productResponse = new ProductResponse();
		List<Product> dbProducts = productService.getProduct("newProduct","Y");

		List<ProductResponse>  productResponseList = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
		    productResponseList.add(productResponse);   
		}
		newProducts.setNewProducts(productResponseList);
		return newProducts;
	}
	
	@RequestMapping(value="/getFeatureProduct", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FeatureProduct getfeatureProduct() throws Exception {
		
		System.out.println("getFeatureProduct:");
		
		FeatureProduct featureProduct = new FeatureProduct();
		ProductResponse productResponse = new ProductResponse();

		List<Product> dbProducts = productService.getProduct("featuredProduct","Y");
		List<ProductResponse>  productResponseList = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			productResponseList.add(productResponse);
		}
		featureProduct.setFeaureProducts(productResponseList);
		return featureProduct;
	}
	
	@RequestMapping(value="/getRecommendedProduct", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public RecommendedProduct getRecommendedProduct() throws Exception {
		
		System.out.println("getRecommendedProduct:");
		
		RecommendedProduct recommendedProduct = new RecommendedProduct();
		ProductResponse productResponse = new ProductResponse();

		List<Product> dbProducts = productService.getProduct("recommendedProduct","Y");
		List<ProductResponse>  productResponseList = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			productResponseList.add(productResponse);
		}
		recommendedProduct.setRecommendedProducts(productResponseList);
		return recommendedProduct;
	}
	
	@RequestMapping(value="/getRecentBought", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public RecentlyBought getRecentBought() throws Exception {
		
		System.out.println("getRecentProduct:");
		
		RecentlyBought recentlyBought = new RecentlyBought();
		ProductResponse productResponse = new ProductResponse();

		List<Product> dbProducts = productService.getProduct("recentlyBought","Y");
		List<ProductResponse>  productResponseList = new ArrayList<ProductResponse>();
		for(Product product:dbProducts) {
			productResponse = getProductDetails(product,true);
			productResponseList.add(productResponse);
		}
		recentlyBought.setRecentlyBought(productResponseList);
		return recentlyBought;
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
}
