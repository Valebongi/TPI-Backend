package utnfc.isi.back.sim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Deposito;
import utnfc.isi.back.sim.service.DepositoService;

import java.util.List;

@RestController
@RequestMapping("/depositos")
@RequiredArgsConstructor
public class DepositoController {
    private final DepositoService depositoService;

    @GetMapping
    public ResponseEntity<List<Deposito>> all() { return ResponseEntity.ok(depositoService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Deposito> get(@PathVariable Integer id) {
        return depositoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Deposito> create(@RequestBody Deposito d) { return ResponseEntity.ok(depositoService.save(d)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        depositoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
