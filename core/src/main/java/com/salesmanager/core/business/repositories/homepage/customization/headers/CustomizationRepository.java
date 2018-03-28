package com.salesmanager.core.business.repositories.homepage.customization.headers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.homepage.customization.headers.Customizations;

public interface CustomizationRepository extends JpaRepository<Customizations, Long>{

	@Query("select c from Customizations c where c.customizationId = ?1")
	Customizations getByCustomizationId(String customizationId);

}
