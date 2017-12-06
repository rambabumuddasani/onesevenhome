package com.salesmanager.core.business.vendor.product.services;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.vendor.product.VendorProductRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.product.vendor.VendorProduct;

@Service("vendorProductService")
public class VendorProductServiceImpl extends SalesManagerEntityServiceImpl<Long, VendorProduct> implements  VendorProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VendorProductServiceImpl.class);
	
	private VendorProductRepository vendorProductRepository;

	@Inject
	public VendorProductServiceImpl(VendorProductRepository vendorProductRepository) {
		super(vendorProductRepository);
		this.vendorProductRepository = vendorProductRepository;
	}

	@Override
	public void save(List<VendorProduct> vpList) {
		LOGGER.debug("Saving vendor products");
		vendorProductRepository.save(vpList);
	}

	@Override
	public void save(VendorProduct vendorProduct) {
		LOGGER.debug("Saving vendor product");
		vendorProductRepository.save(vendorProduct);
	}
	
	@Override
	public List<VendorProduct> findProductsByVendor(Long vendorId) {
		LOGGER.debug("Fetching products by vendor");
		return vendorProductRepository.findProductsByVendor(vendorId);
	}

	@Override
	public List<VendorProduct> findProductWishListByVendor(Long vendorId) {
		LOGGER.debug("Fetching product wish list by vendor");
		return vendorProductRepository.findProductWishListByVendor(vendorId);
	}

	@Override
	public List<VendorProduct> getVendorProducts() {
		LOGGER.debug("fetching vendor products");
		return vendorProductRepository.findVendorProducts();
	}

	@Override
	public VendorProduct getVendorProductById(Long vendorProductId) {
		LOGGER.debug("fetching vendor product by vendor product id");
		return vendorProductRepository.findOne(vendorProductId);
	}
	
	@Override
	public List<VendorProduct> findProductVendors(Long productId) {
		LOGGER.debug("fetching admin activated vendor products");
		return vendorProductRepository.findProductVendors(productId);
	}

	
	@Override
	public List<VendorProduct> findProductVendorsByProductIdAndCustomerPinCode(Long productId, String postalCode) {
		LOGGER.debug("fetching admin activated vendor products");
		return vendorProductRepository.findProductVendorsByProductIdAndCustomerPinCode(productId,postalCode);
	}

}
