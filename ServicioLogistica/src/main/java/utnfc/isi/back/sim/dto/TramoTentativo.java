package utnfc.isi.back.sim.dto;

/**
 * DTO para representar un tramo tentativo calculado por Google Maps
 */
public class TramoTentativo {
    private String origen;
    private String destino;
    private double distancia;
    private double duracion;
    private String tipoVehiculo;
    
    // Constructores
    public TramoTentativo() {}
    
    public TramoTentativo(String origen, String destino, double distancia, 
                         double duracion, String tipoVehiculo) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.duracion = duracion;
        this.tipoVehiculo = tipoVehiculo;
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
    
    public double getDistancia() {
        return distancia;
    }
    
    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
    
    public double getDuracion() {
        return duracion;
    }
    
    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }
    
    public String getTipoVehiculo() {
        return tipoVehiculo;
    }
    
    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
}