package utnfc.isi.back.sim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Tarifa;
import utnfc.isi.back.sim.service.TarifaService;

import java.util.List;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {
    private final TarifaService tarifaService;

    @Autowired
    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @GetMapping
    public ResponseEntity<List<Tarifa>> all() { return ResponseEntity.ok(tarifaService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> get(@PathVariable Long id) {
        return tarifaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tarifa> create(@RequestBody Tarifa t) { return ResponseEntity.ok(tarifaService.save(t)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tarifaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
