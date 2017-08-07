package com.salesmanager.core.model.catalog.product.filter;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.common.Description;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.reference.language.Language;

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
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "FILTER_TYPE", schema= SchemaConstant.SALESMANAGER_SCHEMA,uniqueConstraints=
    @UniqueConstraint(columnNames = {"FILTER_ID","FILTERNAME_ID"}) )
public class FilterType extends SalesManagerEntity<Long, FilterType> {
	private static final long serialVersionUID = -3248423008455359301L;
	
	@Id
	@Column(name = "FILTER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "FILTER_TYPE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Filter.class)
	@JoinColumn(name = "FILTERNAME_ID", nullable = false)
	private Filter filter;

	@Column(name="FILTER_TYPE_NAME", length=120)
	private String filterTypeName;
	
	public FilterType() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public String getFilterTypeName() {
		return filterTypeName;
	}

	public void setFilterTypeName(String filterTypeName) {
		this.filterTypeName = filterTypeName;
	}
	
}
