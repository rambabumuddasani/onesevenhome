package com.salesmanager.shop.store.controller.shoppingCart;

import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.shoppingcart.ShoppingCartService;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartData;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.shop.store.controller.shoppingCart.facade.ShoppingCartFacade;


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

	private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartController.class);

	@Inject
	private ShoppingCartService shoppingCartService;


	@Inject
	private ShoppingCartFacade shoppingCartFacade;



	/**
	 * Add an item to the ShoppingCart (AJAX exposed method)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/addShoppingCartItem"}, method=RequestMethod.POST,produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public String addShoppingCartItem(@RequestBody final ShoppingCartItem item, final HttpServletRequest request) throws Exception {

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
		
		int cartQtry = shoppingCart.getShoppingCartItems().size();
		String response = "{'miniCartData':{'cartQuantity':"+cartQtry+"}}";
		System.out.println("Response "+response);
		System.out.println("shoppingCart "+shoppingCart.getShoppingCartItems());
		return response;
	}

	@RequestMapping(value={"/displayMiniCartByCode"},  method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ShoppingCartData displayMiniCart(final String shoppingCartCode, HttpServletRequest request, Model model){

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
			LOG.error("Error while getting the shopping cart",e);
		}		
		return null;
	}

	//http://localhost:8080/cart/removeMiniShoppingCartItem/3c9cb185cc4c42818790ec73e3e45693/359?userId=1
	@RequestMapping(value={"/removeMiniShoppingCartItem/{shoppingCartCode}/{lineItemId}"},   method = { RequestMethod.GET})
	public @ResponseBody ShoppingCartData removeShoppingCartItem(@PathVariable String shoppingCartCode,
			@PathVariable Long lineItemId, HttpServletRequest request) throws Exception {
		Language language = (Language)request.getAttribute(Constants.LANGUAGE);
		MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		ShoppingCartData cart =  shoppingCartFacade.getShoppingCartData(null, merchantStore, shoppingCartCode);
		if(cart==null) {
			return null;
		}
		ShoppingCartData shoppingCartData=shoppingCartFacade.removeCartItem(lineItemId, cart.getCode(), merchantStore,language);


		if(CollectionUtils.isEmpty(shoppingCartData.getShoppingCartItems())) {
			shoppingCartFacade.deleteShoppingCart(shoppingCartData.getId(), merchantStore);
			request.getSession().removeAttribute(Constants.SHOPPING_CART);
			return null;
		}


		request.getSession().setAttribute(Constants.SHOPPING_CART, cart.getCode());

		LOG.debug("removed item" + lineItemId + "from cart");
		return shoppingCartData;
	}	
}
