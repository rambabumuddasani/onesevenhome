package com.salesmanager.core.business.services.homepage.offers;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.homepage.offers.HomePageOffersRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.homepage.offers.HomePageOffers;


@Service("homePageOffersService")
public class HomePageOffersServiceImpl extends SalesManagerEntityServiceImpl<Long, HomePageOffers> implements  HomePageOffersService{

	 private static final Logger LOGGER = LoggerFactory.getLogger(HomePageOffersServiceImpl.class);
	
	 private HomePageOffersRepository homePageOffersRepository;
	
	 @Inject
     public HomePageOffersServiceImpl(HomePageOffersRepository homePageOffersRepository) {
		super(homePageOffersRepository);
		this.homePageOffersRepository = homePageOffersRepository;
	}

	/*@Override
	public List<HomePageOffers> getAllHomePageOffers() {
		// TODO Auto-generated method stub
		return homePageOffersRepository.findAll();
	}*/
	@Override
	public List<HomePageOffers> getAllHomePageOffers() {
		// TODO Auto-generated method stub
		return homePageOffersRepository.findAllHomePageOffers();
	}

}
