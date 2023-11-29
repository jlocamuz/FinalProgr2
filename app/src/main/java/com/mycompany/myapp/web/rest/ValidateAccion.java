package com.mycompany.myapp.web.rest;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.mycompany.myapp.service.HttpRequesties;

@Service
public class ValidateAccion {

    @Autowired
    private HttpRequesties httpRequesties;
    
    public boolean validateAccion(String accion) {
        System.out.println("Accion desde ordencheck: " + accion);

        // Existence check for accion (assuming your mechanism is implemented)

        String baseUrl = "http://192.168.194.254:8000/api/acciones/buscar";
        String url = baseUrl + "?codigo=" + accion;

        ResponseEntity response = httpRequesties.getRequest(url);

        // Assuming response.getBody() returns Object
        Object responseBody = response.getBody();

        // Check if the response body is an instance of LinkedHashMap
        if (responseBody instanceof LinkedHashMap) {
            // Cast the response body to LinkedHashMap
            LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) responseBody;
            
            // Access the "acciones" key
            Object accionesObject = responseMap.get("acciones");
        
            // Check if acciones is not null before further processing
            if (accionesObject != null) {
                if (accionesObject instanceof List) {
                    List<Object> acciones = (List<Object>) accionesObject;
                    
                    // Check if acciones is not empty before further processing
                    if (!acciones.isEmpty()) {
                        System.out.println("ACCIONES: " + acciones);
                        // Return true if acciones is not empty (contains at least one object)
                        return true;
                    } else {
                        System.out.println("The 'acciones' list is empty in the response body.");
                    }
                } else {
                    System.out.println("The 'acciones' key does not contain a List in the response body.");
                }
            } else {
                System.out.println("The 'acciones' key is null in the response body.");
            }
        
            // Return false if acciones is null or empty
            return false;
        }
        
        // Return false if responseBody is not an instance of LinkedHashMap
        return false;
    }
}
