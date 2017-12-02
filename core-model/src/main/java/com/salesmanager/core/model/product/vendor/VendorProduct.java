package com.salesmanager.core.model.product.vendor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@Table(name = "VENDOR_PRODUCT", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class VendorProduct extends SalesManagerEntity<Long, VendorProduct>{
       
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VENDOR_PRODUCT_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "VENDOR_PRODUCT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="PRODUCT_ID", nullable=false)
	private Product product;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CUSTOMER_ID", nullable=false)
	private Customer customer;
	
	@Column(name="CREATE_DATE",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name="ADMIN_ACTIVATED",nullable=true)
	private Boolean adminActivated=false;
	
	@Column(name="ADMIN_ACTIVATED_DATE",nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date adminActivatedDate;
	
	@Column(name="VENDOR_WISH_LISTED",nullable=true)
	private Boolean vendorWishListed = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean isAdminActivated() {
		return adminActivated;
	}

	public void setAdminActivated(Boolean adminActivated) {
		this.adminActivated = adminActivated;
	}

	public Date getAdminActivatedDate() {
		return adminActivatedDate;
	}

	public void setAdminActivatedDate(Date adminActivatedDate) {
		this.adminActivatedDate = adminActivatedDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Boolean isVendorWishListed() {
		return vendorWishListed;
	}

	public void setVendorWishListed(Boolean vendorWishListed) {
		this.vendorWishListed = vendorWishListed;
	}
}
