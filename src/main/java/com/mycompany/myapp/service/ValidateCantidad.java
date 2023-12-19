package com.mycompany.myapp.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidateCantidad {

    @Autowired
    private HttpRequesties httpRequesties;

    @Value("${spring.catedra.tokencito}")
    private String token;

    // Retorna true si la cantidad es suficiente, false en caso contrario
    public Boolean validateCantidad(String operacion, Integer clienteId, Integer accionId, Integer cantidad) {
        if ("COMPRA".equals(operacion)) {
            return true;
        } else {
            String url =
                "http://192.168.194.254:8000/api/reporte-operaciones/consulta_cliente_accion?clienteId=" +
                clienteId +
                "&accionId=" +
                accionId;

            ResponseEntity<Map<String, Object>> response = httpRequesties.getRequest(url, token);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                Object cantidadActualObj = responseBody.get("cantidadActual");
                if (cantidadActualObj == null) {
                    System.out.println("cantidadActual: null");
                    return false;
                } else if (cantidadActualObj instanceof Integer) {
                    Integer cantidadActual = (Integer) cantidadActualObj;
                    System.out.println("tiene " + cantidadActual + " y quiere vender " + cantidad);
                    if (cantidadActual < cantidad) {
                        return false;
                    }
                    return true;
                }
            } else {
                System.out.println("La solicitud no fue exitosa. Código de estado: " + response.getStatusCodeValue());
                return false;
            }
        }
        return false;
    }
}
