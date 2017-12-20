package com.salesmanager.core.business.repositories.history;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.history.HistoryManagement;


public interface HistoryManagementRepository extends JpaRepository<HistoryManagement,Long>, HistoryManagementRepositoryCustom{

}
