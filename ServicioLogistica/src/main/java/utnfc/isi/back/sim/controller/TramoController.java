package utnfc.isi.back.sim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Tramo;
import utnfc.isi.back.sim.service.TramoService;
import utnfc.isi.back.sim.dto.AsignarCamionRequest;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de tramos
 * Implementa los endpoints definidos en los lineamientos del TP
 */
@RestController
@RequestMapping("/tramos")
@CrossOrigin(origins = "*")
public class TramoController {
    
    private final TramoService tramoService;

    @Autowired
    public TramoController(TramoService tramoService) {
        this.tramoService = tramoService;
    }
    
    /**
     * GET /tramos - Listar todos los tramos
     */
    @GetMapping
    public ResponseEntity<List<Tramo>> listarTramos(
            @RequestParam(value = "estado", required = false) Tramo.EstadoTramo estado,
            @RequestParam(value = "camionId", required = false) Long camionId) {
        // Log removed for Docker compatibility
        
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
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /tramos/{id} - Consultar un tramo específico
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tramo> consultarTramo(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            return tramoService.findById(id)
                    .map(tramo -> ResponseEntity.ok(tramo))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id} - Actualizar tramo (costo real, hora fin, etc.)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Tramo> actualizarTramo(@PathVariable Long id, @Valid @RequestBody Tramo tramo) {
        // Log removed for Docker compatibility
        
        try {
            Tramo tramoActualizado = tramoService.update(id, tramo);
            return ResponseEntity.ok(tramoActualizado);
        } catch (RuntimeException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * FASE 4: PUT /tramos/{id}/asignar - Asignar tramo a un camión
     * El operador asigna un camión específico a un tramo pendiente
     */
    @PutMapping("/{id}/asignar")
    public ResponseEntity<Tramo> asignarCamion(@PathVariable Long id, @RequestParam Long camionId) {
        // Log removed for Docker compatibility
        
        try {
            Tramo tramoAsignado = tramoService.asignarCamion(id, camionId);
            return ResponseEntity.ok(tramoAsignado);
        } catch (IllegalArgumentException e) {
            // Error de validación (estado incorrecto, etc.)
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            // Error de negocio (tramo no encontrado, camión no existe, etc.)
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Error interno del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * FASE 4 ALTERNATIVA: PUT /tramos/{id}/camion - Asignar camión (diseño más RESTful)
     */
    @PutMapping("/{id}/camion")
    public ResponseEntity<Tramo> asignarCamionAlternativo(@PathVariable Long id, @RequestBody AsignarCamionRequest request) {
        try {
            Tramo tramoAsignado = tramoService.asignarCamion(id, request.getCamionId());
            return ResponseEntity.ok(tramoAsignado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id}/iniciar - Iniciar tramo (transportista registra inicio)
     */
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<Tramo> iniciarTramo(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            Tramo tramoIniciado = tramoService.iniciarTramo(id);
            return ResponseEntity.ok(tramoIniciado);
        } catch (IllegalStateException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id}/finalizar - Finalizar tramo (transportista registra fin y costo)
     */
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Tramo> finalizarTramo(@PathVariable Long id, @RequestParam Double costoReal) {
        // Log removed for Docker compatibility
        
        try {
            Tramo tramoFinalizado = tramoService.finalizarTramo(id, costoReal);
            return ResponseEntity.ok(tramoFinalizado);
        } catch (IllegalStateException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /tramos/{id}/estado - Actualizar estado de tramo
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Tramo> actualizarEstadoTramo(@PathVariable Long id, @RequestParam Tramo.EstadoTramo estado) {
        // Log removed for Docker compatibility
        
        try {
            Tramo tramoActualizado = tramoService.actualizarEstado(id, estado);
            return ResponseEntity.ok(tramoActualizado);
        } catch (RuntimeException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /tramos/pendientes - Obtener tramos pendientes de asignación
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<Tramo>> listarTramosPendientes() {
        // Log removed for Docker compatibility
        
        try {
            List<Tramo> tramosPendientes = tramoService.findTramosPendientesDeAsignacion();
            return ResponseEntity.ok(tramosPendientes);
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * DELETE /tramos/{id} - Eliminar tramo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTramo(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            tramoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
