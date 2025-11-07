package utnfc.isi.back.sim.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Cliente REST para comunicarse con el Servicio de Logística
 * Implementa la comunicación definida en los lineamientos del TP
 */
@Component
public class LogisticaClient {
    
    private final RestTemplate restTemplate;
    private final String logisticaServiceUrl;

    public LogisticaClient(RestTemplate restTemplate, 
                          @Value("${servicio.logistica.url:http://localhost:8080/api/logistica}") String logisticaServiceUrl) {
        this.restTemplate = restTemplate;
        this.logisticaServiceUrl = logisticaServiceUrl;
    }

    /**
     * Crea una ruta en el servicio de logística para una solicitud
     */
    public RutaResponse crearRuta(RutaRequest rutaRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<RutaRequest> request = new HttpEntity<>(rutaRequest, headers);
            
            String url = logisticaServiceUrl + "/rutas/crear-automatica";
            ResponseEntity<RutaResponse> response = restTemplate.postForEntity(url, request, RutaResponse.class);
            
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio de logística: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene los detalles de una ruta
     */
    public RutaResponse obtenerRuta(Long rutaId) {
        try {
            String url = logisticaServiceUrl + "/rutas/" + rutaId;
            ResponseEntity<RutaResponse> response = restTemplate.getForEntity(url, RutaResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la ruta: " + e.getMessage(), e);
        }
    }
}