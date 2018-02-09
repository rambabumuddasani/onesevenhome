package com.salesmanager.shop.admin.controller.categories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.catalog.category.CategoryService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.filter.FilterService;
import com.salesmanager.core.business.services.catalog.product.filter.FilterTypeService;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.filter.Filter;
import com.salesmanager.core.model.catalog.product.filter.FilterType;
import com.salesmanager.shop.utils.LabelUtils;
//import com.sun.prism.Image;



@Controller
@CrossOrigin
public class FilterController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterController.class);
	
	@Inject
	CategoryService categoryService;
	
	@Inject
	FilterService filterService;
	
	@Inject
	FilterTypeService filterTypeService;

	@Inject
	LabelUtils messages;

	@Inject
	private ProductService productService;

	@RequestMapping(value="/getFiltersByCategory/{categoryId}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public FilterResponse getAllFilters(@PathVariable String categoryId) {
		FilterResponse filterResponse = new FilterResponse();
		FilterTypeResponse filterTypeResponse = null;
		try
		{
			System.out.println("inside getAllFilters");
			categoryId = categoryId.replaceAll("_", " ");
			List<Filter> filters = filterService.listByCategoryCode(categoryId);
			List filterName = new ArrayList();
			List<FilterTypeResponse> response = new ArrayList<FilterTypeResponse>();
			for(Filter f:filters){
				if(!filterName.contains(f.getFilterName())) {
					filterTypeResponse = new FilterTypeResponse();
					System.out.println("name :"+f.getFilterName());
					filterTypeResponse.setFilterName(f.getFilterName());
					System.out.println("type :"+f.getFilterTypes());
					filterTypeResponse.setFilterType(f.getFilterTypes());
					filterName.add(f.getFilterName());
					response.add(filterTypeResponse);
				}
			}
			
			filterResponse.setFilters(response);
		}catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}
		return filterResponse;
	}

    @RequestMapping(value="/createFilter", method = RequestMethod.POST, 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public CreateFilterResponse createProduct(@RequestBody CreateFilterTypeRequest filterTypeRequest) throws Exception {
    	CreateFilterResponse createFilterResponse = new CreateFilterResponse();
		String categoryName = filterTypeRequest.getCategoryName();
		categoryName = categoryName.replaceAll("_", " ");
		Category category = categoryService.getByCategoryCode(categoryName);
		
		if(category == null){
			System.out.println("Category does not exists with name:"+categoryName);
			createFilterResponse.setStatus(false);
			createFilterResponse.setErrorMessage("Category does not exists with name:"+categoryName);
			return createFilterResponse;
		} else {
			if(!("").equals(filterTypeRequest.getFilterName())){
				Filter filter = new Filter();
				filter.setCategory(category);
				filter.setFilterName(filterTypeRequest.getFilterName());
				filterService.save(filter);
				createFilterResponse.setCategoryId(category.getId());
				createFilterResponse.setFilterName(filterTypeRequest.getFilterName());
				createFilterResponse.setStatus(true);
			}
		}    	
    	return createFilterResponse;
    }
    
    @RequestMapping(value="/createFilterTypes", method = RequestMethod.POST, 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public CreateFilterResponse createFilterTypes(@RequestBody CreateFilterTypeRequest filterTypeRequest) throws Exception {
    	CreateFilterResponse createFilterResponse = new CreateFilterResponse();
		String categoryName = filterTypeRequest.getCategoryName();
		categoryName = categoryName.replaceAll("_", " ");
		Category category = categoryService.getByCategoryCode(categoryName);
		
		if(category == null){
			System.out.println("Category does not exists with name:"+categoryName);
			createFilterResponse.setStatus(false);
			createFilterResponse.setErrorMessage("Category does not exists with name:"+categoryName);
			return createFilterResponse;
		} else {
			Filter filter = getFilterID(filterTypeRequest.getFilterName(), categoryName);
			if(filter == null){
				createFilterResponse.setStatus(false);
				createFilterResponse.setErrorMessage("Filter does not exists with name:"+filterTypeRequest.getFilterName());
				return createFilterResponse;
			}
			if(filterTypeRequest.getFilterTypes() != null){
				FilterType filterType = null;
				
				List<FilterType> filterTypesList = new ArrayList<FilterType>();
				for(String filterTypeName:filterTypeRequest.getFilterTypes()) {
					filterType = new FilterType();
					filterType.setFilter(filter);
					filterType.setFilterTypeName(filterTypeName);
					filterTypesList.add(filterType);
				}
				filter.setFilterTypes(filterTypesList);
				filterService.update(filter);
				createFilterResponse.setCategoryId(category.getId());
				createFilterResponse.setFilterName(filterTypeRequest.getFilterName());
				createFilterResponse.setStatus(true);
			}
		}    	
    	return createFilterResponse;
    }
    
    @RequestMapping(value="/addProductToFilter", method = RequestMethod.POST, 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public CreateFilterResponse addProductToFilter(@RequestBody CreateFilterTypeRequest filterTypeRequest) throws Exception {
    	CreateFilterResponse createFilterResponse = new CreateFilterResponse();
		
		if(filterTypeRequest.getProductId() == null){
			createFilterResponse.setStatus(false);
			createFilterResponse.setErrorMessage("Product ID can not be empty");
			return createFilterResponse;
		} else {
			Product dbProduct = productService.getById(filterTypeRequest.getProductId());
			Filter filter = null;
			if(dbProduct == null) {
				createFilterResponse.setStatus(false);
				createFilterResponse.setErrorMessage("Product does not exists with product id:"+filterTypeRequest.getProductId());
				return createFilterResponse;
			}
			
			if(filterTypeRequest.getFilterTypeIds() != null){
				FilterType filterType = null;
				
				Set<FilterType> filters = new HashSet<FilterType>();
				for(Long filterId:filterTypeRequest.getFilterTypeIds()) {
					filterType = filterTypeService.getById(filterId);
					filterType.setFilter(filter);
					filters.add(filterType);
				}
				dbProduct.setFilters(filters);
				productService.update(dbProduct);
				createFilterResponse.setProductId(filterTypeRequest.getProductId());
				createFilterResponse.setFilterName(filterTypeRequest.getFilterName());
				createFilterResponse.setStatus(true);
			}
		}    	
    	return createFilterResponse;
    }
    
	private Filter getFilterID(String filterName,String categoryId) throws Exception {
		System.out.println("inside getFilterID");
		Filter filter = null;
		List<Filter> filters = filterService.listByCategoryCode(categoryId);
		for(Filter f:filters){
			if(filterName.equals(f.getFilterName())) {
				filter = f;
				break;
			}
		}
		
		return filter;
	}
	
	// Retrieval of filters by search 
	@RequestMapping(value="/getFiltersBySearch", method = RequestMethod.POST)
	@ResponseBody
	public SearchFilterResponse getFiltersBySearch(@RequestBody SearchFilterRequest searchFilterRequest) throws Exception {
		
		LOGGER.debug("Entered getFiltersBySearch");
		
		SearchFilterResponse searchFilterResponse = new SearchFilterResponse();
		
		List<FilterType> searchFiterTypes = filterService.getFilterTypeNamesBySearch(searchFilterRequest.getSearchFilterString());
		
		Map<FilterNames,FilterNames> filterNamesList = new HashMap<FilterNames,FilterNames>();
		LOGGER.debug("size::"+searchFiterTypes.size());
		
		for(FilterType searchFiterType : searchFiterTypes) {
			
			LOGGER.debug("searchFiterType.getFilter().getFilterName() :::"+searchFiterType.getFilter().getFilterName() );
			FilterNames filterNames = new FilterNames();
			filterNames.setCategoryCode(searchFiterType.getFilter().getCategory().getCode());
			filterNames.setFilterName(searchFiterType.getFilter().getFilterName());
			
			if(filterNamesList.containsKey(filterNames)){
				FilterNames existingFilternames = filterNamesList.get(filterNames);
				FilterTypes filterTypes = new FilterTypes();
				filterTypes.setFilterTypeName(searchFiterType.getFilterTypeName());
				filterTypes.setFilterTypeId(searchFiterType.getId());
				existingFilternames.addFilterTypes(filterTypes);
			}else{
				List<FilterTypes> filterTypeList = new ArrayList<FilterTypes>();
				FilterTypes filterTypes = new FilterTypes();
				filterTypes.setFilterTypeName(searchFiterType.getFilterTypeName());
				filterTypes.setFilterTypeId(searchFiterType.getId());
				filterTypeList.add(filterTypes);
				filterNames.setFilterTypes(filterTypeList);
				filterNamesList.put(filterNames, filterNames);
			}
		}
		
		searchFilterResponse.setFilteredData(new HashSet<FilterNames>(filterNamesList.values()));
		LOGGER.debug("Ended getFiltersBySearch"); 
		return searchFilterResponse;
		
	}

}
