package utnfc.isi.back.sim.client;

import java.math.BigDecimal;

/**
 * DTO para recibir la respuesta de la creación de una ruta desde el servicio de logística
 */
public class RutaResponse {
    
    private Long id;
    private Long solicitudId;
    private String estado;
    private BigDecimal costoTotalAproximado;
    private String observaciones;

    // Constructores
    public RutaResponse() {}

    public RutaResponse(Long id, Long solicitudId, String estado, 
                       BigDecimal costoTotalAproximado, String observaciones) {
        this.id = id;
        this.solicitudId = solicitudId;
        this.estado = estado;
        this.costoTotalAproximado = costoTotalAproximado;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSolicitudId() { return solicitudId; }
    public void setSolicitudId(Long solicitudId) { this.solicitudId = solicitudId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getCostoTotalAproximado() { return costoTotalAproximado; }
    public void setCostoTotalAproximado(BigDecimal costoTotalAproximado) { 
        this.costoTotalAproximado = costoTotalAproximado; 
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}