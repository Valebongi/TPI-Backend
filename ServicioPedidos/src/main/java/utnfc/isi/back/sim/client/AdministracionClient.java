package utnfc.isi.back.sim.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

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
     * Obtiene un depósito por su ID usando endpoint interno (sin autenticación)
     */
    public DepositoResponse obtenerDeposito(Integer depositoId) {
        try {
            String url = administracionServiceUrl + "/depositos/" + depositoId + "/interno";
            System.out.println("=== CLIENTE ADMIN: Usando endpoint interno para depósito ID: " + depositoId + " ===");
            ResponseEntity<DepositoResponse> response = restTemplate.getForEntity(url, DepositoResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener depósito con ID: " + depositoId, e);
        }
    }
    
    /**
     * Obtiene un depósito por su ID usando el token del contexto actual
     */
    public DepositoResponse obtenerDepositoConToken(Integer depositoId) {
        try {
            String url = administracionServiceUrl + "/depositos/" + depositoId;
            
            // Obtener el token del contexto de seguridad actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = null;
            
            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();
                token = jwt.getTokenValue();
            }
            
            HttpHeaders headers = new HttpHeaders();
            if (token != null) {
                headers.set("Authorization", "Bearer " + token);
                System.out.println("Usando token del usuario para llamada interna: Bearer " + token.substring(0, Math.min(50, token.length())) + "...");
            }
            
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<DepositoResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, DepositoResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener depósito con ID: " + depositoId, e);
        }
    }
}