package com.salesmanager.shop.admin.controller.categories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.product.filter.FilterService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.category.CategoryDescription;
import com.salesmanager.core.model.catalog.product.filter.Filter;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.LabelUtils;
import com.sun.prism.Image;



@Controller
@CrossOrigin
public class FilterController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterController.class);
	
	@Inject
	CategoryService categoryService;
	
	@Inject
	FilterService filterService;
	
	@Inject
	LabelUtils messages;


	@RequestMapping(value="/getFiltersByCategory/{categoryId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FilterResponse getAllFilters(@PathVariable String categoryId) {
		FilterResponse filterResponse = new FilterResponse();
		FilterTypeResponse filterTypeResponse = null;
		try
		{
			System.out.println("inside getAllFilters");
			List<Filter> filters = filterService.listByCategoryCode(categoryId);
			List<FilterTypeResponse> response = new ArrayList<FilterTypeResponse>();
			for(Filter f:filters){
				filterTypeResponse = new FilterTypeResponse();
				System.out.println("name :"+f.getFilterName());
				filterTypeResponse.setFilterName(f.getFilterName());
				System.out.println("type :"+f.getFilterTypes());
				filterTypeResponse.setFilterType(f.getFilterTypes());
				response.add(filterTypeResponse);
			}
			
			filterResponse.setFilters(response);
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}
		return filterResponse;
	}


}
