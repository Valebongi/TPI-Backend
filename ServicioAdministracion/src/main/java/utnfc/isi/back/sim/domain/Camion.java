package utnfc.isi.back.sim.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "camiones")
public class Camion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_camion")
    private Long id;

    @Column(name = "dominio", nullable = false, length = 30, unique = true)
    private String dominio;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "modelo", length = 100)
    private String modelo;

    @Column(name = "capacidad_peso")
    private BigDecimal capacidadPeso;

    @Column(name = "capacidad_volumen")
    private BigDecimal capacidadVolumen;

    @Column(name = "consumo_km")
    private BigDecimal consumoKm;

    @Column(name = "costo_km_base")
    private BigDecimal costoKmBase;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "transportista", length = 200)
    private String transportista;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "disponible")
    private Boolean disponible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarifa")
    @JsonIgnore
    private Tarifa tarifa;

    // Constructor por defecto
    public Camion() {}

    // Constructor con parámetros principales
    public Camion(String dominio, String marca, String modelo, BigDecimal capacidadPeso, 
                  BigDecimal capacidadVolumen, BigDecimal consumoKm, BigDecimal costoKmBase, 
                  String estado, String transportista, String telefono, Boolean disponible) {
        this.dominio = dominio;
        this.marca = marca;
        this.modelo = modelo;
        this.capacidadPeso = capacidadPeso;
        this.capacidadVolumen = capacidadVolumen;
        this.consumoKm = consumoKm;
        this.costoKmBase = costoKmBase;
        this.estado = estado;
        this.transportista = transportista;
        this.telefono = telefono;
        this.disponible = disponible;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getDominio() {
        return dominio;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public BigDecimal getCapacidadPeso() {
        return capacidadPeso;
    }

    public BigDecimal getCapacidadVolumen() {
        return capacidadVolumen;
    }

    public BigDecimal getConsumoKm() {
        return consumoKm;
    }

    public BigDecimal getCostoKmBase() {
        return costoKmBase;
    }

    public String getEstado() {
        return estado;
    }

    public String getTransportista() {
        return transportista;
    }

    public String getTelefono() {
        return telefono;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setCapacidadPeso(BigDecimal capacidadPeso) {
        this.capacidadPeso = capacidadPeso;
    }

    public void setCapacidadVolumen(BigDecimal capacidadVolumen) {
        this.capacidadVolumen = capacidadVolumen;
    }

    public void setConsumoKm(BigDecimal consumoKm) {
        this.consumoKm = consumoKm;
    }

    public void setCostoKmBase(BigDecimal costoKmBase) {
        this.costoKmBase = costoKmBase;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }
}
