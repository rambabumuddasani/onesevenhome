package com.salesmanager.core.business.repositories.vendor.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductRepository extends JpaRepository<VendorProduct, Long>{
	
	@Query("select distinct vp from VendorProduct vp left join fetch vp.product pd "
			+ " join fetch pd.images img  join fetch pd.attributes attr join fetch pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where customer.id=?1")
	public List<VendorProduct> findProductsByVendor(Long vendorId);

}
