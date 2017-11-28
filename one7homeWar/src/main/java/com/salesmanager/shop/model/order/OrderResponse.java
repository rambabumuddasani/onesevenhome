package com.salesmanager.shop.model.order;

import java.util.List;

public class OrderResponse {

	private boolean isLast;
	private boolean isFirst;
	private int totalPages;
	private int size;
	private int number;
	private String sort;
	private int numberOfElements;
	private List<ReadableOrder> orders;

	public OrderResponse() {
		super();
	}

	public OrderResponse(boolean isLast, boolean isFirst, int totalPages, int size, int number, String sort,
			int numberOfElements) {
		super();
		this.isLast = isLast;
		this.isFirst = isFirst;
		this.totalPages = totalPages;
		this.size = size;
		this.number = number;
		this.sort = sort;
		this.numberOfElements = numberOfElements;
	}
	
	public boolean isLast() {
		return isLast;
	}
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	public boolean isFirst() {
		return isFirst;
	}
	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public int getNumberOfElements() {
		return numberOfElements;
	}
	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}
	public List<ReadableOrder> getOrders() {
		return orders;
	}
	public void setOrders(List<ReadableOrder> orders) {
		this.orders = orders;
	}
}	
