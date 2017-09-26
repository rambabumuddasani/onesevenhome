package com.salesmanager.core.model.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.zone.Zone;

@Embeddable
public class SecondaryDelivery {
	
	@Column (name ="DELIVERY_LAST_NAME_2", length=64)
	private String lastName;

	@Column (name ="DELIVERY_FIRST_NAME_2", length=64)
	private String firstName;

	@Column (name ="DELIVERY_COMPANY_2", length=100)
	private String company;
	
	@Column (name ="DELIVERY_STREET_ADDRESS_2", length=256)
	private String address;

	@Column (name ="DELIVERY_CITY_2", length=100)
	private String city;
	
	@Column (name ="DELIVERY_POSTCODE_2", length=20)
	private String postalCode;
	
	@Column (name ="DELIVERY_STATE_2", length=100)
	private String state;
	
	@Column(name="DELIVERY_TELEPHONE_2", length=32)
	private String telephone;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class)
	@JoinColumn(name="DELIVERY_COUNTRY_ID_2", nullable=true)
	private Country country;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Zone.class)
	@JoinColumn(name="DELIVERY_ZONE_ID_2", nullable=true)
	private Zone zone;
	
	@Transient
	private String latitude = null;
	
	@Transient
	private String longitude = null;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}
	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return telephone;
	}	
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
