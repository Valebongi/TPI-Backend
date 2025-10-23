package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PARAMETROS_GLOBALES")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParametroGlobal {
    @Id
    @Column(name = "CLAVE", length = 100)
    private String clave; // "VALOR_LITRO", "CARGO_GESTION_TRAMO", etc.

    @Column(name = "VALOR", precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "DESCRIPCION", length = 500)
    private String descripcion;

    @Column(name = "ACTIVO")
    @Builder.Default
    private Boolean activo = true;
}