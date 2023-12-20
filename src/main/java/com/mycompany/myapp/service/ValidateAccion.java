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
public class ValidateAccion {

    private final Logger log = LoggerFactory.getLogger(ValidateAccion.class);

    private final HttpRequesties httpRequesties;
    private final String token;

    @Autowired
    public ValidateAccion(HttpRequesties httpRequesties, @Value("${spring.catedra.tokencito}") String token) {
        this.httpRequesties = httpRequesties;
        this.token = token;
    }

    public Integer validateAccion(String accion) {
        log.info("Iniciando validateAccion para la acción: {}", accion);
        String baseUrl = "http://192.168.194.254:8000/api/acciones/buscar";
        String url = baseUrl + "?codigo=" + accion;
        log.info("Construyendo la URL para la acción: {}", url);

        ResponseEntity<Object> response = httpRequesties.getRequest(url, token);
        log.info("Obtenida la respuesta de la API para la acción: {}", accion);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

            if (responseBody.containsKey("acciones")) {
                List<Map<String, Object>> acciones = (List<Map<String, Object>>) responseBody.get("acciones");

                for (Map<String, Object> accionesMap : acciones) {
                    Object idValue = accionesMap.get("id");
                    Object codigo = accionesMap.get("codigo");

                    if (idValue != null && idValue instanceof Number && codigo.equals(accion)) {
                        log.info("Acción encontrada con ID: {}", idValue);
                        return (int) ((Number) idValue).longValue();
                    }
                }

                log.info("No se encontró la acción con el código: {}", accion);
                return null;
            } else {
                log.info("La respuesta no contiene la clave 'acciones'. Procesar según sea necesario.");
                return null;
            }
        } else {
            log.info("La solicitud no fue exitosa. Código de estado: {}", response.getStatusCodeValue());
            return null;
        }
    }
}
