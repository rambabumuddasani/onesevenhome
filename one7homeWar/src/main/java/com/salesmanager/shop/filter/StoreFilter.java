package com.salesmanager.shop.filter;

import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.LanguageUtils;

/**
 * 
 * http://localhost:8080/shop/cart//displayMiniCartByCode?userId=1
 * Servlet Filter implementation class StoreFilter
 */

public class StoreFilter extends HandlerInterceptorAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreFilter.class);
	
	private final static String STORE_REQUEST_PARAMETER = "store";

	@Inject
	private MerchantStoreService merchantService;
	
	@Inject
	private CustomerService customerService;
	
	@Inject
	private LanguageService languageService;

	@Inject
	private LanguageUtils languageUtils;
	
	
	private final static String SERVICES_URL_PATTERN = "/services";
	private final static String REFERENCE_URL_PATTERN = "/reference";
	


    /**
     * Default constructor. 
     */
    public StoreFilter() {

    }
    
	   public boolean preHandle(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            Object handler) throws Exception {

			//request.setCharacterEncoding("UTF-8");
			/**
			 * if url contains /services
			 * exit from here 
			 */
			if(request.getRequestURL().toString().toLowerCase().contains(SERVICES_URL_PATTERN)
				|| request.getRequestURL().toString().toLowerCase().contains(REFERENCE_URL_PATTERN)	
			) {
				return true;
			}

			try {
				/** merchant store **/
				MerchantStore store = (MerchantStore)request.getSession().getAttribute(Constants.MERCHANT_STORE);
				String storeCode = request.getParameter(STORE_REQUEST_PARAMETER);
				
				//remove link set from controllers for declaring active - inactive links
				request.removeAttribute(Constants.LINK_CODE);
				
				if(!StringUtils.isBlank(storeCode)) {
					if(store!=null) {
						if(!store.getCode().equals(storeCode)) {
							store = setMerchantStoreInSession(request, storeCode);
						}
					}else{ // when url sm-shop/shop is being loaded for first time store is null
						store = setMerchantStoreInSession(request, storeCode);
					}
				}
				if(store==null) {
					store = setMerchantStoreInSession(request, MerchantStore.DEFAULT_STORE);
				}
				request.setAttribute(Constants.MERCHANT_STORE, store);
				
				/** customer **/
				Customer customer = null;
				/*
				 * if(customer != null) {
					if(customer.getMerchantStore().getId().intValue() != store.getId().intValue()) {
						request.getSession().removeAttribute(Constants.CUSTOMER);
					}
						if(!customer.isAnonymous()) {
			        	if(!request.isUserInRole("AUTH_CUSTOMER")) {
			        			request.removeAttribute(Constants.CUSTOMER);
				        }
					} 	
					request.setAttribute(Constants.CUSTOMER, customer);
				} */
				if(customer==null) {					
/*					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		        	if(auth != null &&
			        		 request.isUserInRole("AUTH_CUSTOMER")) {
		        		customer = customerService.getByNick(auth.getName());
		        		if(customer!=null) {
		        			request.setAttribute(Constants.CUSTOMER, customer);
		        		}
			        } 					
*/					String cutomerId = request.getParameter("userId");
					if(!isEmpty(cutomerId)){
						long customerIdLong = Long.parseLong(cutomerId);
						customer = customerService.getById(customerIdLong);
		        		if(customer!=null) {
		        			request.setAttribute(Constants.CUSTOMER, customer);
		    				request.getSession().setAttribute(Constants.CUSTOMER, customer);					
		        		}
					}					
				}
				
				/** language & locale **/
				Language language = languageUtils.getRequestLanguage(request, response);
				request.setAttribute(Constants.LANGUAGE, language);
				Locale locale = languageService.toLocale(language);
				LocaleContextHolder.setLocale(locale);				
				
				/*				
				 *
				 * String shoppingCarCode = (String)request.getSession().getAttribute(Constants.SHOPPING_CART);
				if(!isEmpty(shoppingCarCode)) {
					request.setAttribute(Constants.REQUEST_SHOPPING_CART, shoppingCarCode);
				}							
				*/
			} catch (Exception e) {
				LOGGER.error("Error in StoreFilter",e);
			}
			return true;
		   
	   }

	private boolean isEmpty(String cutomerId) {
		return cutomerId == null || cutomerId.trim().isEmpty() || "undefined".equals(cutomerId) ;
	}
	   
	 	   
	   
	   /**
	    * Sets a MerchantStore with the given storeCode in the session.
	    * @param request
	    * @param storeCode The storeCode of the Merchant.
	    * @return the MerchantStore inserted in the session.
	    * @throws Exception
	    */
	   private MerchantStore setMerchantStoreInSession(HttpServletRequest request, String storeCode) throws Exception{
		   if(storeCode == null || request == null)
			   return null;
		   MerchantStore store = merchantService.getByCode(storeCode);
			if(store!=null) {
				request.getSession().setAttribute(Constants.MERCHANT_STORE, store);
			}		
			return store;
	   }

}
