package utnfc.isi.back.sim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Camion;
import utnfc.isi.back.sim.service.CamionService;

import java.util.List;

@RestController
@RequestMapping("/camiones")
public class CamionController {
    private final CamionService camionService;

    @Autowired
    public CamionController(CamionService camionService) {
        this.camionService = camionService;
    }

    @GetMapping
    public ResponseEntity<List<Camion>> all() {
        return ResponseEntity.ok(camionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Camion> get(@PathVariable Long id) {
        return camionService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Camion> create(@RequestBody Camion camion) {
        return ResponseEntity.ok(camionService.save(camion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        camionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ENDPOINT INTERNO: Para uso interno entre servicios (sin autenticación)
    @GetMapping("/{id}/interno")
    public ResponseEntity<Camion> getInterno(@PathVariable Long id) {
        System.out.println("=== SERVICIO ADMIN: Consulta interna de camión ID: " + id + " ===");
        return camionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ENDPOINT INTERNO: Validar disponibilidad del camión
    @GetMapping("/{id}/disponible/interno")
    public ResponseEntity<Boolean> validarDisponibilidadInterno(@PathVariable Long id) {
        System.out.println("=== SERVICIO ADMIN: Validación interna de disponibilidad camión ID: " + id + " ===");
        boolean disponible = camionService.findById(id)
                .map(camion -> "DISPONIBLE".equals(camion.getEstado()))
                .orElse(false);
        return ResponseEntity.ok(disponible);
    }

    // ENDPOINT INTERNO: Cambiar estado del camión (sin autenticación)
    @PutMapping("/{id}/estado/interno")
    public ResponseEntity<Camion> cambiarEstadoInterno(@PathVariable Long id, @RequestBody EstadoCamionRequest request) {
        System.out.println("=== SERVICIO ADMIN: Cambio interno de estado camión ID: " + id + " a estado: " + request.getEstado() + " ===");
        return camionService.findById(id)
                .map(camion -> {
                    camion.setEstado(request.getEstado());
                    return ResponseEntity.ok(camionService.save(camion));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DTO para cambio de estado
    public static class EstadoCamionRequest {
        private String estado;
        
        public EstadoCamionRequest() {}
        
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}
