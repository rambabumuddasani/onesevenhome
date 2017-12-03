package com.salesmanager.shop.bulk.product;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.image.ProductImage;
import com.salesmanager.shop.admin.controller.products.CreateProductRequest;
import com.salesmanager.shop.admin.controller.products.CreateProductResponse;
import com.salesmanager.shop.fileupload.services.StorageException;
import com.salesmanager.shop.fileupload.services.StorageProperties;
import com.salesmanager.shop.fileupload.services.StorageService;
import com.salesmanager.shop.utils.HttpPostClient;

@Controller
@CrossOrigin
public class BulkProductInsertController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BulkProductInsertController.class);

	@Inject
	private ProductService productService;

	@Inject
	private StorageService storageService;
	
	@Inject
	private StorageProperties storageProperties;
	
	@RequestMapping(value="/bulkProductInsertion", method = RequestMethod.POST)
	@ResponseBody
	private List<String> invokeBulkProductInertion(@RequestPart("file") MultipartFile bulkProductFile
			) throws IOException, ServiceException,Exception{
		List<String> responseRroductIds = new ArrayList<String>();
		String fileName = "";
		/*	
		 * 
		 *  public List<String> invokeBulkProductInertion() throws IOException, ServiceException,Exception{
			String fileName = "F:\\onesevenhomeProj\\onesevenhome\\f.txt";
		 */	
		if(bulkProductFile.getSize() != 0) {
			try{
				fileName = storageService.store(bulkProductFile,"bulkOrderproduct");
				LOGGER.info("bulk order upload fileName "+fileName);
			}catch(StorageException se){
				LOGGER.error("Failed while storing file "+bulkProductFile.getName());
				return responseRroductIds;
			}
		}

		List<String> lines = Files.readAllLines(Paths.get(fileName));
		for(String eachLine : lines){	
			String[] eachLineToken = eachLine.split("\\|");
			CreateProductRequest createProjectRequest = new CreateProductRequest();
			/**
			 * productId, sku, description, title, shortDesc, productDescTitle, discountPrice, productName , productPrice, category, imageUrls
			 */

			createProjectRequest.setSku(eachLineToken[0].trim());
			createProjectRequest.setDescription(eachLineToken[1].trim());
			createProjectRequest.setTitle(eachLineToken[2].trim());
			createProjectRequest.setShortDesc(eachLineToken[3].trim());
			createProjectRequest.setProductDescTitle(eachLineToken[4].trim());
			createProjectRequest.setDiscountPrice(eachLineToken[5].trim());
			createProjectRequest.setProductName(eachLineToken[6].trim());
			createProjectRequest.setProductPrice(new BigDecimal(eachLineToken[7].trim()));
			createProjectRequest.setCategory(eachLineToken[8].trim());
			ObjectMapper mapper = new ObjectMapper();
			String jsonRequest = mapper.writeValueAsString(createProjectRequest);
			//String url = "http://localhost:8080/shop/createProduct";
			String url =	"http://103.211.219.128:8080/shop/createProduct";
			//HttpResponse httpResponse = HttpPostClient.invokePostRequest(url, jsonRequest);
			String responseData = HttpPostClient.invokePostRequestAndReturnResponseAsString(url, jsonRequest);
			CreateProductResponse productResponse = mapper.readValue(responseData, CreateProductResponse.class);
			if(!productResponse.isStatus()){
				responseRroductIds.add(" Product creation failed for product "+createProjectRequest.getProductName());
				continue; /* since product status is not true, which means, 
						  something happend  ,which caused product not to insert in DB. */
			}
			Long dbProductId = productResponse.getProductId();			
			responseRroductIds.add(dbProductId.toString());
			Product dbProduct = productService.getById(dbProductId);
			if(dbProduct == null){
					String errorMsg = "No product available with product id : "+dbProduct;
					LOGGER.debug(errorMsg);
					throw new ServiceException(errorMsg);
			}
			String imgURL = eachLineToken[9].trim();
			String[] imgUrlTokens = imgURL.split("&"); // some reg expression
			Set<ProductImage> images = new HashSet<ProductImage>();

			for(String eachImgUrl : imgUrlTokens){
				ProductImage productImage = new ProductImage();
				productImage.setProduct(dbProduct);
				StringBuilder img = new StringBuilder(storageProperties.getLocation())
						//.append(java.io.File.separator)
						.append("product")
						.append(java.io.File.separator)
						.append(eachImgUrl);
				productImage.setProductImageUrl(img.toString()); // set absolute path
				images.add(productImage);
			}
			images.iterator().next().setDefaultImage(true); // blindly set default image 
			//images.productImage.setDefaultImage(isDefaultImage);
			dbProduct.setImages(images);
			productService.update(dbProduct);
		}
		return responseRroductIds;
	}
}
