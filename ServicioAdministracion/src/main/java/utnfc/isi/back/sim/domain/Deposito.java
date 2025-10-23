package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DEPOSITOS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Deposito {
    @Id
    @SequenceGenerator(name = "seq_deposito", sequenceName = "SEQ_DEPOSITO_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_deposito")
    @Column(name = "ID_DEPOSITO")
    private Integer id;

    @Column(name = "NOMBRE", nullable = false, length = 200)
    private String nombre;

    @Column(name = "DIRECCION", length = 400)
    private String direccion;

    @Column(name = "LATITUD", precision = 9, scale = 6)
    private java.math.BigDecimal latitud;

    @Column(name = "LONGITUD", precision = 9, scale = 6)
    private java.math.BigDecimal longitud;

    @Column(name = "COSTO_DIARIO", precision = 10, scale = 2)
    private java.math.BigDecimal costoDiario;

    @Column(name = "CAPACIDAD_MAX")
    private Integer capacidadMax;
}
