package com.salesmanager.shop.populator.customer;

import org.apache.commons.lang.StringUtils;

import com.salesmanager.core.business.exception.ConversionException;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.core.model.common.SecondaryDelivery;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.customer.Address;

public class PersistableCustomerSecondaryShippingAddressPopulator extends AbstractDataPopulator<Address, Customer> {

    @Override
    public Customer populate( Address source, Customer target, MerchantStore store, Language language )
        throws ConversionException
    {
          if( target.getSecondaryDelivery() == null){
              SecondaryDelivery delivery=new SecondaryDelivery();
              delivery.setFirstName( source.getFirstName()) ;
              delivery.setLastName( source.getLastName() );
              
              if(StringUtils.isNotBlank( source.getAddress())){
                  delivery.setAddress( source.getAddress() ); 
              }
              
              if(StringUtils.isNotBlank( source.getCity())){
                  delivery.setCity( source.getCity() );
              }
              
              if(StringUtils.isNotBlank( source.getCompany())){
                  delivery.setCompany( source.getCompany() );
              }
              
              if(StringUtils.isNotBlank( source.getPhone())){
                  delivery.setTelephone( source.getPhone());
              }
              
              if(StringUtils.isNotBlank( source.getPostalCode())){
                  delivery.setPostalCode( source.getPostalCode());
              }
              
              if(StringUtils.isNotBlank( source.getStateProvince())){
                  delivery.setState(source.getStateProvince());
              }
              
              target.setSecondaryDelivery( delivery );
              
              if(StringUtils.isNotBlank( source.getArea())){
            	  target.setArea( source.getArea());
              }
          }
          else{
           target.getSecondaryDelivery().setFirstName( source.getFirstName() );
           target.getSecondaryDelivery().setLastName( source.getLastName() );
          
            // lets fill optional data now
           
           if(StringUtils.isNotBlank( source.getAddress())){
               target.getSecondaryDelivery().setAddress( source.getAddress() ); 
           }
           
           if(StringUtils.isNotBlank( source.getCity())){
               target.getSecondaryDelivery().setCity( source.getCity() );
           }
           
           if(StringUtils.isNotBlank( source.getCompany())){
               target.getSecondaryDelivery().setCompany( source.getCompany() );
           }
           
           if(StringUtils.isNotBlank( source.getPhone())){
               target.getSecondaryDelivery().setTelephone( source.getPhone());
           }
           
           if(StringUtils.isNotBlank( source.getPostalCode())){
               target.getSecondaryDelivery().setPostalCode( source.getPostalCode());
           }
           
           /*if(StringUtils.isNotBlank( source.getStateProvince())){
               target.getDelivery().setPostalCode( source.getStateProvince());
           }*/
           if(StringUtils.isNotBlank( source.getStateProvince())){
               target.getSecondaryDelivery().setState(source.getStateProvince());
           }
           if(StringUtils.isNotBlank( source.getArea())){
         	  target.setArea( source.getArea());
           }
           
          }
         return target;
    }

    @Override
    protected Customer createTarget()
    {
         return null;
    }

   

}
