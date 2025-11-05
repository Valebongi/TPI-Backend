package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Entidad que representa un tramo dentro de una ruta
 * Cada tramo tiene origen, destino, y se asigna a un camión específico
 */
@Entity
@Table(name = "tramos")
public class Tramo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Coordenadas de origen (lat,lng separadas por coma)
    @NotNull
    @Column(name = "origen_coordenadas", nullable = false)
    private String origenCoordenadas;
    
    // Coordenadas de destino (lat,lng separadas por coma)
    @NotNull
    @Column(name = "destino_coordenadas", nullable = false)
    private String destinoCoordenadas;
    
    @Column(name = "origen_descripcion")
    private String origenDescripcion;
    
    @Column(name = "destino_descripcion")
    private String destinoDescripcion;
    
    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoTramo tipo = TipoTramo.TRANSPORTE;
    
    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoTramo estado = EstadoTramo.PENDIENTE;
    
    @Column(name = "costo_aproximado")
    private Double costoAproximado;
    
    @Column(name = "costo_real")
    private Double costoReal;
    
    @Column(name = "distancia_km")
    private Double distanciaKm;
    
    @Column(name = "tiempo_estimado_minutos")
    private Integer tiempoEstimadoMinutos;
    
    @Column(name = "tiempo_real_minutos")
    private Integer tiempoRealMinutos;
    
    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;
    
    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;
    
    @Column(name = "camion_id")
    private Long camionId;
    
    // Relación muchos-a-uno con Ruta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;
    
    // Constructors
    public Tramo() {}
    
    public Tramo(String origenCoordenadas, String destinoCoordenadas, String origenDescripcion, 
                 String destinoDescripcion, TipoTramo tipo, EstadoTramo estado, Double costoAproximado,
                 Double costoReal, Double distanciaKm, Integer tiempoEstimadoMinutos, Integer tiempoRealMinutos,
                 LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Long camionId, Ruta ruta) {
        this.origenCoordenadas = origenCoordenadas;
        this.destinoCoordenadas = destinoCoordenadas;
        this.origenDescripcion = origenDescripcion;
        this.destinoDescripcion = destinoDescripcion;
        this.tipo = tipo;
        this.estado = estado;
        this.costoAproximado = costoAproximado;
        this.costoReal = costoReal;
        this.distanciaKm = distanciaKm;
        this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
        this.tiempoRealMinutos = tiempoRealMinutos;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.camionId = camionId;
        this.ruta = ruta;
    }

    // Getters
    public Long getId() { return id; }
    public String getOrigenCoordenadas() { return origenCoordenadas; }
    public String getDestinoCoordenadas() { return destinoCoordenadas; }
    public String getOrigenDescripcion() { return origenDescripcion; }
    public String getDestinoDescripcion() { return destinoDescripcion; }
    public TipoTramo getTipo() { return tipo; }
    public EstadoTramo getEstado() { return estado; }
    public Double getCostoAproximado() { return costoAproximado; }
    public Double getCostoReal() { return costoReal; }
    public Double getDistanciaKm() { return distanciaKm; }
    public Integer getTiempoEstimadoMinutos() { return tiempoEstimadoMinutos; }
    public Integer getTiempoRealMinutos() { return tiempoRealMinutos; }
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public Long getCamionId() { return camionId; }
    public Ruta getRuta() { return ruta; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setOrigenCoordenadas(String origenCoordenadas) { this.origenCoordenadas = origenCoordenadas; }
    public void setDestinoCoordenadas(String destinoCoordenadas) { this.destinoCoordenadas = destinoCoordenadas; }
    public void setOrigenDescripcion(String origenDescripcion) { this.origenDescripcion = origenDescripcion; }
    public void setDestinoDescripcion(String destinoDescripcion) { this.destinoDescripcion = destinoDescripcion; }
    public void setTipo(TipoTramo tipo) { this.tipo = tipo; }
    public void setEstado(EstadoTramo estado) { this.estado = estado; }
    public void setCostoAproximado(Double costoAproximado) { this.costoAproximado = costoAproximado; }
    public void setCostoReal(Double costoReal) { this.costoReal = costoReal; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
    public void setTiempoEstimadoMinutos(Integer tiempoEstimadoMinutos) { this.tiempoEstimadoMinutos = tiempoEstimadoMinutos; }
    public void setTiempoRealMinutos(Integer tiempoRealMinutos) { this.tiempoRealMinutos = tiempoRealMinutos; }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) { this.fechaHoraInicio = fechaHoraInicio; }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) { this.fechaHoraFin = fechaHoraFin; }
    public void setCamionId(Long camionId) { this.camionId = camionId; }
    public void setRuta(Ruta ruta) { this.ruta = ruta; }
    
    /**
     * Enumeration para los tipos de tramo
     */
    public enum TipoTramo {
        RECOGIDA,     // Recoger mercancía del origen
        TRANSPORTE,   // Transporte entre puntos
        ENTREGA       // Entrega en destino final
    }
    
    /**
     * Enumeration para los estados del tramo
     */
    public enum EstadoTramo {
        PENDIENTE,
        ASIGNADO,
        EN_PROGRESO,
        COMPLETADO,
        CANCELADO
    }
}