package com.salesmanager.core.business.services.catalog.product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.ProductCriteria;
import com.salesmanager.core.model.catalog.product.ProductList;
import com.salesmanager.core.model.catalog.product.description.ProductDescription;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.tax.taxclass.TaxClass;



public interface ProductService extends SalesManagerEntityService<Long, Product> {

	void addProductDescription(Product product, ProductDescription description) throws ServiceException;
	
	ProductDescription getProductDescription(Product product, Language language);
	
	Product getProductForLocale(long productId, Language language, Locale locale) throws ServiceException;
	
	List<Product> getProductsForLocale(Category category, Language language, Locale locale) throws ServiceException;

	List<Product> getProducts(List<Long> categoryIds) throws ServiceException;

	List<Product> getTodaysDeals() throws ServiceException;
	List<Product> getDealOfDay() throws ServiceException;
	List<Product> getProductsListByCategory(String categoryCode) throws ServiceException;
	ProductList listByStore(MerchantStore store, Language language,
			ProductCriteria criteria);

	List<Product> listByStore(MerchantStore store);

	List<Product> listByTaxClass(TaxClass taxClass);
	List<Product> getProductsListByFilters(List<Long> filterIds) throws ServiceException;

	List<Product> getProducts(List<Long> categoryIds, Language language)
			throws ServiceException;

	Product getBySeUrl(MerchantStore store, String seUrl, Locale locale);

	/**
	 * Get a product by sku (code) field  and the language
	 * @param productCode
	 * @param language
	 * @return
	 */
	Product getByCode(String productCode, Language language);

	Product getByProductId(Long productId);

	List<Product> getProduct(String colName, String colValue) throws ServiceException;

	List<Product> getProductsListByFiltersAndPrice(String categoryCode,List<Long> filterIds, BigDecimal minPrice, BigDecimal maxPrice,Double productRating);

	public Product getProductAndProductReviewByProductId(Long productId) ;

	List<Product> getDealOfDay(Date startDate, Date endDate, String status);

}
	
