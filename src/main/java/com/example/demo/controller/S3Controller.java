package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.S3Service;

@RestController
@RequestMapping("/file")
public class S3Controller {
	
	@Autowired
	private S3Service s3Service;
	
	public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }
	
	@GetMapping("/listAllObjects")
	public ResponseEntity<List<String>> viewObjects() {
		try {
            List<String> objects = s3Service.listObjects();
            return ResponseEntity.ok(objects);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@PostMapping("/uploadObject")
    public ResponseEntity<String> uploadObject(@RequestParam("file") MultipartFile file) {
        try {
            s3Service.uploadObject(file);
            return ResponseEntity.ok("Object uploaded successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
	
	@PutMapping("/updateObject/{objectKey}")
    public ResponseEntity<String> updateObject(@PathVariable("objectKey") String objectKey, @RequestParam("file") MultipartFile file) {
        try {
            s3Service.updateObject(objectKey, file);
            return ResponseEntity.ok("Object updated successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

	@DeleteMapping("/deleteObject/{objectKey}")
    public ResponseEntity<String> deleteObject(@PathVariable("objectKey") String objectKey) {
        try {
            s3Service.deleteObject(objectKey);
            return ResponseEntity.ok("Object deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
