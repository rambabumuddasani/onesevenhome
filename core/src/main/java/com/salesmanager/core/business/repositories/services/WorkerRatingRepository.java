package com.salesmanager.core.business.repositories.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.services.WorkerRating;

public interface WorkerRatingRepository extends JpaRepository<WorkerRating, Integer>  {
   
	@Query("select workerRating from CompanyService companyService inner join companyService.workerRating workerRating where companyService.id = ?1")
	List<WorkerRating> findByWorker(Integer workerId);


}
