package utnfc.isi.back.sim.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Proveedor de tokens automático para comunicación entre servicios
 * Implementa Client Credentials Flow de OAuth2 con Keycloak
 * 
 * Características:
 * - Cache de tokens con renovación automática
 * - Thread-safe para uso concurrente
 * - Configuración via properties
 */
@Component
@ConditionalOnProperty(name = "keycloak.service-account.client-id")
public class TokenProvider {

    @Value("${keycloak.service-account.client-id}")
    private String clientId;

    @Value("${keycloak.service-account.client-secret}")
    private String clientSecret;

    @Value("${keycloak.service-account.token-uri-external:${keycloak.service-account.token-uri}}")
    private String tokenUri;

    @Value("${keycloak.service-account.username:}")
    private String username;

    @Value("${keycloak.service-account.password:}")
    private String password;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    // Cache thread-safe del token
    private volatile String cachedToken;
    private volatile LocalDateTime tokenExpiry;
    
    public TokenProvider() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Obtiene un token válido, renovándolo si es necesario
     * @return Bearer token válido
     */
    public synchronized String getToken() {
        if (isTokenValid()) {
            return cachedToken;
        }
        
        return requestNewToken();
    }

    /**
     * Verifica si el token actual sigue siendo válido
     */
    private boolean isTokenValid() {
        return cachedToken != null && 
               tokenExpiry != null && 
               LocalDateTime.now().plusMinutes(5).isBefore(tokenExpiry); // Renovar 5 min antes
    }

    /**
     * Solicita un nuevo token a Keycloak usando Client Credentials Flow
     */
    private String requestNewToken() {
        try {
            // Preparar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Preparar body - usar password flow si está configurado, sino client_credentials
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                // Password Flow (funciona con tpi-backend-client)
                body.add("grant_type", "password");
                body.add("client_id", clientId);
                body.add("username", username);
                body.add("password", password);
                body.add("scope", "openid profile email");
            } else {
                // Client Credentials Flow (requiere service account configurado)
                body.add("grant_type", "client_credentials");
                body.add("client_id", clientId);
                body.add("client_secret", clientSecret);
            }

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            // Hacer la petición
            ResponseEntity<String> response = restTemplate.exchange(
                tokenUri, 
                HttpMethod.POST, 
                request, 
                String.class
            );

            // Parsear respuesta
            TokenResponse tokenResponse = objectMapper.readValue(
                response.getBody(), 
                TokenResponse.class
            );

            // Actualizar cache
            cachedToken = tokenResponse.accessToken;
            tokenExpiry = LocalDateTime.now().plusSeconds(tokenResponse.expiresIn);
            
            return cachedToken;

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo token service account: " + e.getMessage(), e);
        }
    }

    /**
     * DTO para respuesta del token endpoint
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
        
        @JsonProperty("expires_in")
        private long expiresIn;
        
        @JsonProperty("token_type")
        private String tokenType;

        // Getters y setters
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        
        public long getExpiresIn() { return expiresIn; }
        public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
        
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    }
}