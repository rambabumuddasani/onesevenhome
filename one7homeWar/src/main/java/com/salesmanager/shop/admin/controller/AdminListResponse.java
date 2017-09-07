package com.salesmanager.shop.admin.controller;

import java.util.List;

public class AdminListResponse {

	private List<UserVO> adminList;
	public List<UserVO> getAdminList() {
		return adminList;
	}
	public void setAdminList(List<UserVO> adminList) {
		this.adminList = adminList;
	}
    
}
