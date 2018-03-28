package com.salesmanager.core.business.services.homepage.customization.headers;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.homepage.customization.headers.Customizations;

public interface CustomizationService extends SalesManagerEntityService<Long, Customizations>{

	Customizations getByCustomizationId(String customizationId);

}
