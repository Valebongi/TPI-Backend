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
            System.out.println("=== DEBUG AdministracionClient ===");
            System.out.println("URL construida: " + url);
            System.out.println("Base URL: " + administracionServiceUrl);
            System.out.println("Depósito ID: " + depositoId);
            
            ResponseEntity<DepositoResponse> response = restTemplate.getForEntity(url, DepositoResponse.class);
            
            System.out.println("Código de respuesta: " + response.getStatusCode());
            System.out.println("Cuerpo de respuesta: " + response.getBody());
            
            return response.getBody();
        } catch (Exception e) {
            System.err.println("=== ERROR en AdministracionClient ===");
            System.err.println("Error al obtener depósito: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener depósito con ID: " + depositoId, e);
        }
    }
}