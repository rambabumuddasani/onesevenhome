package com.salesmanager.core.model.customer;

import java.math.BigDecimal;
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
@Table(name = "MACHINERY_PORTFOLIO", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class MachineryPortfolio extends SalesManagerEntity<Long, MachineryPortfolio> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MACHINERY_PORTFOLIO_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
 
	@Temporal(TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date createDate;
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "MACHINERY_ID", nullable = false)
    private Customer customer;
	
	@Column (name ="PORTFOLIO_NAME", length=200)
	private String portfolioName;
	
	@Column (name ="IMAGE_URL", length=200)
	private String imageURL;
	
	@Column (name ="ADMIN_APPROVE_STATUS", length=5)
	private String status = "N";
	
	@Column (name ="EQUIPMENT_NAME", length=20)
	private String equipmentName;
	
	@Column (name ="EQUIPMENT_PRICE")
	private BigDecimal equipmentPrice;
	
	@Column (name ="HIRING_TYPE", length=20)
	private String hiringType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public BigDecimal getEquipmentPrice() {
		return equipmentPrice;
	}

	public void setEquipmentPrice(BigDecimal equipmentPrice) {
		this.equipmentPrice = equipmentPrice;
	}

	public String getHiringType() {
		return hiringType;
	}

	public void setHiringType(String hiringType) {
		this.hiringType = hiringType;
	}

	
}
