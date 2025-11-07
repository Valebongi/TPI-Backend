package utnfc.isi.back.sim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de RestTemplate para comunicación entre microservicios
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // RestTemplate simple que soporta PATCH nativo desde Spring 5+
        return new RestTemplate();
    }
}