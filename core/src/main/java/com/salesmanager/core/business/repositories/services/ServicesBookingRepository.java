package com.salesmanager.core.business.repositories.services;

import com.salesmanager.core.model.customer.ServicesBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServicesBookingRepository extends JpaRepository<ServicesBooking, Long> {

}
