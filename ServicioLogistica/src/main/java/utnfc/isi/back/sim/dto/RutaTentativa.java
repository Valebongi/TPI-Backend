package utnfc.isi.back.sim.dto;

import java.util.List;

/**
 * DTO para representar una ruta tentativa calculada por Google Maps
 */
public class RutaTentativa {
    private String origen;
    private String destino;
    private List<TramoTentativo> tramos;
    private double distanciaTotal;
    private double duracionTotal;
    private double costoEstimado;
    
    // Constructores
    public RutaTentativa() {}
    
    public RutaTentativa(String origen, String destino, List<TramoTentativo> tramos, 
                        double distanciaTotal, double duracionTotal, double costoEstimado) {
        this.origen = origen;
        this.destino = destino;
        this.tramos = tramos;
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
    
    public List<TramoTentativo> getTramos() {
        return tramos;
    }
    
    public void setTramos(List<TramoTentativo> tramos) {
        this.tramos = tramos;
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