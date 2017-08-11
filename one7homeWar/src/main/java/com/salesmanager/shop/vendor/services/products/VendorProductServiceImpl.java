package com.salesmanager.shop.vendor.services.products;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.shop.vendor.repositories.products.VendorProductRepository;

@Service("vendorProductService")
public class VendorProductServiceImpl extends SalesManagerEntityServiceImpl<Long, VendorProduct> implements  VendorProductService {

	
	VendorProductRepository vendorProductRepository;

	@Inject
	public VendorProductServiceImpl(VendorProductRepository repository) {
		super(repository);
		this.vendorProductRepository = repository;
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
