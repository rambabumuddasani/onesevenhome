package com.salesmanager.shop.admin.controller;

public class RequirementRequest {

	private Long postRequirementId;
	private String responseMessage;
	public Long getPostRequirementId() {
		return postRequirementId;
	}
	public void setPostRequirementId(Long postRequirementId) {
		this.postRequirementId = postRequirementId;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
}
