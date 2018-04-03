package com.salesmanager.core.business.services.postrequirement;

import java.util.Date;
import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.postrequirement.PostRequirement;

public interface PostRequirementService extends SalesManagerEntityService<Long,PostRequirement>{

	List<PostRequirement> getAllPostRequirements();

	List<PostRequirement> getPostRequirementsByCustomerId(Long customerId);

	List<PostRequirement> getAllPostRequirements(Date startDate, Date endDate);

	List<PostRequirement> getPostRequirementsBasedOnStatusAndDate(String status, Date startDate, Date endDate);

}
