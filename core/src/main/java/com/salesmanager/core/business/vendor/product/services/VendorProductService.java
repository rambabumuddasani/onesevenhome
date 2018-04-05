package com.salesmanager.core.business.vendor.product.services;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductService extends SalesManagerEntityService<Long, VendorProduct> {
	void save(List<VendorProduct> vpList);
	void save(VendorProduct vendorProduct) ;
	List<VendorProduct> findProductsByVendor(Long vendorId);
	List<VendorProduct> findProductWishListByVendor(Long vendorId);
	List<VendorProduct> getVendorProducts();
	VendorProduct getVendorProductById(Long vendorProductId);
	List<VendorProduct> findProductVendors(Long productId);
	List<VendorProduct> findProductVendorsByProductIdAndCustomerPinCode(Long productId, String postalCode);
	List<Customer> getRequestedVendors();
	List<VendorProduct> getVendorAddedProductsByVendorId(Long vendorId);
	List<VendorProduct> getVendorApprovedProductsByVendorId(Long vendorId);
	List<VendorProduct> getByProductIdAndVendorId(Long productId,Long vendorId);
	List<VendorProduct> getVendoProductsProductIdAndVendorId(Long longProductId, Long longVendorId);
	List<Customer> searchRequestedVendorsByName(String searchString);
	List<Customer> searchRequestedVendorsById(Long vendorId);
}
