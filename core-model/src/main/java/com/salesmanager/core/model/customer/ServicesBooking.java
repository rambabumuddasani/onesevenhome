package com.salesmanager.core.model.customer;

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
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.services.Services;

@Entity
@Table(name = "SERVICES_BOOKING", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class ServicesBooking extends SalesManagerEntity<Long, ServicesBooking> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SERVICES_BOOKING_SEQ_NEXT_VAL")
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
    private Customer service;

	@ManyToOne(targetEntity = Services.class)
	@JoinColumn(name = "SERVICE_TYPE_ID", nullable = false)
    private Services serviceType;
	
	@Column(name="COMMENT")
	private String comment;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CONTACT_INFO",length=150)
	private String contactInfo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APPOINTMENT_DATE")
	private Date appointmentDate;
	
	@Column(name="QUERY",length=200)
	private String query;

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

	public Customer getService() {
		return service;
	}

	public void setService(Customer service) {
		this.service = service;
	}

	public Services getServiceType() {
		return serviceType;
	}

	public void setServiceType(Services serviceType) {
		this.serviceType = serviceType;
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

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
}
