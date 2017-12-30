package com.salesmanager.shop.store.controller.order;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.payments.PaymentService;
import com.salesmanager.core.business.services.payments.TransactionService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.EmailTemplatesUtils;
import com.salesmanager.shop.utils.LocaleUtils;

/**
 * Manage order details
 * @author Carl Samson
 */
@Controller
@RequestMapping("/order")
public class OrderActionsControler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderActionsControler.class);
	
	@Inject
	private OrderService orderService;
	
	@Inject
	CountryService countryService;
	
	@Inject
	ZoneService zoneService;
	
	@Inject
	PaymentService paymentService;
	
	@Inject
	CustomerService customerService;
	
	@Inject
	PricingService pricingService;
	
	@Inject
	TransactionService transactionService;
	
	@Inject
	EmailService emailService;
	
	@Inject
	EmailTemplatesUtils emailTemplatesUtils;
	
	@RequestMapping(value="/invoice/{orderId}", method=RequestMethod.GET)
	public void printInvoice(HttpServletRequest request, HttpServletResponse response, Locale locale,@PathVariable("orderId") Long orderId) throws Exception {
		//String sId = request.getParameter("id");
		try {
		//Long id = Long.parseLong(sId);
		//MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.MERCHANT_STORE);
		Order order = orderService.getById(orderId);
		if(order.getMerchant().getId().intValue()!=store.getId().intValue()) {
			throw new Exception("Invalid order");
		}
		Language lang = store.getDefaultLanguage();
		ByteArrayOutputStream stream  = orderService.generateInvoice(store, order, lang);
		StringBuilder attachment = new StringBuilder();
		//attachment.append("attachment; filename=");
		attachment.append(order.getId());
		attachment.append(".pdf");
        response.setHeader("Content-disposition", "attachment;filename=" + attachment.toString());
        //Set the mime type for the response
        response.setContentType("application/pdf");
		response.getOutputStream().write(stream.toByteArray());
		response.flushBuffer();
		} catch(Exception e) {
			LOGGER.error("Error while printing a report",e);
		}
	}
	
	@RequestMapping(value="/sendInvoice", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> sendInvoice(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String sId = request.getParameter("id");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		if(sId==null) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		}
		try {
			Long id = Long.parseLong(sId);
			Order dbOrder = orderService.getById(id);
			if(dbOrder==null) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			if(dbOrder.getMerchant().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			//get customer
			Customer customer = customerService.getById(dbOrder.getCustomerId());
			
			if(customer==null) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Customer does not exist");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			Locale customerLocale = LocaleUtils.getLocale(customer.getDefaultLanguage());
			emailTemplatesUtils.sendOrderEmail(customer.getEmailAddress(), customer, dbOrder, customerLocale, customer.getDefaultLanguage(), store, request.getContextPath());
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch(Exception e) {
			LOGGER.error("Cannot get transactions for order id " + sId, e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	@RequestMapping(value="/sendDownloadEmail", method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> sendDownloadEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sId = request.getParameter("id");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		if(sId==null) {
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		}
		try {
			Long id = Long.parseLong(sId);
			Order dbOrder = orderService.getById(id);

			if(dbOrder==null) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			if(dbOrder.getMerchant().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			//get customer
			Customer customer = customerService.getById(dbOrder.getCustomerId());
			if(customer==null) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Customer does not exist");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			Locale customerLocale = LocaleUtils.getLocale(customer.getDefaultLanguage());
			emailTemplatesUtils.sendOrderDownloadEmail(customer, dbOrder, store, customerLocale, request.getContextPath());
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch(Exception e) {
			LOGGER.error("Cannot get transactions for order id " + sId, e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString(e.getMessage());
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
}
