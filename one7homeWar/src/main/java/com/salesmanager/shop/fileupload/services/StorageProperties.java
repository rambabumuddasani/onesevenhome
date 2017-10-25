package com.salesmanager.shop.fileupload.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@ConfigurationProperties("storage")
@Service
public class StorageProperties {
	
	/**
     * Folder location for storing files
     */
	//private String location = "F:\\cores\\";	// in Windows
	//@Value("uploadFileSystemPath")
	//private String location; // in UNIX 
	//private String location = "/opt/img/vendor/certificate"; // in UNIX 
	private String location = "/opt/img/"; // in UNIX 
	
	public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
