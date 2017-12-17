package com.salesmanager.shop.populator.order;


import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.store.controller.customer.VendorResponse;

public class VendorPopulator extends
AbstractDataPopulator<Customer, VendorResponse> {

	private CustomerService customerService;

	/**
	 * Converts a Customer to an VendorResponse
	 */
	@Override
	public VendorResponse populate(Customer source, VendorResponse target,
			MerchantStore store, Language language) throws ConversionException {
		target.setVendorId(source.getId());
		target.setEmail(source.getEmailAddress());
		target.setVendorName(source.getVendorAttrs().getVendorName());
		target.setVendorOfficeAddress(source.getVendorAttrs().getVendorOfficeAddress());
		target.setVendorMobile(source.getVendorAttrs().getVendorMobile());
		target.setVendorTelephone(source.getVendorAttrs().getVendorTelephone());
		target.setVendorFax(source.getVendorAttrs().getVendorFax());
		target.setVendorConstFirm(source.getVendorAttrs().getVendorConstFirm());
		target.setVendorCompanyNature(source.getVendorAttrs().getVendorCompanyNature());
		target.setVendorRegistrationNo(source.getVendorAttrs().getVendorRegistrationNo());
		target.setVendorPAN(source.getVendorAttrs().getVendorPAN());
		target.setVendorLicense(source.getVendorAttrs().getVendorLicense());
		target.setVendorExpLine(source.getVendorAttrs().getVendorExpLine());
		target.setVendorMajorCust(source.getVendorAttrs().getVendorMajorCust());
		target.setVatRegNo(source.getVendorAttrs().getVendorVatRegNo());
		target.setVendorTIN(source.getVendorAttrs().getVendorTinNumber());
		target.setVendorImageURL(source.getUserProfile());
		target.setAuthCertURL(source.getVendorAttrs().getVendorAuthCert());
		return target;
	}

	@Override
	protected VendorResponse createTarget() {
		return null;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

}
