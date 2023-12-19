package com.mycompany.myapp.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidateCliente {

    @Autowired
    private HttpRequesties httpRequesties;

    @Value("${spring.catedra.tokencito}")
    private String token;

    public Integer validateCliente(Integer cliente) {
        System.out.println("Cliente ID desde ordencheck: " + cliente);
        String url = "http://192.168.194.254:8000/api/clientes";
        ResponseEntity<List<Map<String, Object>>> response = httpRequesties.getRequest(url, token);
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Map<String, Object>> clientes = response.getBody();

            // Verificar si la lista de clientes no es nula y no está vacía
            if (clientes != null && !clientes.isEmpty()) {
                for (Map<String, Object> clienteMap : clientes) {
                    Object idValue = clienteMap.get("id");

                    if (idValue != null && idValue instanceof Number && ((Number) idValue).longValue() == cliente) {
                        System.out.println("Cliente encontrado con ID: " + cliente);
                        return cliente;
                    }
                }

                System.out.println("Cliente no encontrado con ID: " + cliente);
                return null;
            } else {
                System.out.println("La lista de clientes está vacía o es nula en la respuesta.");
                return null;
            }
        } else {
            System.out.println("La solicitud no fue exitosa. Código de estado: " + response.getStatusCodeValue());
            return null;
        }
    }
}
