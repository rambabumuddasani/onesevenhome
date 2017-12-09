package com.salesmanager.core.business.repositories.services;

import com.salesmanager.core.model.customer.ServicesRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServicesRatingRepository extends JpaRepository<ServicesRating, Long> {

}
