package com.salesmanager.shop.admin.controller.login;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.business.vendor.product.services.VendorProductService;
import com.salesmanager.core.model.user.User;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.controller.customer.CustomerLoginController;

@Controller
@CrossOrigin
public class AdminLoginController extends AbstractController {
	
    @Inject
    MerchantStoreService merchantStoreService ;
    
    @Inject
	CustomerService customerService;
    
    @Inject
	VendorProductService vendorProductService;
    
    @Inject
    UserService userService;
    
    @Inject
	@Named("passwordEncoder")
	private PasswordEncoder passwordEncoder;
 

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerLoginController.class);

	private AdminLoginResponse logon(String email, String password) throws Exception {
       
		AdminLoginResponse adminLoginResponse = new AdminLoginResponse();
		
		//User dbUser= userService.getByUserName(userName);
		User dbUser= userService.getByEmail(email);
		if(dbUser==null) {
			LOGGER.debug("Admin not exist for this email "+email);
			adminLoginResponse.setErrorMessage("Admin not exist for this email "+email);
			return adminLoginResponse;
		}
	
		if (!passwordEncoder.matches(password, dbUser.getAdminPassword())){
			LOGGER.debug("Failed to Login, Invalid credentials");
			adminLoginResponse.setErrorMessage("Failed to Login, Invalid credentials");
			adminLoginResponse.setStatus("false");
			return adminLoginResponse;
		}
		LOGGER.debug("Login successful");
		adminLoginResponse.setSuccessMessage("You are Successfully logged in as "+dbUser.getAdminName());
		adminLoginResponse.setStatus("true");
		adminLoginResponse.setId(dbUser.getId());
		adminLoginResponse.setAdminName(dbUser.getAdminName());
		
		dbUser.setLastAccess(dbUser.getLoginTime());
		dbUser.setLoginTime(new Date());
		LOGGER.debug("Last access :"+dbUser.getLastAccess());
		userService.saveOrUpdate(dbUser);
		return adminLoginResponse;
	}

	@RequestMapping(value="/admin/login", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AdminLoginResponse basicLogon(@RequestBody AdminLoginRequest adminLoginRequest) throws Exception {
		LOGGER.debug("Entered Login");
		String email = adminLoginRequest.getEmailAddress();
		String password = adminLoginRequest.getPassword();
		
		AdminLoginResponse adminLoginResponse = this.logon(email, password);
		LOGGER.debug("Ended Login");
		return adminLoginResponse;
	}
}
