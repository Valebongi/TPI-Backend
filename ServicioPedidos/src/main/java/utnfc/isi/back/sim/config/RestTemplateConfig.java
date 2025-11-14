package utnfc.isi.back.sim.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuración de RestTemplate con interceptor para autenticación automática
 * en llamadas internas entre servicios
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
                    System.out.println("🔍 INTERCEPTOR: Verificando URL: " + url);
                    if (isInternalServiceCall(url)) {
                        System.out.println("✅ INTERCEPTOR: URL detectada como interna, obteniendo token...");
                        String token = tokenProvider.getToken();
                        if (token != null) {
                            System.out.println("✅ INTERCEPTOR: Token obtenido, añadiendo a headers");
                            request.getHeaders().add("Authorization", "Bearer " + token);
                        } else {
                            System.out.println("❌ INTERCEPTOR: Token es NULL");
                        }
                    } else {
                        System.out.println("⚠️ INTERCEPTOR: URL NO detectada como interna");
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
        // No agregar token para endpoints /interno que no requieren autenticación
        if (url.contains("/interno")) {
            return false;
        }
        
        return url.contains("/api/admin") || 
               url.contains("/api/logistica") || 
               url.contains("tpi-api-gateway") ||
               url.contains("servicio-administracion") || 
               url.contains("servicio-logistica") ||
               url.contains("localhost:8080/api/admin");
    }
}