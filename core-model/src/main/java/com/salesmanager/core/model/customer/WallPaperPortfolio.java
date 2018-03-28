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
import javax.persistence.Transient;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.product.price.FinalPrice;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.services.Services;

@Entity
@Table(name = "WALLPAPER_PORTFOLIO", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class WallPaperPortfolio extends SalesManagerEntity<Long, WallPaperPortfolio> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "WALLPAPER_PORTFOLIO_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
 
	@Temporal(TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date createDate;
	
	@ManyToOne(targetEntity = Customer.class)
	@JoinColumn(name = "WALLPAPER_ID", nullable = false)
    private Customer customer;
	
	@Column (name ="PORTFOLIO_NAME", length=200)
	private String portfolioName;
	
	@Column (name ="IMAGE_URL", length=200)
	private String imageURL;

	@Column(name = "PRICE")
	private BigDecimal price;
	
	@Column (name ="SIZE", length=50)
	private String size;
	
	@Column (name ="THICKNESS", length=50)
	private String thickness;
	
	@Column (name ="BRAND", length=50)
	private String brand;
	
	@Column (name ="ADMIN_APPROVE_STATUS", length=5)
	private String status = "N";
	
	@Column (name ="SERVICE_CHARGES")
	private BigDecimal serviceCharges;

	@Transient
	private Integer quantity = new Integer(0);

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getServiceCharges() {
		return serviceCharges;
	}

	public void setServiceCharges(BigDecimal serviceCharges) {
		this.serviceCharges = serviceCharges;
	}

	
}
