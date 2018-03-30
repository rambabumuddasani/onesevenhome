package com.salesmanager.shop.admin.controller;

import java.util.Comparator;

public class TotalProductRevenueAscComparator implements Comparator<ProductRevenueVO> {

	@Override
	public int compare(ProductRevenueVO o1, ProductRevenueVO o2) {
		
		if(o1.getTotalRevenue()==o2.getTotalRevenue())  
			return 0;  
		else if(o1.getTotalRevenue()>o2.getTotalRevenue())  
			return 1;  
		else  
			return -1;  
   }

}
