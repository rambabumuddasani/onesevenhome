package com.salesmanager.shop.constants;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	
	public final static String SLASH = "/";
	public final static String BLANK = "";
	public final static String EQUALS = "=";

    public final static String RESPONSE_STATUS = "STATUS";
    public final static String RESPONSE_SUCCESS = "SUCCESS";
    public final static String DEFAULT_LANGUAGE = "en";
	public final static String LANGUAGE = "LANGUAGE";
	public final static String LANG = "lang";
	public final static String BREADCRUMB = "BREADCRUMB";

	public final static String HOME_MENU_KEY = "menu.home";
	public final static String HOME_URL = "/shop";
	public final static String ADMIN_URI = "/admin";
	public final static String SHOP_URI = "/shop";
	public final static String SHOP = "shop";
	public final static String REF = "ref";
	public final static String REF_C = "c:";
	public final static String REF_SPLITTER = ":";
	
	public final static String FILE_NOT_FOUND = "File not found";
	


	public final static String DEFAULT_DOMAIN_NAME = "localhost:8080";

	public final static String ADMIN_STORE = "ADMIN_STORE";
	public final static String ADMIN_USER = "ADMIN_USER";
	public final static String MERCHANT_STORE = "MERCHANT_STORE";
	public final static String SHOPPING_CART = "SHOPPING_CART";
	public final static String CUSTOMER = "CUSTOMER";
	public final static String ORDER = "ORDER";
	public final static String ORDER_ID = "ORDER_ID";
	public final static String ORDER_ID_TOKEN = "ORDER_ID_TOKEN";
	public final static String SHIPPING_SUMMARY = "SHIPPING_SUMMARY";
	public final static String SHIPPING_OPTIONS = "SHIPPING_OPTIONS";
	public final static String ORDER_SUMMARY = "ORDER_SIMMARY";


	public final static String GROUP_ADMIN = "ADMIN";
	public final static String PERMISSION_AUTHENTICATED = "AUTH";
	public final static String PERMISSION_CUSTOMER_AUTHENTICATED = "AUTH_CUSTOMER";
	public final static String GROUP_SUPERADMIN = "SUPERADMIN";
	public final static String GROUP_CUSTOMER = "CUSTOMER";
	public final static String ANONYMOUS_CUSTOMER = "ANONYMOUS_CUSTOMER";


	public final static String CONTENT_IMAGE = "CONTENT";
	public final static String CONTENT_LANDING_PAGE = "LANDING_PAGE";
	public final static String CONTENT_CONTACT_US = "contact";

	public final static String STATIC_URI = "/static";
	public final static String FILES_URI = "/files";
	public final static String PRODUCT_URI= "/product";
	public final static String CATEGORY_URI = "/category";
	public final static String PRODUCT_ID_URI= "/productid";
	public final static String ORDER_DOWNLOAD_URI= "/order/download";

	public final static String URL_EXTENSION= ".html";
	public final static String REDIRECT_PREFIX ="redirect:";




	public final static String STORE_CONFIGURATION = "STORECONFIGURATION";

	public final static String HTTP_SCHEME= "http";
	
	public final static String SHOP_SCHEME = "SHOP_SCHEME";
	public final static String FACEBOOK_APP_ID = "shopizer.facebook-appid";

	public final static String MISSED_CACHE_KEY = "MISSED";
	public final static String CONTENT_CACHE_KEY = "CONTENT";
	public final static String CONTENT_PAGE_CACHE_KEY = "CONTENT_PAGE";
	public final static String CATEGORIES_CACHE_KEY = "CATALOG_CATEGORIES";
	public final static String PRODUCTS_GROUP_CACHE_KEY = "CATALOG_GROUP";
	public final static String SUBCATEGORIES_CACHE_KEY = "CATALOG_SUBCATEGORIES";
	public final static String RELATEDITEMS_CACHE_KEY = "CATALOG_RELATEDITEMS";
	public final static String MANUFACTURERS_BY_PRODUCTS_CACHE_KEY = "CATALOG_BRANDS_BY_PRODUCTS";
	public final static String CONFIG_CACHE_KEY = "CONFIG";

	public final static String REQUEST_CONTENT_OBJECTS = "CONTENT";
	public final static String REQUEST_CONTENT_PAGE_OBJECTS = "CONTENT_PAGE";
	public final static String REQUEST_TOP_CATEGORIES = "TOP_CATEGORIES";
	public final static String REQUEST_PAGE_INFORMATION = "PAGE_INFORMATION";
	public final static String REQUEST_SHOPPING_CART = "SHOPPING_CART";
	public final static String REQUEST_CONFIGS = "CONFIGS";

	public final static String KEY_FACEBOOK_PAGE_URL = "facebook_page_url";
	public final static String KEY_PINTEREST_PAGE_URL = "pinterest";
	public final static String KEY_GOOGLE_ANALYTICS_URL = "google_analytics_url";
	public final static String KEY_INSTAGRAM_URL = "instagram";
	public final static String KEY_GOOGLE_API_KEY = "google_api_key";
	public final static String KEY_TWITTER_HANDLE = "twitter_handle";
	public final static String KEY_SESSION_ADDRESS = "readableDelivery";

	public final static String CATEGORY_LINEAGE_DELIMITER = "/";
	public final static int MAX_REVIEW_RATING_SCORE = 5;
	public final static int MAX_ORDERS_PAGE = 5;
	public final static String SUCCESS = "success";
	public final static String CANCEL = "cancel";
	
	public final static String START = "start";
	public final static String MAX = "max";
	
	public final static String CREDIT_CARD_YEARS_CACHE_KEY = "CREDIT_CARD_YEARS_CACHE_KEY";
	public final static String MONTHS_OF_YEAR_CACHE_KEY = "MONTHS_OF_YEAR_CACHE_KEY";
	
	public final static String INIT_TRANSACTION_KEY = "init_transaction";

    public final static String LINK_CODE = "LINK_CODE";
    
    public final static String COOKIE_NAME_USER = "user";
    public final static String COOKIE_NAME_CART = "cart";
    public final static String RESPONSE_KEY_USERNAME = "userName";
    
    public final static String DEBUG_MODE = "debugMode";
    
    public final static String PENDING_FOR_APPROVAL = "Pending for Approval";
	public final static String APPROVED = "Approved";
	
	public final static String PRODUCT_VENDORS = "Product Vendors";
	public final static String SERVICE_PROVIDER = "Service Provider";
	public final static String ARCHITECTS = "Architects";
	public final static String WALLPAPER = "WallPaper Vendors";
	public final static String MACHINERY_EQUIPMENT = "Machinery & Equipment";
	public final static String CUSTOMERS = "Customer";
	
	public final static String WALLPAPER_PORTFOLIO = "Wallpaper";
	
    public static Map<String,String> customerTypes = new HashMap<String,String>();
	
	static {
		customerTypes.put("1", "VENDOR");
		customerTypes.put("2", "SERVICE");
		customerTypes.put("3", "ARCHITECTS");
		customerTypes.put("4", "WALL PAPER");
		customerTypes.put("5", "MACHINERY & EQUIPMENT");
	}
	
    public static Map<String,String> architectTypes = new HashMap<String,String>();
	
	static {
		architectTypes.put("1", "INTERIOR");
		architectTypes.put("2", "LANDSCAPE");
		architectTypes.put("3", "CIVIL");
		
	}
	private static final String DEFAULT = "DEFAULT";

	//private static final String WALLPAPER_CAT = "Wallpaper";
	
	// Defining constants for handling multipart files
	public final static String USER_PROFILE = "USER_PROFILE";
	public final static String CERTIFICATE = "CERTIFICATE";
	public final static String FILE_1 = "FILE_1";
	public final static String FILE_2 = "FILE_2";
	public final static String FILE_3 = "FILE_3";
	
	public final static String ADMIN_POSTREQUIREMENT_RESPONDED = "Responded";
	public final static String ADMIN_POSTREQUIREMENT_PENDING = "Pending";
	
	public final static String VENDOR_NAME = "VENDOR_NAME";
	public final static String VENDOR_ID = "VENDOR_ID";
	public final static String REGISTERED_VENDOR = "REGISTERED_VENDOR";
	public final static String VENDOR_PRODUCTS = "VENDOR_PRODUCTS";
	public final static String PAYMENT_VENDOR = "PAYMENT_VENDOR";
	public final static String SERVICES_BOOKING = "SERVICES_BOOKING";
	public final static String USER_NAME = "USER_NAME";
	public final static String USER_ID = "USER_ID";
	public final static String ARCHITECT_PORTFOLIO = "ARCHITECT_PORTFOLIO";
	public final static String MACHINERY_PORTFOLIO = "MACHINERY_PORTFOLIO";
	public final static String WALLPAPER_PF = "WALLPAPER_PF";
	public final static String PRODUCT_NAME = "PRODUCT_NAME";
	public final static String PRODUCT_ID = "PRODUCT_ID";
	

}
