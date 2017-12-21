package com.salesmanager.shop.controller.vendor;

import java.util.List;

import com.salesmanager.core.model.customer.VendorRating;

public class VendorBookingResponse {
     
	private String successMessage;
	private String errorMessage;
	private boolean status;
	private List<VendorRating> vendorRatingList;
	private Double avgRating;
	private int totalReviews;

	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public List<VendorRating> getVendorRatingList() {
		return vendorRatingList;
	}
	public void setVendorRatingList(List<VendorRating> vendorRatingList) {
		this.vendorRatingList = vendorRatingList;
	}
	public Double getAvgRating() {
		return avgRating;
	}
	public void setAvgRating(Double avgRating) {
		this.avgRating = avgRating;
	}
	public int getTotalReviews() {
		return totalReviews;
	}
	public void setTotalReviews(int totalReviews) {
		this.totalReviews = totalReviews;
	}
	
}
