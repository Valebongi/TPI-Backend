package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "depositos")
public class Deposito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deposito")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "direccion", length = 400)
    private String direccion;

    @Column(name = "latitud", precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 9, scale = 6)
    private BigDecimal longitud;

    @Column(name = "costo_diario", precision = 10, scale = 2)
    private BigDecimal costoDiario;

    @Column(name = "capacidad_max")
    private Integer capacidadMax;

    // Constructor por defecto requerido por JPA
    public Deposito() {}

    // Constructor con parámetros para facilitar creación
    public Deposito(String nombre, String direccion, BigDecimal latitud, 
                    BigDecimal longitud, BigDecimal costoDiario, Integer capacidadMax) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.costoDiario = costoDiario;
        this.capacidadMax = capacidadMax;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }

    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }

    public BigDecimal getCostoDiario() { return costoDiario; }
    public void setCostoDiario(BigDecimal costoDiario) { this.costoDiario = costoDiario; }

    public Integer getCapacidadMax() { return capacidadMax; }
    public void setCapacidadMax(Integer capacidadMax) { this.capacidadMax = capacidadMax; }
}
