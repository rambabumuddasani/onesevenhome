package com.salesmanager.shop.controller.vendor.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.product.vendor.VendorProduct;

@Controller
@CrossOrigin
public class VendorProductController {
	
	@Inject
	CustomerService customerService;
	
	@Inject
	ProductService productService;
	
	@Inject
	VendorProductService vendorProductService;
	
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
		return vendorProductResponse;
	}
}
