package com.salesmanager.shop.vendor.services.products;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.shop.vendor.repositories.products.VendorProductRepository;

@Service("vendorProductService")
public class VendorProductServiceImpl implements VendorProductService {
	
	VendorProductRepository vendorProductRepository;

	@Override
	public void save(Product entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Product entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create(Product entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Product entity) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Product getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long count() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(List<VendorProduct> vpList) {
		vendorProductRepository.save(vpList);
		
	}

	/*@Override
	public void save(VendorProduct vendorProduct) {
		
		  vendorProductRepository.save(vendorProduct);
		
	}*/



}
