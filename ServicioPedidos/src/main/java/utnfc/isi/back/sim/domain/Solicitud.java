package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SOLICITUDES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Solicitud {
    
    @Id
    @SequenceGenerator(name = "seq_solicitud", sequenceName = "SEQ_SOLICITUD_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_solicitud")
    @Column(name = "ID_SOLICITUD")
    private Long id;

    @Column(name = "NUMERO", nullable = false, unique = true, length = 50)
    @Builder.Default
    private String numero = generateSolicitudNumber();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENEDOR", nullable = false)
    @NotNull(message = "El contenedor es obligatorio")
    private Contenedor contenedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", nullable = false)  
    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    @Column(name = "COSTO_ESTIMADO", precision = 12, scale = 2)
    private BigDecimal costoEstimado;

    @Column(name = "TIEMPO_ESTIMADO_HORAS")
    private Integer tiempoEstimadoHoras;

    @Column(name = "COSTO_FINAL", precision = 12, scale = 2)
    private BigDecimal costoFinal;

    @Column(name = "TIEMPO_REAL_HORAS")
    private Integer tiempoRealHoras;

    @Column(name = "ESTADO", nullable = false, length = 50)
    @Builder.Default
    private String estado = "BORRADOR"; // BORRADOR, PROGRAMADA, EN_TRANSITO, ENTREGADA, CANCELADA

    @Column(name = "FECHA_CREACION", nullable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "FECHA_PROGRAMACION")
    private LocalDateTime fechaProgramacion;

    @Column(name = "FECHA_INICIO_TRANSITO")
    private LocalDateTime fechaInicioTransito;

    @Column(name = "FECHA_ENTREGA")
    private LocalDateTime fechaEntrega;

    @Column(name = "OBSERVACIONES", length = 1000)
    private String observaciones;

    @Column(name = "RUTA_ID")
    private Long rutaId; // ID de la ruta en el servicio de logística

    private static String generateSolicitudNumber() {
        return "SOL-" + System.currentTimeMillis();
    }
}