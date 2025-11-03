package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad que representa una ruta de transporte
 * Una ruta está asociada a una solicitud y contiene múltiples tramos
 */
@Entity
@Table(name = "rutas")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    // Relación uno-a-muchos con Tramos
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tramo> tramos;
    
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