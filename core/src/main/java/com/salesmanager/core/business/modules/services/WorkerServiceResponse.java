package com.salesmanager.core.business.modules.services;

import java.util.Set;

import com.salesmanager.core.model.services.CompanyService;

public class WorkerServiceResponse {

	Set<ServicesWorkerVO> workers;

	public Set<ServicesWorkerVO> getWorkers() {
		return workers;
	}

	public void setWorkers(Set<ServicesWorkerVO> workers) {
		this.workers = workers;
	}
	
	
}
