package com.salesmanager.core.model.services;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@Table(name = "SERVICES", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Services extends SalesManagerEntity<Integer, Services> {
	private static final long serialVersionUID = 7671103335743647656L;
	
	
	public final static String DEFAULT_STORE = "DEFAULT";
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="services")
	private Set<CompanyService> serviceWorkers=new HashSet<CompanyService>();
	
	@Id
	@Column(name = "ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "STORE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Integer id;

	@NotEmpty
	@Column(name = "SERVICE_TYPE", nullable=false, length=100)
	private String serviceType;

	@Column(name = "IMAGEURL1", length=100)
	private String imageURL1;
	
	@Column(name = "IMAGEURL2", length=100)
	private String imageURL2;
	
	@Column(name="description",length=250)
	private String description;
	

	public Services() {
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return this.id;
	}
	
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	

	public String getImageURL1() {
		return imageURL1;
	}

	public void setImageURL1(String imageURL1) {
		this.imageURL1 = imageURL1;
	}

	public String getImageURL2() {
		return imageURL2;
	}

	public void setImageURL2(String imageURL2) {
		this.imageURL2 = imageURL2;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
