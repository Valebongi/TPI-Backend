package utnfc.isi.back.sim.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Ruta;
import utnfc.isi.back.sim.domain.Tramo;
import utnfc.isi.back.sim.service.RutaService;
import utnfc.isi.back.sim.service.TramoService;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de rutas
 * Implementa los endpoints definidos en los lineamientos del TP
 */
@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class RutaController {
    
    private final RutaService rutaService;
    private final TramoService tramoService;
    
    /**
     * GET /rutas - Listar rutas activas
     */
    @GetMapping
    public ResponseEntity<List<Ruta>> listarRutas() {
        log.info("GET /api/rutas - Listando rutas activas");
        
        try {
            List<Ruta> rutas = rutaService.findRutasActivas();
            return ResponseEntity.ok(rutas);
        } catch (Exception e) {
            log.error("Error al listar rutas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /rutas/{id} - Consultar detalle de una ruta
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ruta> consultarRuta(@PathVariable Long id) {
        log.info("GET /api/rutas/{} - Consultando ruta por ID", id);
        
        try {
            return rutaService.findById(id)
                    .map(ruta -> ResponseEntity.ok(ruta))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al consultar ruta ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * POST /rutas - Crear una nueva ruta asociada a una solicitud
     */
    @PostMapping
    public ResponseEntity<Ruta> crearRuta(@Valid @RequestBody Ruta ruta) {
        log.info("POST /api/rutas - Creando nueva ruta para solicitud ID: {}", ruta.getSolicitudId());
        
        try {
            Ruta rutaCreada = rutaService.save(ruta);
            return ResponseEntity.status(HttpStatus.CREATED).body(rutaCreada);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear ruta: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error al crear ruta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /rutas/{id} - Actualizar estado o información de la ruta
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ruta> actualizarRuta(@PathVariable Long id, @Valid @RequestBody Ruta ruta) {
        log.info("PUT /api/rutas/{} - Actualizando ruta", id);
        
        try {
            Ruta rutaActualizada = rutaService.update(id, ruta);
            return ResponseEntity.ok(rutaActualizada);
        } catch (RuntimeException e) {
            log.warn("Error al actualizar ruta ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar ruta ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /rutas/{id}/tramos - Listar tramos de una ruta
     */
    @GetMapping("/{id}/tramos")
    public ResponseEntity<List<Tramo>> listarTramos(@PathVariable Long id) {
        log.info("GET /api/rutas/{}/tramos - Listando tramos de la ruta", id);
        
        try {
            // Verificar que la ruta exista
            if (rutaService.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            List<Tramo> tramos = tramoService.findByRutaId(id);
            return ResponseEntity.ok(tramos);
        } catch (Exception e) {
            log.error("Error al listar tramos de ruta ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * POST /rutas/{id}/tramos - Agregar un tramo nuevo a una ruta
     */
    @PostMapping("/{id}/tramos")
    public ResponseEntity<Tramo> agregarTramo(@PathVariable Long id, @Valid @RequestBody Tramo tramo) {
        log.info("POST /api/rutas/{}/tramos - Agregando tramo a la ruta", id);
        
        try {
            // Verificar que la ruta exista
            return rutaService.findById(id)
                    .map(ruta -> {
                        tramo.setRuta(ruta);
                        Tramo tramoCreado = tramoService.save(tramo);
                        
                        // Actualizar cantidad de tramos en la ruta
                        rutaService.calcularCostoTotal(id);
                        
                        return ResponseEntity.status(HttpStatus.CREATED).body(tramoCreado);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al agregar tramo: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error al agregar tramo a ruta ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /rutas/{id}/estado - Actualizar estado de la ruta
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Ruta> actualizarEstadoRuta(@PathVariable Long id, @RequestParam Ruta.EstadoRuta estado) {
        log.info("PUT /api/rutas/{}/estado - Actualizando estado a: {}", id, estado);
        
        try {
            Ruta rutaActualizada = rutaService.actualizarEstado(id, estado);
            return ResponseEntity.ok(rutaActualizada);
        } catch (RuntimeException e) {
            log.warn("Error al actualizar estado de ruta ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al actualizar estado de ruta ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /rutas/solicitud/{solicitudId} - Obtener ruta por ID de solicitud
     */
    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<Ruta> consultarRutaPorSolicitud(@PathVariable Long solicitudId) {
        log.info("GET /api/rutas/solicitud/{} - Consultando ruta por solicitud ID", solicitudId);
        
        try {
            return rutaService.findBySolicitudId(solicitudId)
                    .map(ruta -> ResponseEntity.ok(ruta))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al consultar ruta por solicitud ID {}: {}", solicitudId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * DELETE /rutas/{id} - Eliminar ruta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRuta(@PathVariable Long id) {
        log.info("DELETE /api/rutas/{} - Eliminando ruta", id);
        
        try {
            rutaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Error al eliminar ruta ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al eliminar ruta ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}