package com.salesmanager.shop.admin.controller;

public class DealUpdateOrRemoveResponse {

	    private String successMsg;
	    private String errorMesg;
	    private String status;
		public String getSuccessMsg() {
			return successMsg;
		}
		public void setSuccessMsg(String successMsg) {
			this.successMsg = successMsg;
		}
		public String getErrorMesg() {
			return errorMesg;
		}
		public void setErrorMesg(String errorMesg) {
			this.errorMesg = errorMesg;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
}
