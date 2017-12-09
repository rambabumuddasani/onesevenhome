package com.salesmanager.core.business.repositories.catalog.product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.ProductCriteria;
import com.salesmanager.core.model.catalog.product.ProductList;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.tax.taxclass.TaxClass;

public interface ProductRepositoryCustom {
	
	
	
	

		ProductList listByStore(MerchantStore store, Language language,
			ProductCriteria criteria);
		
		 Product getByFriendlyUrl(MerchantStore store,String seUrl, Locale locale);

		List<Product> getProductsListByCategories(@SuppressWarnings("rawtypes") Set categoryIds);

		List<Product> getProductsListByCategories(Set<Long> categoryIds,
				Language language);

		List<Product> listByTaxClass(TaxClass taxClass);

		List<Product> listByStore(MerchantStore store);

		Product getProductForLocale(long productId, Language language,
				Locale locale);

		Product getById(Long productId);

		Product getByCode(String productCode, Language language);

		List<Product> getProductsForLocale(MerchantStore store,
				Set<Long> categoryIds, Language language, Locale locale);
		
		List<Product> getTodaysDeals();
		List<Product> getAdminTodaysDeals();
		List<Product> getProductsListByCategory(String categoryCode);
		List<Product> getDealOfDay();
        List<Product> getAllDealOfDay(String columnName,String columnValue);
		List<Product> getProduct(String columnName, String coulumnValue);
		List<Product> getProductsListByFilters(@SuppressWarnings("rawtypes") Set filterIds);
		List<Product> findProductsByFiltersAndPrice(String categoryCode,List<Long> filterIds, BigDecimal minPrice, BigDecimal maxPrice,Double productRating);

		Product getProductAndProductReviewByProductId(Long productId);
		List<Product> getProductsListBySearch(String searchString);

		List<Product> getVendorNotAssociatedProductsListByCategory(String categoryCode, List<Long> productId);

}
