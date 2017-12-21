package com.salesmanager.core.business.repositories.vendor;

import java.util.List;

import com.salesmanager.core.model.customer.VendorRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendorRatingRepository extends JpaRepository<VendorRating, Long> {
	@Query("select vr from VendorRating vr where vr.vendor.id = ?1")
	List<VendorRating> getVendorReviews(Long vendorId);

}
