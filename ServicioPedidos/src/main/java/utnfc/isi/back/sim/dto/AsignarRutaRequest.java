package utnfc.isi.back.sim.dto;

/**
 * DTO para la asignación de ruta a una solicitud
 * FASE 3: El operador elige una ruta y la asigna a la solicitud
 */
public class AsignarRutaRequest {
    private String origen;
    private String destino;
    private double distanciaTotal;
    private double duracionTotal;
    private double costoEstimado;
    
    // Constructor vacío
    public AsignarRutaRequest() {}
    
    // Constructor completo
    public AsignarRutaRequest(String origen, String destino, double distanciaTotal, 
                             double duracionTotal, double costoEstimado) {
        this.origen = origen;
        this.destino = destino;
        this.distanciaTotal = distanciaTotal;
        this.duracionTotal = duracionTotal;
        this.costoEstimado = costoEstimado;
    }
    
    // Getters y Setters
    public String getOrigen() {
        return origen;
    }
    
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    
    public String getDestino() {
        return destino;
    }
    
    public void setDestino(String destino) {
        this.destino = destino;
    }
    
    public double getDistanciaTotal() {
        return distanciaTotal;
    }
    
    public void setDistanciaTotal(double distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
    }
    
    public double getDuracionTotal() {
        return duracionTotal;
    }
    
    public void setDuracionTotal(double duracionTotal) {
        this.duracionTotal = duracionTotal;
    }
    
    public double getCostoEstimado() {
        return costoEstimado;
    }
    
    public void setCostoEstimado(double costoEstimado) {
        this.costoEstimado = costoEstimado;
    }
}