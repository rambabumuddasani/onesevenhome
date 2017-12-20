package com.salesmanager.core.business.services.historymanage;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.history.HistoryManagementRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.history.HistoryManagement;

@Service("historyManagementService")
public class HistoryManagementServiceImpl extends SalesManagerEntityServiceImpl<Long,HistoryManagement> implements HistoryManagementService{

	@Inject
	HistoryManagementRepository historyManagementRepository;
	
	@Inject
	HistoryManagementServiceImpl(HistoryManagementRepository historyManagementRepository) {
		super(historyManagementRepository);
		this.historyManagementRepository = historyManagementRepository;
	}
	
}
