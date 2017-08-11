package com.salesmanager.shop.vendor.controller.products;

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
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.product.vendor.VendorProduct;
//import com.salesmanager.shop.admin.controller.products.VendorProductRequest;
import com.salesmanager.shop.vendor.services.products.VendorProductService;

@Controller
@CrossOrigin
public class VendorProductController {
	
	@Inject
	CustomerService customerService;
	
	@Inject
	ProductService productService;
	
	@Inject
	VendorProductService vendorProductService;
	
	@RequestMapping(value="/addVendorProducts", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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
		
		for(String productId : productIds){
			System.out.println("Entered for:");
			System.out.println("ProductId:"+productId);
			Product dbProduct = productService.getById(Long.parseLong(productId));
			System.out.println("dbProduct:"+dbProduct);
			VendorProduct vendorProduct = new VendorProduct();
			vendorProduct.setProduct(dbProduct);
			vendorProduct.setCustomer(customer);
			vendorProduct.setCreatedDate(new Date());
			System.out.println("VendorProduct:"+vendorProduct);
			//vpList.add(vendorProduct);
			vendorProductService.save(vendorProduct);
		}
		
		System.out.println("vpList:"+vpList);
		
		//vendorProductService.save(vpList);
		VendorProducts vProducts = new VendorProducts();
		List<VendorProducts> vList = new ArrayList<VendorProducts>();
		for(VendorProduct vendorProducts: vpList) {
			vProducts.setProductId(vendorProducts.getId());
			vList.add(vProducts);
		    
		}
		
		vendorProductResponse.setVendorProducts(vList);
		return vendorProductResponse;
	}
	
	@RequestMapping(value="/dummy", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public VendorProductRequest dummyMethod(@RequestBody VendorProductRequest vendorProductRequest ) {
		return vendorProductRequest;
	}
}
