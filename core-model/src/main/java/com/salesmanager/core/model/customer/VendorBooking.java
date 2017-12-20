package com.salesmanager.core.model.customer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.services.Services;

@Entity
@Table(name = "VENDOR_BOOKING", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class VendorBooking extends SalesManagerEntity<Long, VendorBooking> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "VENDOR_BOOKING_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
 
	@Temporal(TemporalType.DATE)
	@Column(name="BOOKING_DATE")
	private Date bookingDate;
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "SERVICE_ID", nullable = false)
    private Customer vendor;

	@Column(name="COMMENT")
	private String comment;
	
	@Column(name="STATUS")
	private String status;

	@Temporal(TemporalType.DATE)
	@Column(name="CLOSING_DATE")
	private Date closingDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getVendor() {
		return vendor;
	}

	public void setVendor(Customer vendor) {
		this.vendor = vendor;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	
}
