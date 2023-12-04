package com.mycompany.myapp.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.mycompany.myapp.service.HttpRequesties;

import java.util.List;
import java.util.Map;

@Service
public class ValidateAccion {

    @Autowired
    private HttpRequesties httpRequesties;

    public Long validateAccion(String accion) {
        System.out.println("Accion desde ordencheck: " + accion);

        String baseUrl = "http://192.168.194.254:8000/api/acciones/buscar";
        String url = baseUrl + "?codigo=" + accion;

        ResponseEntity<Map<String, Object>> response = httpRequesties.getRequest(url);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = response.getBody();

            if (responseBody.containsKey("acciones")) {
                // Si la respuesta contiene la clave "acciones", extraer la lista de acciones
                List<Map<String, Object>> acciones = (List<Map<String, Object>>) responseBody.get("acciones");

                for (Map<String, Object> accionesMap : acciones) {
                    Object idValue = accionesMap.get("id");
                    Object codigo = accionesMap.get("codigo");

                    if (idValue != null && idValue instanceof Number && codigo.equals(accion)) {
                        System.out.println("Acción encontrada con ID: " + idValue);
                        return ((Number) idValue).longValue();
                    }
                }

                System.out.println("Acción no encontrada con código: " + accion);
                return null;
            } else {
                System.out.println("La respuesta no contiene la clave 'acciones'. Procesar según sea necesario.");
                return null;
            }
        } else {
            System.out.println("La solicitud no fue exitosa. Código de estado: " + response.getStatusCodeValue());
            return null;
        }
    }
}
