package com.salesmanager.shop.admin.controller.products;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;



public class CreateProductResponse {

	Long productId;
	boolean status;
	String errorMsg;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	
}
