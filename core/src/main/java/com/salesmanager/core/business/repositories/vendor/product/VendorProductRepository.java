package com.salesmanager.core.business.repositories.vendor.product;

import org.springframework.data.jpa.repository.JpaRepository;
import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductRepository extends JpaRepository<VendorProduct, Long>{

}
