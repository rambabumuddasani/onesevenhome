package com.salesmanager.core.business.services.customer;

import java.util.Comparator;

import com.salesmanager.core.business.modules.services.ServicesWorkerVO;

public class ServiceWorkersDescRatingComparator implements Comparator<ServicesWorkerVO> {

	@Override
	public int compare(ServicesWorkerVO o1, ServicesWorkerVO o2) {
		if(o1.getAvgRating()==o2.getAvgRating())  
			return 0;  
		else if(o1.getAvgRating()>o2.getAvgRating())  
			return 1;  
		else  
			return -1; 
	}

}
