package com.salesmanager.core.model.history;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@Table(name = "HISTORY_MANAGEMENT", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class HistoryManagement extends SalesManagerEntity<Long,HistoryManagement>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3265935869726286959L;

	@Id
	@Column(name = "HISTORY_MANG_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "HISTORY_MANG_ID_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name= "PRODUCT_ID")
	private Long productId;
	
	@Column(name= "PRODUCT_NAME")
	private String productName;
	
	@Column(name= "PRODUCT_PRICE")
	private BigDecimal productPrice;
	
	@Column(name= "PRODUCT_DISCOUNT_PRICE")
	private BigDecimal productDiscountPrice;
	
	@Temporal(TemporalType.DATE)
	@Column(name= "PRODUCT_PRICE_STARTDATE")
	private Date productPriceStartDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name= "PRODUCT_PRICE_ENDDATE")
	private Date productPriceEndDate;
	
	@Column(name= "ENABLE_FOR")
	private String enableFor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public BigDecimal getProductDiscountPrice() {
		return productDiscountPrice;
	}

	public void setProductDiscountPrice(BigDecimal productDiscountPrice) {
		this.productDiscountPrice = productDiscountPrice;
	}

	public Date getProductPriceStartDate() {
		return productPriceStartDate;
	}

	public void setProductPriceStartDate(Date productPriceStartDate) {
		this.productPriceStartDate = productPriceStartDate;
	}

	public Date getProductPriceEndDate() {
		return productPriceEndDate;
	}

	public void setProductPriceEndDate(Date productPriceEndDate) {
		this.productPriceEndDate = productPriceEndDate;
	}

	public String getEnableFor() {
		return enableFor;
	}

	public void setEnableFor(String enableFor) {
		this.enableFor = enableFor;
	}
}
