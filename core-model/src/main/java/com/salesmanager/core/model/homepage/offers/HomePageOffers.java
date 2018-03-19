package com.salesmanager.core.model.homepage.offers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@Table(name = "HOMEPAGE_OFFERS", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class HomePageOffers extends SalesManagerEntity<Long, HomePageOffers>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2497640907840074695L;

	@Id
	@Column(name = "HOMEPAGE_OFFERS_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "HOMEPAGE_OFFERS_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="CATEGORY_NAME",length=30)
	private String categoryName;
	
	@Column(name="SECTION_NAME",length=10)
	private String sectionName;
	
	@Column(name="SUB_CATEGORY_NAME",length=80)
	private String subCategoryName;
	
	@Column(name="DESCRIPTION",length=20)
	private String description;
	
	@Column(name="CATEGORY_TITLE",length=30)
	private String categoryTitle;
	
	@Column(name="DISCOUNT")
	private Integer discount;
	
	/*@Column(name="SERVICE_NAME",length=20)
	private String serviceName;*/
	
	@Column(name="CATEGORY_IMAGE_URL",length=80)
	private String categoryImageURL;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public String getCategoryImageURL() {
		return categoryImageURL;
	}

	public void setCategoryImageURL(String categoryImageURL) {
		this.categoryImageURL = categoryImageURL;
	}
	
	

}
