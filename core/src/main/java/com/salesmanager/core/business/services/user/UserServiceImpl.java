package com.salesmanager.core.business.services.user;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.user.UserRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.user.User;




public class UserServiceImpl extends SalesManagerEntityServiceImpl<Long, User>
		implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserRepository userRepository;
	
	@Inject
	public UserServiceImpl(UserRepository userRepository) {
		super(userRepository);
		this.userRepository = userRepository;

	}
	
	@Inject
	private EmailService emailService;
	
	@Override
	public User getByUserName(String userName) throws ServiceException {
		LOGGER.debug("Fetching user by username");
		return userRepository.findByUserName(userName);
		
	}
	
	@Override
	public void delete(User user) throws ServiceException {
		LOGGER.debug("Deleting User");
		User u = this.getById(user.getId());
		super.delete(u);
		
	}

	@Override
	public List<User> listUser() throws ServiceException {
		try {
			return userRepository.findAll();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<User> listByStore(MerchantStore store) throws ServiceException {
		LOGGER.debug("Fetching users by store");
		try {
			return userRepository.findByStore(store.getId());
		} catch (Exception e) {
			LOGGER.error("Error while Fetching users by store");
			throw new ServiceException(e);
		}
	}

	
	@Override
	public void saveOrUpdate(User user) throws ServiceException {
		LOGGER.debug("save or upate user");
/*		if(user.getId()==null || user.getId().longValue()==0) {
			userDao.save(user);
		} else {
			userDao.update(user);
		}*/
		
		userRepository.save(user);
		
	}

	@Override
	public User getByEmail(String email) {
		LOGGER.debug("Fetching user by email");
		return userRepository.findByEmail(email);
	}

}
