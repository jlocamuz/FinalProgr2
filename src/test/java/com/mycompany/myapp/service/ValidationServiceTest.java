package com.mycompany.myapp.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import com.mycompany.myapp.domain.Orden;
import com.mycompany.myapp.domain.enumeration.Modo;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ValidationServiceTest {

    @InjectMocks
    private ValidationService validationService;

    @Mock
    private ValidateAccion mockValidateAccion;

    @Mock
    private ValidateCliente mockValidateCliente;

    @Mock
    private ValidateFecha mockValidateFecha;

    @Mock
    private ValidateCantidad mockValidateCantidad;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateOrdenValid() {
        // Mocking validation responses
        when(mockValidateAccion.validateAccion("AAPL")).thenReturn(1);
        when(mockValidateCliente.validateCliente(1102)).thenReturn(1102);
        when(mockValidateFecha.validarFecha(any(), any())).thenReturn(true);
        when(mockValidateCantidad.validateCantidad(any(), anyInt(), anyInt(), anyInt())).thenReturn(true);

        // Create a sample order
        Orden validOrder = new Orden();
        validOrder.setCliente(1102);
        validOrder.setAccionId(1);
        validOrder.setAccion("AAPL");
        validOrder.setOperacion("VENTA");
        validOrder.setPrecio(118.23);
        validOrder.setCantidad(10);
        validOrder.setModo(Modo.FINDIA);
        validOrder.setFechaOperacion(ZonedDateTime.now());

        // Call the method under test
        boolean result = validationService.validateOrden(validOrder);

        // Validate the result
        assertTrue(result);
    }

    @Test
    void testValidateOrdenInvalid() {
        // Mocking validation responses
        when(mockValidateAccion.validateAccion("AAPLx")).thenReturn(null);
        when(mockValidateCliente.validateCliente(1102)).thenReturn(1102);
        when(mockValidateFecha.validarFecha(any(), any())).thenReturn(false);
        when(mockValidateCantidad.validateCantidad(any(), anyInt(), anyInt(), anyInt())).thenReturn(true);

        // Create a sample order
        Orden invalidOrder = new Orden();
        invalidOrder.setCliente(1102);
        invalidOrder.setAccionId(1);
        invalidOrder.setAccion("AAPLx");
        invalidOrder.setOperacion("VENTA");
        invalidOrder.setPrecio(118.23);
        invalidOrder.setCantidad(10);
        invalidOrder.setModo(Modo.FINDIA);
        invalidOrder.setFechaOperacion(ZonedDateTime.now());

        // Call the method under test
        boolean result = validationService.validateOrden(invalidOrder);

        // Validate the result
        assertFalse(result);
    }
}
