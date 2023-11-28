package com.mycompany.myapp.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrdenCheck {

    public void validateOrden(ResponseEntity<Map<String, List<Map<String, Object>>>> response) {
        System.out.println("TYPEEEEEEEEEEEEEEEE" + response.getBody().getClass());

        // Extract the list of maps from the response body
        List<Map<String, Object>> acciones = response.getBody().get("acciones");

        // Iterate over the list of maps
        for (Map<String, Object> accion : acciones) {
            // Extract values from each map
            Object id = accion.get("id");
            Object codigo = accion.get("codigo");
            Object empresa = accion.get("empresa");

            // Do something with the values
            System.out.println("id: " + id + ", codigo: " + codigo + ", empresa: " + empresa);
        }
    }
}
