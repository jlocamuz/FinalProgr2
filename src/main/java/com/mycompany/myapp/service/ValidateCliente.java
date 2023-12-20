package com.mycompany.myapp.service;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidateCliente {

    private final Logger log = LoggerFactory.getLogger(ValidateCliente.class);

    @Autowired
    private HttpRequesties httpRequesties;

    @Value("${spring.catedra.tokencito}")
    private String token;

    public Integer validateCliente(Integer cliente) {
        log.info("Iniciando validateCliente para el cliente: {}", cliente);

        String url = "http://192.168.194.254:8000/api/clientes";
        log.info("Construyendo la URL para la consulta de clientes: {}", url);

        ResponseEntity<Object> response = httpRequesties.getRequest(url, token);
        log.info("Obtenida la respuesta de la API para la consulta de clientes.");

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Map<String, Object>> clientes = (List<Map<String, Object>>) response.getBody();

            // Verificar si la lista de clientes no es nula y no está vacía
            if (clientes != null && !clientes.isEmpty()) {
                for (Map<String, Object> clienteMap : clientes) {
                    Object idValue = clienteMap.get("id");

                    if (idValue != null && idValue instanceof Number && ((Number) idValue).longValue() == cliente) {
                        log.info("Cliente validado exitosamente. ID del cliente: {}", cliente);
                        return cliente;
                    }
                }

                log.info("Cliente no encontrado en la lista.");
                return null;
            } else {
                log.info("La lista de clientes está vacía.");
                return null;
            }
        } else {
            log.info("La solicitud no fue exitosa. Código de estado: {}", response.getStatusCodeValue());
            return null;
        }
    }
}
