package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un tramo dentro de una ruta
 * Cada tramo tiene origen, destino, y se asigna a un camión específico
 */
@Entity
@Table(name = "tramos")
@Data
@NoArgsConstructor
@AllArgsConstructor
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