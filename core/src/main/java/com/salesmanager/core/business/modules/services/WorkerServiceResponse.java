package com.salesmanager.core.business.modules.services;

import java.util.List;

import com.salesmanager.core.model.services.CompanyService;

public class WorkerServiceResponse {

	List<ServicesWorkerVO> workers;

	public List<ServicesWorkerVO> getWorkers() {
		return workers;
	}

	public void setWorkers(List<ServicesWorkerVO> workers) {
		this.workers = workers;
	}
	
	
}
