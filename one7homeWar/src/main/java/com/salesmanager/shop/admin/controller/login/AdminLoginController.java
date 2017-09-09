package com.salesmanager.shop.admin.controller.login;

import java.util.ArrayList;
import java.util.List;

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
import com.salesmanager.core.model.customer.Customer;
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
 

	private static final Logger LOG = LoggerFactory.getLogger(CustomerLoginController.class);

	/*private LoginResponse logon(String userName, String password) throws Exception {

		LoginResponse loginResponse = new LoginResponse();
		try {
			LOG.debug("Authenticating Admin " + userName);

			//user goes to shop filter first so store and language are set
			MerchantStore store = merchantStoreService.getByCode("DEFAULT");  //i will come back here

			//Language language = (Language)request.getAttribute("LANGUAGE");

			//check if username is from the appropriate store
			Customer customerModel = customerFacade.getCustomerByUserName(userName, store);

			System.out.println("customermodel"+customerModel);
			if(customerModel==null) {
				loginResponse.setSuccess(false);
				loginResponse.setErrorMessage(" Failed to login , invalid credentials."); 
		        
				return loginResponse;
			}

			if(customerModel.getActivated().equals("0")){
				loginResponse.setSuccess(false);
				loginResponse.setErrorMessage("User account is not activated."); 
		        
				return loginResponse;
				
			}
			
			if(!(customerModel.getIsAdmin().equals("Y")||customerModel .getIsSuperAdmin().equals("Y"))) {
				loginResponse.setSuccess(false);
				loginResponse.setErrorMessage("You are not authorised as admin"); 
		        
				return loginResponse;
			}
			customerFacade.authenticate(customerModel, userName, password);
		
			loginResponse.setSuccess(true);
			loginResponse.setUserId(customerModel.getId());
			if(customerModel.getIsAdmin().equals("Y")){
			    loginResponse.setType("ADMIN");
			}else {
				loginResponse.setType("SUPER ADMIN");
			}
			loginResponse.setName(customerModel.getBilling().getFirstName() + " " + customerModel.getBilling().getLastName());
			if(customerModel.getCustomerType() != null){
				if(customerModel.getCustomerType().equals("1")){
					loginResponse.setType("VENDOR");
					loginResponse.setName(customerModel.getVendorAttrs().getVendorName());
				}
			}
		} catch (AuthenticationException ex) {
			loginResponse.setSuccess(false);
			loginResponse.setErrorMessage(" Failed to login , invalid credentials.");
		} catch(Exception e) {
			loginResponse.setSuccess(false);
			loginResponse.setErrorMessage(" Failed to login , invalid credentials.");        	
		}
		return loginResponse;
	}

	//http://localhost:8080/shop/customer/authenticate.html?userName=shopizer&password=password&storeCode=DEFAULT
	@RequestMapping(value="/admin/login", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody LoginResponse basicLogon(@RequestBody LoginRequest loginRequest) throws Exception {
		
		System.out.println("username"+loginRequest.getUserName()+" pasword "+loginRequest.getPassword());
		String userName = loginRequest.getUserName();
		String password = loginRequest.getPassword();
		
		LoginResponse loginResponse = this.logon(userName, password);
		return loginResponse;
	}*/
	
	/*@RequestMapping(value="/vendor/activate", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ActivateVendorResponse activateVendor(@RequestBody ActivateVendorRequest activateVendorRequest) {
		
		System.out.println("Entered activateVendor:");
		
		List<String> vendorIds= activateVendorRequest.getVendorId();
		System.out.println(vendorIds);
		
		ActivateVendorResponse activateVendorResponse = new ActivateVendorResponse();
		List<ActivateVendor> actVendorList = new ArrayList<ActivateVendor>();
		
		for(String vendorId:vendorIds) {
			Customer customer = customerService.getById(Long.parseLong(vendorId));
			if(customer==null) {
				activateVendorResponse.setErrorMessage("Vendor is not found");
			}
			customer.setIsVendorActivated("Y");
			ActivateVendor activateVendor = new ActivateVendor();
			activateVendor.setVendorId(customer.getId()); 
			actVendorList.add(activateVendor);
		}
		
		activateVendorResponse.setActiveVendors(actVendorList);
		return activateVendorResponse;
		
	}*/
	
	private AdminLoginResponse logon(String email, String password) throws Exception {

		AdminLoginResponse adminLoginResponse = new AdminLoginResponse();
		
		//User dbUser= userService.getByUserName(userName);
		User dbUser= userService.getByEmail(email);
		if(dbUser==null) {
			adminLoginResponse.setErrorMessage("Admin not exist for this email "+email);
			return adminLoginResponse;
		}
	
		if (!passwordEncoder.matches(password, dbUser.getAdminPassword())){
			adminLoginResponse.setErrorMessage("Failed to Login, Invalid credentials");
			adminLoginResponse.setStatus("false");
			return adminLoginResponse;
		}
		
		adminLoginResponse.setSuccessMessage("You are Successfully logged in as "+dbUser.getAdminName());
		adminLoginResponse.setStatus("true");
		adminLoginResponse.setId(dbUser.getId());
		adminLoginResponse.setAdminName(dbUser.getAdminName());

		return adminLoginResponse;
	}

	@RequestMapping(value="/admin/login", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody AdminLoginResponse basicLogon(@RequestBody AdminLoginRequest adminLoginRequest) throws Exception {
		
		System.out.println("username"+adminLoginRequest.getEmailAddress()+" password "+adminLoginRequest.getPassword());
		String email = adminLoginRequest.getEmailAddress();
		String password = adminLoginRequest.getPassword();
		
		AdminLoginResponse adminLoginResponse = this.logon(email, password);
		return adminLoginResponse;
	}
}
