package com.salesmanager.core.business.services.customer.testmonial.review;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.customer.CustomerTestimonial;

public interface CustomerTestmonialService extends SalesManagerEntityService<Long,CustomerTestimonial> {

	List<CustomerTestimonial> getAllTestimonials();

	CustomerTestimonial getTestimonialById(Long customerIdLong);

	List<CustomerTestimonial> getApprovedTestimonial();

	List<CustomerTestimonial> getDeclinedtestimonials();

	
}
