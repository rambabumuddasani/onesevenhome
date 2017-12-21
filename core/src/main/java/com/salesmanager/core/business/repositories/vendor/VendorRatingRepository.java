package com.salesmanager.core.business.repositories.vendor;

import com.salesmanager.core.model.customer.VendorRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VendorRatingRepository extends JpaRepository<VendorRating, Long> {

}
