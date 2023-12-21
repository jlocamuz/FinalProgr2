package com.mycompany.myapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HttpRequesties {

    private final Logger log = LoggerFactory.getLogger(HttpRequesties.class);

    public ResponseEntity<Object> getRequest(String url, String token) {
        log.info("get {}", url);
        RestTemplate restTemplate = new RestTemplate();

        // Construir la URL de manera segura con UriComponentsBuilder
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        // Cambiar el tipo de retorno a ResponseEntity<Object>
        ResponseEntity<Object> responseEntity = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, entity, Object.class);

        // Obtener el código de estado HTTP
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        log.info("status {}", statusCode);

        return responseEntity;
    }
}
