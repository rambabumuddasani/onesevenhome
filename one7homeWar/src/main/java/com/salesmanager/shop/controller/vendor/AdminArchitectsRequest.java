package com.salesmanager.shop.controller.vendor;

public class AdminArchitectsRequest {

	private String status;
	private Long portfolioId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(Long portfolioId) {
		this.portfolioId = portfolioId;
	}
}
