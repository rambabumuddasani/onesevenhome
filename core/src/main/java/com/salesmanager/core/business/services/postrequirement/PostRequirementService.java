package com.salesmanager.core.business.services.postrequirement;

import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.postrequirement.PostRequirement;

public interface PostRequirementService extends SalesManagerEntityService<Long,PostRequirement>{

	List<PostRequirement> getAllPostRequirements();

}
