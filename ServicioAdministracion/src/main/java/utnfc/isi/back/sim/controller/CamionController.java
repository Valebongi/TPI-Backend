package utnfc.isi.back.sim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Camion;
import utnfc.isi.back.sim.service.CamionService;

import java.util.List;

@RestController
@RequestMapping("/camiones")
@RequiredArgsConstructor
public class CamionController {
    private final CamionService camionService;

    @GetMapping
    public ResponseEntity<List<Camion>> all() {
        return ResponseEntity.ok(camionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Camion> get(@PathVariable Integer id) {
        return camionService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Camion> create(@RequestBody Camion camion) {
        return ResponseEntity.ok(camionService.save(camion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        camionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
