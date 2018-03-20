package com.salesmanager.core.business.services.user.updates;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.user.updates.UserUpdatesRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.user.updates.UserUpdates;

@Service("userUpdatesService")
public class UserUpdatesServiceImpl extends SalesManagerEntityServiceImpl<Long, UserUpdates> implements  UserUpdatesService{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserUpdatesServiceImpl.class);
	
	private UserUpdatesRepository userUpdatesRepository;
	
	@Inject
    public UserUpdatesServiceImpl(UserUpdatesRepository userUpdatesRepository) {
		super(userUpdatesRepository);
		this.userUpdatesRepository = userUpdatesRepository;
	}

	@Override
	public UserUpdates getByEmailAddress(String emailAddress) {
		return userUpdatesRepository.getByEmailAddress(emailAddress);
	}
}
