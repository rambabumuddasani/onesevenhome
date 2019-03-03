package com.salesmanager.shop.controller.user.updates;

import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.services.user.updates.UserUpdatesService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.user.updates.UserUpdates;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;


@Controller
@CrossOrigin
public class UserUpdatesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUpdatesController.class);
	
	private final static String EXTERNAL_USER_UPDATES_TMPL = "email_template_external_user_updates.ftl";
	
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	@Inject
	UserUpdatesService userUpdatesService;
	
	@Inject
    MerchantStoreService merchantStoreService ;
	
	@Inject
	private EmailUtils emailUtils;
	
	@Inject
	EmailService emailService;
	
	@Inject
	private LabelUtils messages;
	
	/* Saving external user email address for updates
	 * 
	 */
	@RequestMapping(value="/saveExternalUser", method = RequestMethod.POST)
	@ResponseBody
	public ExternalUserResponse saveExternalUser(@RequestBody ExternalUserRequest externalUserRequest) throws Exception{
		
		LOGGER.debug("Entered saveExternalUser");
		
		ExternalUserResponse externalUserResponse = new ExternalUserResponse();
		
		String emailAddress = externalUserRequest.getEmailAddress();
		
		try {
			
		if(StringUtils.isEmpty(emailAddress) || emailAddress.equals(" ")|| emailAddress.equals(null)) {
			externalUserResponse.setErrorMessage("Email address should not be empty or null");
			externalUserResponse.setStatus(FALSE);
			return externalUserResponse;
		}
		
		UserUpdates userUpdates = new UserUpdates();
		
		userUpdates.setEmailAddress(emailAddress);
		
		userUpdatesService.save(userUpdates);
		
		LOGGER.debug("User saved");
		
		LOGGER.debug("Sending mail to user starts");
		
		final Locale locale  = new Locale("en");
	
        MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");
		
		Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		
		templateTokens.put(EmailConstants.EMAIL_USER_NAME, emailAddress);
		templateTokens.put(EmailConstants.EMAIL_URL_LINK, messages.getMessage("email.url.link",locale));
		
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.external.user.updates",locale));
		email.setTo(emailAddress);
		email.setTemplateName(EXTERNAL_USER_UPDATES_TMPL);
		email.setTemplateTokens(templateTokens);

		emailService.sendHtmlEmail(EmailConstants.ADMIN_EMAIL_SENDER, email);
		
		LOGGER.debug("Email sent to user");
		
		externalUserResponse.setSuccessMessage("Thank you for showing your interest in One Seven Home. Check out for periodic updates in your email account");
		externalUserResponse.setStatus(TRUE);
		
		}catch(Exception e) {
			e.printStackTrace();
			LOGGER.debug("Error while saving external user "+e.getMessage());
			externalUserResponse.setErrorMessage("Error while saving external user");
			externalUserResponse.setStatus(FALSE);
			return externalUserResponse;
		}
		
		LOGGER.debug("Ended saveExternalUser");
		return externalUserResponse;
		
	}
}
