package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Orden;
import com.mycompany.myapp.domain.enumeration.Modo;
import com.mycompany.myapp.repository.OrdenRepository;
import com.mycompany.myapp.service.HttpRequesties;
import com.mycompany.myapp.service.ValidateAccion;
import com.mycompany.myapp.service.ValidateCantidad;
import com.mycompany.myapp.service.ValidateCliente;
import com.mycompany.myapp.service.ValidateFecha;
import com.mycompany.myapp.service.ValidationService;
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
import org.springframework.data.jpa.domain.Specification;
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
    private ValidationService validationService;

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
    public ResponseEntity<Map<String, List<Orden>>> getFiltroNoProcesadas(
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

        Map<String, List<Orden>> responseMap = new HashMap<>();
        responseMap.put("ordenes no procesadas", ordenes);

        // Especificar los tipos de manera explícita al construir ResponseEntity
        return new ResponseEntity<Map<String, List<Orden>>>(responseMap, HttpStatus.OK);
    }

    // filters. REPORTES
    @GetMapping("/buscador")
    @ResponseBody
    public ResponseEntity<Map<String, List<Orden>>> buscarOrden(
        @RequestParam(name = "modo", required = false) Modo modo,
        @RequestParam(name = "cliente", required = false) Integer cliente,
        @RequestParam(name = "accionId", required = false) Integer accionId,
        @RequestParam(name = "accion", required = false) String accion,
        @RequestParam(name = "operacion", required = false) String operacion,
        @RequestParam(name = "cantidad", required = false) Integer cantidad,
        @RequestParam(name = "precio", required = false) Double precio,
        @RequestParam(name = "fechaOperacion", required = false) ZonedDateTime fechaOperacion
    ) {
        List<Orden> ordenes = new ArrayList<>();

        String filtros = "Filtros aplicados: ";

        Specification<Orden> spec = Specification.where(null);

        // Apply filters based on available parameters
        if (cliente != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("cliente"), cliente));
            filtros += "cliente " + cliente + ", ";
        }
        if (accionId != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("accionId"), accionId));
            filtros += "accionId " + accionId + ", ";
        }
        if (accion != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("accion"), accion));
            filtros += "accion " + accion + ", ";
        }
        if (modo != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("modo"), modo));
            filtros += "modo " + modo + ", ";
        }
        if (operacion != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("operacion"), operacion));
            filtros += "operacion " + operacion + ", ";
        }
        // Add conditions for other parameters...

        // Fetch orders based on combined specifications
        ordenes = ordenRepository.findAll(spec);

        Map<String, List<Orden>> responseMap = new HashMap<>();
        responseMap.put(filtros, ordenes);

        // Specify types explicitly when constructing ResponseEntity
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Object> createOrden(@RequestBody Orden orden) throws URISyntaxException {
        if (orden.getId() != null) {
            throw new BadRequestAlertException("A new orden cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (validationService.validateOrden(orden)) {
            // Guardar la orden
            log.info("FECHA" + orden.getFechaOperacion());
            Orden result = ordenRepository.save(orden);
            log.info("Orden creada");
            return ResponseEntity
                .created(new URI("/api/ordens/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } else {
            log.info("Orden rechazada");
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
        //String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTcwMzg1MzQyMn0.Q85bdKxKK7XPm9OkKjlcX7zFEg0Ux1FNXIf9sJ5rydizXyzpJ9p1Id3Emo7g6udy5uSHiqp1X6LO5GxPkmcTEw";
        //ResponseEntity<Object> response = httpRequesties.getRequest("http://procesador:8080/api/users", token);
        return ordenRepository.findAll();
    }

    @GetMapping("/procesador")
    public ResponseEntity<Map<String, List<Orden>>> getAllOrdensProcesadaFalse() {
        System.out.println("me llama el procesador!");
        // Obtener todas las órdenes con procesada=false
        List<Orden> ordens = ordenRepository.findByProcesadaFalse();

        // Establecer procesada en true para las órdenes obtenidas
        ordens.forEach(orden -> orden.setProcesada(true));
        ordenRepository.saveAll(ordens);

        // Crear un mapa con la clave "ordenes" y la lista de órdenes como valor
        Map<String, List<Orden>> responseMap = new HashMap<>();
        responseMap.put("ordenes", ordens);

        return ResponseEntity.ok(responseMap);
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
                ordenRepository.deleteById(id);
                log.info("orden anulada");
                return ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build();
            } else {
                // La orden está procesada, devolver una respuesta con JSON
                Map<String, String> response = new HashMap<>();
                response.put("error", "La orden está procesada y no puede ser eliminada");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
        } else {
            log.info("id - orden no existe");
            // La orden no existe, devolver una respuesta not found (404)
            return ResponseEntity.notFound().build();
        }
    }
}
