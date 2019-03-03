package com.salesmanager.core.business.email.config;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.email.config.EmailConfigRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.email.config.EmailConfig;

@Service("emailConfigService")
public class EmailConfigServiceImpl extends SalesManagerEntityServiceImpl<Long, EmailConfig> implements EmailConfigService {

	private EmailConfigRepository emailConfigRepository;
	
	@Inject
	public EmailConfigServiceImpl(EmailConfigRepository emailConfigRepository) {
		super(emailConfigRepository);
		this.emailConfigRepository = emailConfigRepository;
	}

	@Override
	public EmailConfig findEmailConfigById(Long id) {
		return emailConfigRepository.findById(id);	
	}
}