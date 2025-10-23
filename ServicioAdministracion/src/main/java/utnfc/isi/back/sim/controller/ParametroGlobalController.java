package utnfc.isi.back.sim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.ParametroGlobal;
import utnfc.isi.back.sim.service.ParametroGlobalService;

import java.util.List;

@RestController
@RequestMapping("/parametros")
@RequiredArgsConstructor
public class ParametroGlobalController {
    private final ParametroGlobalService parametroGlobalService;

    @GetMapping
    public ResponseEntity<List<ParametroGlobal>> all() { 
        return ResponseEntity.ok(parametroGlobalService.findAll()); 
    }

    @GetMapping("/{clave}")
    public ResponseEntity<ParametroGlobal> get(@PathVariable String clave) {
        return parametroGlobalService.findById(clave).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParametroGlobal> create(@RequestBody ParametroGlobal parametro) { 
        return ResponseEntity.ok(parametroGlobalService.save(parametro)); 
    }

    @PutMapping("/{clave}")
    public ResponseEntity<ParametroGlobal> update(@PathVariable String clave, @RequestBody ParametroGlobal parametro) {
        parametro.setClave(clave);
        return ResponseEntity.ok(parametroGlobalService.save(parametro));
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> delete(@PathVariable String clave) {
        parametroGlobalService.deleteById(clave);
        return ResponseEntity.noContent().build();
    }
}