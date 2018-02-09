package com.salesmanager.core.model.catalog.product.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.merchant.MerchantStore;

@Entity
@Table(name = "FILTER_CATEGORY", schema= SchemaConstant.SALESMANAGER_SCHEMA,uniqueConstraints=
    @UniqueConstraint(columnNames = {"CATEGORY_ID","FILTERNAME_ID"}) )


public class Filter extends SalesManagerEntity<Long, Filter> {
	private static final long serialVersionUID = -846291242449186747L;
	
	@Id
	@Column(name = "FILTERNAME_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "FILTER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Valid
	@OneToMany(mappedBy="filter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<FilterType> filterTypes = new ArrayList<FilterType>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CATEGORY_ID", nullable=false)
	private Category category;
	
	@Column(name = "FILTER_NAME", length=100)
	private String filterName;

	public Filter() {
	}
	
	public Filter(Category category) {
		this.category = category;
		this.id = 0L;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public List<FilterType> getFilterTypes() {
		return filterTypes;
	}

	public void setFilterTypes(List<FilterType> filterTypes) {
		this.filterTypes = filterTypes;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public FilterType getFilterType() {
		if(filterTypes!=null && filterTypes.size()>0) {
			return filterTypes.iterator().next();
		}
		
		return null;
	}
}