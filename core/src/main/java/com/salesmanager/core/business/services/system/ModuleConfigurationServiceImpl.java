package com.salesmanager.core.business.services.system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.system.ModuleConfigurationRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.services.reference.loader.IntegrationModulesLoader;
import com.salesmanager.core.business.utils.CacheUtils;
import com.salesmanager.core.model.system.IntegrationModule;
import com.salesmanager.core.model.system.ModuleConfig;

@Service("moduleConfigurationService")
public class ModuleConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<Long, IntegrationModule> implements
		ModuleConfigurationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleConfigurationServiceImpl.class);
	
	@Inject
	private IntegrationModulesLoader integrationModulesLoader;
	

	
	private ModuleConfigurationRepository moduleConfigurationRepository;
	
	@Inject
	private CacheUtils cache;
	
	@Inject
	public ModuleConfigurationServiceImpl(
			ModuleConfigurationRepository moduleConfigurationRepository) {
			super(moduleConfigurationRepository);
			this.moduleConfigurationRepository = moduleConfigurationRepository;
	}
	
	@Override
	public IntegrationModule getByCode(String moduleCode) {
		return moduleConfigurationRepository.findByCode(moduleCode);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<IntegrationModule> getIntegrationModules(String module) {
		
		
		List<IntegrationModule> modules = null;
		try {
			
			//CacheUtils cacheUtils = CacheUtils.getInstance();
			modules = (List<IntegrationModule>) cache.getFromCache("INTEGRATION_M)" + module);
			if(modules==null) {
				modules = moduleConfigurationRepository.findByModule(module);
				//set json objects
				for(IntegrationModule mod : modules) {
					
					String regions = mod.getRegions();
					if(regions!=null) {
						Object objRegions=JSONValue.parse(regions); 
						JSONArray arrayRegions=(JSONArray)objRegions;
						Iterator i = arrayRegions.iterator();
						while(i.hasNext()) {
							mod.getRegionsSet().add((String)i.next());
						}
					}
					
					
					String details = mod.getConfigDetails();
					if(details!=null) {
						
						//Map objects = mapper.readValue(config, Map.class);

						Map<String,String> objDetails= (Map<String, String>) JSONValue.parse(details); 
						mod.setDetails(objDetails);

						
					}
					
					
					String configs = mod.getConfiguration();
					if(configs!=null) {
						//Map objects = mapper.readValue(config, Map.class);
						Object objConfigs=JSONValue.parse(configs); 
						JSONArray arrayConfigs=(JSONArray)objConfigs;
						
						Map<String,ModuleConfig> moduleConfigs = new HashMap<String,ModuleConfig>();
						
						Iterator i = arrayConfigs.iterator();
						while(i.hasNext()) {
							
							Map values = (Map)i.next();
							String env = (String)values.get("env");
		            		ModuleConfig config = new ModuleConfig();
		            		config.setScheme((String)values.get("scheme"));
		            		config.setHost((String)values.get("host"));
		            		config.setPort((String)values.get("port"));
		            		config.setUri((String)values.get("uri"));
		            		config.setEnv((String)values.get("env"));
		            		if((String)values.get("config1")!=null) {
		            			config.setConfig1((String)values.get("config1"));
		            		}
		            		if((String)values.get("config2")!=null) {
		            			config.setConfig1((String)values.get("config2"));
		            		}
		            		
		            		moduleConfigs.put(env, config);
		            		
		            		
							
						}
						
						mod.setModuleConfigs(moduleConfigs);
						

					}


				}
				cache.putInCache(modules, "INTEGRATION_M)" + module);
			}

		} catch (Exception e) {
			LOGGER.error("getIntegrationModules()", e);
		}
		return modules;
		
		
	}

/*	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = new String(Files.readAllBytes(Paths.get("D://fileData.txt")));
		String json = {
				"module": "PAYMENT",
				"code": "stripe",
				"type":"creditcard",
				"version":"",
				"regions": ["US","CA","GB","AU","FI","DK","IE","NO","SE"],
				"image":"stripe.png",
				"configuration":[{"env":"TEST","scheme":"https","host":"www.stripe.com","port":"443","uri":"/"},{"env":"PROD","scheme":"https","host":"www.stripe.com","port":"443","uri":"/"}]
			};	
		
		Map<String,Object> map = mapper.readValue(json, Map.class);
		for(Map.Entry<String, Object> entry : map.entrySet()){
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
		String s = mapper.writeValueAsString(map.get("configuration"));
		System.out.println("string data "+s);
		//System.out.println(map);
	}	
*/	
	@Override
	public void createOrUpdateModule(String json) throws ServiceException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			
			
			@SuppressWarnings("rawtypes")
			Map object = mapper.readValue(json, Map.class);
			
			IntegrationModule module = integrationModulesLoader.loadModule(object);
			
            if(module!=null) {
            	IntegrationModule m = this.getByCode(module.getCode());
            	if(m!=null) {
            		this.delete(m);	 	
            	}
            	this.create(module);
            }



  		} catch (Exception e) {
  			throw new ServiceException(e);
  		} 
		
		
		
		
	}
	
	

	



}
