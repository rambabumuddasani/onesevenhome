package com.salesmanager.shop.controller.user.updates;

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

import com.salesmanager.core.business.services.user.updates.UserUpdatesService;
import com.salesmanager.core.model.user.updates.UserUpdates;


@Controller
@CrossOrigin
public class UserUpdatesController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUpdatesController.class);
	
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	@Inject
	UserUpdatesService userUpdatesService;
	
	/* Saving external user email address for updates
	 * 
	 */
	@RequestMapping(value="/saveExternalUser", method = RequestMethod.POST)
	@ResponseBody
	public ExternalUserResponse saveExternalUser(@RequestBody ExternalUserRequest externalUserRequest) throws Exception{
		
		LOGGER.debug("Entered saveExternalUser");
		
		ExternalUserResponse externalUserResponse = new ExternalUserResponse();
		
		try {
			
		String emailAddress = externalUserRequest.getEmailAddress();
		
		if(StringUtils.isEmpty(emailAddress) || emailAddress.equals(" ")|| emailAddress.equals(null)) {
			externalUserResponse.setErrorMessage("Email address should not be empty or null");
			externalUserResponse.setStatus(FALSE);
			return externalUserResponse;
		}
		
		UserUpdates userUpdates = new UserUpdates();
		
		userUpdates.setEmailAddress(emailAddress);
		
		userUpdatesService.save(userUpdates);
		
		LOGGER.debug("User saved");
		externalUserResponse.setSuccessMessage("External User saved successfully");
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
