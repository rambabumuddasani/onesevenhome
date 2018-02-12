/**
 *
 */
package com.salesmanager.shop.populator.shoppingCart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.configuration.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.salesmanager.core.business.utils.AbstractDataPopulator;
import com.salesmanager.core.model.catalog.product.attribute.ProductOptionDescription;
import com.salesmanager.core.model.catalog.product.attribute.ProductOptionValueDescription;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.OrderSummary;
import com.salesmanager.core.model.order.OrderTotalSummary;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.model.order.OrderTotal;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartAttribute;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartData;
import com.salesmanager.shop.model.shoppingcart.ShoppingCartItem;
import com.salesmanager.shop.populator.order.VendorPopulator;
import com.salesmanager.shop.store.controller.customer.VendorResponse;
import com.salesmanager.shop.utils.ImageFilePath;

public class ShoppingCartDataPopulator extends AbstractDataPopulator<ShoppingCart,ShoppingCartData> {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartDataPopulator.class);

    private PricingService pricingService;

    private  ShoppingCartCalculationService shoppingCartCalculationService;
    
    private ImageFilePath imageUtils;

	private CustomerService customerService;
    
	public ImageFilePath getimageUtils() {
		return imageUtils;
	}

	public void setimageUtils(ImageFilePath imageUtils) {
		this.imageUtils = imageUtils;
	}

    @Override
    public ShoppingCartData createTarget() {
        return new ShoppingCartData();
    }

    public ShoppingCartCalculationService getOrderService() {
        return shoppingCartCalculationService;
    }

    public PricingService getPricingService() {
        return pricingService;
    }

    @Override
    public ShoppingCartData populate(final ShoppingCart shoppingCart,
                                     final ShoppingCartData cart, final MerchantStore store, final Language language) {
    	int cartQuantity = 0;
        cart.setCode(shoppingCart.getShoppingCartCode());
        Set<com.salesmanager.core.model.shoppingcart.ShoppingCartItem> items = shoppingCart.getLineItems();
        List<ShoppingCartItem> shoppingCartItemsList=Collections.emptyList();
        try{
            if(items!=null) {
                shoppingCartItemsList=new ArrayList<ShoppingCartItem>();
                for(com.salesmanager.core.model.shoppingcart.ShoppingCartItem item : items) {

                    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                    shoppingCartItem.setCode(cart.getCode());
                    
                    if(item.getVendorId() != null && !StringUtils.isEmpty(item.getVendorId())){
	                    Customer vendor = customerService.getById(item.getVendorId());
						VendorResponse vendorResponse = new VendorResponse();
						VendorPopulator vendorPopulator  = new VendorPopulator();
						vendorPopulator.populate(vendor, vendorResponse, null, null);
						shoppingCartItem.setVendorDetails(vendorResponse);
                    }
                    if(StringUtils.isEmpty(item.getProductCategory()) || "DEFAULT".equals(item.getProductCategory()) ){
    					cartQuantity = productTypeCartItem(store, cartQuantity, item, shoppingCartItem);                    	
                    }else if(Constants.WALLPAPER_PORTFOLIO.equals(item.getProductCategory())){
                    	cartQuantity = wallpaperTypeCartItem(store, cartQuantity, item, shoppingCartItem);
                       	shoppingCartItem.setCategory(Constants.WALLPAPER_PORTFOLIO);
                    }
                    shoppingCartItemsList.add(shoppingCartItem);
                }
            }
            if(CollectionUtils.isNotEmpty(shoppingCartItemsList)){
                cart.setShoppingCartItems(shoppingCartItemsList);
            }

            OrderSummary summary = new OrderSummary();
            List<com.salesmanager.core.model.shoppingcart.ShoppingCartItem> productsList = new ArrayList<com.salesmanager.core.model.shoppingcart.ShoppingCartItem>();
            productsList.addAll(shoppingCart.getLineItems());
            summary.setProducts(productsList);
            OrderTotalSummary orderSummary = shoppingCartCalculationService.calculate(shoppingCart,store, language );

            if(CollectionUtils.isNotEmpty(orderSummary.getTotals())) {
            	List<OrderTotal> totals = new ArrayList<OrderTotal>();
            	for(com.salesmanager.core.model.order.OrderTotal t : orderSummary.getTotals()) {
            		OrderTotal total = new OrderTotal();
            		total.setCode(t.getOrderTotalCode());
            		total.setValue(t.getValue());
            		totals.add(total);
            	}
            	cart.setTotals(totals);
            }
            
            cart.setSubTotal(pricingService.getDisplayAmount(orderSummary.getSubTotal(), store));
            cart.setTotal(pricingService.getDisplayAmount(orderSummary.getTotal(), store));
            cart.setQuantity(cartQuantity);
            cart.setDistinctItemQty(cart.getShoppingCartItems().size());
            cart.setTotalDiscount(pricingService.getDisplayAmount(orderSummary.getTotalDiscount(), store));
            cart.setId(shoppingCart.getId());
        }
        catch(ServiceException ex){
            LOG.error( "Error while converting cart Model to cart Data.."+ex );
            throw new ConversionException( "Unable to create cart data", ex );
        } catch (com.salesmanager.core.business.exception.ConversionException ex) {
            throw new ConversionException( "Unable to create cart data", ex );		}
        return cart;	


    }

	private int wallpaperTypeCartItem(final MerchantStore store, int cartQuantity,
			com.salesmanager.core.model.shoppingcart.ShoppingCartItem item, ShoppingCartItem shoppingCartItem)
			throws ServiceException {
		shoppingCartItem.setProductCode(item.getWallPaperPortfolio().getPortfolioName());
		shoppingCartItem.setProductVirtual(item.isProductVirtual())	;
		shoppingCartItem.setProductId(item.getProductId());
		shoppingCartItem.setId(item.getId());
		shoppingCartItem.setName(item.getWallPaperPortfolio().getPortfolioName());
		shoppingCartItem.setPrice(pricingService.getDisplayAmount(item.getItemPrice(),store));
		shoppingCartItem.setQuantity(item.getQuantity());
		cartQuantity = cartQuantity + 1; // considering each wallpaper size is 1
		shoppingCartItem.setProductPrice(item.getItemPrice());
		shoppingCartItem.setSubTotal(pricingService.getDisplayAmount(item.getSubTotal(), store));
		if(item.getFinalPrice().isDiscounted()){
			shoppingCartItem.setDiscountPrice(pricingService.getDisplayAmount(item.getFinalPrice().getDiscountedPrice(),store));
		}
			String imagePath = item.getWallPaperPortfolio().getImageURL();
		    shoppingCartItem.setImage(imagePath);
		    return cartQuantity;
	}
    
	private int productTypeCartItem(final MerchantStore store, int cartQuantity,
			com.salesmanager.core.model.shoppingcart.ShoppingCartItem item, ShoppingCartItem shoppingCartItem)
			throws ServiceException {
		shoppingCartItem.setProductCode(item.getProduct().getSku());
		shoppingCartItem.setProductVirtual(item.isProductVirtual())	;
		shoppingCartItem.setProductId(item.getProductId());
		shoppingCartItem.setId(item.getId());
		shoppingCartItem.setName(item.getProduct().getProductDescription().getName());
		shoppingCartItem.setPrice(pricingService.getDisplayAmount(item.getItemPrice(),store));
		shoppingCartItem.setQuantity(item.getQuantity());
		
		/*int itemQty = item.getQuantity().intValue();
		int itemPrice = item.getItemPrice().intValue();
		shoppingCartItem.setTotalPriceOfEachItem(itemQty*itemPrice);
		cartQuantity = cartQuantity + item.getQuantity();*/
		//shoppingCartItem.setTotalPriceOfEachItem(itemQty*itemPrice);
		cartQuantity = cartQuantity + item.getQuantity();
		shoppingCartItem.setProductPrice(item.getItemPrice());
		shoppingCartItem.setSubTotal(pricingService.getDisplayAmount(item.getSubTotal(), store));
		if(item.getFinalPrice().isDiscounted()){
			shoppingCartItem.setDiscountPrice(pricingService.getDisplayAmount(item.getFinalPrice().getDiscountedPrice(),store));
		}
		ProductImage image = item.getProduct().getProductImage();
		if(image!=null && imageUtils!=null) {
		    //String imagePath = imageUtils.buildProductImageUtils(store, item.getProduct().getSku(), image.getProductImage());
			String imagePath = image.getProductImageUrl();
		    shoppingCartItem.setImage(imagePath);
		}
		Set<com.salesmanager.core.model.shoppingcart.ShoppingCartAttributeItem> attributes = item.getAttributes();
		if(attributes!=null) {
		    List<ShoppingCartAttribute> cartAttributes = new ArrayList<ShoppingCartAttribute>();
		    for(com.salesmanager.core.model.shoppingcart.ShoppingCartAttributeItem attribute : attributes) {
		        ShoppingCartAttribute cartAttribute = new ShoppingCartAttribute();
		        cartAttribute.setId(attribute.getId());
		        cartAttribute.setAttributeId(attribute.getProductAttributeId());
		        cartAttribute.setOptionId(attribute.getProductAttribute().getProductOption().getId());
		        cartAttribute.setOptionValueId(attribute.getProductAttribute().getProductOptionValue().getId());
		        List<ProductOptionDescription> optionDescriptions = attribute.getProductAttribute().getProductOption().getDescriptionsSettoList();
		        List<ProductOptionValueDescription> optionValueDescriptions = attribute.getProductAttribute().getProductOptionValue().getDescriptionsSettoList();
		        if(!CollectionUtils.isEmpty(optionDescriptions) && !CollectionUtils.isEmpty(optionValueDescriptions)) {
		        	cartAttribute.setOptionName(optionDescriptions.get(0).getName());
		        	cartAttribute.setOptionValue(optionValueDescriptions.get(0).getName());
		        	cartAttributes.add(cartAttribute);
		        }
		    }
		    shoppingCartItem.setShoppingCartAttributes(cartAttributes);
		}
		return cartQuantity;
	}





    public void setPricingService(final PricingService pricingService) {
        this.pricingService = pricingService;
    }






    public void setShoppingCartCalculationService(final ShoppingCartCalculationService shoppingCartCalculationService) {
        this.shoppingCartCalculationService = shoppingCartCalculationService;
    }

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}




}
