package com.salesmanager.shop.vendor.services.products;

import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductService extends SalesManagerEntityService<Long, VendorProduct> {

	void save(List<VendorProduct> vpList) throws ServiceException;

	void save(VendorProduct vendorProduct) throws ServiceException;
    
}
