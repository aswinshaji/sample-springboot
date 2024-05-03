package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.example.demo.services.S3Service;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(S3Controller.class)
public class S3ControllerTests {

    @MockBean
    private S3Service s3ServiceMock;
    
    @InjectMocks
    private S3Controller s3ControllerMock;

    @Test
    public void testViewObjects() throws Exception {
    	List<String> mockResponse = List.of("test.json");
        when(s3ServiceMock.listObjects()).thenReturn(mockResponse);

        ResponseEntity<List<String>> responseEntity = s3ControllerMock.viewObjects();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
    }
    
    @Test
    public void testUploadObject() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test data".getBytes());

        ResponseEntity<String> responseEntity = s3ControllerMock.uploadObject(file);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Object uploaded successfully", responseEntity.getBody());
        verify(s3ServiceMock).uploadObject(file);
    }
    
    @Test
    public void testUpdateObject() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Updated data".getBytes());

        ResponseEntity<String> responseEntity = s3ControllerMock.updateObject("test.json", file);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Object updated successfully", responseEntity.getBody());
        verify(s3ServiceMock).updateObject("test.json", file);
    }
    
    @Test
    public void testDeleteObject() {
        ResponseEntity<String> responseEntity = s3ControllerMock.deleteObject("test.json");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Object deleted successfully", responseEntity.getBody());
        verify(s3ServiceMock).deleteObject("test.json");
    }
    
    @Test
    public void testViewObjects_ExceptionHandling() throws Exception {
    	
    	when(s3ServiceMock.listObjects()).thenThrow(new RuntimeException("S3 Service Exception"));

        ResponseEntity<List<String>> responseEntity = s3ControllerMock.viewObjects();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
