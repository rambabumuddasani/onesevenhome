package com.salesmanager.shop.admin.controller.products;

import java.util.Comparator;

import com.salesmanager.core.model.catalog.product.review.ProductReview;

public class DateComparator implements Comparator<ProductReview> {

	@Override
	public int compare(ProductReview o1, ProductReview o2) {
		
		return -o1.getReviewDate().compareTo(o2.getReviewDate());
	}

}
