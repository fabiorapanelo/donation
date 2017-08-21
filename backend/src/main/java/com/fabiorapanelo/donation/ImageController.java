package com.fabiorapanelo.donation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    
	@SuppressWarnings("rawtypes")
	@PostMapping("/users/{userId}/upload-image")
    public ResponseEntity uploadImage(@PathVariable String userId, @RequestParam("image") MultipartFile image) throws IOException {
		
		String filename = userId + "-" + image.getOriginalFilename();
		
		File file = new File(uploadDir + filename);
        image.transferTo(file);
        
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/images/{filename}/")
				.buildAndExpand(filename).toUri();

        return ResponseEntity.created(location).build();
    }
}
