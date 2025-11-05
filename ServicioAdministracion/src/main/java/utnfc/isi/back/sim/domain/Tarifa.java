package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tarifas")
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", length = 2048)
    private String descripcion;

    @Column(name = "valor_base", precision = 10, scale = 2)
    private BigDecimal valorBase;

    @Column(name = "tipo_calculo", length = 50)
    private String tipoCalculo;

    // Constructor por defecto
    public Tarifa() {}

    // Constructor con parámetros
    public Tarifa(String nombre, String descripcion, BigDecimal valorBase, String tipoCalculo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valorBase = valorBase;
        this.tipoCalculo = tipoCalculo;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getValorBase() {
        return valorBase;
    }

    public String getTipoCalculo() {
        return tipoCalculo;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setValorBase(BigDecimal valorBase) {
        this.valorBase = valorBase;
    }

    public void setTipoCalculo(String tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }
}
