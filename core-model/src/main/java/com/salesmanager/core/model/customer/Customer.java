package com.salesmanager.core.model.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.catalog.category.Category;
import com.salesmanager.core.model.catalog.product.review.ProductReview;
import com.salesmanager.core.model.common.Billing;
import com.salesmanager.core.model.common.Delivery;
import com.salesmanager.core.model.common.SecondaryDelivery;
import com.salesmanager.core.model.common.VendorAttributes;
import com.salesmanager.core.model.customer.attribute.CustomerAttribute;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.product.vendor.VendorProduct;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.services.Services;
import com.salesmanager.core.model.user.Group;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table(name = "CUSTOMER", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Customer extends SalesManagerEntity<Long, Customer> {
	private static final long serialVersionUID = -6966934116557219193L;
	
	@Id
	@Column(name = "CUSTOMER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	//@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "customer")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
	private Set<CustomerAttribute> attributes = new HashSet<CustomerAttribute>();
	
	@Column(name="CUSTOMER_GENDER", length=1, nullable=true)
	@Enumerated(value = EnumType.STRING)
	private CustomerGender gender;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CUSTOMER_DOB")
	private Date dateOfBirth;
	
	@Email
	@NotEmpty
	@Column(name="CUSTOMER_EMAIL_ADDRESS", length=96, nullable=false)
	private String emailAddress;
	
	@Column(name="CUSTOMER_NICK", length=96)
	private String nick;

	@Column(name="CUSTOMER_COMPANY", length=100)
	private String company;
	

	@Column(name="CUSTOMER_PASSWORD", length=60)
	private String password;

	
	@Column(name="CUSTOMER_ANONYMOUS")
	private boolean anonymous;
	
	@Column(name="CUSTOMER_TYPE", length=2)
	private String customerType;

	@Column(name="ACTIVATED", length=2)
	private String activated;
	
	@Column(name="OFID", length=50)
	private String ofid;

	@Column (name ="USER_PROFILE", length=200)
	private String userProfile;

	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Language.class)
	@JoinColumn(name = "LANGUAGE_ID", nullable=false)
	private Language defaultLanguage;
	


	@OneToMany(mappedBy = "customer", targetEntity = ProductReview.class)
	private List<ProductReview> reviews = new ArrayList<ProductReview>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MERCHANT_ID", nullable=false)
	private MerchantStore merchantStore;
	
	@Embedded
	private Delivery delivery = null;

	@Embedded
	private SecondaryDelivery secondaryDelivery = null;
	
	@Valid
	@Embedded
	private Billing billing = null;
	
	@Embedded
	private VendorAttributes vendorAttrs = null;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "CUSTOMER_GROUP", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "CUSTOMER_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "GROUP_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private List<Group> groups = new ArrayList<Group>();
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "CUSTOMER_SERVICES", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "CUSTOMER_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private List<Services> services = new ArrayList<Services>();
	
	@ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "VENDOR_PROF_SERVICES", schema=SchemaConstant.SALESMANAGER_SCHEMA, joinColumns = { 
			@JoinColumn(name = "CUSTOMER_ID", nullable = false, updatable = false) }
			, 
			inverseJoinColumns = { @JoinColumn(name = "CATEGORY_ID", 
					nullable = false, updatable = false) }
	)
	@Cascade({
		org.hibernate.annotations.CascadeType.DETACH,
		org.hibernate.annotations.CascadeType.LOCK,
		org.hibernate.annotations.CascadeType.REFRESH,
		org.hibernate.annotations.CascadeType.REPLICATE
		
	})
	private List<Category> categories = new ArrayList<Category>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
	private Set<VendorProduct> vendorProduct = new HashSet<VendorProduct>();

	
	@Transient
	private String showCustomerStateList;
	
	@Transient
	private String showBillingStateList;
	
	@Transient
	private String showDeliveryStateList;
	
	@Column(name="CUSTOMER_AREA",length=100)
	private String area;
	
	@Column(name="IS_ADMIN", length=3)
	private String isAdmin;
	
	@Column(name="IS_SUPER_ADMIN", length=3)
	private String isSuperAdmin;
	
	@Column(name="IS_VENDOR_ACTIVATED",nullable=false)
	private String isVendorActivated="N";
	
	@Column(name="GST",length=20)
	private String gst;
	
	@Column(name="REVIEW_AVG")
	private BigDecimal avgReview;
	
	@Column (name ="FILE_1", length=200)
	private String file1;
	
	@Column (name ="FILE_2", length=200)
	private String file2;
	
	@Column (name ="FILE_3", length=200)
	private String file3;
	
	public Customer() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public Date getDateOfBirth() {
		return CloneUtils.clone(dateOfBirth);
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = CloneUtils.clone(dateOfBirth);
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}


	public List<ProductReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews;
	}

	public void setMerchantStore(MerchantStore merchantStore) {
		this.merchantStore = merchantStore;
	}

	public MerchantStore getMerchantStore() {
		return merchantStore;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}

	public Billing getBilling() {
		return billing;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}
	public String getShowCustomerStateList() {
		return showCustomerStateList;
	}

	public void setShowCustomerStateList(String showCustomerStateList) {
		this.showCustomerStateList = showCustomerStateList;
	}

	public String getShowBillingStateList() {
		return showBillingStateList;
	}

	public void setShowBillingStateList(String showBillingStateList) {
		this.showBillingStateList = showBillingStateList;
	}

	public String getShowDeliveryStateList() {
		return showDeliveryStateList;
	}

	public void setShowDeliveryStateList(String showDeliveryStateList) {
		this.showDeliveryStateList = showDeliveryStateList;
	}
	
	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(Language defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public void setAttributes(Set<CustomerAttribute> attributes) {
		this.attributes = attributes;
	}

	public Set<CustomerAttribute> getAttributes() {
		return attributes;
	}

	public void setGender(CustomerGender gender) {
		this.gender = gender;
	}

	public CustomerGender getGender() {
		return gender;
	}

	public VendorAttributes getVendorAttrs() {
		return vendorAttrs;
	}

	public void setVendorAttrs(VendorAttributes vendorAttrs) {
		this.vendorAttrs = vendorAttrs;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getActivated() {
		return activated;
	}

	public void setActivated(String activated) {
		this.activated = activated;
	}

	public String getOfid() {
		return ofid;
	}

	public void setOfid(String ofid) {
		this.ofid = ofid;
	}

	public Set<VendorProduct> getVendorProduct() {
		return vendorProduct;
	}

	public void setVendorProduct(Set<VendorProduct> vendorProduct) {
		this.vendorProduct = vendorProduct;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(String isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getIsVendorActivated() {
		return isVendorActivated;
	}

	public void setIsVendorActivated(String isVendorActivated) {
		this.isVendorActivated = isVendorActivated;
	}

	public SecondaryDelivery getSecondaryDelivery() {
		return secondaryDelivery;
	}

	public void setSecondaryDelivery(SecondaryDelivery delivery2) {
		this.secondaryDelivery = delivery2;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public List<Services> getServices() {
		return services;
	}

	public void setServices(List<Services> services) {
		this.services = services;
	}

	public String getGst() {
		return gst;
	}

	public void setGst(String gst) {
		this.gst = gst;
	}

	public BigDecimal getAvgReview() {
		return avgReview;
	}

	public void setAvgReview(BigDecimal avgReview) {
		this.avgReview = avgReview;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getFile1() {
		return file1;
	}

	public void setFile1(String file1) {
		this.file1 = file1;
	}

	public String getFile2() {
		return file2;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}

	public String getFile3() {
		return file3;
	}

	public void setFile3(String file3) {
		this.file3 = file3;
	}
	
}
