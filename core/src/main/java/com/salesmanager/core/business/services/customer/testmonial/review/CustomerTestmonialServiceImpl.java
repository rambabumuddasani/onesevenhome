package com.salesmanager.core.business.services.customer.testmonial.review;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.cusomer.testimonial.CustomerTestimonialRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.customer.CustomerTestimonial;

@Service("customerTestmonialService")
public class CustomerTestmonialServiceImpl extends SalesManagerEntityServiceImpl<Long,CustomerTestimonial> implements CustomerTestmonialService {

	@Inject
	private CustomerTestimonialRepository customerTestimonialRepository;
	
	@Inject
	public CustomerTestmonialServiceImpl(CustomerTestimonialRepository customerTestimonialRepository) {
		super(customerTestimonialRepository);
		this.customerTestimonialRepository = customerTestimonialRepository;
	}

	@Override
	public List<CustomerTestimonial> getAllTestimonials() {
		
		return customerTestimonialRepository.findAll();
	}

	@Override
	public CustomerTestimonial getTestimonialById(Long testimonialIdLong) {
		
		return customerTestimonialRepository.getTestimonialById(testimonialIdLong);
	}

	@Override
	public List<CustomerTestimonial> getApprovedTestimonial() {
		
		return customerTestimonialRepository.getApprovedTestimonial();
	}

	@Override
	public List<CustomerTestimonial> getDeclinedtestimonials() {
		
		return customerTestimonialRepository.getDeclinedtestimonials();
	}
}
