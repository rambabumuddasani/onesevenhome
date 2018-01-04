package com.salesmanager.core.business.services.historymanage;

import java.util.Date;
import java.util.List;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.history.HistoryManagement;

public interface HistoryManagementService extends SalesManagerEntityService<Long,HistoryManagement> {

	List<HistoryManagement> getHistoryOfDeals();

	List<HistoryManagement> getHistoryOfDealsByDate(Date fromDate,Date toDate);

}
