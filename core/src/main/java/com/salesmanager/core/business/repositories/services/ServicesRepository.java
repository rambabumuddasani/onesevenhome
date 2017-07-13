package com.salesmanager.core.business.repositories.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.services.Services;

public interface ServicesRepository extends JpaRepository<Services, Integer> {

}
