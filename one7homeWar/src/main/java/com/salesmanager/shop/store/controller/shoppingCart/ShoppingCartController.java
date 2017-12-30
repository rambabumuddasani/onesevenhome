package com.salesmanager.shop.store.controller.shoppingCart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartService;
import com.salesmanager.core.business.services.system.SystemConfigurationService;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.OrderTotalSummary;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.core.model.system.SystemConfiguration;
import com.salesmanager.core.modules.integration.shipping.model.ShippingQuotePrePostProcessModule;
import com.salesmanager.shop.admin.controller.system.ShippingModuleConfig;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartData;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.shop.store.controller.shoppingCart.facade.ShoppingCartFacade;
import com.salesmanager.shop.store.model.shoppingCart.ShoppingCartItemResponse;


/**
 * A mini shopping cart is available on the public shopping section from the upper menu
 * Landing page, Category page (list of products) and Product details page contains a form
 * that let the user add an item to the cart, see the quantity of items, total price of items
 * in the cart and remove items
 *
 * Add To Cart
 * ---------------
 * The add to cart is 100% driven by javascript / ajax. The code is available in webapp\resources\js\functions.js
 *
 * <!-- Simple add to cart html example ${id} is the product id -->
 * <form id="input-${id}">
 *  <input type="text" class="input-small" id="quantity-productId-${id}" placeholder="1" value="1">
 * 	<a href="#" class="addToCart" productId="${id}">Add to cart</a>
 * </form>
 *
 * The javascript function creates com.salesmanager.web.entity.shoppingcart.ShoppingCartItem and ShoppingCartAttribute based on user selection
 * The javascript looks in the cookie if a shopping cart code exists ex $.cookie( 'cart' ); // requires jQuery-cookie
 * The javascript posts the ShoppingCartItem and the shopping cart code if present to /shop/addShoppingCartItem.html
 *
 * @see
 *
 *  javascript re-creates the shopping cart div item (div id shoppingcart) (see webapp\pages\shop\templates\bootstrap\sections\header.jsp)
 * The javascript set the shopping cart code in the cookie
 *
 * Display a page
 * ----------------
 *
 * When a page is displayed from the shopping section, the shopping cart has to be displayed
 * 4 paths 1) No shopping cart 2) A shopping cart exist in the session 3) A shopping cart code exists in the cookie  4) A customer is logeed in and a shopping cart exists in the database
 *
 * 1) No shopping cart, nothing to do !
 *
 * 2) StoreFilter will tak care of a ShoppingCart present in the HttpSession
 *
 * 3) Once a page is displayed and no cart returned from the controller, a javascript looks on load in the cookie to see if a shopping cart code is present
 * 	  If a code is present, by ajax the cart is loaded and displayed
 *
 * 4) No cart in the session but the customer logs in, the system looks in the DB if a shopping cart exists, if so it is putted in the session so the StoreFilter can manage it and putted in the request
 *
 * @author Carl Samson
 * @author Umesh
 */
@CrossOrigin
@Controller
@RequestMapping("/cart/")
public class ShoppingCartController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartController.class);

	@Inject
	private ShoppingCartService shoppingCartService;


	@Inject
	private ShoppingCartFacade shoppingCartFacade;

	@Inject
	@Qualifier("shippingDistancePreProcessor")
	ShippingQuotePrePostProcessModule shippingQuotePrePostProcessModule;

	@Inject
	private CustomerService customerService;

    @Inject
    ShoppingCartCalculationService shoppingCartCalculationService;

	@Inject
	private SystemConfigurationService systemConfigurationService;

	/**
	 * Add an item to the ShoppingCart (AJAX exposed method)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/addShoppingCartItem"}, method=RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ShoppingCartItemResponse addShoppingCartItem(@RequestBody final ShoppingCartItem item, final HttpServletRequest request) throws Exception {
        LOGGER.debug("Entered addShoppingCartItem");
		ShoppingCartData shoppingCart=null;

		//Look in the HttpSession to see if a customer is logged in
		MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		Customer customer = getSessionAttribute(  Constants.CUSTOMER, request );

		if(customer != null) {
			com.salesmanager.core.model.shoppingcart.ShoppingCart customerCart = shoppingCartService.getByCustomer(customer);
			if(customerCart!=null) {
				shoppingCart = shoppingCartFacade.getShoppingCartData( customerCart);
			}
		}

		//if shoppingCart is null create a new one
		if(shoppingCart==null) {
			shoppingCart = new ShoppingCartData();
			String code = UUID.randomUUID().toString().replaceAll("-", "");
			shoppingCart.setCode(code);
		}

		shoppingCart = shoppingCartFacade.addItemsToShoppingCart( shoppingCart, item, store,language,customer );
		request.getSession().setAttribute(Constants.SHOPPING_CART, shoppingCart.getCode());
		int cartQty = shoppingCart.getShoppingCartItems().size();
		ShoppingCartItemResponse shoppingCartItemResponse = new ShoppingCartItemResponse();
		shoppingCartItemResponse.setCartQuantity(cartQty);
		LOGGER.debug("Ended addShoppingCartItem");
		return shoppingCartItemResponse;
	}
	
	//  http://localhost:8080/shop/cart/displayCart?userId=1
	@RequestMapping(value={"/displayCart"},  method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ShoppingCartData displayMiniCart(final String shoppingCartCode, HttpServletRequest request, Model model){
        LOGGER.debug("Entered displayMiniCart");
		try {
			MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
			Customer customer = getSessionAttribute(  Constants.CUSTOMER, request );
			ShoppingCartData cart =  shoppingCartFacade.getShoppingCartData(customer,merchantStore,shoppingCartCode);
			if(cart!=null) {
				request.getSession().setAttribute(Constants.SHOPPING_CART, cart.getCode());
			}
			if(cart==null) {
				request.getSession().removeAttribute(Constants.SHOPPING_CART);//make sure there is no cart here
				cart = new ShoppingCartData();//create an empty cart
			}
			return cart;
		} catch(Exception e) {
			LOGGER.error("Error while getting the shopping cart",e);
		}	
		LOGGER.debug("Ended displayMiniCart");
		return new ShoppingCartData();
	}

	//http://localhost:8080/cart/removeShoppingCartItem/3c9cb185cc4c42818790ec73e3e45693/359?userId=1
	@RequestMapping(value={"/removeShoppingCartItem/{shoppingCartCode}/{lineItemId}"},   method = { RequestMethod.GET})
	public @ResponseBody ShoppingCartData removeShoppingCartItem(@PathVariable String shoppingCartCode,
			@PathVariable Long lineItemId, HttpServletRequest request) throws Exception {
		LOGGER.debug("Entered removeShoppingCartItem");
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		ShoppingCartData cart =  shoppingCartFacade.getShoppingCartData(null, merchantStore, shoppingCartCode);
		if(cart==null) {
			return new ShoppingCartData();
		}
		ShoppingCartData shoppingCartData=shoppingCartFacade.removeCartItem(lineItemId, cart.getCode(), merchantStore,language);


		if(CollectionUtils.isEmpty(shoppingCartData.getShoppingCartItems())) {
			shoppingCartFacade.deleteShoppingCart(shoppingCartData.getId(), merchantStore);
			request.getSession().removeAttribute(Constants.SHOPPING_CART);
			return new ShoppingCartData();
		}


		request.getSession().setAttribute(Constants.SHOPPING_CART, cart.getCode());

		LOGGER.debug("removed item" + lineItemId + "from cart");
		return shoppingCartData;
	}	
	
	/**
	 * Update the quantity of an item in the Shopping Cart (AJAX exposed method)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/updateShoppingCartItem"},  method = { RequestMethod.POST })
	public @ResponseBody ShoppingCartData updateShoppingCartItem( @RequestBody final ShoppingCartItem[] shoppingCartItems, final HttpServletRequest request)  {
		LOGGER.debug("Entered updateShoppingCartItem");
		ShoppingCartData shoppingCart = null;
	    MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
	    Language language = (Language)request.getAttribute(Constants.LANGUAGE);
        
        String cartCode = (String)request.getSession().getAttribute(Constants.SHOPPING_CART);
        
        if(StringUtils.isEmpty(cartCode)) {
        	LOGGER.error(" cart code is null, returning validation failed message" );
        }
        try {
        	List<ShoppingCartItem> items = Arrays.asList(shoppingCartItems);
			shoppingCart = shoppingCartFacade.updateCartItems(items, store, language);
		} catch (Exception e) {
			LOGGER.error("Excption while updating cart" ,e);
		}
        if(shoppingCart == null){
        	shoppingCart = new ShoppingCartData();
        }
        LOGGER.debug("Ended updateShoppingCartItem");
        return shoppingCart;
	}


    /*
     * preferedShippingAddress can be 0 -> default billing address
     * 								  1 -> delivery address
     * 								  2 -> secondary delivery address
     */

	// address/addrpref/1?userId=1
	@RequestMapping(value="/address/addrpref/{preferedShippingAddress}")
	@ResponseBody
	public OrderTotalSummary calculateShippingCost(@PathVariable("preferedShippingAddress") Integer preferedShippingAddress ,
			HttpServletRequest request) throws Exception {
		 Customer customer = getSessionAttribute(  Constants.CUSTOMER, request);
		 String userPinCode = null;
		 if(preferedShippingAddress == 0) {
			 userPinCode = customer.getBilling().getPostalCode();
		 }else if(preferedShippingAddress == 1) {
			 userPinCode = customer.getDelivery().getPostalCode();
		 }else if(preferedShippingAddress == 2) {
			 userPinCode = customer.getSecondaryDelivery().getPostalCode();
		 }else {
			 throw new Exception("invalid preferedShippingAddress value");
		 }
		 ShoppingCart shoppingCart = shoppingCartService.getByCustomer(customer);
		 Set<com.salesmanager.core.model.shoppingcart.ShoppingCartItem> lineItems =  shoppingCart.getLineItems();
		 List<Long> vendorIds = new ArrayList<Long>();
		 List<String> vendorPostalCodes = new ArrayList<String>();
		 for(com.salesmanager.core.model.shoppingcart.ShoppingCartItem item : lineItems) {
			 vendorIds.add(item.getVendorId());
			 Customer vendor = customerService.getById(item.getVendorId());
			 vendorPostalCodes.add(vendor.getBilling().getPostalCode());
			// String vendorPostalCode = vendor.getBilling().getPostalCode();
		 }
		 Map<Long,Long> vendorDistanceMap = getVendorDistance(userPinCode, vendorIds, vendorPostalCodes);
		 MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		 Language language = (Language) request.getAttribute( Constants.LANGUAGE );

		 OrderTotalSummary orderSummary = shoppingCartCalculationService.calculate(shoppingCart,merchantStore, language );
         
		 BigDecimal total = orderSummary.getTotal();
		 Map<Long,Long> vendorDistanceCostMap = new HashMap<>();
		 SystemConfiguration configuration = systemConfigurationService.getByKey(SchemaConstant.SHIPPING_DISTANCE_CONFIG);
		 ObjectMapper mapper = new ObjectMapper();	
		 ShippingModuleConfig config = mapper.readValue(configuration.getValue(), ShippingModuleConfig.class);
		 
		 long freeShippingDistanceRange = config.getFreeShippingDistanceRange(); // in KMs
		 //long eachKmDistanceCostInRs = 2l; // in RS
		 long minOrderCostLimit = config.getMinOrderPrice();	// in RS
		 long beyandDistancePrice = config.getBeyandDistancePrice();
		 Map<Integer, Long>  priceByDistance = config.getPriceByDistance();
		 long totalShippingCost =  0l;
		 /* if total = 5000 and minOrderCostLimit is 3000, then 
				if vendor to cutomer distance is in given configured free shipping range, 
					then shipping charges will be zero
				else
				 	calculate the shipping price based on configured shipping range values and vendor to cusotmer range.
			else (i.e. orderTotal is not more than given minOrderCostLimit
				 	calculate the shipping price based on configured shipping range values and vendor to cusotmer range.
		 	
		 */	
		 // else , shipping charge will be applicable.
		 if(total.intValue() >= minOrderCostLimit){	
		 for(Map.Entry<Long, Long> entry : vendorDistanceMap.entrySet()) {
			 long vendorDistanceFromCustomerLoc  = entry.getValue();
			 if(vendorDistanceFromCustomerLoc > freeShippingDistanceRange){
					long price = compuatePriceByDistanceRange(priceByDistance, vendorDistanceFromCustomerLoc,beyandDistancePrice);
					totalShippingCost += price;
			 }
		 	}
		 }else{
			 for(Map.Entry<Long, Long> entry : vendorDistanceMap.entrySet()) {
				 	long vendorDistanceFromCustomerLoc  = entry.getValue();
					long price = compuatePriceByDistanceRange(priceByDistance, vendorDistanceFromCustomerLoc,beyandDistancePrice);
					totalShippingCost += price;
			 }
		 }
		 if(totalShippingCost <= 0){
			 totalShippingCost = 0; // which means it is free shipping 
		 }
		 System.out.println("totalShippingCost "+totalShippingCost);
         System.out.println("total "+total);
         orderSummary.setShippingCharges(new BigDecimal(totalShippingCost));
         total = total.add(orderSummary.getShippingCharges());
         orderSummary.setTotal(total);
         shoppingCart.setShippingCharges(totalShippingCost);
         shoppingCartService.saveOrUpdate(shoppingCart);
		 //ShoppingCartData cart =  shoppingCartFacade.getShoppingCartData(customer,merchantStore,null);
		 return orderSummary;
	}


	private Map<Long,Long> getVendorDistance(String userPinCode, List<Long> vendorIds, List<String> vendorPostalCodes) {
		List<Long> distanceInMeters = shippingQuotePrePostProcessModule.getDistnaceBetweenVendorAndCustomer(vendorPostalCodes, userPinCode);
		 Map<Long,Long> vendorDistanceFromCustomerLocation = new HashMap<Long,Long>();
		 int vIndex = 0;
		 for(Long vId : vendorIds){
			 vendorDistanceFromCustomerLocation.put(vId, distanceInMeters.get(vIndex++));
		 }
		 return vendorDistanceFromCustomerLocation;
	}
	
	private static long compuatePriceByDistanceRange(Map<Integer, Long> priceByDistance,long vendorDistanceFromCustomer,long beyandDistancePrice){
		long vendorShippingPrice = 0l;
		if(vendorDistanceFromCustomer == 0){ // if vendor and customer lies in same pin code, then distance would be zero.
			return vendorShippingPrice;
		}
		for(Map.Entry<Integer, Long> entry : priceByDistance.entrySet()){
			int distance = entry.getKey();
			//long price = entry.getValue();
			if(vendorDistanceFromCustomer <= distance){
				vendorShippingPrice = entry.getValue();
				break;
			}
		}
		if(vendorShippingPrice == 0){
			vendorShippingPrice =  beyandDistancePrice; // which means, vendorDistanceFromCustomer is beyond our configured data.
		}
		return vendorShippingPrice;
	}
/*	public static void main(String[] args) {
		long beyandDistancePrice = 100l;
		Map<Integer, Long> priceByDistance = new LinkedHashMap<>();
		priceByDistance.put(10, 1l);
		priceByDistance.put(20, 2l);
		priceByDistance.put(30, 3l);
		priceByDistance.put(40, 4l);
		long vendorDistanceFromCustomer = 41;
		long price = compuatePriceByDistanceRange(priceByDistance, vendorDistanceFromCustomer,beyandDistancePrice);
		System.out.println("shipping cost "+price+" Rs");
	}	

*/	}
