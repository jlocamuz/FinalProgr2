package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.service.HttpRequesties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ValidateCliente {

    @Autowired
    private HttpRequesties httpRequesties;

    public boolean validateCliente(Long cliente) {
        System.out.println("Cliente ID desde ordencheck: " + cliente);
        String url = "http://192.168.194.254:8000/api/clientes/";
        ResponseEntity<Map<String, Object>> response = httpRequesties.getRequest(url);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = response.getBody();

            if (responseBody.containsKey("clientes")) {
                // Si la respuesta contiene la clave "clientes", extraer la lista de clientes
                List<Map<String, Object>> clientes = (List<Map<String, Object>>) responseBody.get("clientes");

                for (Map<String, Object> clienteMap : clientes) {
                    Object idValue = clienteMap.get("id");

                    if (idValue != null && idValue instanceof Number && ((Number) idValue).longValue() == cliente) {
                        System.out.println("Cliente encontrado con ID: " + cliente);
                        return true;
                    }
                }

                System.out.println("Cliente no encontrado con ID: " + cliente);
                return false;
            } else {
                System.out.println("La respuesta no contiene la clave 'clientes'. Procesar según sea necesario.");
                return false;
            }
        } else {
            System.out.println("La solicitud no fue exitosa. Código de estado: " + response.getStatusCodeValue());
            return false;
        }
    }
}
