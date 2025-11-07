package utnfc.isi.back.sim.dto;

import java.math.BigDecimal;

/**
 * DTO para recibir solicitudes de creación de rutas desde otros microservicios
 */
public class RutaCreacionRequest {
    
    private Long solicitudId;
    private String origenCoordenadas;  // "lat,lng"
    private String destinoCoordenadas; // "lat,lng"
    private String origenDescripcion;
    private String destinoDescripcion;
    private BigDecimal pesoContenedor;
    private BigDecimal volumenContenedor;

    // Constructores
    public RutaCreacionRequest() {}

    public RutaCreacionRequest(Long solicitudId, String origenCoordenadas, String destinoCoordenadas,
                              String origenDescripcion, String destinoDescripcion,
                              BigDecimal pesoContenedor, BigDecimal volumenContenedor) {
        this.solicitudId = solicitudId;
        this.origenCoordenadas = origenCoordenadas;
        this.destinoCoordenadas = destinoCoordenadas;
        this.origenDescripcion = origenDescripcion;
        this.destinoDescripcion = destinoDescripcion;
        this.pesoContenedor = pesoContenedor;
        this.volumenContenedor = volumenContenedor;
    }

    // Getters y Setters
    public Long getSolicitudId() { return solicitudId; }
    public void setSolicitudId(Long solicitudId) { this.solicitudId = solicitudId; }

    public String getOrigenCoordenadas() { return origenCoordenadas; }
    public void setOrigenCoordenadas(String origenCoordenadas) { this.origenCoordenadas = origenCoordenadas; }

    public String getDestinoCoordenadas() { return destinoCoordenadas; }
    public void setDestinoCoordenadas(String destinoCoordenadas) { this.destinoCoordenadas = destinoCoordenadas; }

    public String getOrigenDescripcion() { return origenDescripcion; }
    public void setOrigenDescripcion(String origenDescripcion) { this.origenDescripcion = origenDescripcion; }

    public String getDestinoDescripcion() { return destinoDescripcion; }
    public void setDestinoDescripcion(String destinoDescripcion) { this.destinoDescripcion = destinoDescripcion; }

    public BigDecimal getPesoContenedor() { return pesoContenedor; }
    public void setPesoContenedor(BigDecimal pesoContenedor) { this.pesoContenedor = pesoContenedor; }

    public BigDecimal getVolumenContenedor() { return volumenContenedor; }
    public void setVolumenContenedor(BigDecimal volumenContenedor) { this.volumenContenedor = volumenContenedor; }
}