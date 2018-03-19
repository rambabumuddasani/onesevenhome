package com.salesmanager.core.business.services.homepage.offers;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.homepage.offers.HomePageOffers;

public interface HomePageOffersService extends SalesManagerEntityService<Long, HomePageOffers> {

	List<HomePageOffers> getAllHomePageOffers();

}
