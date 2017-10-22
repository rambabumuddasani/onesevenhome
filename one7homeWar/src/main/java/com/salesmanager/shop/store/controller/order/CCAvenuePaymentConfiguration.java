package com.salesmanager.shop.store.controller.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
//@PropertySource("classpath:onesevenhome.properties")
//@PropertySource("file:${app.home}/app.properties")
//@ConfigurationProperties
public class CCAvenuePaymentConfiguration {

	@Value("${ccav.accessCode}")
	private String accessCode;
	
	@Value("${ccav.workingKey}")	
	private String workingKey;

	@Value("${ccav.merchantID}")	
	private String merchantID;
	
	@Value("${ccav.currency}")	
	private String currency;
	
	@Value("${ccav.cancel_url}")	
	private String cancelUrl;
	
	@Value("${ccav.redirect_url}")	
	private String redirectUrl;

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getWorkingKey() {
		return workingKey;
	}

	public void setWorkingKey(String workingKey) {
		this.workingKey = workingKey;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
