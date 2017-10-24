package com.salesmanager.core.business.repositories.vendor.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.product.vendor.VendorProduct;


public interface VendorProductRepository extends JpaRepository<VendorProduct, Long>{
	
/*	@Query("select distinct vp from VendorProduct vp left join fetch vp.product pd "
			+ " join fetch pd.images img  join fetch pd.attributes attr join fetch pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where customer.id=?1")
*/	@Query("select distinct vp from VendorProduct vp left join fetch vp.product pd "
			+ " join fetch pd.images img  left join fetch pd.attributes pattr join fetch  pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where vp.vendorWishListed = FALSE and customer.id=?1")
	public List<VendorProduct> findProductsByVendor(Long vendorId);

	@Query("select distinct vp from VendorProduct vp  left join fetch vp.product pd "
			+ " join fetch pd.images img left join fetch pd.attributes pattr join fetch pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where vp.vendorWishListed = TRUE  and vp.customer.id=?1 ")
	public List<VendorProduct> findProductWishListByVendor(Long vendorId);

	public List<VendorProduct> findVendorById(Long vendorId);

	/*@Query("select vp from VendorProduct vp join fetch vp.product pd "
			+ "join fetch pd.descriptions descriptions join fetch vp.customer customer where vp.adminActivated = FALSE ")*/
	@Query("select distinct vp from VendorProduct vp  left join fetch vp.product pd "
			+ " join fetch pd.images img left join fetch pd.attributes pattr join fetch pd.availabilities av join fetch av.prices prices"
			+ " join fetch pd.descriptions descriptions join fetch vp.customer customer where vp.adminActivated = FALSE ")
	public List<VendorProduct> findVendorProducts();

	@Query("select distinct vp from VendorProduct vp join fetch vp.product pd join fetch vp.customer customer"
			+ " where vp.adminActivated = TRUE and pd.id=?1 ")
	public List<VendorProduct> findProductVendors(Long productId);

	/*	@Query("select vp from VendorProduct vp where vp.id=?1")
	public VendorProduct findVendorProductById(Long vendorProductId);
*/
	
}
