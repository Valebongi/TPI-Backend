package utnfc.isi.back.sim.client;

import java.math.BigDecimal;

/**
 * DTO para respuestas del servicio de administración sobre depósitos
 */
public class DepositoResponse {
    
    private Integer id;
    private String nombre;
    private String direccion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private BigDecimal costoDiario;
    private Integer capacidadMax;

    // Constructores
    public DepositoResponse() {}

    public DepositoResponse(Integer id, String nombre, String direccion, BigDecimal latitud, 
                           BigDecimal longitud, BigDecimal costoDiario, Integer capacidadMax) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.costoDiario = costoDiario;
        this.capacidadMax = capacidadMax;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public BigDecimal getCostoDiario() {
        return costoDiario;
    }

    public void setCostoDiario(BigDecimal costoDiario) {
        this.costoDiario = costoDiario;
    }

    public Integer getCapacidadMax() {
        return capacidadMax;
    }

    public void setCapacidadMax(Integer capacidadMax) {
        this.capacidadMax = capacidadMax;
    }
}