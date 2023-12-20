package com.mycompany.myapp.service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidateCantidad {

    private final Logger log = LoggerFactory.getLogger(ValidateCantidad.class);

    @Autowired
    private HttpRequesties httpRequesties;

    @Value("${spring.catedra.tokencito}")
    private String token;

    // Retorna true si la cantidad es suficiente, false en caso contrario
    public Boolean validateCantidad(String operacion, Integer clienteId, Integer accionId, Integer cantidad) {
        //log.debug("Iniciando validateCantidad para la operación: {}, clienteId: {}, accionId: {}, cantidad: {}",
        //operacion, clienteId, accionId, cantidad);
        if ("COMPRA".equals(operacion)) {
            log.info("Operación de compra. Se permite la transacción.");
            return true;
        } else {
            String url =
                "http://192.168.194.254:8000/api/reporte-operaciones/consulta_cliente_accion?clienteId=" +
                clienteId +
                "&accionId=" +
                accionId;
            log.info("Construyendo la URL para la consulta: {}", url);

            ResponseEntity<Object> response = httpRequesties.getRequest(url, token);
            log.info("Obtenida la respuesta de la API para la consulta.");

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                Object cantidadActualObj = responseBody.get("cantidadActual");

                if (cantidadActualObj == null) {
                    log.info("La respuesta no contiene la cantidadActual. No se permite la transacción.");
                    return false;
                } else if (cantidadActualObj instanceof Integer) {
                    Integer cantidadActual = (Integer) cantidadActualObj;

                    if (cantidadActual < cantidad) {
                        log.info(
                            "Cantidad insuficiente para la transacción. Cantidad actual: {}, Cantidad solicitada: {}",
                            cantidadActual,
                            cantidad
                        );
                        return false;
                    }

                    log.info(
                        "Cantidad suficiente para la transacción. Cantidad actual: {}, Cantidad solicitada: {}",
                        cantidadActual,
                        cantidad
                    );
                    return true;
                }
            } else {
                log.info("La solicitud no fue exitosa. Código de estado: {}", response.getStatusCodeValue());
                return false;
            }
        }
        return false;
    }
}
