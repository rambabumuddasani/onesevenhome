package com.salesmanager.core.business.vendor.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductRepository extends JpaRepository<VendorProduct, Long>{

}
