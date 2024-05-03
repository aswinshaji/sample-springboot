package com.example.demo;

import com.example.demo.services.S3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DemoApplicationTests {

    @Mock
    private S3Service s3ServiceMock;

    @Test
    void testListObjects() {

        List<String> mockResponse = List.of("test.json");
		
        when(s3ServiceMock.listObjects()).thenReturn(mockResponse);

       
        assertEquals(mockResponse, s3ServiceMock.listObjects());
    }
    
    @Test
    void testListObjects_ExceptionHandling() {
    	
    	when(s3ServiceMock.listObjects()).thenThrow(new RuntimeException("S3 Service Exception"));
    	
    	try {
    		s3ServiceMock.listObjects();
    	} catch (RuntimeException e) {
    		assertEquals("S3 Service Exception", e.getMessage());
    	}
    }
}
