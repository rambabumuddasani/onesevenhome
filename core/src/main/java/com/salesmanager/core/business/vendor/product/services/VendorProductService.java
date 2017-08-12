package com.salesmanager.core.business.vendor.product.services;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductService extends SalesManagerEntityService<Long, VendorProduct> {
	void save(List<VendorProduct> vpList);
	void save(VendorProduct vendorProduct) ;
}
