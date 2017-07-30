package com.salesmanager.shop.store.controller.customer;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.merchant.MerchantStoreService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.reference.zone.Zone;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.shop.constants.ApplicationConstants;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.fileupload.services.StorageException;
import com.salesmanager.shop.fileupload.services.StorageService;
import com.salesmanager.shop.model.customer.Address;
import com.salesmanager.shop.model.customer.AnonymousCustomer;
import com.salesmanager.shop.model.customer.CustomerEntity;
import com.salesmanager.shop.model.customer.SecuredShopPersistableCustomer;
import com.salesmanager.shop.model.customer.Vendor;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartData;
import com.salesmanager.shop.populator.shoppingCart.ShoppingCartDataPopulator;
import com.salesmanager.shop.store.controller.AbstractController;
import com.salesmanager.shop.store.controller.ControllerConstants;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.utils.CaptchaRequestUtils;
import com.salesmanager.shop.utils.EmailTemplatesUtils;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.ImageFilePath;
import com.salesmanager.shop.utils.LabelUtils;

//import com.salesmanager.core.business.customer.CustomerRegistrationException;

/**
 * Registration of a new customer
 * @author Carl Samson
 *
 */

@SuppressWarnings( "deprecation" )
// http://stackoverflow.com/questions/17444258/how-to-use-new-passwordencoder-from-spring-security
@Controller
@CrossOrigin
public class CustomerRegistrationController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegistrationController.class);
    
    
	@Inject
	private CoreConfiguration coreConfiguration;

	@Inject
	private LanguageService languageService;


	@Inject
	private CountryService countryService;

	
	@Inject
	private ZoneService zoneService;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	EmailService emailService;

	@Inject
	private LabelUtils messages;
	
	@Inject
	private CustomerFacade customerFacade;
	
	@Inject
    private AuthenticationManager customerAuthenticationManager;
	
	@Inject
	private EmailTemplatesUtils emailTemplatesUtils;
	
	@Inject
	private CaptchaRequestUtils captchaRequestUtils;
	
	@Inject
	@Qualifier("img")
	private ImageFilePath imageUtils;
	
    @Inject
    private ShoppingCartCalculationService shoppingCartCalculationService;
    
    @Inject
    private PricingService pricingService;
	
	@Inject
	private EmailUtils emailUtils;
	
	@Inject
    private CustomerService customerService;
	
    @Inject
    private StorageService storageService;


	private final static String NEW_USER_ACTIVATION_TMPL = "email_template_new_user_activate.ftl";


	@RequestMapping(value="/registration.html", method=RequestMethod.GET)
	public String displayRegistration(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);

		model.addAttribute( "recapatcha_public_key", coreConfiguration.getProperty( ApplicationConstants.RECAPTCHA_PUBLIC_KEY ) );
		
		SecuredShopPersistableCustomer customer = new SecuredShopPersistableCustomer();
		AnonymousCustomer anonymousCustomer = (AnonymousCustomer)request.getAttribute(Constants.ANONYMOUS_CUSTOMER);
		if(anonymousCustomer!=null) {
			customer.setBilling(anonymousCustomer.getBilling());
		}
		
		model.addAttribute("customer", customer);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(store.getStoreTemplate());

		return template.toString();


	}

    @RequestMapping( value = "/register.html", method = RequestMethod.POST )
    public String registerCustomer( @Valid
    @ModelAttribute("customer") SecuredShopPersistableCustomer customer, BindingResult bindingResult, Model model,
                                    HttpServletRequest request, HttpServletResponse response, final Locale locale )
        throws Exception
    {
        MerchantStore merchantStore = (MerchantStore) request.getAttribute( Constants.MERCHANT_STORE );
        Language language = super.getLanguage(request);

        String userName = null;
        String password = null;
        
        model.addAttribute( "recapatcha_public_key", coreConfiguration.getProperty( ApplicationConstants.RECAPTCHA_PUBLIC_KEY ) );
        
        if(!StringUtils.isBlank(request.getParameter("g-recaptcha-response"))) {
        	boolean validateCaptcha = captchaRequestUtils.checkCaptcha(request.getParameter("g-recaptcha-response"));
        	
            if ( !validateCaptcha )
            {
                LOGGER.debug( "Captcha response does not matched" );
    			FieldError error = new FieldError("captchaChallengeField","captchaChallengeField",messages.getMessage("validaion.recaptcha.not.matched", locale));
    			bindingResult.addError(error);
            }
        }
        

        if ( StringUtils.isNotBlank( customer.getUserName() ) )
        {
            if ( customerFacade.checkIfUserExists( customer.getUserName(), merchantStore ) )
            {
                LOGGER.debug( "Customer with username {} already exists for this store ", customer.getUserName() );
            	FieldError error = new FieldError("userName","userName",messages.getMessage("registration.username.already.exists", locale));
            	bindingResult.addError(error);
            }
            userName = customer.getUserName();
        }
        
        
        if ( StringUtils.isNotBlank( customer.getPassword() ) &&  StringUtils.isNotBlank( customer.getCheckPassword() ))
        {
            if (! customer.getPassword().equals(customer.getCheckPassword()) )
            {
            	FieldError error = new FieldError("password","password",messages.getMessage("message.password.checkpassword.identical", locale));
            	bindingResult.addError(error);

            }
            password = customer.getPassword();
        }

        if ( bindingResult.hasErrors() )
        {
            LOGGER.debug( "found {} validation error while validating in customer registration ",
                         bindingResult.getErrorCount() );
            StringBuilder template =
                new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
            return template.toString();

        }

        @SuppressWarnings( "unused" )
        CustomerEntity customerData = null;
        try
        {
            //set user clear password
        	customer.setClearPassword(password);
        	customerData = customerFacade.registerCustomer( customer, merchantStore, language );
        }
       /* catch ( CustomerRegistrationException cre )
        {
            LOGGER.error( "Error while registering customer.. ", cre);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
            StringBuilder template =
                            new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
             return template.toString();
        }*/
        catch ( Exception e )
        {
            LOGGER.error( "Error while registering customer.. ", e);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
            StringBuilder template =
                            new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
            return template.toString();
        }
              
        /**
         * Send registration email
         */
        emailTemplatesUtils.sendRegistrationEmail( customer, merchantStore, locale, request.getContextPath() );

        /**
         * Login user
         */
        
        try {
        	
	        //refresh customer
	        Customer c = customerFacade.getCustomerByUserName(customer.getUserName(), merchantStore);
	        //authenticate
	        customerFacade.authenticate(c, userName, password);
	        super.setSessionAttribute(Constants.CUSTOMER, c, request);
	        
	        StringBuilder cookieValue = new StringBuilder();
            cookieValue.append(merchantStore.getCode()).append("_").append(c.getNick());
	        
            //set username in the cookie
            Cookie cookie = new Cookie(Constants.COOKIE_NAME_USER, cookieValue.toString());
            cookie.setMaxAge(60 * 24 * 3600);
            cookie.setPath(Constants.SLASH);
            response.addCookie(cookie);
            
            
            String sessionShoppingCartCode= (String)request.getSession().getAttribute( Constants.SHOPPING_CART );
            if(!StringUtils.isBlank(sessionShoppingCartCode)) {
	            ShoppingCart shoppingCart = customerFacade.mergeCart( c, sessionShoppingCartCode, merchantStore, language );
	            ShoppingCartData shoppingCartData=this.populateShoppingCartData(shoppingCart, merchantStore, language);
	            if(shoppingCartData !=null) {
	                request.getSession().setAttribute(Constants.SHOPPING_CART, shoppingCartData.getCode());
	            }

	            //set username in the cookie
	            Cookie c1 = new Cookie(Constants.COOKIE_NAME_CART, shoppingCartData.getCode());
	            c1.setMaxAge(60 * 24 * 3600);
	            c1.setPath(Constants.SLASH);
	            response.addCookie(c1);
	            
            }

	        return "redirect:/shop/customer/dashboard.html";
        
        
        } catch(Exception e) {
        	LOGGER.error("Cannot authenticate user ",e);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
        }
        
        
        StringBuilder template =
                new StringBuilder().append( ControllerConstants.Tiles.Customer.register ).append( "." ).append( merchantStore.getStoreTemplate() );
        return template.toString();

    }
    
   
	
	@ModelAttribute("countryList")
	public List<Country> getCountries(final HttpServletRequest request){
	    
        Language language = (Language) request.getAttribute( "LANGUAGE" );
        try
        {
            if ( language == null )
            {
                language = (Language) request.getAttribute( "LANGUAGE" );
            }

            if ( language == null )
            {
                language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
            }
            
            List<Country> countryList=countryService.getCountries( language );
            return countryList;
        }
        catch ( ServiceException e )
        {
            LOGGER.error( "Error while fetching country list ", e );

        }
        return Collections.emptyList();
    }
	
	@ModelAttribute("zoneList")
    public List<Zone> getZones(final HttpServletRequest request){
	    return zoneService.list();
	}
	
	
	

	
	
    private ShoppingCartData populateShoppingCartData(final ShoppingCart cartModel , final MerchantStore store, final Language language){

        ShoppingCartDataPopulator shoppingCartDataPopulator = new ShoppingCartDataPopulator();
        shoppingCartDataPopulator.setShoppingCartCalculationService( shoppingCartCalculationService );
        shoppingCartDataPopulator.setPricingService( pricingService );
        
        try
        {
            return shoppingCartDataPopulator.populate(  cartModel ,  store,  language);
        }
        catch ( ConversionException ce )
        {
           LOGGER.error( "Error in converting shopping cart to shopping cart data", ce );

        }
        return null;
    }
    
    /*@RequestMapping( value = "/securedcustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SecuredShopPersistableCustomer getCustomerData(){
    	SecuredShopPersistableCustomer c = new SecuredShopPersistableCustomer();
    	c.setUserName("ram");
    	c.setPassword("password");
    	c.setFirstName("ramu");
    	c.setLastName("m");
    	c.setGender("M");
    	c.setEmailAddress("ram@gmail.com");
    	c.setPassword("ram@1234");
    	c.setCheckPassword("ram@1234");
    	c.setLanguage("en");
    	c.setStoreCode("DEFAULT");
    	Address billingAdd = new Address();
    	billingAdd.setAddress("");
    	billingAdd.setBillingAddress(true);
    	billingAdd.setCity("Mancherial");
    	billingAdd.setCompany("Nokia Networks");
    	billingAdd.setFirstName("ram");
    	billingAdd.setLastName("m");
    	billingAdd.setPostalCode("500045");
    	billingAdd.setCountry("India");    	
    	c.setBilling(billingAdd);
    	c.setDelivery(billingAdd);
    	return c;
    }*/
    
    
    @Inject
    MerchantStoreService merchantStoreService ;
    
    //LanguageService languageService;
    
    @RequestMapping( value = "/customer/user/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CustomerEntity> registerCustomers(@RequestBody SecuredShopPersistableCustomer customer,HttpServletRequest request)
        throws Exception
    {	System.out.println("customer ");
    	final Locale locale  = request.getLocale();
    	System.out.println("Entered registration");
        MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");  //i will come back here
        System.out.println("merchantStore "+merchantStore);
        //Language language = super.getLanguage(request);
        Language language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
        String userName = null;
        String password = null;
        if ( StringUtils.isNotBlank( customer.getUserName() ) )
        {
            if ( customerFacade.checkIfUserExists( customer.getUserName(), merchantStore ) )
            {
                LOGGER.debug( "Customer with username {} already exists for this store ", customer.getUserName() );
            	FieldError error = new FieldError("userName","userName",messages.getMessage("registration.username.already.exists", locale));
            	System.out.println(" error "+error);
            	//bindingResult.addError(error);
            }
            userName = customer.getUserName();
        }
        
        
        if ( StringUtils.isNotBlank( customer.getPassword() ) &&  StringUtils.isNotBlank( customer.getCheckPassword() ))
        {
            if (! customer.getPassword().equals(customer.getCheckPassword()) )
            {
            	FieldError error = new FieldError("password","password",messages.getMessage("message.password.checkpassword.identical", locale));
            	System.out.println("error "+error);
            }
            password = customer.getPassword();
            	
        }	
        System.out.println("userName "+userName+" password "+password);
        @SuppressWarnings( "unused" )
        CustomerEntity customerData = null;
        try
        {
            //set user clear password
        	customer.setClearPassword(password);
        	customerData = customerFacade.registerCustomer( customer, merchantStore, language );
            System.out.println("customerData is "+customerData);
        }       catch ( Exception e ) {
            LOGGER.error( "Error while registering customer.. ", e);
             return new ResponseEntity<CustomerEntity>(customerData,HttpStatus.CONFLICT);
        }  
        return new ResponseEntity<CustomerEntity>(customerData,HttpStatus.OK);         
    }
    
	@RequestMapping(value="/customer/register", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public CustomerResponse registerCustomer(@RequestBody CustomerRequest customerRequest)
        throws Exception
    {	
		System.out.println("customer ");
    	CustomerResponse customerResponse = new CustomerResponse();
    	SecuredShopPersistableCustomer customer = new SecuredShopPersistableCustomer();
    	customer.setEmailAddress(customerRequest.getEmail());
    	customer.setPassword(customerRequest.getPassword());
    	customer.setCheckPassword(customerRequest.getConfirmPassword());
    	customer.setFirstName(customerRequest.getFirstName());
    	customer.setLastName(customerRequest.getLastName());
    	customer.setGender(customerRequest.getGender());
    	customer.setUserName(customerRequest.getEmail());
    	customer.setStoreCode("DEFAULT");
    	Address billing = new Address();
    	billing.setFirstName(customerRequest.getFirstName());
    	billing.setLastName(customerRequest.getLastName());
    	billing.setBillingAddress(true);
    	billing.setAddress(customerRequest.getAddress());
    	billing.setCity(customerRequest.getCity());
    	billing.setStateProvince(customerRequest.getState());
    	billing.setPostalCode(customerRequest.getPostalCode());
    	billing.setPhone(customerRequest.getMobileNo());
    	billing.setCountry("IN");
    	
    	Address delivery = new Address();
    	delivery.setFirstName(customerRequest.getFirstName());
    	delivery.setLastName(customerRequest.getLastName());
    	delivery.setAddress(customerRequest.getAddress());
    	delivery.setCity(customerRequest.getCity());
    	delivery.setStateProvince(customerRequest.getState());
    	delivery.setPostalCode(customerRequest.getPostalCode());
    	delivery.setPhone(customerRequest.getMobileNo());

    	customer.setBilling(billing);
    	customer.setDelivery(delivery);
    	customer.setCustomerType("0");
    	
    	final Locale locale  = new Locale("en");
    	System.out.println("Entered registration");
        MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");  //i will come back here
        System.out.println("merchantStore "+merchantStore);
        //Language language = super.getLanguage(request);
        Language language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
        String userName = null;
        String password = null;
        if ( StringUtils.isNotBlank( customer.getUserName() ) )
        {
            if ( customerFacade.checkIfUserExists( customer.getUserName(), merchantStore ) )
            {
                LOGGER.debug( "Customer with username {} already exists for this store ", customer.getUserName() );
            	customerResponse.setErrorMessage(messages.getMessage("registration.username.already.exists", locale));
            	return customerResponse;
            }
            userName = customer.getUserName();
        }
        
        
        if ( StringUtils.isNotBlank( customer.getPassword() ) &&  StringUtils.isNotBlank( customer.getCheckPassword() ))
        {
            if (! customer.getPassword().equals(customer.getCheckPassword()) )
            {
            	customerResponse.setErrorMessage(messages.getMessage("message.password.checkpassword.identical", locale));
            	return customerResponse;
            }
            password = customer.getPassword();
            	
        }	
        
        if ( StringUtils.isBlank( customerRequest.getActivationURL() ))
        {
        	customerResponse.setErrorMessage(messages.getMessage("failure.customer.activation.link", locale));
        	return customerResponse;
        }
        System.out.println("userName "+userName+" password "+password);
        @SuppressWarnings( "unused" )
        CustomerEntity customerData = null;
        try
        {
            //set user clear password
        	customer.setClearPassword(password);
        	customerData = customerFacade.registerCustomer( customer, merchantStore, language );
            System.out.println("customerData is "+customerData);
        }       catch ( Exception e )
        {
            LOGGER.error( "Error while registering customer.. ", e);
            customerResponse.setErrorMessage(e.getMessage());
             return customerResponse;
        }  
         
       
        customerResponse.setSuccessMessage(messages.getMessage("success.newuser.msg",locale));
        
        String activationURL = customerRequest.getActivationURL()+"?emailid="+customerRequest.getEmail();
        //sending email
        String[] activationURLArg = {activationURL};
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getFirstName());
		templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, customer.getLastName());
		templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username",locale));
		templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_ACTIVATION, messages.getMessage("email.newuser.text.activation",locale));
		templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_ACTIVATION_LINK, messages.getMessage("email.newuser.text.activation.link",activationURLArg,locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));

		
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.newuser.text.activation",locale));
		email.setTo(customerRequest.getEmail());
		email.setTemplateName(NEW_USER_ACTIVATION_TMPL);
		email.setTemplateTokens(templateTokens);


		
		emailService.sendHtmlEmail(merchantStore, email);
        
        
        return customerResponse;         
    }

	@RequestMapping(value="/vendor/register", method = RequestMethod.POST)
	@ResponseBody
    public CustomerResponse registerCustomer(@RequestPart("vendorRequest") VendorRequest vendorRequest,
    		@RequestPart("file") MultipartFile vendorCertificate) throws Exception {
		System.out.println("vendor file "+vendorCertificate);
    	CustomerResponse customerResponse = new CustomerResponse();
    	SecuredShopPersistableCustomer customer = new SecuredShopPersistableCustomer();
    	customer.setEmailAddress(vendorRequest.getEmail());
    	customer.setPassword(vendorRequest.getPassword());
    	customer.setCheckPassword(vendorRequest.getConfirmPassword());
    	customer.setFirstName(vendorRequest.getVendorName());
    	customer.setLastName(vendorRequest.getVendorName());
    	customer.setUserName(vendorRequest.getEmail());
    	customer.setStoreCode("DEFAULT");
    	Address billing = new Address();
    	billing.setFirstName(vendorRequest.getVendorName());
    	billing.setLastName(vendorRequest.getVendorName());
    	billing.setBillingAddress(true);
    	billing.setAddress(vendorRequest.getVendorOfficeAddress());
    	billing.setPhone(vendorRequest.getVendorTelephone());
    	billing.setCountry("IN");
    	
    	Address delivery = new Address();
    	delivery.setFirstName(vendorRequest.getVendorName());
    	delivery.setLastName(vendorRequest.getVendorName());
    	delivery.setAddress(vendorRequest.getVendorOfficeAddress());
    	delivery.setPhone(vendorRequest.getVendorTelephone());

    	customer.setBilling(billing);
    	customer.setDelivery(delivery);
    	customer.setCustomerType("1");

    	// Store file into file sytem
    	String certFileName = "";
    	try{
			certFileName = storageService.store(vendorCertificate);
    	}catch(StorageException se){
    		System.out.println("StoreException occured, do wee need continue ");
    		
    	}
    	
    	Vendor vendorAttrs = new Vendor();
    	vendorAttrs.setVendorName(vendorRequest.getVendorName());
    	vendorAttrs.setVendorOfficeAddress(vendorRequest.getVendorOfficeAddress());
    	vendorAttrs.setVendorMobile(vendorRequest.getVendorMobile());
    	vendorAttrs.setVendorTelephone(vendorRequest.getVendorTelephone());
    	vendorAttrs.setVendorFax(vendorRequest.getVendorFax());
    	vendorAttrs.setVendorConstFirm(vendorRequest.getVendorConstFirm());
    	vendorAttrs.setVendorCompanyNature(vendorRequest.getVendorCompanyNature());
    	vendorAttrs.setVendorRegistrationNo(vendorRequest.getVendorRegistrationNo());
    	vendorAttrs.setVendorPAN(vendorRequest.getVendorPAN());
    	vendorAttrs.setVendorAuthCert(vendorRequest.getVendorAuthCert()); // do we need to comment this line
    	vendorAttrs.setVendorExpLine(vendorRequest.getVendorExpLine());
    	vendorAttrs.setVendorMajorCust(vendorRequest.getVendorMajorCust());
    	//vendorAttrs.setVendorTerms(vendorRequest.getVendorTerms());
    	vendorAttrs.setVendorAuthCert(certFileName);	// is it correct do we need other column to store file path.
    	
    	customer.setVendor(vendorAttrs);
    	
    	final Locale locale  = new Locale("en");
    	System.out.println("Entered registration");
        MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT");  //i will come back here
        System.out.println("merchantStore "+merchantStore);
        //Language language = super.getLanguage(request);
        Language language = languageService.getByCode( Constants.DEFAULT_LANGUAGE );
        String userName = null;
        String password = null;
        if ( StringUtils.isNotBlank( customer.getUserName() ) )
        {
            if ( customerFacade.checkIfUserExists( customer.getUserName(), merchantStore ) )
            {
                LOGGER.debug( "Customer with username {} already exists for this store ", customer.getUserName() );
            	customerResponse.setErrorMessage(messages.getMessage("registration.username.already.exists", locale));
            	return customerResponse;
            }
            userName = customer.getUserName();
        }
        
        
        if ( StringUtils.isNotBlank( customer.getPassword() ) &&  StringUtils.isNotBlank( customer.getCheckPassword() ))
        {
            if (! customer.getPassword().equals(customer.getCheckPassword()) )
            {
            	customerResponse.setErrorMessage(messages.getMessage("message.password.checkpassword.identical", locale));
            	return customerResponse;
            }
            password = customer.getPassword();
            	
        }	
        if ( StringUtils.isBlank( vendorRequest.getActivationURL() ))
        {
        	customerResponse.setErrorMessage(messages.getMessage("failure.vendor.activation.link", locale));
        	return customerResponse;
        }

        System.out.println("userName "+userName+" password "+password);
        @SuppressWarnings( "unused" )
        CustomerEntity customerData = null;
        try
        {
            //set user clear password
        	customer.setClearPassword(password);
        	customer.setActivated("0");
        	customerData = customerFacade.registerCustomer( customer, merchantStore, language );
            System.out.println("customerData is "+customerData);
        } catch ( Exception e )
        {	/// if any exception raised during creation of customer we have to delete the certificate
        	storageService.deleteFile(certFileName);
            LOGGER.error( "Error while registering customer.. ", e);
            customerResponse.setErrorMessage(e.getMessage());
             return customerResponse;
        }  
         
       
        customerResponse.setSuccessMessage("Vendor registration successfull");
        
        String activationURL = vendorRequest.getActivationURL()+"?emailid="+vendorRequest.getEmail();
        //sending email
        String[] activationURLArg = {activationURL};
        Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(merchantStore, messages, locale);
		templateTokens.put(EmailConstants.EMAIL_USER_FIRSTNAME, customer.getFirstName());
		templateTokens.put(EmailConstants.EMAIL_USER_LASTNAME, customer.getLastName());
		templateTokens.put(EmailConstants.EMAIL_ADMIN_USERNAME_LABEL, messages.getMessage("label.generic.username",locale));
		templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_ACTIVATION, messages.getMessage("email.newuser.text.activation",locale));
		templateTokens.put(EmailConstants.EMAIL_TEXT_NEW_USER_ACTIVATION_LINK, messages.getMessage("email.newuser.text.activation.link",activationURLArg,locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_PASSWORD_LABEL, messages.getMessage("label.generic.password",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));
		templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl",locale));

		
		Email email = new Email();
		email.setFrom(merchantStore.getStorename());
		email.setFromEmail(merchantStore.getStoreEmailAddress());
		email.setSubject(messages.getMessage("email.newuser.text.activation",locale));
		email.setTo(vendorRequest.getEmail());
		email.setTemplateName(NEW_USER_ACTIVATION_TMPL);
		email.setTemplateTokens(templateTokens);


		
		emailService.sendHtmlEmail(merchantStore, email);
		return customerResponse;         
    }
	@RequestMapping(value="/user/activate", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public CustomerResponse acvitateUser(@RequestBody CustomerRequest customerRequest)
        throws Exception
    {	
		System.out.println("customer activate");
		CustomerResponse customerResponse = new CustomerResponse();
		MerchantStore merchantStore = merchantStoreService.getByCode("DEFAULT"); 
		final Locale locale  = new Locale("en");
        Customer customer = customerFacade.getCustomerByUserName(customerRequest.getEmail(), merchantStore );
        if ( customer == null )
        {
        	customerResponse.setErrorMessage("User not available");
        	return customerResponse;
        }
		customer.setActivated("1");
		customerFacade.updateCustomer(customer);
		customerResponse.setSuccessMessage("User activated");
		
		return customerResponse;
    }

    
}
