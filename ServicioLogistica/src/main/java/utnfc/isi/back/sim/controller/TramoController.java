package utnfc.isi.back.sim.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Tramo;
import utnfc.isi.back.sim.service.TramoService;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de tramos
 * Implementa los endpoints definidos en los lineamientos del TP
 */
@RestController
@RequestMapping("/api/tramos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TramoController {
    
    private final TramoService tramoService;
    
    /**
     * GET /tramos - Listar todos los tramos
     */
    @GetMapping
    public ResponseEntity<List<Tramo>> listarTramos(
            @RequestParam(required = false) Tramo.EstadoTramo estado,
            @RequestParam(required = false) Long camionId) {
        log.info("GET /api/tramos - Listando tramos con estado: {} y camión: {}", estado, camionId);
        
        try {
            List<Tramo> tramos;
            
            if (estado != null && camionId != null) {
                tramos = tramoService.findByCamionId(camionId).stream()
                        .filter(tramo -> tramo.getEstado() == estado)
                        .toList();
            } else if (estado != null) {
                tramos = tramoService.findByEstado(estado);
            } else if (camionId != null) {
                tramos = tramoService.findByCamionId(camionId);
            } else {
                tramos = tramoService.findAll();
            }
            
            return ResponseEntity.ok(tramos);
        } catch (Exception e) {
            log.error("Error al listar tramos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /tramos/{id} - Consultar un tramo específico
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tramo> consultarTramo(@PathVariable Long id) {
        log.info("GET /api/tramos/{} - Consultando tramo por ID", id);
        
        try {
            return tramoService.findById(id)
                    .map(tramo -> ResponseEntity.ok(tramo))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al consultar tramo ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id} - Actualizar tramo (costo real, hora fin, etc.)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tramo> actualizarTramo(@PathVariable Long id, @Valid @RequestBody Tramo tramo) {
        log.info("PUT /api/tramos/{} - Actualizando tramo", id);
        
        try {
            Tramo tramoActualizado = tramoService.update(id, tramo);
            return ResponseEntity.ok(tramoActualizado);
        } catch (RuntimeException e) {
            log.warn("Error al actualizar tramo ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar tramo ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id}/asignar - Asignar tramo a un camión
     */
    @PutMapping("/{id}/asignar")
    public ResponseEntity<Tramo> asignarCamion(@PathVariable Long id, @RequestParam Long camionId) {
        log.info("PUT /api/tramos/{}/asignar - Asignando al camión ID: {}", id, camionId);
        
        try {
            Tramo tramoAsignado = tramoService.asignarCamion(id, camionId);
            return ResponseEntity.ok(tramoAsignado);
        } catch (RuntimeException e) {
            log.warn("Error al asignar tramo ID {} al camión {}: {}", id, camionId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error al asignar tramo ID {} al camión {}: {}", id, camionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id}/iniciar - Iniciar tramo (transportista registra inicio)
     */
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<Tramo> iniciarTramo(@PathVariable Long id) {
        log.info("PUT /api/tramos/{}/iniciar - Iniciando tramo", id);
        
        try {
            Tramo tramoIniciado = tramoService.iniciarTramo(id);
            return ResponseEntity.ok(tramoIniciado);
        } catch (IllegalStateException e) {
            log.warn("Error de estado al iniciar tramo ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("Error al iniciar tramo ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al iniciar tramo ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id}/finalizar - Finalizar tramo (transportista registra fin y costo)
     */
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Tramo> finalizarTramo(@PathVariable Long id, @RequestParam Double costoReal) {
        log.info("PUT /api/tramos/{}/finalizar - Finalizando tramo con costo: {}", id, costoReal);
        
        try {
            Tramo tramoFinalizado = tramoService.finalizarTramo(id, costoReal);
            return ResponseEntity.ok(tramoFinalizado);
        } catch (IllegalStateException e) {
            log.warn("Error de estado al finalizar tramo ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("Error al finalizar tramo ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al finalizar tramo ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id}/estado - Actualizar estado de tramo
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Tramo> actualizarEstadoTramo(@PathVariable Long id, @RequestParam Tramo.EstadoTramo estado) {
        log.info("PUT /api/tramos/{}/estado - Actualizando estado a: {}", id, estado);
        
        try {
            Tramo tramoActualizado = tramoService.actualizarEstado(id, estado);
            return ResponseEntity.ok(tramoActualizado);
        } catch (RuntimeException e) {
            log.warn("Error al actualizar estado de tramo ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar estado de tramo ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /tramos/pendientes - Obtener tramos pendientes de asignación
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<Tramo>> listarTramosPendientes() {
        log.info("GET /api/tramos/pendientes - Listando tramos pendientes de asignación");
        
        try {
            List<Tramo> tramosPendientes = tramoService.findTramosPendientesDeAsignacion();
            return ResponseEntity.ok(tramosPendientes);
        } catch (Exception e) {
            log.error("Error al listar tramos pendientes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * DELETE /tramos/{id} - Eliminar tramo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTramo(@PathVariable Long id) {
        log.info("DELETE /api/tramos/{} - Eliminando tramo", id);
        
        try {
            tramoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Error al eliminar tramo ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al eliminar tramo ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}