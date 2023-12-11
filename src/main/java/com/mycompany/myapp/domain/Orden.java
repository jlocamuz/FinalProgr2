package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.myapp.domain.enumeration.Modo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Orden.
 */
@Entity
@Table(name = "orden")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orden implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cliente", nullable = false)
    private Integer cliente;

    @NotNull
    @Column(name = "accion_id", nullable = false)
    private Integer accionId;

    @Column(name = "accion")
    private String accion;

    @NotNull
    @Column(name = "operacion", nullable = false)
    private String operacion;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "fecha_operacion")
    private ZonedDateTime fechaOperacion = ZonedDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "modo", nullable = false)
    private Modo modo;

    private Boolean procesada = false; // Valor predeterminado

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orden id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCliente() {
        return this.cliente;
    }

    public Orden cliente(Integer cliente) {
        this.setCliente(cliente);
        return this;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public Integer getAccionId() {
        return this.accionId;
    }

    public Orden accionId(Integer accionId) {
        this.setAccionId(accionId);
        return this;
    }

    public void setAccionId(Integer accionId) {
        this.accionId = accionId;
    }

    public String getAccion() {
        return this.accion;
    }

    public Orden accion(String accion) {
        this.setAccion(accion);
        return this;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getOperacion() {
        return this.operacion;
    }

    public Orden operacion(String operacion) {
        this.setOperacion(operacion);
        return this;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Orden cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Orden precio(Double precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public ZonedDateTime getFechaOperacion() {
        return this.fechaOperacion;
    }

    public Orden fechaOperacion(ZonedDateTime fechaOperacion) {
        this.setFechaOperacion(fechaOperacion);
        return this;
    }

    public void setFechaOperacion(ZonedDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public Modo getModo() {
        return this.modo;
    }

    public Orden modo(Modo modo) {
        this.setModo(modo);
        return this;
    }

    public void setModo(Modo modo) {
        this.modo = modo;
    }

    public Boolean getProcesada() {
        return this.procesada;
    }

    public Orden procesada(Boolean procesada) {
        this.setProcesada(procesada);
        return this;
    }

    public void setProcesada(Boolean procesada) {
        this.procesada = procesada;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orden)) {
            return false;
        }
        return getId() != null && getId().equals(((Orden) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orden{" +
            "id=" + getId() +
            ", cliente=" + getCliente() +
            ", accionId=" + getAccionId() +
            ", accion='" + getAccion() + "'" +
            ", operacion='" + getOperacion() + "'" +
            ", cantidad=" + getCantidad() +
            ", precio=" + getPrecio() +
            ", fechaOperacion='" + getFechaOperacion() + "'" +
            ", modo='" + getModo() + "'" +
            ", procesada='" + getProcesada() + "'" +
            "}";
    }
}
