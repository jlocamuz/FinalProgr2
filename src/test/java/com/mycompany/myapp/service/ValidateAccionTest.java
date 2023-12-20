package com.mycompany.myapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.service.HttpRequesties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ValidateAccionTest {

    @InjectMocks
    private ValidateAccion validateAccion;

    @Mock
    private HttpRequesties mockHttpRequesties;

    // Configuración común
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        String baseUrl = "http://192.168.194.254:8000/api/acciones/buscar";
        ResponseEntity<Object> successfulResponse = new ResponseEntity<>(getSampleResponse(), HttpStatus.OK);

        // Ensure that the getRequest method returns a non-null response for the specified URL and token
        when(mockHttpRequesties.getRequest(baseUrl + "?codigo=PAM", "mockToken")).thenReturn(successfulResponse);
        when(mockHttpRequesties.getRequest(baseUrl + "?codigo=AAPLx", "mockToken")).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        // Add this line to ensure that the getRequest method returns a non-null response for other cases as well
        when(mockHttpRequesties.getRequest(baseUrl + "?codigo=valid", "mockToken")).thenReturn(new ResponseEntity<>(HttpStatus.OK));
    }

    @Test
    void testValidateAccionFound() {
        // Configurar el comportamiento simulado del servicio httpRequesties
        String accion = "AAPL";
        String baseUrl = "http://192.168.194.254:8000/api/acciones/buscar";
        String url = baseUrl + "?codigo=" + accion;
        Map<String, Object> responseBody = getSampleResponse();
        ResponseEntity<Object> successfulResponse = new ResponseEntity<>(responseBody, HttpStatus.OK);

        // Configurar el comportamiento de mockHttpRequesties
        when(mockHttpRequesties.getRequest(url, "mockToken")).thenReturn(successfulResponse);

        // Llamar al método bajo prueba
        Integer result = validateAccion.validateAccion(accion);

        // Verificar el resultado
        assertEquals(1, result.intValue());
    }

    @Test
    void testValidateAccionNotFound() {
        // Configurar el comportamiento simulado del servicio httpRequesties
        String accion = "InvalidCode";
        String baseUrl = "http://192.168.194.254:8000/api/acciones/buscar";
        String url = baseUrl + "?codigo=" + accion;

        // Configurar el comportamiento de mockHttpRequesties para devolver una respuesta vacía
        when(mockHttpRequesties.getRequest(url, "mockToken")).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        // Llamar al método bajo prueba
        Integer result = validateAccion.validateAccion(accion);

        // Verificar el resultado
        assertNull(result);
    }

    @Test
    void testValidateAccionEmptyResponse() {
        // Configurar el comportamiento simulado del servicio httpRequesties
        String accion = "AAPL";
        String baseUrl = "http://192.168.194.254:8000/api/acciones/buscar";
        String url = baseUrl + "?codigo=" + accion;

        // Configurar el comportamiento de mockHttpRequesties para devolver una respuesta vacía
        when(mockHttpRequesties.getRequest(url, "mockToken")).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Llamar al método bajo prueba
        Integer result = validateAccion.validateAccion(accion);

        // Verificar el resultado
        assertNull(result);
    }

    private Map<String, Object> getSampleResponse() {
        Map<String, Object> sampleResponse = new HashMap<>();
        List<Map<String, Object>> accionesList = new ArrayList<>();

        accionesList.add(createAccionesMap(1, "AAPL", "Apple Inc."));
        accionesList.add(createAccionesMap(2, "GOOGL", "Alphabet Inc. (google)"));

        sampleResponse.put("acciones", accionesList);
        return sampleResponse;
    }

    private Map<String, Object> createAccionesMap(int id, String codigo, String empresa) {
        Map<String, Object> accionesMap = new HashMap<>();
        accionesMap.put("id", id);
        accionesMap.put("codigo", codigo);
        accionesMap.put("empresa", empresa);
        return accionesMap;
    }
}
