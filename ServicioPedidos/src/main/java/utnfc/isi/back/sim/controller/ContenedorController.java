package utnfc.isi.back.sim.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Contenedor;
import utnfc.isi.back.sim.service.ContenedorService;

import java.util.List;

@RestController
@RequestMapping("/contenedores")
@RequiredArgsConstructor
@Slf4j
public class ContenedorController {
    
    private final ContenedorService contenedorService;
    
    @GetMapping
    public ResponseEntity<List<Contenedor>> getAllContenedores(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String filtro) {
        
        log.info("GET /contenedores - clienteId: {}, estado: {}, filtro: {}", clienteId, estado, filtro);
        
        List<Contenedor> contenedores;
        
        if (filtro != null && !filtro.trim().isEmpty()) {
            contenedores = contenedorService.findByFiltro(filtro);
        } else if (clienteId != null && estado != null) {
            contenedores = contenedorService.findByClienteIdAndEstado(clienteId, estado);
        } else if (clienteId != null) {
            contenedores = contenedorService.findByClienteId(clienteId);
        } else if (estado != null) {
            contenedores = contenedorService.findByEstado(estado);
        } else {
            contenedores = contenedorService.findAll();
        }
        
        return ResponseEntity.ok(contenedores);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Contenedor> getContenedorById(@PathVariable Long id) {
        log.info("GET /contenedores/{}", id);
        
        return contenedorService.findById(id)
                .map(contenedor -> ResponseEntity.ok(contenedor))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Contenedor> getContenedorByCodigo(@PathVariable String codigo) {
        log.info("GET /contenedores/codigo/{}", codigo);
        
        return contenedorService.findByCodigo(codigo)
                .map(contenedor -> ResponseEntity.ok(contenedor))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Contenedor> createContenedor(@Valid @RequestBody Contenedor contenedor) {
        log.info("POST /contenedores - {}", contenedor.getCodigo());
        
        try {
            Contenedor nuevoContenedor = contenedorService.save(contenedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContenedor);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear contenedor: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Contenedor> updateContenedor(@PathVariable Long id, @Valid @RequestBody Contenedor contenedor) {
        log.info("PUT /contenedores/{}", id);
        
        try {
            Contenedor contenedorActualizado = contenedorService.update(id, contenedor);
            return ResponseEntity.ok(contenedorActualizado);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar contenedor: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Contenedor> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        log.info("PATCH /contenedores/{}/estado - nuevo estado: {}", id, estado);
        
        try {
            Contenedor contenedorActualizado = contenedorService.actualizarEstado(id, estado);
            return ResponseEntity.ok(contenedorActualizado);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar estado del contenedor: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenedor(@PathVariable Long id) {
        log.info("DELETE /contenedores/{}", id);
        
        try {
            contenedorService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar contenedor: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}