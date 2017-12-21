package com.salesmanager.shop.admin.controller.products;

import java.util.List;

import com.salesmanager.shop.store.model.paging.PaginationData;

public class PaginatedReviewResponse {

	private PaginationData paginationData;
	private List reviewList;
	private Double avgReview;
	private Long totalratingCount;
	private String successMessage;
	private String errorMessage;
	private boolean status;
	
	public PaginationData getPaginationData() {
		return paginationData;
	}
	public void setPaginationData(PaginationData paginationData) {
		this.paginationData = paginationData;
	}
	public List getReviewList() {
		return reviewList;
	}
	public void setReviewList(List reviewList) {
		this.reviewList = reviewList;
	}
	public Double getAvgReview() {
		return avgReview;
	}
	public void setAvgReview(Double avgReview) {
		this.avgReview = avgReview;
	}
	public Long getTotalratingCount() {
		return totalratingCount;
	}
	public void setTotalratingCount(Long totalratingCount) {
		this.totalratingCount = totalratingCount;
	}
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

}
