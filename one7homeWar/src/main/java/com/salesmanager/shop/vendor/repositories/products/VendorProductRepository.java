package com.salesmanager.shop.vendor.repositories.products;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.salesmanager.core.business.repositories.catalog.product.ProductRepositoryCustom;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.product.vendor.VendorProduct;

public interface VendorProductRepository extends JpaRepository<VendorProduct, Long>, VendorProductRepositoryCustom{

}
