package com.salesmanager.core.model.user.updates;

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
@Table(name = "EXTERNAL_USERS_UPDATES", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class UserUpdates extends SalesManagerEntity<Long, UserUpdates>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5087551232959461206L;

	@Id
	@Column(name = "EXTERNAL_USERS_UPDATES_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "EXTERNAL_USERS_UPDATES_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="USER_EMAIL_ADDRESS", length=96, nullable=false)
	private String emailAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
