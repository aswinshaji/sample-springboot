package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.S3Service;

@RestController
@RequestMapping("/file")
public class S3Controller {
	
	@Autowired
	private S3Service s3Service;
	@GetMapping("/listAllObjects")
	public ResponseEntity<List<String>> viewObjects() {
        return new ResponseEntity<>(s3Service.listObjects(),HttpStatus.OK);
    }
}
