package com.salesmanager.shop.admin.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.user.UserService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.user.User;

@Controller
@CrossOrigin
public class AdminController {
	
	@Inject
	private MerchantStoreService merchantStoreService;
	
	@Inject
	private UserService userService;

    @Inject
    private LanguageService  languageService;
    
    @Inject
    private CountryService countryService;
    
    @Inject
	@Named("passwordEncoder")
	private PasswordEncoder passwordEncoder;
    
	@RequestMapping(value="/admin/updatestore", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminUpdateStoreResponse updateMerchantStore(@RequestBody AdminUpdateStoreRequest adminUpdateStoreRequest) {
	    AdminUpdateStoreResponse adminUpdateStoreResponse=new AdminUpdateStoreResponse();
	    MerchantStore merchantStore=null;
		try {
			merchantStore = merchantStoreService.getByCode(adminUpdateStoreRequest.getStoreCode());
			if(merchantStore==null) {
				adminUpdateStoreResponse.setErrorMessage("Store is not found,unable to update");
				adminUpdateStoreResponse.setStatus("false");
				return adminUpdateStoreResponse;
			}
		} catch (ServiceException e) {
			adminUpdateStoreResponse.setErrorMessage("Error in updating Store");
			adminUpdateStoreResponse.setStatus("false");
		}
	    merchantStore.setStorename(adminUpdateStoreRequest.getStoreName());
	    merchantStore.setCode(adminUpdateStoreRequest.getStoreCode());
	    merchantStore.setStorephone(adminUpdateStoreRequest.getStorePhone());
	    merchantStore.setStoreEmailAddress(adminUpdateStoreRequest.getEmailAddress());
	    merchantStore.setStoreaddress(adminUpdateStoreRequest.getStoreAddress());
	    merchantStore.setStorecity(adminUpdateStoreRequest.getStoreCity());
	    merchantStore.setStorestateprovince(adminUpdateStoreRequest.getStoreState());
	    merchantStore.setStorepostalcode(adminUpdateStoreRequest.getStorePostalCode());
	    //merchantStore.setCountry(adminUpdateStoreRequest.getStoreCountry());
	    
	    try {
			merchantStoreService.update(merchantStore);
		} catch (ServiceException e) {
			e.printStackTrace();
			adminUpdateStoreResponse.setErrorMessage("Error in updating store");
			adminUpdateStoreResponse.setStatus("false");
			return adminUpdateStoreResponse;
		}
	        adminUpdateStoreResponse.setSuccessMessage("Store updated successfully");
	        adminUpdateStoreResponse.setStatus("true");
	        return adminUpdateStoreResponse;
	    
    }
	
	@RequestMapping(value="/admin/getStore", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public StoreInfoResponse getStoreInfo(HttpServletRequest request) {
		
		StoreInfoResponse storeInfoResponse=new StoreInfoResponse();
		try {
			MerchantStore merchantStore=merchantStoreService.getMerchantStore(MerchantStore.DEFAULT_STORE);
	        System.out.println("merchantStore="+merchantStore);
	       
	        StoreInfo storeInfo = new StoreInfo();
	        storeInfo.setStoreName(merchantStore.getStorename());
	        storeInfo.setStoreAddress(merchantStore.getStoreaddress());
	        storeInfo.setEmailAddress(merchantStore.getStoreEmailAddress());
	        storeInfo.setStorePhone(merchantStore.getStorephone());
	        storeInfo.setStoreCity(merchantStore.getStorecity());
	        storeInfo.setStoreState(merchantStore.getStorestateprovince());
	        storeInfo.setStorePostalCode(merchantStore.getStorepostalcode());
	        storeInfo.setStoreCountry(merchantStore.getCountry().getIsoCode());
	        storeInfo.setStoreCode(merchantStore.getCode());
	        
	        User user = userService.getById(1l);
	        storeInfo.setAdminName(user.getAdminName());
	        storeInfo.setLastAccess(user.getLastAccess());
	        storeInfoResponse.setStoreInfo(storeInfo);
	        
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		  
		    return storeInfoResponse;
		
	}
	
	@RequestMapping(value="/admin/update", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public EditUserAdminResponse updateAdmin(@RequestBody EditUserAdminRequest editUserAdminRequest)
        throws Exception {
		
		    System.out.println("editUserAdmin :");
		    EditUserAdminResponse editUserAdminResponse = new EditUserAdminResponse();
		    String stringId = editUserAdminRequest.getId();
		    Long longId = Long.parseLong(stringId);
			User dbUser = userService.getById(longId);
			
			if(dbUser==null) {
				editUserAdminResponse.setErrorMessage("Admin is null for this id: "+longId);
				editUserAdminResponse.setSucessMessage("false");
				return editUserAdminResponse;
			}
			
			dbUser.setFirstName(editUserAdminRequest.getFirstName());
			dbUser.setLastName(editUserAdminRequest.getLastName());
			//Language  language = languageService.getByCode(editUserAdminRequest.getDefaultLang());
			//dbUser.setDefaultLanguage(language);
			dbUser.setAdminName(editUserAdminRequest.getUserName());
			dbUser.setAdminEmail(editUserAdminRequest.getEmail());
			MerchantStore store = merchantStoreService.getByCode(editUserAdminRequest.getStoreCode());
			dbUser.setMerchantStore(store);
			
			userService.saveOrUpdate(dbUser);
			
			editUserAdminResponse.setSucessMessage("Admin profile updated successfully");
			editUserAdminResponse.setStatus("true");
			
	        return editUserAdminResponse;
		
		
	}
	
	@RequestMapping(value="/admin/list", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public AdminListResponse getAdminList() throws Exception {
		AdminListResponse  adminListResponse = new AdminListResponse();
		List<UserVO> userList = new ArrayList<UserVO>();
		MerchantStore store = merchantStoreService.getByCode(MerchantStore.DEFAULT_STORE);
	    List<User> users = userService.listByStore(store);
	    for(User user:users) {
	       UserVO userVO = new UserVO();
	       userVO.setId(user.getId());
	       userVO.setAdminName(user.getAdminName());
	       userVO.setStoreCode(user.getMerchantStore().getCode());
	       userVO.setEmail(user.getAdminEmail());
	       userVO.setFirstName(user.getFirstName());
	       userVO.setLastName(user.getLastName());
	       //userVO.setDefaultLang(user.getDefaultLanguage());
	       userList.add(userVO);
	    }
	    adminListResponse.setAdminList(userList);
		return adminListResponse;
		
	}
	
	@RequestMapping(value="/admin/updatepassword", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public UpdatePasswordResp changePassword(@RequestBody UpdatePasswordReq updatePasswordReq) throws Exception {
		
		UpdatePasswordResp updatePasswordResp = new UpdatePasswordResp();
		String stringId = updatePasswordReq.getId();
		Long longId = Long.parseLong(stringId);
		User dbUser = userService.getById(longId);
		
		if(dbUser==null) {
			updatePasswordResp.setErrorMessage("Admin is not exist for this id: "+longId);
			updatePasswordResp.setStatus("false");
			return updatePasswordResp;
		}
		String pass = passwordEncoder.encode(updatePasswordReq.getNewPassword());
		dbUser.setAdminPassword(pass);
		userService.update(dbUser);	
		updatePasswordResp.setSuccessMessage("Password updated successfully");
		updatePasswordResp.setStatus("true");
		return updatePasswordResp;
	}
	
}
