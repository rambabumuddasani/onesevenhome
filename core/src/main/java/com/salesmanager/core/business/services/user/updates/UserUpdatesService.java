package com.salesmanager.core.business.services.user.updates;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.user.updates.UserUpdates;

public interface UserUpdatesService extends SalesManagerEntityService<Long, UserUpdates>{

	UserUpdates getByEmailAddress(String emailAddress);

}
