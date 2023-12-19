package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Orden;
import com.mycompany.myapp.domain.enumeration.Modo;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ValidateFecha {

    private static final LocalTime HORA_INICIO = LocalTime.of(9, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(18, 0);

    public boolean validarFechaYModo(Orden orden) {
        ZonedDateTime ahoraEnArgentina = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

        switch (orden.getModo()) {
            case AHORA:
                return esHoyEntre9y6(orden.getFechaOperacion(), ahoraEnArgentina);
            case FINDIA:
                orden.setFechaOperacion(ajustarHora(ahoraEnArgentina, 18, 0));
                return true;
            case PRINCIPIODIA:
                ZonedDateTime manana = ahoraEnArgentina
                    .with(TemporalAdjusters.next(DayOfWeek.from(ahoraEnArgentina.getDayOfWeek())))
                    .with(LocalTime.of(9, 0));
                orden.setFechaOperacion(manana);
                return true;
            default:
                // Otros modos, no realizar cambios en la fechaOperacion
                return false;
        }
    }

    private boolean esHoyEntre9y6(ZonedDateTime fechaOperacion, ZonedDateTime ahora) {
        return (
            fechaOperacion.toLocalDate().isEqual(ahora.toLocalDate()) &&
            fechaOperacion.toLocalTime().isAfter(HORA_INICIO) &&
            fechaOperacion.toLocalTime().isBefore(HORA_FIN)
        );
    }

    private ZonedDateTime ajustarHora(ZonedDateTime fechaOperacion, int hora, int minuto) {
        return fechaOperacion.with(LocalTime.of(hora, minuto));
    }
}
