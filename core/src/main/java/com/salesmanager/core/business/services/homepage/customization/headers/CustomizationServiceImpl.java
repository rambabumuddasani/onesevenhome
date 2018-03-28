package com.salesmanager.core.business.services.homepage.customization.headers;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.homepage.customization.headers.CustomizationRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.homepage.customization.headers.Customizations;

@Service("customizationService")
public class CustomizationServiceImpl extends SalesManagerEntityServiceImpl<Long, Customizations> implements CustomizationService{
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(CustomizationServiceImpl.class);
		
	 private CustomizationRepository customizationRepository;
	
	 @Inject
     public CustomizationServiceImpl(CustomizationRepository customizationRepository) {
		super(customizationRepository);
		this.customizationRepository = customizationRepository;
	}

	@Override
	public Customizations getByCustomizationId(String customizationId) {
		return customizationRepository.getByCustomizationId(customizationId);
	}

}
