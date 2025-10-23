package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CAMIONES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Camion {
    @Id
    @SequenceGenerator(name = "seq_camion", sequenceName = "SEQ_CAMION_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_camion")
    @Column(name = "ID_CAMION")
    private Integer id;

    @Column(name = "DOMINIO", nullable = false, length = 30, unique = true)
    private String dominio;

    @Column(name = "MARCA", length = 100)
    private String marca;

    @Column(name = "MODELO", length = 100)
    private String modelo;

    @Column(name = "CAPACIDAD_PESO")
    private java.math.BigDecimal capacidadPeso;

    @Column(name = "CAPACIDAD_VOLUMEN")
    private java.math.BigDecimal capacidadVolumen;

    @Column(name = "CONSUMO_KM")
    private java.math.BigDecimal consumoKm;

    @Column(name = "COSTO_KM_BASE")
    private java.math.BigDecimal costoKmBase;

    @Column(name = "ESTADO", length = 50)
    private String estado;

    @Column(name = "TRANSPORTISTA", length = 200)
    private String transportista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TARIFA")
    private Tarifa tarifa;
}
