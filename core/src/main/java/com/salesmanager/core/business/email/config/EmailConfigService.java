package com.salesmanager.core.business.email.config;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.email.config.EmailConfig;


public interface EmailConfigService extends SalesManagerEntityService<Long, EmailConfig> {
	EmailConfig findEmailConfigById(Long id);
}