package com.salesmanager.core.business.vendor.product.services;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.vendor.product.repositories.VendorProductRepository;
import com.salesmanager.core.model.product.vendor.VendorProduct;


@Service("vendorProductService")
public class VendorProductServiceImpl extends SalesManagerEntityServiceImpl<Long, VendorProduct> implements  VendorProductService {

	
	VendorProductRepository vendorProductRepository;

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
}
