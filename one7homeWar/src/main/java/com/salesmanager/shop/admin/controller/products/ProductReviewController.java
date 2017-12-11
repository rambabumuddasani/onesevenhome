package com.salesmanager.shop.admin.controller.products;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.review.ProductReviewService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.review.ProductReview;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.admin.controller.VendorProductVO;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.controller.AbstractController;
import com.salesmanager.shop.store.model.paging.PaginationData;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@CrossOrigin
public class ProductReviewController extends AbstractController{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductReviewController.class);
	
	@Inject
	private ProductService productService;
	
	@Inject
	private ProductReviewService productReviewService;
	
	@Inject
	LabelUtils messages;
	
	
	/*@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/reviews.html", method=RequestMethod.GET)
	public String displayProductReviews(@RequestParam("id") long productId,Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		
		setMenu(model, request);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		

		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		Product product = productService.getById(productId);
		
		if(product==null) {
			return "redirect:/admin/products/products.html";
		}
		
		if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			return "redirect:/admin/products/products.html";
		}
		
		
		model.addAttribute("product", product);
		
		return ControllerConstants.Tiles.Product.productReviews;

	}*/
	
	@RequestMapping(value="/products/{productId}/reviews", method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaginatedReviewResponse productReviews(@PathVariable Long productId, @RequestParam(value="pageNumber", defaultValue = "1") int page , @RequestParam(value="pageSize", defaultValue="15") int size) {
		PaginatedReviewResponse paginatedReviewResponse = new PaginatedReviewResponse();
		LOGGER.debug("Entered productReviews method");
		//String sProductId = request.getParameter("productId");
		//ProductReviewResponse productReviewResponse = new ProductReviewResponse();
		List<ProductReviewVO> productReviewResponseList =new ArrayList<ProductReviewVO>();		
		
		try {

			List<ProductReview> reviews = productReviewService.getByProductId(productId);
			
			
			for(ProductReview review : reviews) {
				ProductReviewVO productReviewVO = new ProductReviewVO();
				//productReviewVO.setId(productId);
				//productReviewVO.setId(review.getId());
				//productReviewVO.setStatus(1);
				productReviewVO.setReviewRating(review.getReviewRating());
				productReviewVO.setReviewDate(review.getReviewDate());
				//productReviewVO.setReviewRead(review.getReviewRead());
				productReviewVO.setName(review.getCustomer().getBilling().getFirstName());
				//productReviewVO.setCustomerId(review.getCustomer().getId());
				//productReviewVO.setProductId(review.getProduct().getId());;
				productReviewVO.setDescription(review.getDescription());
				//productReviewVO.setDescriptionName(review.getDescriptionName());
				productReviewResponseList.add(productReviewVO);
			}
			//productReviewResponse.setProductReviews(productReviewResponseList);
			Double avgReview = getAvgReview(reviews);
			Long totalRatingCount = getTotalRatingCount(reviews);
			//productReviewResponse.setAvgReview(avgReview);
			//productReviewResponse.setTotalRatingCount(totalRatingCount);
			paginatedReviewResponse.setAvgReview(avgReview);
			paginatedReviewResponse.setTotalratingCount(totalRatingCount);
			PaginationData paginaionData=createPaginaionData(page,size);
	    	calculatePaginaionData(paginaionData,size, productReviewResponseList.size());
	    	paginatedReviewResponse.setPaginationData(paginaionData);
			if(productReviewResponseList == null || productReviewResponseList.isEmpty() || productReviewResponseList.size() < paginaionData.getCountByPage()){
				paginatedReviewResponse.setReviewList(productReviewResponseList);
				return paginatedReviewResponse;
			}
	    	List<ProductReviewVO> paginatedResponses = productReviewResponseList.subList(paginaionData.getOffset(), paginaionData.getCountByPage());
	    	paginatedReviewResponse.setReviewList(paginatedResponses);
	    	LOGGER.debug("Ended getVendorProducts");
			//return paginatedResponse;
			/*Product dbProduct = productService.getById(productId);
			BigDecimal productAvgReview = new BigDecimal(avgReview);
			dbProduct.setProductReviewAvg(productAvgReview);
			productService.update(dbProduct);*/
			//productReviewResponse.setStatus(1);
		} catch (Exception e) {			
		    LOGGER.error("Error in retrieving Product Reviews",e.getMessage());
		}	
		LOGGER.debug("Ended productReviews method");
		return paginatedReviewResponse;
	}
	
    public Long getTotalRatingCount(List<ProductReview> reviews) {
	LOGGER.debug("Entered getTotalRatingCount");
	Long totalRatingCount = 0l;
	
	Map<Double,Integer> ratingMap = new HashMap<Double , Integer> (5);
	
	for(ProductReview review :reviews ) {	
		Double reviewRating = review.getReviewRating();
		if(ratingMap.containsKey(reviewRating)){
			Integer count = ratingMap.get(reviewRating);
			ratingMap.put(reviewRating,++count);
		}else {
			ratingMap.put(reviewRating,1);
		}
	}

	/**
	 * formual is 
	 * 
	 * avgRating = ((5*ratingMap.get(5))+(4*ratingMap.get(4))+(3*ratingMap.get(3))+(2*ratingMap.get(2))+(1*ratingMap.get(1))/(ratingMap.get(5)+ratingMap.get(4)+ratingMap.get(3)+ratingMap.get(2)+ratingMap.get(1));
	 */
	Set<Map.Entry<Double, Integer>> entrySet = ratingMap.entrySet();
	
	Double totalNumarater = 0d;
	Long totalDenaminator = 0l;
	for(Map.Entry<Double, Integer> eachEntry : entrySet){
		totalNumarater += (eachEntry.getKey()*eachEntry.getValue());
		totalDenaminator += eachEntry.getValue();
	}
	
	if(totalDenaminator == 0l){
		return 0l;
	}
    totalRatingCount = totalDenaminator;
    LOGGER.debug("Ended getTotalRatingCount");
	return totalRatingCount;
	}



     public Double getAvgReview(List<ProductReview> reviews) {
		LOGGER.debug("Entered getAvgReview");
		Double avgReview = 0d;
		Map<Double,Integer> ratingMap = new HashMap<Double , Integer> (5);
		
		for(ProductReview review :reviews ) {	
			Double reviewRating = review.getReviewRating();
			if(ratingMap.containsKey(reviewRating)){
				Integer count = ratingMap.get(reviewRating);
				ratingMap.put(reviewRating,++count);
			}else {
				ratingMap.put(reviewRating,1);
			}
		}
		
		Set<Map.Entry<Double, Integer>> entrySet = ratingMap.entrySet();
	
		Double totalNumarater = 0d;
		Double totalDenaminator = 0d;
		for(Map.Entry<Double, Integer> eachEntry : entrySet){
			totalNumarater += (eachEntry.getKey()*eachEntry.getValue());
			totalDenaminator += eachEntry.getValue();
		}
		System.out.println(" totalNumarater "+totalNumarater);
		System.out.println("totalDenaminator "+totalDenaminator);
		if(totalDenaminator == 0){
			return 0d;
		}
	    avgReview = totalNumarater/totalDenaminator;
	    String  formatedAvgReviewStr = getFormattedAvgReview(avgReview);
	    Double formatedAvgReview = Double.parseDouble(formatedAvgReviewStr);
	    LOGGER.debug("Entered getAvgReview");
		return formatedAvgReview;
	}

	
	private String getFormattedAvgReview(Double avgReview) {
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(1); //Sets the maximum number of digits after the decimal point
		df.setMinimumFractionDigits(0); //Sets the minimum number of digits after the decimal point
		df.setGroupingUsed(false); //If false thousands separator such ad 1,000 wont work so it will display 1000
	    return df.format(avgReview);
	}

	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="/admin/products/reviews/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageProductReviews(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		Long productId;
		Product product = null;
		
		try {
			productId = Long.parseLong(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("Product id is not valid");
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		}

		
		try {

			product = productService.getById(productId);

			
			if(product==null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			if(product.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Product id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			
			Language language = (Language)request.getAttribute("LANGUAGE");

			
			List<ProductReview> reviews = productReviewService.getByProduct(product);
			


			for(ProductReview review : reviews) {
				Map entry = new HashMap();
				entry.put("reviewId", review.getId());
				entry.put("rating", review.getReviewRating().intValue());
				Set<ProductReviewDescription> descriptions = review.getDescriptions();
				String reviewDesc= "";
				if(!CollectionUtils.isEmpty(descriptions)) {
					reviewDesc = descriptions.iterator().next().getDescription();
				}
				//for(ProductReviewDescription description : descriptions){
				//	if(description.getLanguage().getCode().equals(language.getCode())) {
				//		reviewDesc = description.getDescription();
				//	}
				//}
				entry.put("description", reviewDesc);
				resp.addDataEntry(entry);
			}

			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);
		
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);


	}*/
	
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value="admin/products/reviews/remove.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteProductReview(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sReviewid = request.getParameter("reviewId");

		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		
		try {
			
			Long reviewId = Long.parseLong(sReviewid);

			
			ProductReview review = productReviewService.getById(reviewId);
			

			if(review==null || review.getProduct().getMerchantStore().getId().intValue()!=store.getId()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);			
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
			} 
			

			productReviewService.delete(review);
			
			
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		
		
		} catch (Exception e) {
			LOGGER.error("Error while deleting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	
	@Inject
	private CustomerService customerService;
	
	//Rest API save product review
	//@CrossOrigin
	@RequestMapping(value="/products/reviews/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ProductReviewSaveResponse saveProductReview(@RequestBody ProductReviewRequest productReviewReq) {
		LOGGER.debug("Saving product review");
		ProductReviewSaveResponse  productReviewSaveResponse = new ProductReviewSaveResponse();
		
		try {
			//if product rating is not there in request object, then why we need to store data in table
			if(StringUtils.isEmpty(productReviewReq.getRating())){
				productReviewSaveResponse.setStatus("false");
				productReviewSaveResponse.setMessage("Rating is mandatory , Error in saving the Product review");
				return productReviewSaveResponse;
			}
			ProductReview productReview = new ProductReview();
			productReview.setReviewRating( productReviewReq.getRating());
			productReview.setDescription( productReviewReq.getDescription());
			productReview.setDescriptionName( productReviewReq.getDescriptionName());
			productReview.setStatus(1);
			Product product = productService.getProductAndProductReviewByProductId(productReviewReq.getProductId());
			product.setId(productReviewReq.getProductId());
			productReview.setProduct(product);
			productReview.setReviewDate(new Date());
			product.getProductReview().add(productReview);
			// get customer from session object

			List<ProductReview>  productReviews = new ArrayList<ProductReview>(product.getProductReview());
			Double avgReview = getAvgReview(productReviews);
			product.setProductReviewAvg(new BigDecimal(avgReview));
			
			//Customer customer = (Customer)request.getSession().getAttribute(Constants.CUSTOMER);	// ram hard coded here
			Customer customer = customerService.getById(productReviewReq.getUserId());
			productReview.setCustomer(customer);
			productReviewService.save(productReview);
			if(productReview.getId() == null) {
				LOGGER.debug("Error in saving the Product review");
				productReviewSaveResponse.setStatus("false");
				productReviewSaveResponse.setMessage("Error in saving the Product review");		
				return productReviewSaveResponse;
			}
			LOGGER.debug("Product Review saved successfully");
			productReviewSaveResponse.setStatus("true");
		    productReviewSaveResponse.setMessage("Product Review saved successfully");		     
		    //return  productReviewSaveResponse;
		} catch (Exception e) {			
			LOGGER.error("Error while saving productReview"+e.getMessage());
			productReviewSaveResponse.setStatus("false");
			productReviewSaveResponse.setMessage("Error in saving the Product review");
			return productReviewSaveResponse;
		}		 
		return productReviewSaveResponse;
	}
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products", "catalogue-products");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("catalogue");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}
	
	

}
