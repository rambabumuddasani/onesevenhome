package com.salesmanager.shop.controller.vendor.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.store.controller.customer.VendorResponse;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@CrossOrigin
public class VendorProductController {
	
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


	private final static String VENDOR_ADD_PRODUCTS_TPL = "email_template_vendor_add_products.ftl";
	
	
	@RequestMapping(value="/addVendorProducts", method = RequestMethod.POST) 
	@ResponseBody
	public VendorProductResponse addVendorProducts(@RequestBody VendorProductRequest vendorProductRequest ) throws Exception {
	   
		System.out.println("Entered addVendorProducts:");
		String vendorId = vendorProductRequest.getVendorId();
		System.out.println(vendorId);
		Customer customer = customerService.getById(Long.parseLong(vendorId));
		System.out.println("Customer:"+customer);
		List<String> productIds = vendorProductRequest.getProductId();
		System.out.println(productIds);
		
		VendorProductResponse vendorProductResponse = new VendorProductResponse(); 
		
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
		System.out.println("vpList:"+vpList.size());
		vendorProductService.save(vpList);
		vendorProductResponse.setVenderId(vendorId);
		vendorProductResponse.setVendorProducts(vList);
		
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

		
		return vendorProductResponse;
	}
	
	@RequestMapping(value="/addVendorWishListProducts", method = RequestMethod.POST) 
	@ResponseBody
	public VendorProductResponse addVendorWishListProducts(@RequestBody VendorProductRequest vendorProductRequest ) throws Exception {
	   
		System.out.println("Entered addVendorProducts:");
		String vendorId = vendorProductRequest.getVendorId();
		System.out.println(vendorId);
		Customer customer = customerService.getById(Long.parseLong(vendorId));
		System.out.println("Customer:"+customer);
		List<String> productIds = vendorProductRequest.getProductId();
		System.out.println(productIds);
		
		VendorProductResponse vendorProductResponse = new VendorProductResponse(); 
		
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
		
		System.out.println("vpList:"+vpList.size());
		vendorProductService.save(vpList);
		vendorProductResponse.setVenderId(vendorId);
		vendorProductResponse.setVendorProducts(vList);
		
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
		return vendorProductResponse;
	}
	
	@RequestMapping(value={"/wishlist/{vendorId}"},  method = { RequestMethod.GET })
	@ResponseBody	
	public VendortProductList getWishListProducts(@PathVariable Long vendorId){
		VendortProductList vendorProductList = new VendortProductList();
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
                try {
					vpData.setProductPrice(pricingService.calculateProductPrice(product).getOriginalPrice().toString());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				vendorProductData.add(vpData);
			}
		}
		vendorProductList.setVendorProductData(vendorProductData);
		return vendorProductList;
	}

	@RequestMapping(value={"/productList/{vendorId}"},  method = { RequestMethod.GET })
	@ResponseBody	
	public VendortProductList getVendorProductList(@PathVariable Long vendorId){
		VendortProductList vendorProductList = new VendortProductList();
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
					e.printStackTrace();
				}
				vendorProductData.add(vpData);
			}
		}
		vendorProductList.setVendorProductData(vendorProductData);
		return vendorProductList;
	}

	@RequestMapping(value={"/vendors/{productId}"},  method = { RequestMethod.GET })
	@ResponseBody	
	public VendorsList getProductVendors(@PathVariable Long productId){
		VendorsList vendorsList = new VendorsList();
		List<VendorResponse> vendorsDataForProduct = new ArrayList<VendorResponse>();
		try {
		List<VendorProduct> dbVendorProductList = vendorProductService.findProductVendors(productId);
		System.out.println("dbVendorProductList size=="+dbVendorProductList.size());
		for(VendorProduct vendorProduct : dbVendorProductList){
			VendorResponse vendorResponse = new VendorResponse();
			System.out.println("customer --vendor=="+vendorProduct.getCustomer().getEmailAddress());
			if(vendorProduct.getCustomer() != null) {
				vendorResponse.setEmail(vendorProduct.getCustomer().getEmailAddress());
				vendorResponse.setVendorName(vendorProduct.getCustomer().getVendorAttrs().getVendorName());
				vendorResponse.setVendorOfficeAddress(vendorProduct.getCustomer().getVendorAttrs().getVendorOfficeAddress());
				vendorResponse.setVendorMobile(vendorProduct.getCustomer().getVendorAttrs().getVendorMobile());
				vendorResponse.setVendorTelephone(vendorProduct.getCustomer().getVendorAttrs().getVendorTelephone());
				vendorResponse.setVendorFax(vendorProduct.getCustomer().getVendorAttrs().getVendorFax());
				vendorResponse.setVendorConstFirm(vendorProduct.getCustomer().getVendorAttrs().getVendorConstFirm());
				vendorResponse.setVendorCompanyNature(vendorProduct.getCustomer().getVendorAttrs().getVendorCompanyNature());
				vendorResponse.setVendorRegistrationNo(vendorProduct.getCustomer().getVendorAttrs().getVendorRegistrationNo());
				vendorResponse.setVendorPAN(vendorProduct.getCustomer().getVendorAttrs().getVendorPAN());
				vendorResponse.setVendorLicense(vendorProduct.getCustomer().getVendorAttrs().getVendorLicense());
				vendorResponse.setVendorExpLine(vendorProduct.getCustomer().getVendorAttrs().getVendorExpLine());
				vendorResponse.setVendorMajorCust(vendorProduct.getCustomer().getVendorAttrs().getVendorMajorCust());
				vendorResponse.setVatRegNo(vendorProduct.getCustomer().getVendorAttrs().getVendorVatRegNo());
				vendorResponse.setVendorTIN(vendorProduct.getCustomer().getVendorAttrs().getVendorTinNumber());
				vendorResponse.setVendorImageURL(vendorProduct.getCustomer().getVendorAttrs().getVendorAuthCert());
				vendorResponse.setAuthCertURL(vendorProduct.getCustomer().getVendorAttrs().getVendorAuthCert());
				
				vendorsDataForProduct.add(vendorResponse);
			}
			
		}
		vendorsList.setVendorsDataForProduct(vendorsDataForProduct);
		
		}catch(Exception e){
			System.out.println("error occured while retrieving vendors based on product id ="+productId+"---"+e.getMessage());
			e.printStackTrace(System.out);
		}
		return vendorsList;
	}
}
