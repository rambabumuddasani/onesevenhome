package com.salesmanager.core.business.services.services;

import java.util.Set;

import com.salesmanager.core.business.modules.services.WorkerRatingResponse;
import com.salesmanager.core.business.modules.services.WorkerServiceResponse;
import com.salesmanager.core.model.services.CompanyService;


public interface WorkerService {
 
	  public Set<CompanyService> getWorkerByService(Integer serviceId);
	  
	  public WorkerServiceResponse getWorkerByServiceType(String serviceType);

	  public WorkerRatingResponse  getWorkrRatingdByWorker(Integer workerId);
}
