package com.salesmanager.shop.controller.vendor;

public class WallPaperDetailsResponse {

	WallPaperDetails wallPaperDetails;
	private String successmessage;
	private String errorMessage;
	private String status;
	public WallPaperDetails getWallPaperDetails() {
		return wallPaperDetails;
	}
	public void setWallPaperDetails(WallPaperDetails wallPaperDetails) {
		this.wallPaperDetails = wallPaperDetails;
	}
	public String getSuccessmessage() {
		return successmessage;
	}
	public void setSuccessmessage(String successmessage) {
		this.successmessage = successmessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
