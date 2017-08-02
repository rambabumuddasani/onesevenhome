package com.salesmanager.shop.admin.controller.products;

import java.util.List;

public class RecentlyBought {
	
	List<ProductResponse> recentlyBought;

	public List<ProductResponse> getRecentlyBought() {
		return recentlyBought;
	}

	public void setRecentlyBought(List<ProductResponse> recentlyBought) {
		this.recentlyBought = recentlyBought;
	} 
	
}
