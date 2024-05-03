package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

@SpringBootTest
@ExtendWith({MockitoExtension.class})
public class S3ServiceTests {
	
	@Mock
	private S3Service s3ServiceMock;
    

    @BeforeEach
    public void setUp() {
        
    	ListObjectsResponse response = ListObjectsResponse.builder()
                .contents(S3Object.builder().key("test.json").build())
                .build();
        
        List<String> objectKeys = response.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
        
        lenient().when(s3ServiceMock.listObjects()).thenReturn(objectKeys);
    }
    
    @Test
    public void testListObjects() {

        List<String> result = s3ServiceMock.listObjects();

        assertEquals(1, result.size());
        assertEquals("test.json", result.get(0));
    }
    
    @Test
    public void testUploadObject() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test data".getBytes());
        s3ServiceMock.uploadObject(file);
        
    }
    
    @Test
    public void testUpdateObject() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Updated data".getBytes());
        s3ServiceMock.updateObject("test.json", file);
        
    }
    
    @Test
    public void testDeleteObject() {
        s3ServiceMock.deleteObject("test.json");
        
    }
    
    @Test
    public void testListObjects_ExceptionHandling() {
    	
    	when(s3ServiceMock.listObjects()).thenThrow(new RuntimeException("S3 Service Exception"));
    	
    	try {
    		s3ServiceMock.listObjects();
    		
    		fail("Expected RuntimeException was not thrown");
    	} catch (RuntimeException e) {
    		assertEquals("S3 Service Exception", e.getMessage());
    	}
    }
}
