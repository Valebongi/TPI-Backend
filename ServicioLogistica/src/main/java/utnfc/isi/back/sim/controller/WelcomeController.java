package utnfc.isi.back.sim.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de bienvenida e información del servicio
 */
@RestController
public class WelcomeController {
    
    /**
     * Endpoint de bienvenida en la ruta raíz
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Servicio de Logística");
        response.put("version", "1.0.0");
        response.put("status", "UP");
        response.put("description", "Microservicio para gestión de rutas y tramos de transporte");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("Rutas", "/api/rutas");
        endpoints.put("Tramos", "/api/tramos");
        endpoints.put("Health", "/actuator/health");
        endpoints.put("Info", "/actuator/info");
        
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint de información del servicio
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> serviceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "ServicioLogistica");
        info.put("description", "Microservicio de gestión logística y rutas");
        info.put("version", "1.0.0");
        info.put("port", 8082);
        info.put("database", "PostgreSQL - tpi_backend_logistics_db");
        
        return ResponseEntity.ok(info);
    }
}