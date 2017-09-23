package com.salesmanager.core.business.vendor.product.services;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.vendor.product.VendorProductRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.product.vendor.VendorProduct;

@Service("vendorProductService")
public class VendorProductServiceImpl extends SalesManagerEntityServiceImpl<Long, VendorProduct> implements  VendorProductService {

	
	private VendorProductRepository vendorProductRepository;

	@Inject
	public VendorProductServiceImpl(VendorProductRepository vendorProductRepository) {
		super(vendorProductRepository);
		this.vendorProductRepository = vendorProductRepository;
	}

	@Override
	public void save(List<VendorProduct> vpList) {
		vendorProductRepository.save(vpList);
	}

	@Override
	public void save(VendorProduct vendorProduct) {
		vendorProductRepository.save(vendorProduct);
	}
	
	@Override
	public List<VendorProduct> findProductsByVendor(Long vendorId) {
		return vendorProductRepository.findProductsByVendor(vendorId);
	}

	@Override
	public List<VendorProduct> findProductWishListByVendor(Long vendorId) {
		return vendorProductRepository.findProductWishListByVendor(vendorId);
	}

	@Override
	public List<VendorProduct> getVendorProducts() {
		return vendorProductRepository.findVendorProducts();
	}

	@Override
	public VendorProduct getVendorProductById(Long vendorProductId) {
		// TODO Auto-generated method stub
		return vendorProductRepository.findOne(vendorProductId);
	}

	
}
