package com.shopizer.modules.shipping.distance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.salesmanager.core.model.common.Delivery;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.shipping.PackageDetails;
import com.salesmanager.core.model.shipping.ShippingConfiguration;
import com.salesmanager.core.model.shipping.ShippingOrigin;
import com.salesmanager.core.model.shipping.ShippingQuote;
import com.salesmanager.core.model.system.IntegrationConfiguration;
import com.salesmanager.core.model.system.IntegrationModule;
import com.salesmanager.core.modules.constants.Constants;
import com.salesmanager.core.modules.integration.IntegrationException;
import com.salesmanager.core.modules.integration.shipping.model.ShippingQuotePrePostProcessModule;

/**
 * Uses google api to get lng, lat and distance in km for a given delivery address
 * The route will be displayed on a map to the end user and available
 * from the admin section
 * 
 * The module can be configured to use miles by changing distance.inMeters
 * 
 * To use this pre-processor you will need a google api-key
 * 
 * Access google developers console
 * https://console.developers.google.com/project
 * 
 * Geocoding
 * Distance Matrix
 * Directions
 * 
 * Create new key for server application
 * Copy API key
 * https://console.developers.google.com
 * 
 * https://developers.google.com/maps/documentation/webservices/client-library
 * https://github.com/googlemaps/google-maps-services-java/tree/master/src/test/java/com/google/maps
 * 
 * @author carlsamson
 *
 */
public class ShippingDistancePreProcessorImpl implements ShippingQuotePrePostProcessModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingDistancePreProcessorImpl.class);

	private final static String BLANK = " ";

	private final static String MODULE_CODE = "shippingDistanceModule";


	private String apiKey;

	private List<String> allowedZonesCodes = null;



	public List<String> getAllowedZonesCodes() {
		return allowedZonesCodes;
	}





	public void setAllowedZonesCodes(List<String> allowedZonesCodes) {
		this.allowedZonesCodes = allowedZonesCodes;
	}


	public static void main(String[] args) {
		ShippingDistancePreProcessorImpl obj = new ShippingDistancePreProcessorImpl();
		String customerPinCode = "500018";
		//List<String> vendorPinCodes = Arrays.asList("500030", "500044", "501301","500027","500013","500015","500060","500028");
		List<String> vendorPinCodes = Arrays.asList("560036", "504303");
		List<Long> distnaceList  = obj.getDistnaceBetweenVendorAndCustomer(vendorPinCodes, customerPinCode);
		System.out.println("Distnace "+distnaceList);
	}

	@Override
	public  List<Long> getDistnaceBetweenVendorAndCustomer(List<String> vendorPinCodes,String customerPinCode)  {		
		List<Long> distenceInKm = new ArrayList<Long>();
		//List<Long> distnaceList = new ArrayList<Long>();

		/** which destinations are supported by this module **/
		if(vendorPinCodes == null || vendorPinCodes.isEmpty()) {
			return distenceInKm;
		}
		if(StringUtils.isBlank(customerPinCode)) {
			return distenceInKm;
		}
		//apiKey = "AIzaSyAmCfWHdkYxhLbzFWwtBx8k6KzEhOdO9ok";
		Validate.notNull(apiKey, "Requires the configuration of google apiKey");
		GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
		//build origin address
		StringBuilder originAddress = new StringBuilder();
		originAddress.append(customerPinCode);		
		//build destination address
		StringBuilder destinationAddress = new StringBuilder();
		for(String vendorPin : vendorPinCodes){
			destinationAddress.append(vendorPin+"|");			
		}
		destinationAddress.deleteCharAt(destinationAddress.length()-1);
		try {
			if(!StringUtils.isBlank(originAddress) && !StringUtils.isBlank(destinationAddress)) {				
				DistanceMatrix  distanceRequest = DistanceMatrixApi.newRequest(context)
						.origins(originAddress.toString())
						.destinations(destinationAddress.toString())
						.awaitIgnoreError();
				if(distanceRequest!=null) {
					DistanceMatrixRow distanceMax = distanceRequest.rows[0];
					if(distanceMax != null ){
						for(DistanceMatrixElement distMatElement : distanceMax.elements){
							Distance distance = distMatElement.distance;
							if(distance != null){
								distenceInKm.add(distanceInKMs(distance));
								System.out.println(" distance in KMs "+distance.humanReadable);
							}
						}
					}
					//Distance distance = distanceMax.elements[0].distance;
					//quote.getQuoteInformations().put(Constants.DISTANCE_KEY, 0.001 * distance.inMeters);
					System.out.println("distance "+distenceInKm);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while calculating the shipping distance",e);
		}
		return distenceInKm;
	}


	private long distanceInKMs(Distance distance) {
		return (long) Math.floor(distance.inMeters/ 1000);
	}


	public void prePostProcessShippingQuotes(ShippingQuote quote,
			List<PackageDetails> packages, BigDecimal orderTotal,
			Delivery delivery, ShippingOrigin origin, MerchantStore store,
			IntegrationConfiguration globalShippingConfiguration,
			IntegrationModule currentModule,
			ShippingConfiguration shippingConfiguration,
			List<IntegrationModule> allModules, Locale locale)
					throws IntegrationException {


		/** which destinations are supported by this module **/

		if(delivery.getZone()==null) {
			return;
		}

		boolean zoneAllowed = false;
		if(allowedZonesCodes!=null) {
			for(String zoneCode : allowedZonesCodes) {
				if(zoneCode.equals(delivery.getZone().getCode())) {
					zoneAllowed = true;
					break;
				}
			}
		}

		if(!zoneAllowed) {
			return;
		}

		if(StringUtils.isBlank(delivery.getPostalCode())) {
			return;
		}

		Validate.notNull(apiKey, "Requires the configuration of google apiKey");

		GeoApiContext context = new GeoApiContext().setApiKey(apiKey);

		//build origin address
		StringBuilder originAddress = new StringBuilder();

		originAddress.append(origin.getAddress()).append(BLANK)
		.append(origin.getCity()).append(BLANK)
		.append(origin.getPostalCode()).append(BLANK);

		if(!StringUtils.isBlank(origin.getState())) {
			originAddress.append(origin.getState()).append(" ");
		}
		if(origin.getZone()!=null) {
			originAddress.append(origin.getZone().getCode()).append(" ");
		}
		originAddress.append(origin.getCountry().getIsoCode());


		//build destination address
		StringBuilder destinationAddress = new StringBuilder();

		destinationAddress.append(delivery.getAddress()).append(BLANK);
		if(!StringUtils.isBlank(delivery.getCity())) {
			destinationAddress.append(delivery.getCity()).append(BLANK);
		}
		destinationAddress.append(delivery.getPostalCode()).append(BLANK);

		if(!StringUtils.isBlank(delivery.getState())) {
			destinationAddress.append(delivery.getState()).append(" ");
		}
		if(delivery.getZone()!=null) {
			destinationAddress.append(delivery.getZone().getCode()).append(" ");
		}
		destinationAddress.append(delivery.getCountry().getIsoCode());


		try {
			GeocodingResult[] originAdressResult =  GeocodingApi.geocode(context,
					originAddress.toString()).await();

			GeocodingResult[] destinationAdressResult =  GeocodingApi.geocode(context,
					destinationAddress.toString()).await();

			if(originAdressResult.length>0 && destinationAdressResult.length>0) {
				LatLng originLatLng = originAdressResult[0].geometry.location;
				LatLng destinationLatLng = destinationAdressResult[0].geometry.location;

				delivery.setLatitude(String.valueOf(destinationLatLng.lat));
				delivery.setLongitude(String.valueOf(destinationLatLng.lng));

				//keep latlng for further usage in order to display the map


				DistanceMatrix  distanceRequest = DistanceMatrixApi.newRequest(context)
						.origins(new LatLng(originLatLng.lat, originLatLng.lng))
						.destinations(new LatLng(destinationLatLng.lat, destinationLatLng.lng))
						.awaitIgnoreError();

				if(distanceRequest!=null) {
					DistanceMatrixRow distanceMax = distanceRequest.rows[0];
					Distance distance = distanceMax.elements[0].distance;
					quote.getQuoteInformations().put(Constants.DISTANCE_KEY, 0.001 * distanceInKMs(distance));
				}

			}
		} catch (Exception e) {
			LOGGER.error("Exception while calculating the shipping distance",e);
		}
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getModuleCode() {
		return MODULE_CODE;
	}
}

/**
 * http://maps.googleapis.com/maps/api/distancematrix/json?origins=504303&destinations=500018&mode=driving&language=en-EN&sensor=false
{
"destination_addresses": [
"Hyderabad, Telangana 500018, India"
],
"origin_addresses": [
"Telangana 504303, India"
],
"rows": [
{
   "elements": [
       {
           "distance": {
               "text": "243 km",
               "value": 243180
           },
           "duration": {
               "text": "4 hours 46 mins",
               "value": 17148
           },
           "status": "OK"
       }
   ]
}
],
"status": "OK"
}
 */
