package com.mycompany.myapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpRequesties {
    @Value("${spring.catedra.tokencito}")
    private String token;
	
    public ResponseEntity getRequest(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        // Obtener el código de estado HTTP
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        System.out.println("Código de estado HTTP: " + statusCode.value());

		return responseEntity;
    }
}
