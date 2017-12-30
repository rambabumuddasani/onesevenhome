package com.salesmanager.shop.admin.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ShippingModuleConfig {

/*	public static void main(String[] args) throws IOException {
		ShippingModuleConfig obj = new ShippingModuleConfig();
		ObjectMapper mapper = new ObjectMapper();
		obj.setBeyandDistancePrice("100");
		obj.setFreeShippingDistanceRange("10");
		obj.setMinOrderPrice("34");
		Map<Integer,Long> priceByDistance = new HashMap<Integer,Long>();
		priceByDistance.put(110, 0l);
		priceByDistance.put(20, 7l);
		priceByDistance.put(30, 9l);
		priceByDistance.put(40, 10l);
		Map<Integer, Long> sortedMap = priceByDistance.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
       
		obj.setPriceByDistance(sortedMap);
		//Map<String,Object> map = mapper.readValue(json, Map.class);
   		String jsonConfigString = mapper.writeValueAsString(obj);
   		System.out.println("jsonConfigString "+jsonConfigString);
   		ShippingModuleConfig o2 = mapper.readValue(jsonConfigString, ShippingModuleConfig.class);
   		System.out.println("o2 "+o2);
	}		
*/		
	//private Long systemConfigId;
	private long freeShippingDistanceRange;
	private long minOrderPrice;
	private long beyandDistancePrice;	
	Map<Integer,Long> priceByDistance;
	
	public long getFreeShippingDistanceRange() {
		return freeShippingDistanceRange;
	}
	public void setFreeShippingDistanceRange(long freeShippingDistanceRange) {
		this.freeShippingDistanceRange = freeShippingDistanceRange;
	}
	public long getMinOrderPrice() {
		return minOrderPrice;
	}
	public void setMinOrderPrice(long minOrderPrice) {
		this.minOrderPrice = minOrderPrice;
	}
	public long getBeyandDistancePrice() {
		return beyandDistancePrice;
	}
	public void setBeyandDistancePrice(long beyandDistancePrice) {
		this.beyandDistancePrice = beyandDistancePrice;
	}
	public Map<Integer, Long> getPriceByDistance() {
		return priceByDistance;
	}
	public void setPriceByDistance(Map<Integer, Long> priceByDistance) {
		this.priceByDistance = priceByDistance;
	}
	
	@Override
	public String toString() {
		return "ShippingModuleConfig [freeShippingDistanceRange=" + freeShippingDistanceRange + ", minOrderPrice="
				+ minOrderPrice + ", beyandDistancePrice=" + beyandDistancePrice + ", priceByDistance="
				+ priceByDistance + "]";
	}
}
