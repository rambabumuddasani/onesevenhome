package com.salesmanager.core.model.services;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.generic.SalesManagerEntity;


@Entity
@Table(name = "WORKER_RATING", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class WorkerRating extends SalesManagerEntity<Integer,WorkerRating> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7671103335743647656L;
							
	@Id
	@Column(name = "ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "STORE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Integer id;
	
	@Column(name="RATING")
	private Integer rating;
	
	@Column(name="RATING_DESC",length=250)
	private String ratingDesc;
	
    @JsonIgnore
	@ManyToOne(targetEntity = CompanyService.class,fetch=FetchType.LAZY)
	@JoinColumn(name="SERVICE_WORKER_ID",nullable=false)
	private CompanyService servicesWorker;
    
    @JsonIgnore
   	@ManyToOne(targetEntity = Customer.class,fetch=FetchType.LAZY)
   	@JoinColumn(name="CUSTOMER_ID",nullable=false)
   	private Customer customer;
    
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date createDate;
	
		

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public WorkerRating() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getRatingDesc() {
		return ratingDesc;
	}

	public void setRatingDesc(String rating_desc) {
		this.ratingDesc = rating_desc;
	}
}
