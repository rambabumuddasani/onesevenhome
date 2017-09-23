/**
 * 
 */
package com.salesmanager.shop.admin.controller;

/**
 * @author welcome
 *
 */
public class UserVO {
	
	private Long id;
	private String adminName;
	private String storeCode;
	private String email;
	private String firstName;
	private String lastName;
	//private Language defaultLang;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getStoreCode() {
		return storeCode;
	}
	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/*public Language getDefaultLang() {
		return defaultLang;
	}
	public void setDefaultLang(Language defaultLang) {
		this.defaultLang = defaultLang;
	}*/

}
