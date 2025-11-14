package utnfc.isi.back.sim.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuración de RestTemplate para comunicación entre microservicios
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(@Autowired(required = false) TokenProvider tokenProvider) {
        RestTemplate restTemplate = new RestTemplate();
        
        // Solo añadir interceptor si TokenProvider está disponible
        if (tokenProvider != null) {
            ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
                try {
                    String url = request.getURI().toString();
                    
                    // Solo agregar token para llamadas internas (servicios administrativos)
                    if (isInternalServiceCall(url)) {
                        String token = tokenProvider.getToken();
                        if (token != null) {
                            request.getHeaders().add("Authorization", "Bearer " + token);
                        }
                    }
                } catch (Exception e) {
                    // Continúa sin token en caso de error (no rompe funcionalidad existente)
                }
                
                return execution.execute(request, body);
            };

            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
            interceptors.add(interceptor);
            restTemplate.setInterceptors(interceptors);
        }
        
        return restTemplate;
    }

    /**
     * Determina si la URL corresponde a una llamada interna que requiere autenticación
     */
    private boolean isInternalServiceCall(String url) {
        return url.contains("/api/admin") || 
               url.contains("/api/logistica") || 
               url.contains("servicio-administracion") || 
               url.contains("servicio-logistica") ||
               url.contains("localhost:8080/api/admin");
    }
}