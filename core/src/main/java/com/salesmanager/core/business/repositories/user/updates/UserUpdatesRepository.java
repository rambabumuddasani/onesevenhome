package com.salesmanager.core.business.repositories.user.updates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.user.updates.UserUpdates;

public interface UserUpdatesRepository  extends JpaRepository<UserUpdates, Long>{

	@Query("select u from UserUpdates u where u.emailAddress = ?1")
	UserUpdates getByEmailAddress(String emailAddress);

}
