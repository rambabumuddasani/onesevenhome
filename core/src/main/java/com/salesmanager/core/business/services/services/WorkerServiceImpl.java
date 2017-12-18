package com.salesmanager.core.business.services.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.modules.services.ServicesWorkerVO;
import com.salesmanager.core.business.modules.services.WorkerRatingResponse;
import com.salesmanager.core.business.modules.services.WorkerRatingVO;
import com.salesmanager.core.business.modules.services.WorkerServiceResponse;
import com.salesmanager.core.business.repositories.services.WorkerRatingRepository;
import com.salesmanager.core.business.repositories.services.CompanyServiceRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.services.CompanyService;
import com.salesmanager.core.model.services.WorkerRating;

@Service
public class WorkerServiceImpl extends SalesManagerEntityServiceImpl<Integer, CompanyService> implements WorkerService  {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkerServiceImpl.class);
	
	@Inject
	CompanyServiceRepository serviceWorkerRepository;
	
	@Inject
	WorkerRatingRepository workerRatingRepository;

	@Inject
	public WorkerServiceImpl(CompanyServiceRepository repository) {
		super(repository);	
		serviceWorkerRepository = repository;
	}

	@Override
	public Set<CompanyService> getWorkerByService(Integer serviceId) {
		LOGGER.debug("Fetching service companies by service id");
		Set<CompanyService> workerService=serviceWorkerRepository.findByServiceId(serviceId);
		return workerService;
	}

/*	@Override
	public Set<ServicesWorker> getWorkerByServiceType(String serviceType) {		
		Set<ServicesWorker> workerService=serviceWorkerRepository.findByServiceType(serviceType);
		
		return workerService;
	}
*/


	public WorkerServiceResponse getWorkerByServiceType(String serviceType) {	
		LOGGER.debug("Fetching service companies by service type");
		WorkerServiceResponse response = new WorkerServiceResponse();
		/*Set<CompanyService> serviceWorker = serviceWorkerRepository.findByServiceType(serviceType);
		Set<ServicesWorkerVO> servicesWorkerVOSet= new HashSet<ServicesWorkerVO>();
		for(CompanyService eachWorker : serviceWorker){
			int avgRating = getAvgWorkerRating(eachWorker.getWorkerRating());
			int totalRating= getTotalRating(eachWorker.getWorkerRating());
			ServicesWorkerVO servicesWorkerVO = new ServicesWorkerVO();
			servicesWorkerVO.setId(eachWorker.getId());
			servicesWorkerVO.setCompanyName(eachWorker.getCompanyName());
			servicesWorkerVO.setHouseNumber(eachWorker.getHouseNumber());
			servicesWorkerVO.setStreet(eachWorker.getStreet());
			servicesWorkerVO.setArea(eachWorker.getArea());
			servicesWorkerVO.setCity(eachWorker.getCity());
			servicesWorkerVO.setState(eachWorker.getState());
			servicesWorkerVO.setPinCode(eachWorker.getPinCode());
			servicesWorkerVO.setContactNumber(eachWorker.getContactNumber());
			servicesWorkerVO.setImageUrl(eachWorker.getImageUrl());
			servicesWorkerVO.setAvgRating(avgRating);
			servicesWorkerVO.setTotalRating(totalRating);
			servicesWorkerVOSet.add(servicesWorkerVO);
		}
		response.setWorkers(servicesWorkerVOSet);*/
		return response;
	}
	private int getTotalRating(Set<WorkerRating> ratings) {
    LOGGER.debug("Calculating total rating ");
    int totalRating=0;
  //Set<WorkerRating> ratings =worker.getWorkerRating();
  		Map<Integer,Integer> ratingMap = new HashMap<Integer , Integer> (5);

  		for(WorkerRating rating : ratings) {	
  			Integer workerRating = rating.getRating();
  			if(ratingMap.containsKey(workerRating)){
  				Integer count = ratingMap.get(workerRating);
  				ratingMap.put(workerRating,count++);
  			}else {
  				ratingMap.put(workerRating,1);
  			}
  		}
  		Set<Map.Entry<Integer, Integer>> entrySet = ratingMap.entrySet();
  		System.out.println("ratingMap "+ratingMap);
  		int totalNumarater = 0;
  		int totalDenaminator = 0;
  		for(Map.Entry<Integer, Integer> eachEntry : entrySet){
  			totalNumarater += (eachEntry.getKey()*eachEntry.getValue());
  			totalDenaminator += eachEntry.getValue();
  		}
  		System.out.println(" totalNumarater "+totalNumarater);
  		System.out.println("totalDenaminator "+totalDenaminator);
  		if(totalDenaminator == 0){
  			return 0;
  		}
  	    totalRating =totalDenaminator;
  		return totalRating;
    
}

	private int getAvgWorkerRating(Set<WorkerRating> ratings) {
        LOGGER.debug("Calculating average rating");
		int avgRating = 0;
		//Set<WorkerRating> ratings =worker.getWorkerRating();
		Map<Integer,Integer> ratingMap = new HashMap<Integer , Integer> (5);

		for(WorkerRating rating : ratings) {	
			Integer workerRating = rating.getRating();
			if(ratingMap.containsKey(workerRating)){
				Integer count = ratingMap.get(workerRating);
				ratingMap.put(workerRating,count++);
			}else {
				ratingMap.put(workerRating,1);
			}
		}
		// now we need to apply the formula
		/**
		 * formual is 
		 * 
		 * avgRating = ((5*ratingMap.get(5))+(4*ratingMap.get(4))+(3*ratingMap.get(3))+(2*ratingMap.get(2))+(1*ratingMap.get(1))/(ratingMap.get(5)+ratingMap.get(4)+ratingMap.get(3)+ratingMap.get(2)+ratingMap.get(1));
		 */
		Set<Map.Entry<Integer, Integer>> entrySet = ratingMap.entrySet();
		System.out.println("ratingMap "+ratingMap);
		int totalNumarater = 0;
		int totalDenaminator = 0;
		for(Map.Entry<Integer, Integer> eachEntry : entrySet){
			totalNumarater += (eachEntry.getKey()*eachEntry.getValue());
			totalDenaminator += eachEntry.getValue();
		}
		System.out.println(" totalNumarater "+totalNumarater);
		System.out.println("totalDenaminator "+totalDenaminator);
		if(totalDenaminator == 0){
			return 0;
		}
	    avgRating = totalNumarater/totalDenaminator;
		return avgRating;
	}

	@Override
	public WorkerRatingResponse getWorkrRatingdByWorker(Integer workerId) {
		LOGGER.debug("Get WorkrRatingdByWorker by id");
		WorkerRatingResponse workerRatingResponse = new WorkerRatingResponse();
		/*List<WorkerRating> workerRatings = workerRatingRepository.findByWorker(workerId);
		List<WorkerRatingVO> workerRatingVOSet = new ArrayList<WorkerRatingVO>();
	    for(WorkerRating ratings:workerRatings) {
	    	WorkerRatingVO workerRatingVO = new WorkerRatingVO(); 
	    	workerRatingVO.setId(ratings.getId());
	    	workerRatingVO.setRating(ratings.getRating());
	    	workerRatingVO.setRatingDesc(ratings.getRatingDesc());
	    	workerRatingVO.setCreateDate(ratings.getCreateDate());
	    	workerRatingVO.setCustomerName("Ram");
	    	workerRatingVOSet.add(workerRatingVO);
	    }
	    
	    workerRatingResponse.setWorkerRatings(workerRatingVOSet);*/
		
		return  workerRatingResponse;		
	}
	

}
