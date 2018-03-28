package com.salesmanager.core.model.homepage.customization.headers;

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
@Table(name = "CUSTOMIZATIONS", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Customizations extends SalesManagerEntity<Long, Customizations>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9196318433408609503L;

	@Id
	@Column(name = "ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "CUSTOMIZATIONS_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="CUSTOMIZATION_ID",length=50)
	private String customizationId;
	
	@Column(name="HEADER",length=150)
	private String header;
	
	@Column(name="SUB_HEADER",length=250)
	private String subHeader;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomizationId() {
		return customizationId;
	}

	public void setCustomizationId(String customizationId) {
		this.customizationId = customizationId;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getSubHeader() {
		return subHeader;
	}

	public void setSubHeader(String subHeader) {
		this.subHeader = subHeader;
	}


}
