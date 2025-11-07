package utnfc.isi.back.sim.dto;

/**
 * DTO para la asignación de camión a un tramo
 * FASE 4: El operador asigna un camión específico a un tramo
 */
public class AsignarCamionRequest {
    private Long camionId;
    private String observaciones;
    
    // Constructores
    public AsignarCamionRequest() {}
    
    public AsignarCamionRequest(Long camionId, String observaciones) {
        this.camionId = camionId;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public Long getCamionId() {
        return camionId;
    }
    
    public void setCamionId(Long camionId) {
        this.camionId = camionId;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}