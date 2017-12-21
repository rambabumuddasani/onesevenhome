package com.salesmanager.core.business.repositories.services;

import java.util.List;

import com.salesmanager.core.model.customer.ServicesRating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServicesRatingRepository extends JpaRepository<ServicesRating, Long> {
	@Query("select sr from ServicesRating sr where sr.service.id = ?1")
	List<ServicesRating> getServicesReviews(Long serviceId);

}
