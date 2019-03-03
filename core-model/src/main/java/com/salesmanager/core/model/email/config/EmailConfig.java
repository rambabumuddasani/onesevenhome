package com.salesmanager.core.model.email.config;

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
@Table(name = "EMAIL_CONFIG", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class EmailConfig extends SalesManagerEntity<Long, EmailConfig>  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EMAIL_CONFIG_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
		pkColumnValue = "EMAIL_CONFIG_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="HOST", length=25)
	private String host;
	
	@Column(name="PORT", length=25)
	private String port;
	
	@Column(name="PROTOCOL", length=6)
	private String protocol;
	
	@Column(name="USER_NAME", length=25)
	private String username;
	
	@Column(name="PASSWORD", length=25)
	private String password;
	
	@Column(name="SMTP_AUTH")
	private boolean smtpAuth = false;
	
	@Column(name="STARTTLS")
	private boolean starttls = false;
	
	@Column(name="EMAIL_TEMPLATE_PATH", length=25)
	private String emailTemplatesPath = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public boolean isStarttls() {
		return starttls;
	}

	public void setStarttls(boolean starttls) {
		this.starttls = starttls;
	}

	public String getEmailTemplatesPath() {
		return emailTemplatesPath;
	}

	public void setEmailTemplatesPath(String emailTemplatesPath) {
		this.emailTemplatesPath = emailTemplatesPath;
	}
	
}
