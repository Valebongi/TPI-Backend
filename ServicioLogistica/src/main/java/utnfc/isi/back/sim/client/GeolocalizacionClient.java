package utnfc.isi.back.sim.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class GeolocalizacionClient {
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String GEO_SERVICE_URL = "http://servicio-geolocalizacion:8080";
    
    /**
     * Calcula la distancia entre dos puntos usando el servicio de geolocalización
     */
    public DistanciaResponse calcularDistancia(String origen, String destino) {
        try {
            String url = GEO_SERVICE_URL + "/api/distancia?origen=" + origen + "&destino=" + destino;
            ResponseEntity<DistanciaResponse> response = restTemplate.getForEntity(url, DistanciaResponse.class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("=== GEOLOCALIZACIÓN CLIENT: Error al calcular distancia: " + e.getMessage() + " ===");
            throw new RuntimeException("Error al calcular distancia entre " + origen + " y " + destino, e);
        }
    }
    
    /**
     * DTO para respuesta de distancia
     */
    public static class DistanciaResponse {
        private String origen;
        private String destino;
        private Double kilometros;
        private String duracionTexto;
        
        // Constructores
        public DistanciaResponse() {}
        
        public DistanciaResponse(String origen, String destino, Double kilometros, String duracionTexto) {
            this.origen = origen;
            this.destino = destino;
            this.kilometros = kilometros;
            this.duracionTexto = duracionTexto;
        }
        
        // Getters y Setters
        public String getOrigen() { return origen; }
        public void setOrigen(String origen) { this.origen = origen; }
        
        public String getDestino() { return destino; }
        public void setDestino(String destino) { this.destino = destino; }
        
        public Double getKilometros() { return kilometros; }
        public void setKilometros(Double kilometros) { this.kilometros = kilometros; }
        
        public String getDuracionTexto() { return duracionTexto; }
        public void setDuracionTexto(String duracionTexto) { this.duracionTexto = duracionTexto; }
    }
}