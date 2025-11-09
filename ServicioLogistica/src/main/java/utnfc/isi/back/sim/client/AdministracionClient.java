package utnfc.isi.back.sim.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Cliente REST para comunicarse con el Servicio de Administración
 * Implementa la comunicación definida en los lineamientos del TP
 */
@Component
public class AdministracionClient {
    
    private final RestTemplate restTemplate;
    private final String administracionServiceUrl;

    public AdministracionClient(RestTemplate restTemplate, 
                              @Value("${servicio.administracion.url:http://servicio-administracion:8080}") String administracionServiceUrl) {
        this.restTemplate = restTemplate;
        this.administracionServiceUrl = administracionServiceUrl;
    }

    /**
     * Verifica si un camión existe y está disponible
     */
    public boolean existeCamion(Long camionId) {
        try {
            String url = administracionServiceUrl + "/camiones/" + camionId;
            ResponseEntity<CamionResponse> response = restTemplate.getForEntity(url, CamionResponse.class);
            return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
        } catch (Exception e) {
            // Si hay error al consultar, asumimos que el camión no existe o no está disponible
            return false;
        }
    }

    /**
     * Obtiene los detalles de un camión
     */
    public CamionResponse obtenerCamion(Long camionId) {
        try {
            String url = administracionServiceUrl + "/camiones/" + camionId;
            ResponseEntity<CamionResponse> response = restTemplate.getForEntity(url, CamionResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el camión: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene todos los depósitos disponibles
     */
    public DepositoResponse[] obtenerDepositos() {
        try {
            String url = administracionServiceUrl + "/depositos";
            ResponseEntity<DepositoResponse[]> response = restTemplate.getForEntity(url, DepositoResponse[].class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("=== ADMIN CLIENT: Error al obtener depósitos: " + e.getMessage() + " ===");
            return new DepositoResponse[0];
        }
    }
}