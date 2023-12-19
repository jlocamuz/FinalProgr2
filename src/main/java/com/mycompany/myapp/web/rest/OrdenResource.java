package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Orden;
import com.mycompany.myapp.repository.OrdenRepository;
import com.mycompany.myapp.service.HttpRequesties;
import com.mycompany.myapp.service.ValidateAccion;
import com.mycompany.myapp.service.ValidateCantidad;
import com.mycompany.myapp.service.ValidateCliente;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Orden}.
 */
@RestController
@RequestMapping("/api/ordens")
@Transactional
public class OrdenResource {

    @Autowired
    private HttpRequesties httpRequesties;

    @Autowired
    private ValidateAccion validateAccionInj;

    @Autowired
    private ValidateCliente validateClienteInj;

    // @Autowired
    // private ValidateFecha validateFecha;

    @Autowired
    private ValidateCantidad validateCantidadInj;

    private final Logger log = LoggerFactory.getLogger(OrdenResource.class);

    private static final String ENTITY_NAME = "orden";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdenRepository ordenRepository;

    public OrdenResource(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    // PARA ANULAR

    @GetMapping("/noprocesadas")
    @ResponseBody
    public ResponseEntity<List<Orden>> getFiltroNoProcesadas(
        @RequestParam(required = false) String modo,
        @RequestParam(required = false) Integer cliente,
        @RequestParam(required = false) Integer accionId,
        @RequestParam(required = false) String accion,
        @RequestParam(required = false) String operacion,
        @RequestParam(required = false) Integer cantidad,
        @RequestParam(required = false) Double precio,
        @RequestParam(required = false) ZonedDateTime fechaOperacion
    ) {
        // Obtén todas las órdenes no procesadas
        List<Orden> ordenes = ordenRepository.findByProcesadaFalse();

        // Aplica filtros adicionales basados en parámetros
        if (cliente != null) {
            ordenes = ordenes.stream().filter(orden -> Objects.equals(orden.getCliente(), cliente)).collect(Collectors.toList());
        }
        if (accionId != null) {
            ordenes = ordenes.stream().filter(orden -> Objects.equals(orden.getAccionId(), accionId)).collect(Collectors.toList());
        }
        if (accion != null) {
            ordenes = ordenes.stream().filter(orden -> Objects.equals(orden.getAccion(), accion)).collect(Collectors.toList());
        }
        if (operacion != null) {
            ordenes = ordenes.stream().filter(orden -> Objects.equals(orden.getOperacion(), operacion)).collect(Collectors.toList());
        }
        if (cantidad != null) {
            ordenes = ordenes.stream().filter(orden -> Objects.equals(orden.getCantidad(), cantidad)).collect(Collectors.toList());
        }
        if (precio != null) {
            ordenes = ordenes.stream().filter(orden -> Objects.equals(orden.getPrecio(), precio)).collect(Collectors.toList());
        }
        if (fechaOperacion != null) {
            ordenes =
                ordenes.stream().filter(orden -> Objects.equals(orden.getFechaOperacion(), fechaOperacion)).collect(Collectors.toList());
        }

        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    // filters. REPORTES
    @GetMapping("/buscador")
    @ResponseBody
    public ResponseEntity<List<Orden>> getOrdenByCliente(
        @RequestParam(required = false) String modo,
        @RequestParam(required = false) Integer cliente,
        @RequestParam(required = false) Integer accionId,
        @RequestParam(required = false) String accion,
        @RequestParam(required = false) String operacion,
        @RequestParam(required = false) Integer cantidad,
        @RequestParam(required = false) Double precio,
        @RequestParam(required = false) ZonedDateTime fechaOperacion
    ) {
        List<Orden> ordenes = new ArrayList<>();

        // Further filter based on other parameters if available
        if (cliente != null) {
            ordenes = ordenRepository.findByClienteAndOperacion(cliente, operacion);
        } else if (accionId != null) {
            ordenes = ordenRepository.findByAccionIdAndOperacion(accionId, operacion);
        } else if (accion != null) {
            ordenes = ordenRepository.findByAccionAndOperacion(accion, operacion);
        } else if (operacion != null) {
            ordenes = ordenRepository.findByOperacionAndOperacion(operacion, operacion);
        } else if (cantidad != null) {
            ordenes = ordenRepository.findByCantidadAndOperacion(cantidad, operacion);
        } else if (precio != null) {
            ordenes = ordenRepository.findByPrecioAndOperacion(precio, operacion);
        } else if (fechaOperacion != null) {
            ordenes = ordenRepository.findByFechaOperacionAndOperacion(fechaOperacion, operacion);
        }

        return new ResponseEntity<>(ordenes, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> createOrden(@RequestBody Orden orden) throws URISyntaxException {
        if (orden.getId() != null) {
            throw new BadRequestAlertException("A new orden cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Validar y obtener IDs
        String operacion = orden.getOperacion();
        String accion = orden.getAccion();
        Integer cliente = orden.getCliente();
        Integer cantidad = orden.getCantidad();

        Integer accionId = validateAccionInj.validateAccion(accion); // int
        Integer clienteId = validateClienteInj.validateCliente(cliente); // int
        if (clienteId != null && accionId != null && validateCantidadInj.validateCantidad(operacion, clienteId, accionId, cantidad)) {
            // Guardar la orden
            Orden result = ordenRepository.save(orden);
            log.debug("Orden creada");
            return ResponseEntity
                .created(new URI("/api/ordens/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } else {
            return ResponseEntity.badRequest().header(null).body("Accion, cliente, cantidad insuficiente o fecha no válida");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orden> updateOrden(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Orden orden)
        throws URISyntaxException {
        if (orden.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orden.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Orden result = ordenRepository.save(orden);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orden.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Orden> partialUpdateOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Orden orden
    ) throws URISyntaxException {
        if (orden.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orden.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Orden> result = ordenRepository
            .findById(orden.getId())
            .map(existingOrden -> {
                if (orden.getCliente() != null) {
                    existingOrden.setCliente(orden.getCliente());
                }
                if (orden.getAccionId() != null) {
                    existingOrden.setAccionId(orden.getAccionId());
                }
                if (orden.getAccion() != null) {
                    existingOrden.setAccion(orden.getAccion());
                }
                if (orden.getOperacion() != null) {
                    existingOrden.setOperacion(orden.getOperacion());
                }
                if (orden.getCantidad() != null) {
                    existingOrden.setCantidad(orden.getCantidad());
                }
                if (orden.getPrecio() != null) {
                    existingOrden.setPrecio(orden.getPrecio());
                }
                if (orden.getFechaOperacion() != null) {
                    existingOrden.setFechaOperacion(orden.getFechaOperacion());
                }
                if (orden.getModo() != null) {
                    existingOrden.setModo(orden.getModo());
                }
                if (orden.getProcesada() != null) {
                    existingOrden.setProcesada(orden.getProcesada());
                }

                return existingOrden;
            })
            .map(ordenRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orden.getId().toString())
        );
    }

    @GetMapping("")
    public List<Orden> getAllOrdens() {
        String token =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTcyODgyOTM1MSwiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzAyOTA5MzUxfQ.4BjXQcquavqIn7N18ykwyPJG_GjVYDt1K4XvjMpIEoSj9tUt7u7Mr7b6pq5uHnFNCRwFbbMHdVObbw1QZOPoEQ";
        //ResponseEntity<Map<String, Object>> response = httpRequesties.getRequest("http://jhipster2:9090/api/users", token);
        return ordenRepository.findAll();
    }

    @GetMapping("/procesador")
    public ResponseEntity<List<Orden>> getAllOrdensProcesadaFalse() {
        // Obtener todas las órdenes con procesada=false
        List<Orden> ordens = ordenRepository.findByProcesadaFalse();

        // Establecer procesada en true para las órdenes obtenidas
        ordens.forEach(orden -> orden.setProcesada(true));
        ordenRepository.saveAll(ordens);

        return ResponseEntity.ok(ordens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> getOrden(@PathVariable Long id) {
        Optional<Orden> orden = ordenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orden);
    }

    // borrar solo cdo este procesada.
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrden(@PathVariable Long id) {
        // Obtener la orden por ID
        Optional<Orden> optionalOrden = ordenRepository.findById(id);

        // Verificar si la orden existe
        if (optionalOrden.isPresent()) {
            Orden orden = optionalOrden.get();

            // Verificar si la orden no está procesada
            if (!orden.getProcesada()) {
                // La orden no está procesada, se puede eliminar
                log.debug("orden anulada");
                ordenRepository.deleteById(id);

                return ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build();
            } else {
                log.debug("La orden está procesada y no puede ser eliminada");
                // La orden está procesada, devolver una respuesta con JSON
                Map<String, String> response = new HashMap<>();
                response.put("error", "La orden está procesada y no puede ser eliminada");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } else {
            log.debug("id - orden no existe");
            // La orden no existe, devolver una respuesta not found (404)
            return ResponseEntity.notFound().build();
        }
    }
}
