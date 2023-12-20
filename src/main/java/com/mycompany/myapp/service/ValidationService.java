package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Orden;
import com.mycompany.myapp.domain.enumeration.Modo;
import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    @Autowired
    private ValidateAccion validateAccionInj;

    @Autowired
    private ValidateCliente validateClienteInj;

    @Autowired
    private ValidateFecha validateFechaInj;

    @Autowired
    private ValidateCantidad validateCantidadInj;

    public boolean validateOrden(Orden orden) {
        // Validar y obtener IDs
        String operacion = orden.getOperacion();
        String accion = orden.getAccion();
        Integer cliente = orden.getCliente();
        Integer cantidad = orden.getCantidad();
        Modo modo = orden.getModo();
        ZonedDateTime fecha = orden.getFechaOperacion();

        Integer accionId = validateAccionInj.validateAccion(accion); // int
        Integer clienteId = validateClienteInj.validateCliente(cliente); // int
        //Boolean fechaBoolean = validateFechaInj.validarFecha(modo, fecha);
        Boolean fechaBoolean = true;
        return (
            clienteId != null &&
            accionId != null &&
            fechaBoolean &&
            validateCantidadInj.validateCantidad(operacion, clienteId, accionId, cantidad)
        );
    }
}
