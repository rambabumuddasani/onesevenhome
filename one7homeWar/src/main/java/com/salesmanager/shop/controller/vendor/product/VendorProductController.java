package com.salesmanager.shop.controller.vendor.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.shop.constants.EmailConstants;
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
			productsInfo.setProductId(dbProduct.getId());
			productsInfo.setProductName(dbProduct.getSku());
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
}
