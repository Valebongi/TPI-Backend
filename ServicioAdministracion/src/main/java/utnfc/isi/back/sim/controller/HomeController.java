package utnfc.isi.back.sim.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "ServicioAdministracion está funcionando!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}