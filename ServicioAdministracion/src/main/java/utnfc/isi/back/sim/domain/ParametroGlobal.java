package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "parametros_globales")
@Data
public class ParametroGlobal {
    @Id
    @Column(name = "clave", length = 100)
    private String clave;

    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    // Setter explícito para evitar problemas de compilación
    public void setClave(String clave) {
        this.clave = clave;
    }

    @Column(name = "activo")
    private Boolean activo = true;
}