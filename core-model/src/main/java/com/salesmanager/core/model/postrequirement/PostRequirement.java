package com.salesmanager.core.model.postrequirement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@Table(name = "POST_REQUIREMENT", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class PostRequirement extends SalesManagerEntity<Long,PostRequirement> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2128377820332692429L;

	@Id
	@Column(name = "POST_REQUIREMENT_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "POST_REQUIREMENT_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="STATE", length=100)
	private String state;
	
	@Column(name="QUERY", length=300)
	private String query;
	
	@Column(name="CUSTOMER_ID", nullable = false)
	private Long customerId;
	
	@Column(name = "CATEGORY_ID", nullable = false)
	private Long categoryId;
	
	@Column(name="RESPONSE_MESSAGE", length=256)
	private String responseMessage;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="POST_DATE")
	private Date postedDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public Date getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	

}
