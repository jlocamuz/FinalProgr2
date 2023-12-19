package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Orden;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orden entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByProcesadaFalse();

    List<Orden> findByCliente(Integer cliente);

    List<Orden> findByAccionId(Integer accionId);

    List<Orden> findByAccion(String accion);

    List<Orden> findByOperacion(String operacion);

    List<Orden> findByCantidad(Integer cantidad);

    List<Orden> findByPrecio(Double precio);

    List<Orden> findByFechaOperacion(ZonedDateTime fechaOperacion);

    List<Orden> findByModo(String modo);

    // Add more query methods as needed
    // Additional methods
    List<Orden> findByClienteAndOperacion(Integer cliente, String modo);

    List<Orden> findByAccionIdAndOperacion(Integer accionId, String modo);

    List<Orden> findByAccionAndOperacion(String accion, String modo);

    List<Orden> findByOperacionAndOperacion(String operacion, String modo);

    List<Orden> findByCantidadAndOperacion(Integer cantidad, String modo);

    List<Orden> findByPrecioAndOperacion(Double precio, String modo);

    List<Orden> findByFechaOperacionAndOperacion(ZonedDateTime fechaOperacion, String modo);
}
