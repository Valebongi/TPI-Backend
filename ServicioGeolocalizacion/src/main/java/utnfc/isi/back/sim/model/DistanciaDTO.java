package utnfc.isi.back.sim.model;

public class DistanciaDTO {
    private String origen;
    private String destino;
    private double kilometros;
    private String duracionTexto;

    // Constructors
    public DistanciaDTO() {}

    public DistanciaDTO(String origen, String destino, double kilometros, String duracionTexto) {
        this.origen = origen;
        this.destino = destino;
        this.kilometros = kilometros;
        this.duracionTexto = duracionTexto;
    }

    // Getters
    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public double getKilometros() {
        return kilometros;
    }

    public String getDuracionTexto() {
        return duracionTexto;
    }

    // Setters
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setKilometros(double kilometros) {
        this.kilometros = kilometros;
    }

    public void setDuracionTexto(String duracionTexto) {
        this.duracionTexto = duracionTexto;
    }
}