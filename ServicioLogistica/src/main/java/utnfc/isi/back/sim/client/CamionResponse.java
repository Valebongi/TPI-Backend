package utnfc.isi.back.sim.client;

/**
 * DTO para recibir la respuesta de camión desde el servicio de administración
 */
public class CamionResponse {
    
    private Long id;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private Double pesoMaximo;
    private Double volumenMaximo;
    private String estado;
    private String tipoCarroceria;
    private String tipoCombustible;
    private Double consumoPorKm;
    private String observaciones;

    // Constructores
    public CamionResponse() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public Double getPesoMaximo() { return pesoMaximo; }
    public void setPesoMaximo(Double pesoMaximo) { this.pesoMaximo = pesoMaximo; }

    public Double getVolumenMaximo() { return volumenMaximo; }
    public void setVolumenMaximo(Double volumenMaximo) { this.volumenMaximo = volumenMaximo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTipoCarroceria() { return tipoCarroceria; }
    public void setTipoCarroceria(String tipoCarroceria) { this.tipoCarroceria = tipoCarroceria; }

    public String getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }

    public Double getConsumoPorKm() { return consumoPorKm; }
    public void setConsumoPorKm(Double consumoPorKm) { this.consumoPorKm = consumoPorKm; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}