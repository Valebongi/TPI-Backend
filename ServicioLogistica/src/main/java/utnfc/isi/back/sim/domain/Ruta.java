package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Entidad que representa una ruta de transporte
 * Una ruta está asociada a una solicitud y contiene múltiples tramos
 */
@Entity
@Table(name = "rutas")
public class Ruta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "solicitud_id", nullable = false)
    private Long solicitudId;
    
    @Column(name = "cantidad_tramos")
    private Integer cantidadTramos;
    
    @Column(name = "cantidad_depositos")
    private Integer cantidadDepositos;
    
    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoRuta estado = EstadoRuta.PLANIFICADA;
    
    @Column(name = "costo_total_aproximado")
    private Double costoTotalAproximado;
    
    @Column(name = "costo_total_real")
    private Double costoTotalReal;
    
    @Column(name = "observaciones", length = 500)
    private String observaciones;
    
    @Column(name = "fecha_creacion")
    private java.time.LocalDateTime fechaCreacion;
    
    // Relación uno-a-muchos con Tramos
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Evita problemas de serialización con lazy loading
    private List<Tramo> tramos;
    
    // Constructors
    public Ruta() {}
    
    public Ruta(Long solicitudId, Integer cantidadTramos, Integer cantidadDepositos, EstadoRuta estado,
                Double costoTotalAproximado, Double costoTotalReal, List<Tramo> tramos) {
        this.solicitudId = solicitudId;
        this.cantidadTramos = cantidadTramos;
        this.cantidadDepositos = cantidadDepositos;
        this.estado = estado;
        this.costoTotalAproximado = costoTotalAproximado;
        this.costoTotalReal = costoTotalReal;
        this.tramos = tramos;
    }

    // Getters
    public Long getId() { return id; }
    public Long getSolicitudId() { return solicitudId; }
    public Integer getCantidadTramos() { return cantidadTramos; }
    public Integer getCantidadDepositos() { return cantidadDepositos; }
    public EstadoRuta getEstado() { return estado; }
    public Double getCostoTotalAproximado() { return costoTotalAproximado; }
    public Double getCostoTotalReal() { return costoTotalReal; }
    public List<Tramo> getTramos() { return tramos; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSolicitudId(Long solicitudId) { this.solicitudId = solicitudId; }
    public void setCantidadTramos(Integer cantidadTramos) { this.cantidadTramos = cantidadTramos; }
    public void setCantidadDepositos(Integer cantidadDepositos) { this.cantidadDepositos = cantidadDepositos; }
    public void setEstado(EstadoRuta estado) { this.estado = estado; }
    public void setCostoTotalAproximado(Double costoTotalAproximado) { this.costoTotalAproximado = costoTotalAproximado; }
    public void setCostoTotalReal(Double costoTotalReal) { this.costoTotalReal = costoTotalReal; }
    public void setTramos(List<Tramo> tramos) { this.tramos = tramos; }
    
    // Getters y Setters para los nuevos campos
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public java.time.LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(java.time.LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    // Builder simple
    public static Ruta builder() {
        return new Ruta();
    }
    
    public Ruta solicitudId(Long solicitudId) {
        this.solicitudId = solicitudId;
        return this;
    }
    
    public Ruta estado(EstadoRuta estado) {
        this.estado = estado;
        return this;
    }
    
    public Ruta fechaCreacion(java.time.LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }
    
    public Ruta observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }
    
    public Ruta build() {
        return this;
    }
    
    /**
     * Enumeration para los posibles estados de una ruta
     */
    public enum EstadoRuta {
        PLANIFICADA,
        EN_PROGRESO,
        COMPLETADA,
        CANCELADA
    }
}