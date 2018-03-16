package com.salesmanager.shop.admin.controller;

import java.util.Comparator;

public class TotalRevenueDescComparator implements Comparator<VendorRevenueVO> {

	/*@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return 0;
	}*/

	@Override
	public int compare(VendorRevenueVO o1, VendorRevenueVO o2) {
		// TODO Auto-generated method stub
		if(o1.getTotalRevenue()==o2.getTotalRevenue())  
			return 0;  
			else if(o1.getTotalRevenue()>o2.getTotalRevenue())  
			return 1;  
			else  
			return -1;  
			
	}

}
