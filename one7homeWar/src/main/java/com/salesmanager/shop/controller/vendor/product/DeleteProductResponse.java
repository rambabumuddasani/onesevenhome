package com.salesmanager.shop.controller.vendor.product;

import java.util.List;

public class DeleteProductResponse {

	private String venderId;
	private List<ProductsInfo> vendorProducts;
	private String errorMessage;
	private String successMessage;
	private String status;
	public String getVenderId() {
		return venderId;
	}
	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}
	public List<ProductsInfo> getVendorProducts() {
		return vendorProducts;
	}
	public void setVendorProducts(List<ProductsInfo> vendorProducts) {
		this.vendorProducts = vendorProducts;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
