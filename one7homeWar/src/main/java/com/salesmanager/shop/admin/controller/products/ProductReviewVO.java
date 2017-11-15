package com.salesmanager.shop.admin.controller.products;

import java.util.Date;

import javax.persistence.Column;

import org.hibernate.annotations.Type;

public class ProductReviewVO {
	
	//private Long id;
	//private Integer status;
	private Double reviewRating;
	private Date reviewDate;
	//private Long reviewRead;
	//private Long customerId;
	//private Long productId;
	//private String descriptionName;
	private String description;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Double getReviewRating() {
		return reviewRating;
	}
	public void setReviewRating(Double reviewRating) {
		this.reviewRating = reviewRating;
	}
	public Date getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
	
	
}
