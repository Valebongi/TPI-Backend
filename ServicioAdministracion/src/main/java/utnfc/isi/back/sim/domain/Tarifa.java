package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TARIFAS")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tarifa {
    @Id
    @SequenceGenerator(name = "seq_tarifa", sequenceName = "SEQ_TARIFA_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tarifa")
    @Column(name = "ID_TARIFA")
    private Integer id;

    @Column(name = "NOMBRE", nullable = false, length = 200)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 2048)
    private String descripcion;

    @Column(name = "VALOR_BASE", precision = 10, scale = 2)
    private java.math.BigDecimal valorBase;

    @Column(name = "TIPO_CALCULO", length = 50)
    private String tipoCalculo;
}
