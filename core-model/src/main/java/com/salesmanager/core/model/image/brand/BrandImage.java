package com.salesmanager.core.model.image.brand;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.generic.SalesManagerEntity;

@Entity
@Table(name = "BRAND_IMAGE", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class BrandImage extends SalesManagerEntity<Long, ProductImage> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -712756118617861448L;

	@Id
	@Column(name = "BRAND_IMAGE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "BRAND_IMAGE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="BRAND_IMG")
	private String image;
	
	@Column(name = "BRAND_NAME")
	private String name;
	
	@Column(name = "STATUS")
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
