package utnfc.isi.back.sim.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Contenedor;
import utnfc.isi.back.sim.service.ContenedorService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contenedores")
public class ContenedorController {
    
    private final ContenedorService contenedorService;

    @Autowired
    public ContenedorController(ContenedorService contenedorService) {
        this.contenedorService = contenedorService;
    }
    
    @GetMapping
    public ResponseEntity<List<Contenedor>> getAllContenedores(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String filtro) {
        
        // Log removed for Docker compatibility
        
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
        // Log removed for Docker compatibility
        
        return contenedorService.findById(id)
                .map(contenedor -> ResponseEntity.ok(contenedor))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Contenedor> getContenedorByCodigo(@PathVariable String codigo) {
        // Log removed for Docker compatibility
        
        return contenedorService.findByCodigo(codigo)
                .map(contenedor -> ResponseEntity.ok(contenedor))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Contenedor> createContenedor(@Valid @RequestBody Contenedor contenedor) {
        // Log removed for Docker compatibility
        
        try {
            Contenedor nuevoContenedor = contenedorService.save(contenedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoContenedor);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Contenedor> updateContenedor(@PathVariable Long id, @Valid @RequestBody Contenedor contenedor) {
        // Log removed for Docker compatibility
        
        try {
            Contenedor contenedorActualizado = contenedorService.update(id, contenedor);
            return ResponseEntity.ok(contenedorActualizado);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Contenedor> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        // Log removed for Docker compatibility
        
        try {
            Contenedor contenedorActualizado = contenedorService.actualizarEstado(id, estado);
            return ResponseEntity.ok(contenedorActualizado);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContenedor(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            contenedorService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /contenedores/{id}/interno - Consulta interna de contenedor sin autenticación
     * Para uso interno entre servicios
     */
    @GetMapping("/{id}/interno")
    public ResponseEntity<Contenedor> getContenedorInterno(@PathVariable Long id) {
        System.out.println("=== SERVICIO PEDIDOS: Consulta interna de contenedor ID: " + id + " ===");
        
        try {
            Optional<Contenedor> contenedorOpt = contenedorService.findById(id);
            if (contenedorOpt.isPresent()) {
                return ResponseEntity.ok(contenedorOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
