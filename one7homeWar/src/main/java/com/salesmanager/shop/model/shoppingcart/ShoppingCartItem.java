package com.salesmanager.shop.model.shoppingcart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.shop.model.ShopEntity;
import com.salesmanager.shop.store.controller.customer.VendorResponse;


public class ShoppingCartItem extends ShopEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String price;
	private String image;
	@JsonIgnore
	private BigDecimal productPrice;
	private int quantity;             
	private long productId;
	private String productCode;
	private String code;//shopping cart code
	@JsonIgnore
	private boolean productVirtual;
	
	private String subTotal;
	private String discountPrice;
	private long vendorId;

	private String category;
	private VendorResponse vendorDetails = new VendorResponse();
	
	@JsonIgnore
	private List<ShoppingCartAttribute> shoppingCartAttributes;
	
	@JsonIgnore
	private String customerId;	
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public long getVendorId() {
		return vendorId;
	}
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
	public String getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<ShoppingCartAttribute> getShoppingCartAttributes() {
		return shoppingCartAttributes;
	}
	public void setShoppingCartAttributes(List<ShoppingCartAttribute> shoppingCartAttributes) {
		this.shoppingCartAttributes = shoppingCartAttributes;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImage() {
		return image;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public boolean isProductVirtual() {
		return productVirtual;
	}
	public void setProductVirtual(boolean productVirtual) {
		this.productVirtual = productVirtual;
	}
	public String getCustomerId() {
		// TODO Auto-generated method stub
		return customerId;
	}
	/*public long getTotalPriceOfEachItem() {
		return totalPriceOfEachItem;
	}
	public void setTotalPriceOfEachItem(long totalPriceOfEachItem) {
		this.totalPriceOfEachItem = totalPriceOfEachItem;
	}*/
	public VendorResponse getVendorDetails() {
		return vendorDetails;
	}
	public void setVendorDetails(VendorResponse vendorResponse) {
		this.vendorDetails = vendorResponse;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
