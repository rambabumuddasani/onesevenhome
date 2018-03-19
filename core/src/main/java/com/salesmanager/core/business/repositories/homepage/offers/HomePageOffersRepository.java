package com.salesmanager.core.business.repositories.homepage.offers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.homepage.offers.HomePageOffers;

public interface HomePageOffersRepository extends JpaRepository<HomePageOffers, Long>{

	@Query("select hp from HomePageOffers hp order by hp.sectionName asc")
	List<HomePageOffers> findAllHomePageOffers();

}
