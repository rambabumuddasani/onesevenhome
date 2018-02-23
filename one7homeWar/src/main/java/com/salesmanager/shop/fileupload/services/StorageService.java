package com.salesmanager.shop.fileupload.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	
		void init();	

	    String store(MultipartFile file);

	    String store(MultipartFile file,String type);

	    Stream<Path> loadAll();

	    Path load(String filename);

	    Resource loadAsResource(String filename);

	    void deleteAll();
	    
	    void deleteFile(String fileName);

		String customFileStore(MultipartFile file, String dirPath);
}
