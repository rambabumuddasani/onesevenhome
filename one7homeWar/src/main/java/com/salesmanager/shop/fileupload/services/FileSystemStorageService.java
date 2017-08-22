package com.salesmanager.shop.fileupload.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.tomcat.jni.File;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service("storageService")
public class FileSystemStorageService implements StorageService {
	
	private final Path rootLocation;

    @Inject
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file) {
    	StringBuilder filePath = new StringBuilder();
    	//filePath.append(rootLocation+"\\"); // assumption output will be /opt/imp/vendor
    	filePath.append(rootLocation+java.io.File.pathSeparator);
    	try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            filePath.append(getUniqFileName(file.getOriginalFilename())); // do we need to give orginal file name or form certificate name based on customer information.
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filePath.toString()));
            return filePath.toString();        // /opt/img/vendor/cert1.jpg
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    private String getUniqFileName(String orgFileName){
    	return Calendar.getInstance().getTimeInMillis()+"_"+orgFileName;
    }
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

	@Override
	public void deleteFile(String fileName){
		try {
			Files.delete(Paths.get(fileName)); // fileName (i.e. filePath+fileName)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("failed to delete the file");
			e.printStackTrace();
		}
	}
	

}
