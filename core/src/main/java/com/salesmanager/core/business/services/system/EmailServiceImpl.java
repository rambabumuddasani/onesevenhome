package com.salesmanager.core.business.services.system;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.email.config.EmailConfigServiceImpl;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.modules.email.EmailConfig;
import com.salesmanager.core.business.modules.email.HtmlEmailSender;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.system.MerchantConfiguration;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	@Inject
	private MerchantConfigurationService merchantConfigurationService;
	
	@Inject
	EmailConfigServiceImpl emailConfigService;
	
	@Inject
	private HtmlEmailSender sender;
	
	@Override
	public void sendHtmlEmail(MerchantStore store, Email email) throws ServiceException, Exception {

		EmailConfig emailConfig = getEmailConfiguration(store);
		
		sender.setEmailConfig(emailConfig);
		sender.send(email);
	}
	
	@Override
	@Async
	public void sendHtmlEmail(Long emailConfigId, Email email) throws ServiceException, Exception {
		com.salesmanager.core.model.email.config.EmailConfig dbEmailConfig = getEmailConfiguration(emailConfigId);
		EmailConfig emailConfig = null;
		if(dbEmailConfig == null) {
			emailConfig = null;
		}else {
			 emailConfig = getEmailConfigFromDbEmailConfig(dbEmailConfig);
		}
		System.out.println("emailConfig "+emailConfig);
		sender.setEmailConfig(emailConfig);
		sender.send(email);	
	}
	
	private EmailConfig getEmailConfigFromDbEmailConfig(com.salesmanager.core.model.email.config.EmailConfig dbEmailConfig) {
		EmailConfig emailConfig = new EmailConfig();
		emailConfig.setHost(dbEmailConfig.getHost());
		emailConfig.setPassword(dbEmailConfig.getPassword());
		emailConfig.setPort(dbEmailConfig.getPort());
		emailConfig.setProtocol(dbEmailConfig.getProtocol());
		emailConfig.setSmtpAuth(dbEmailConfig.isSmtpAuth());
		emailConfig.setStarttls(dbEmailConfig.isStarttls());
		emailConfig.setEmailTemplatesPath(dbEmailConfig.getEmailTemplatesPath());
		return emailConfig;
	}
	
	@Override
	public EmailConfig getEmailConfiguration(MerchantStore store) throws ServiceException {
		
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		EmailConfig emailConfig = null;
		if(configuration!=null) {
			String value = configuration.getValue();
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				emailConfig = mapper.readValue(value, EmailConfig.class);
			} catch(Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return emailConfig;
	}
	
	@Override
	public com.salesmanager.core.model.email.config.EmailConfig getEmailConfiguration(Long emailConfigId)  {
		return emailConfigService.findEmailConfigById(emailConfigId);
	}
	
	@Override
	public void saveEmailConfiguration(EmailConfig emailConfig, MerchantStore store) throws ServiceException {
		MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(Constants.EMAIL_CONFIG, store);
		if(configuration==null) {
			configuration = new MerchantConfiguration();
			configuration.setMerchantStore(store);
			configuration.setKey(Constants.EMAIL_CONFIG);
		}
		
		String value = emailConfig.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);
	}

}
