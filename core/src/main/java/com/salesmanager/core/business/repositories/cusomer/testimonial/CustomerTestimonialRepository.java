package com.salesmanager.core.business.repositories.cusomer.testimonial;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.customer.CustomerTestimonial;

public interface CustomerTestimonialRepository extends JpaRepository<CustomerTestimonial,Long>,CustomerTestimonialRepositoryCustom{
    
	@Query("select ct from CustomerTestimonial ct where ct.id =?1")
	CustomerTestimonial getTestimonialById(Long testimonialIdLong);

	
	@Query("select ct from CustomerTestimonial ct where ct.status = 'Y'")
	List<CustomerTestimonial> getApprovedTestimonial();

	@Query("select ct from CustomerTestimonial ct where ct.status = 'N'")
	List<CustomerTestimonial> getDeclinedtestimonials();

}
