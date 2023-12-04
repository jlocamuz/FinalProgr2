package com.mycompany.myapp.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.service.HttpRequesties;

import java.util.Map;

@Service
public class ValidateCantidad {

    @Autowired
    private HttpRequesties httpRequesties;

    // Retorna true si la cantidad es suficiente, false en caso contrario
    public Boolean validateCantidad(String operacion, Long clienteId, Long accionId, Integer cantidad) {

        if(operacion == "COMPRA"){
            return true;
        }else{
            String url = "http://192.168.194.254:8000/api/reporte-operaciones/consulta_cliente_accion?clienteId=" + clienteId + "&accionId=" + accionId;

            ResponseEntity<Map<String, Object>> response = httpRequesties.getRequest(url);
    
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                    Object cantidadActualObj = responseBody.get("cantidadActual");
                System.out.println("cantidadActual: " + cantidadActualObj);
                if(cantidadActualObj == null){
                    System.out.println("cantidadActual: null");
                    return false;
                }else if(cantidadActualObj instanceof Integer){
                    System.out.println("its and integer!" + cantidadActualObj);
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
