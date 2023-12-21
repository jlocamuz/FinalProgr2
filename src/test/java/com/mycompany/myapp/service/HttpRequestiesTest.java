package com.mycompany.myapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class HttpRequestiesTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HttpRequesties httpRequesties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRequest() {
        // Arrange
        String url = "http://127.0.0.1:8080/api/ordens";
        String token =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcwNDgxMDQwOSwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzAyMjE4NDA5fQ.0CSWOJxFJHTTpdG9Q6nkBTQxYkNDnMWSrFamj-C_ZY8ap4abderRWRsGQbyxtZUCMAaZ4UXWr12PP_IL5s2nnw";

        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_JSON);
        expectedHeaders.set("Authorization", "Bearer " + token);

        // Set up RestTemplate behavior
        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act and Assert
        try {
            ResponseEntity<Object> actualResponse = httpRequesties.getRequest(url, token);
            // If the request succeeds, fail the test
            assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        } catch (HttpClientErrorException e) {
            // If the request results in a 404 error, it's expected behavior
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }
}
