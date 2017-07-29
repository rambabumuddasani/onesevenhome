package com.salesmanager.shop.fileupload.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@ConfigurationProperties("storage")
@Service
public class StorageProperties {
	
	/**
     * Folder location for storing files
     */
   // private String location = "upload-dir";
	private String location = "D:\\";
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
