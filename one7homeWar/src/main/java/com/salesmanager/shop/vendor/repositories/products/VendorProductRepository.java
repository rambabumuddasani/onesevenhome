package com.salesmanager.shop.vendor.repositories.products;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.product.vendor.VendorProduct;

public interface VendorProductRepository extends JpaRepository<VendorProduct, Long>{

}
