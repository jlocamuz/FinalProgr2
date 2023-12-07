package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Orden;
import com.mycompany.myapp.repository.OrdenRepository;
import com.mycompany.myapp.service.HttpRequesties;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    /**
     * {@code POST  /ordens} : Create a new orden.
     *
     * @param orden the orden to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orden, or with status {@code 400 (Bad Request)} if the orden has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Object> createOrden(@RequestBody Orden orden) throws URISyntaxException {
        if (orden.getId() != null) {
            throw new BadRequestAlertException("A new orden cannot already have an ID", ENTITY_NAME, "idexists");
        }

        //log.debug("atributo procesada {}", orden.getProcesada());
        String operacion = orden.getOperacion();
        String accion = orden.getAccion();
        Integer cliente = orden.getCliente();
        Integer cantidad = orden.getCantidad();

        Integer accionId = validateAccionInj.validateAccion(accion); // int
        Integer clienteId = validateClienteInj.validateCliente(cliente); // int
        //validateCantidadInj.validateCantidad(operacion, clienteId, accionId, cantidad)
        if (clienteId != null && accionId != null && validateCantidadInj.validateCantidad(operacion, clienteId, accionId, cantidad)) {
            Orden result = ordenRepository.save(orden);
            return ResponseEntity
                .created(new URI("/api/ordens/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } else {
            return ResponseEntity.badRequest().header(null).body("Accion, cliente o cantidad insuficiente");
        }
    }

    /**
     * {@code PUT  /ordens/:id} : Updates an existing orden.
     *
     * @param id the id of the orden to save.
     * @param orden the orden to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orden,
     * or with status {@code 400 (Bad Request)} if the orden is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orden couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Orden> updateOrden(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Orden orden)
        throws URISyntaxException {
        log.debug("REST request to update Orden : {}, {}", id, orden);
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

    /**
     * {@code PATCH  /ordens/:id} : Partial updates given fields of an existing orden, field will ignore if it is null
     *
     * @param id the id of the orden to save.
     * @param orden the orden to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orden,
     * or with status {@code 400 (Bad Request)} if the orden is not valid,
     * or with status {@code 404 (Not Found)} if the orden is not found,
     * or with status {@code 500 (Internal Server Error)} if the orden couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Orden> partialUpdateOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Orden orden
    ) throws URISyntaxException {
        log.debug("REST request to partial update Orden partially : {}, {}", id, orden);
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

                return existingOrden;
            })
            .map(ordenRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orden.getId().toString())
        );
    }

    /**
     * {@code GET  /ordens} : get all the ordens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordens in body.
     */
    @GetMapping("")
    public List<Orden> getAllOrdens() {
        log.debug("REST request to get all Ordens");
        return ordenRepository.findAll();
    }

    /**
     * {@code GET  /ordens/:id} : get the "id" orden.
     *
     * @param id the id of the orden to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orden, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Orden> getOrden(@PathVariable Long id) {
        log.debug("REST request to get Orden : {}", id);
        Optional<Orden> orden = ordenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orden);
    }

    // filters.
    @GetMapping("/buscador")
    @ResponseBody
    public ResponseEntity<List<Orden>> getOrdenByCliente(
        @RequestParam(required = false) String modo,
        @RequestParam(required = false) Integer cliente,
        @RequestParam(required = false) Integer accionId,
        @RequestParam(required = false) String accion,
        @RequestParam(required = true) String operacion,
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

    /**
     * {@code DELETE  /ordens/:id} : delete the "id" orden.
     *
     * @param id the id of the orden to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
        log.debug("REST request to delete Orden : {}", id);
        ordenRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
