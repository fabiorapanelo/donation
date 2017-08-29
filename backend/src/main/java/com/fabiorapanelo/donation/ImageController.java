package com.fabiorapanelo.donation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {
    public static final String uploadDir = System.getProperty("user.dir") + "/images/";
	
    
    @GetMapping("/images/{filename:.+}")
    public void getImage(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
    	
    	File file = new File(uploadDir + filename);
    	InputStream in = new FileInputStream(file);
   
    	try {
    		String contentType = Files.probeContentType(file.toPath());
    		response.setContentType(contentType);
    	} catch (IOException ioException){}
    	
    	IOUtils.copy(in, response.getOutputStream());
    }
    
	@PostMapping("/users/{userId}/upload-images")
    public ResponseEntity<ImageUpload> uploadingPost(@PathVariable String userId, @RequestParam("images") MultipartFile[] images) throws IOException {
        
		ImageUpload imageUpload = new ImageUpload();
		
		List<String> filenames = new ArrayList<>();
		
		for(MultipartFile image : images) {
			
			String originalFileName = image.getOriginalFilename();
			
            String filename = userId + "-" + UUID.randomUUID() + getExtension(originalFileName);
            
    		File file = new File(uploadDir + filename);
            image.transferTo(file);
            
            filenames.add(filename);
        }
        
		imageUpload.setImages(filenames);
		
		return new ResponseEntity<ImageUpload>(imageUpload, HttpStatus.OK);
    }
	
	
	public String getExtension(String filename){
		
		int i = filename.lastIndexOf('.');
        if (i > 0) {
            return filename.substring(i);
        }
        
        return "";
	}
}
