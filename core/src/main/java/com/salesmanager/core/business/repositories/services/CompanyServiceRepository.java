package com.salesmanager.core.business.repositories.services;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.services.CompanyService;
import com.salesmanager.core.model.services.WorkerRating;

public interface CompanyServiceRepository extends JpaRepository<CompanyService,Integer> {
	
	//@Query("select sw from ServicesWorker as sw inner join fetch sw.services s inner join fetch s.serviceWorkers swk where s.id = ?1")
	@Query("select sw from Services s inner join s.serviceWorkers sw where s.id = ?1")
	public Set<CompanyService> findByServiceId(Integer serviceId);

	@Query("select sw from Services s inner join s.serviceWorkers sw where s.serviceType = ?1")
	public Set<CompanyService> findByServiceType(String serviceType);
    
	
}
