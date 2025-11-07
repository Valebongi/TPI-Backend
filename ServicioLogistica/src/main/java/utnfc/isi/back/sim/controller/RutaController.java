package utnfc.isi.back.sim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Ruta;
import utnfc.isi.back.sim.domain.Tramo;
import utnfc.isi.back.sim.service.RutaService;
import utnfc.isi.back.sim.service.TramoService;
import utnfc.isi.back.sim.dto.RutaCreacionRequest;
import utnfc.isi.back.sim.dto.RutaCreacionResponse;
import utnfc.isi.back.sim.dto.RutaTentativa;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de rutas
 * Implementa los endpoints definidos en los lineamientos del TP
 */
@RestController
@RequestMapping("/rutas")
@CrossOrigin(origins = "*")
public class RutaController {
    
    private final RutaService rutaService;
    private final TramoService tramoService;

    @Autowired
    public RutaController(RutaService rutaService, TramoService tramoService) {
        this.rutaService = rutaService;
        this.tramoService = tramoService;
    }
    
    /**
     * GET /rutas/tentativas - Calcular rutas tentativas usando Google Maps
     * FASE 2: El operador solicita rutas tentativas para evaluar opciones
     */
    @GetMapping("/tentativas")
    public ResponseEntity<List<RutaTentativa>> calcularRutasTentativas(
            @RequestParam("origen") String origen,
            @RequestParam("destino") String destino) {
        
        try {
            List<RutaTentativa> rutasTentativas = rutaService.calcularRutasTentativas(origen, destino);
            return ResponseEntity.ok(rutasTentativas);
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al calcular rutas tentativas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /rutas - Listar rutas activas
     */
    @GetMapping
    public ResponseEntity<List<Ruta>> listarRutas() {
        // Log removed for Docker compatibility
        
        try {
            List<Ruta> rutas = rutaService.findRutasActivas();
            return ResponseEntity.ok(rutas);
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al listar rutas
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /rutas/{id} - Consultar detalle de una ruta
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ruta> consultarRuta(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            return rutaService.findById(id)
                    .map(ruta -> ResponseEntity.ok(ruta))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al consultar ruta
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * POST /rutas - Crear una nueva ruta asociada a una solicitud
     */
    @PostMapping
    public ResponseEntity<Ruta> crearRuta(@Valid @RequestBody Ruta ruta) {
        // Log removed for Docker compatibility: Creando nueva ruta
        
        try {
            Ruta rutaCreada = rutaService.save(ruta);
            return ResponseEntity.status(HttpStatus.CREATED).body(rutaCreada);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility: Error de validación al crear ruta
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al crear ruta
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * POST /rutas/crear-automatica - Crear ruta automáticamente desde otros microservicios
     * Este endpoint permite la creación automática de rutas cuando se crean solicitudes
     */
    @PostMapping("/crear-automatica")
    public ResponseEntity<RutaCreacionResponse> crearRutaAutomatica(@Valid @RequestBody RutaCreacionRequest request) {
        // Log removed for Docker compatibility: Creando ruta automática para solicitud
        
        try {
            Ruta rutaCreada = rutaService.crearRutaAutomatica(request);
            
            RutaCreacionResponse response = new RutaCreacionResponse(
                    rutaCreada.getId(),
                    rutaCreada.getSolicitudId(),
                    rutaCreada.getEstado().toString(),
                    rutaCreada.getCostoTotalAproximado() != null ? 
                        new java.math.BigDecimal(rutaCreada.getCostoTotalAproximado().toString()) : null,
                    rutaCreada.getObservaciones()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility: Error de validación al crear ruta automática
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al crear ruta automática
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * POST /rutas/desde-solicitud/{solicitudId} - Crear ruta automáticamente desde ID de solicitud
     * FASE 3: Crear ruta y cambiar solicitud a estado PROGRAMADA
     */
    @PostMapping("/desde-solicitud/{solicitudId}")
    public ResponseEntity<RutaCreacionResponse> crearRutaDesdeSolicitud(@PathVariable Long solicitudId) {
        System.out.println("=== CONTROLADOR RUTAS: Creando ruta desde solicitud ID: " + solicitudId + " ===");
        
        try {
            Ruta rutaCreada = rutaService.crearRutaDesdeSolicitud(solicitudId);
            
            RutaCreacionResponse response = new RutaCreacionResponse(
                    rutaCreada.getId(),
                    rutaCreada.getSolicitudId(),
                    rutaCreada.getEstado().toString(),
                    rutaCreada.getCostoTotalAproximado() != null ? 
                        new java.math.BigDecimal(rutaCreada.getCostoTotalAproximado().toString()) : null,
                    rutaCreada.getObservaciones()
            );
            
            System.out.println("=== CONTROLADOR RUTAS: Ruta creada exitosamente con ID: " + rutaCreada.getId() + " ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            System.out.println("=== CONTROLADOR RUTAS: Error de validación: " + e.getMessage() + " ===");
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("=== CONTROLADOR RUTAS: Error interno: " + e.getMessage() + " ===");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /rutas/{id} - Actualizar estado o información de la ruta
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ruta> actualizarRuta(@PathVariable Long id, @Valid @RequestBody Ruta ruta) {
        // Log removed for Docker compatibility
        
        try {
            Ruta rutaActualizada = rutaService.update(id, ruta);
            return ResponseEntity.ok(rutaActualizada);
        } catch (RuntimeException e) {
            // Log removed for Docker compatibility: Error al actualizar ruta
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al actualizar ruta
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /rutas/{id}/tramos - Listar tramos de una ruta
     */
    @GetMapping("/{id}/tramos")
    public ResponseEntity<List<Tramo>> listarTramos(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            // Verificar que la ruta exista
            if (rutaService.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            List<Tramo> tramos = tramoService.findByRutaId(id);
            return ResponseEntity.ok(tramos);
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al listar tramos de ruta
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * POST /rutas/{id}/tramos - Agregar un tramo nuevo a una ruta
     */
    @PostMapping("/{id}/tramos")
    public ResponseEntity<Tramo> agregarTramo(@PathVariable Long id, @Valid @RequestBody Tramo tramo) {
        // Log removed for Docker compatibility
        
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
            // Log removed for Docker compatibility: Error de validación al agregar tramo
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al agregar tramo a ruta
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /rutas/{id}/estado - Actualizar estado de la ruta
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Ruta> actualizarEstadoRuta(@PathVariable Long id, @RequestParam Ruta.EstadoRuta estado) {
        // Log removed for Docker compatibility
        
        try {
            Ruta rutaActualizada = rutaService.actualizarEstado(id, estado);
            return ResponseEntity.ok(rutaActualizada);
        } catch (RuntimeException e) {
            // Log removed for Docker compatibility: Error al actualizar estado de ruta
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al actualizar estado de ruta
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * GET /rutas/solicitud/{solicitudId} - Obtener ruta por ID de solicitud
     */
    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<Ruta> consultarRutaPorSolicitud(@PathVariable Long solicitudId) {
        // Log removed for Docker compatibility
        
        try {
            return rutaService.findBySolicitudId(solicitudId)
                    .map(ruta -> ResponseEntity.ok(ruta))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al consultar ruta por solicitud
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * DELETE /rutas/{id} - Eliminar ruta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRuta(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            rutaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Log removed for Docker compatibility: Error al eliminar ruta
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility: Error al eliminar ruta
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
