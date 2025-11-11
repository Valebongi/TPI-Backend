package utnfc.isi.back.sim.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

/**
 * Cliente REST para comunicarse con el Servicio de Administración
 * Permite obtener información de depósitos
 */
@Component
public class AdministracionClient {
    
    private final RestTemplate restTemplate;
    private final String administracionServiceUrl;

    public AdministracionClient(RestTemplate restTemplate, 
                               @Value("${servicio.administracion.url:http://localhost:8080/api/admin}") String administracionServiceUrl) {
        this.restTemplate = restTemplate;
        this.administracionServiceUrl = administracionServiceUrl;
    }

    /**
     * Obtiene un depósito por su ID
     */
    public DepositoResponse obtenerDeposito(Integer depositoId) {
        try {
            String url = administracionServiceUrl + "/depositos/" + depositoId;
            ResponseEntity<DepositoResponse> response = restTemplate.getForEntity(url, DepositoResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener depósito con ID: " + depositoId, e);
        }
    }
}