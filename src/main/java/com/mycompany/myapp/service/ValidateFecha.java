package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Orden;
import com.mycompany.myapp.domain.enumeration.Modo;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ValidateFecha {

    private static final LocalTime HORA_INICIO = LocalTime.of(9, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(18, 0);

    private final Logger log = LoggerFactory.getLogger(ValidateFecha.class);

    public boolean validarFecha(Modo modo, ZonedDateTime fecha) {
        log.info("Iniciando validarFecha");

        if (Modo.AHORA.equals(modo)) {
            ZonedDateTime ahoraEnArgentina = ZonedDateTime.now();
            ZonedDateTime fechaOperacion = fecha;

            boolean esHoyEntre9y6 =
                fechaOperacion.toLocalDate().isEqual(ahoraEnArgentina.toLocalDate()) &&
                fechaOperacion.toLocalTime().isAfter(HORA_INICIO) &&
                fechaOperacion.toLocalTime().isBefore(HORA_FIN);

            log.info("Modo AHORA. Fecha de operación: {}. ¿Está hoy entre las 9 y las 18? {}", fechaOperacion, esHoyEntre9y6);

            return esHoyEntre9y6;
        }

        log.info("Modo distinto de AHORA. Se permite la operación en cualquier fecha.");
        return true;
    }
}
