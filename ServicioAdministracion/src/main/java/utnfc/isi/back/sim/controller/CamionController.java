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
}
