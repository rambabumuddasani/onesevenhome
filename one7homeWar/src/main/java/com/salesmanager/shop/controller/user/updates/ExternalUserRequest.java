package com.salesmanager.shop.controller.user.updates;

import java.io.Serializable;

public class ExternalUserRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3978400384373684180L;
	
	private String emailAddress;

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
