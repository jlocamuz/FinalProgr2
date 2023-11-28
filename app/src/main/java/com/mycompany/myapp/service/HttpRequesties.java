package com.mycompany.myapp.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpRequesties {

    String getUrl = "https://dog.ceo/api/breeds/image/random";
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJidWluYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODg4OC9hcGkvdjEvc2VjdXJpdHkvbG9naW4iLCJleHAiOjE2NTc5Mjc3ODN9.i2hFeLWEBY3YwsB1llREmZXRN53YPtIGFqPUQ4oLw6Q";

    public void getRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(getUrl, HttpMethod.GET, entity, Object.class);

        // Obtener el código de estado HTTP
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        System.out.println("Código de estado HTTP: " + statusCode.value());

        // Verificar el código de estado
        if (statusCode == HttpStatus.OK) {
            // La solicitud fue exitosa (código de estado 200 OK)
            Object responseBody = responseEntity.getBody();
            System.out.println("Respuesta exitosa: " + responseBody);
        } else {
            // Manejar otros códigos de estado según sea necesario
            System.out.println("La solicitud no fue exitosa. Código de estado: " + statusCode.value());
        }
    }
}
