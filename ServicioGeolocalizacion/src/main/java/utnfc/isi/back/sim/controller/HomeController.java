package utnfc.isi.back.sim.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("servicio", "Servicio de Geolocalización");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("descripcion", "Microservicio para cálculo de distancias usando Google Maps Distance Matrix API");
        response.put("puerto", 8084);
        
        Map<String, Object> endpoints = new HashMap<>();
        endpoints.put("distancia", "GET /api/distancia?origen={lat,lng}&destino={lat,lng} - Calcular distancia entre dos puntos");
        endpoints.put("swagger", "GET /doc/swagger-ui.html - Documentación interactiva de la API");
        endpoints.put("health", "GET /actuator/health - Estado del servicio");
        
        response.put("endpoints", endpoints);
        
        Map<String, String> ejemplos = new HashMap<>();
        ejemplos.put("Córdoba a Mendoza", "/api/distancia?origen=-31.4167,-64.1833&destino=-32.8908,-68.8272");
        ejemplos.put("Buenos Aires a Córdoba", "/api/distancia?origen=-34.6118,-58.3960&destino=-31.4167,-64.1833");
        
        response.put("ejemplos", ejemplos);
        
        response.put("swagger_ui", "http://localhost:8084/doc/swagger-ui.html");
        
        return response;
    }
}