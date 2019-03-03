package com.salesmanager.core.business.repositories.email.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.salesmanager.core.model.email.config.EmailConfig;
/**
 * Email config repository
 * @author ram
 *
 */
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long>  {

	@Query("select e from EmailConfig e where e.id = ?1")
	EmailConfig findById(Long id);
	
}
