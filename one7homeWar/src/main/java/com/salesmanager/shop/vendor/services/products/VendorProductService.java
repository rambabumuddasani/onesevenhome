package com.salesmanager.shop.vendor.services.products;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductService extends SalesManagerEntityService<Long, Product> {

	void save(List<VendorProduct> vpList);

	//void save(VendorProduct vendorProduct);
    
}
