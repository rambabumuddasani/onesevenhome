package com.salesmanager.shop.admin.controller;

import java.util.Comparator;

public class TotalRevenueComparator implements Comparator<VendorRevenueVO> {

	public int compare(VendorRevenueVO o1, VendorRevenueVO o2) {
		
		if(o1.getTotalRevenue()==o2.getTotalRevenue())  
			return 0;  
			else if(o1.getTotalRevenue()>o2.getTotalRevenue())  
			return 1;  
			else  
			return -1;  
			}

}
