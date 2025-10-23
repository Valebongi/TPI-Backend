package utnfc.isi.back.sim.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> info = new HashMap<>();
        info.put("servicio", "ServicioAdministracion - Fleet & Master Data MS");
        info.put("version", "1.0.0");
        info.put("estado", "FUNCIONANDO");
        info.put("endpoints", Map.of(
            "camiones", "/camiones",
            "depositos", "/depositos", 
            "tarifas", "/tarifas",
            "parametros", "/parametros",
            "health", "/health"
        ));
        return info;
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "servicio", "ServicioAdministracion");
    }
}