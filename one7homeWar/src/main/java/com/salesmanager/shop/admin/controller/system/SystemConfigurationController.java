package com.salesmanager.shop.admin.controller.system;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.constants.SystemConstants;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.system.SystemConfigurationService;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.system.SystemConfiguration;

@RestController
@RequestMapping("/shippingDistanceConfig")
public class SystemConfigurationController {

	@Inject
	private SystemConfigurationService systemConfigurationService;
	
	@RequestMapping(value={"/saveOrUpdate"}, method=RequestMethod.POST)
	public void saveConfiguration(@RequestBody ShippingModuleConfig shippingModuleConfig) throws Exception{		
		ObjectMapper mapper = new ObjectMapper();
		//SystemConfiguration configuration = new SystemConfiguration();
		SystemConfiguration configuration = systemConfigurationService.getByKey(SchemaConstant.SHIPPING_DISTANCE_CONFIG);

		if(configuration == null) {
				configuration = new SystemConfiguration();
				configuration.setKey(SchemaConstant.SHIPPING_DISTANCE_CONFIG);
				configuration.getAuditSection().setDateCreated(new Date());
		}
		configuration.getAuditSection().setDateModified(new Date());
		configuration.getAuditSection().setModifiedBy(SystemConstants.SYSTEM_USER);
		
		Map<Integer,Long> priceByDistance  = shippingModuleConfig.getPriceByDistance();
        Map<Integer, Long> sortedpriceByDistanceMap = priceByDistance.entrySet().stream()
                .sorted(Map.Entry. <Integer, Long> comparingByKey())
                //.sorted(Map.Entry. <Integer, Long> comparingByKey().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		shippingModuleConfig.setPriceByDistance(sortedpriceByDistanceMap);
   		String jsonConfigString = mapper.writeValueAsString(shippingModuleConfig);
		configuration.setValue(jsonConfigString);
		try {
			systemConfigurationService.create(configuration);
		} catch (ServiceException e) {
			throw new Exception("Exception during crea	tion of configuration object "+e);
		}
	}
	
	@RequestMapping(value={"/getShippingModuleConfig"},method=RequestMethod.GET)
	public ShippingModuleConfig getShippingModuleConfig() throws JsonParseException, JsonMappingException, IOException, ServiceException{
		SystemConfiguration configuration = systemConfigurationService.getByKey(SchemaConstant.SHIPPING_DISTANCE_CONFIG);
		ObjectMapper mapper = new ObjectMapper();	
		ShippingModuleConfig config = mapper.readValue(configuration.getValue(), ShippingModuleConfig.class);
		return config;
	}
		
}
