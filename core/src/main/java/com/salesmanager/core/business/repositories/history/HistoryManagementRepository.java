package com.salesmanager.core.business.repositories.history;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.history.HistoryManagement;


public interface HistoryManagementRepository extends JpaRepository<HistoryManagement,Long>, HistoryManagementRepositoryCustom{

	@Query("select hm from HistoryManagement hm where hm.productPriceStartDate>=?1 and hm.productPriceEndDate<=?2")
	List<HistoryManagement> getHistoryOfDealsByDate(Date fromDate, Date toDate);

}
