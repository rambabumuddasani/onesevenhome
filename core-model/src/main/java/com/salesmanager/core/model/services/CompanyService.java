package com.salesmanager.core.model.services;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@Table(name = "COMPANY_SERVICE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class CompanyService extends SalesManagerEntity<Integer, CompanyService> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "STORE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="SERVICE_ID",nullable=false)
	private Services services;
	
	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, mappedBy="servicesWorker")
	private Set<WorkerRating> workerRating = new HashSet<WorkerRating>(0);
	
	public Set<WorkerRating> getWorkerRating() {
		return workerRating;
	}

	public void setWorkerRating(Set<WorkerRating> workerRating) {
		this.workerRating = workerRating;
	}

	/*@Column(name = "FIRST_NAME",length=100)
	private String firstName;

	@Column(name = "LAST_NAME", length=100)
	private String lastName;
	
	@Column(name = "EMAIL", length=100)
	private String email;
	
	
	@Column(name = "MOBILE_NUMBER", length=100)
	private String mobileNumber;
	
	@Column(name = "LOCATION", length=100)
	private String location;*/
	
	@Column(name = "COMP_NAME", length=100)
	private String companyName;
	
	@Column(name = "HOUSE_NUMBER", length=100)
	private String houseNumber;
	
	@Column(name = "STREET", length=100)
	private String street;
	
	@Column(name = "AREA", length=100)
	private String area;
	
	@Column(name = "CITY", length=100)
	private String city;
	
	@Column(name = "STATE", length=100)
	private String state;
	
	@Column(name = "PINCODE", length=100)
	private String pinCode;
	
	@Column(name = "COUNTRY", length=100)
	private String country;
	
	@Column(name = "IMAGEURL", length=100)
	private String imageUrl;
	
	@Column(name = "WEBSITE_NAME", length=100)
	private String websiteName;
	
	@Column(name = "CONTACT_NUMBER", length=100)
	private String contactNumber;
	
	
	public CompanyService() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
}
