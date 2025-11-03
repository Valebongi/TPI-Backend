package utnfc.isi.back.sim.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Contenedor;
import utnfc.isi.back.sim.domain.Solicitud;
import utnfc.isi.back.sim.service.SolicitudService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
@Slf4j
public class SolicitudController {
    
    private final SolicitudService solicitudService;
    
    @GetMapping
    public ResponseEntity<List<Solicitud>> getAllSolicitudes(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        log.info("GET /solicitudes - clienteId: {}, estado: {}, fechaInicio: {}, fechaFin: {}", 
                clienteId, estado, fechaInicio, fechaFin);
        
        List<Solicitud> solicitudes;
        
        if (fechaInicio != null && fechaFin != null) {
            solicitudes = solicitudService.findByFechaCreacion(fechaInicio, fechaFin);
        } else if (clienteId != null && estado != null) {
            solicitudes = solicitudService.findByClienteIdAndEstado(clienteId, estado);
        } else if (clienteId != null) {
            solicitudes = solicitudService.findByClienteId(clienteId);
        } else if (estado != null) {
            solicitudes = solicitudService.findByEstado(estado);
        } else {
            solicitudes = solicitudService.findAll();
        }
        
        return ResponseEntity.ok(solicitudes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> getSolicitudById(@PathVariable Long id) {
        log.info("GET /solicitudes/{}", id);
        
        return solicitudService.findById(id)
                .map(solicitud -> ResponseEntity.ok(solicitud))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/numero/{numero}")
    public ResponseEntity<Solicitud> getSolicitudByNumero(@PathVariable String numero) {
        log.info("GET /solicitudes/numero/{}", numero);
        
        return solicitudService.findByNumero(numero)
                .map(solicitud -> ResponseEntity.ok(solicitud))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<SeguimientoResponse> getSeguimiento(@PathVariable Long id) {
        log.info("GET /solicitudes/{}/seguimiento", id);
        
        return solicitudService.findById(id)
                .map(solicitud -> {
                    SeguimientoResponse seguimiento = SeguimientoResponse.builder()
                            .solicitudId(solicitud.getId())
                            .numero(solicitud.getNumero())
                            .estado(solicitud.getEstado())
                            .fechaCreacion(solicitud.getFechaCreacion())
                            .fechaProgramacion(solicitud.getFechaProgramacion())
                            .fechaInicioTransito(solicitud.getFechaInicioTransito())
                            .fechaEntrega(solicitud.getFechaEntrega())
                            .contenedorCodigo(solicitud.getContenedor().getCodigo())
                            .contenedorEstado(solicitud.getContenedor().getEstado())
                            .clienteNombre(solicitud.getCliente().getNombre() + " " + solicitud.getCliente().getApellido())
                            .costoEstimado(solicitud.getCostoEstimado())
                            .costoFinal(solicitud.getCostoFinal())
                            .tiempoEstimadoHoras(solicitud.getTiempoEstimadoHoras())
                            .tiempoRealHoras(solicitud.getTiempoRealHoras())
                            .build();
                    
                    return ResponseEntity.ok(seguimiento);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Solicitud> createSolicitud(@Valid @RequestBody CrearSolicitudRequest request) {
        log.info("POST /solicitudes - clienteId: {}", request.getClienteId());
        
        try {
            Solicitud nuevaSolicitud = solicitudService.crearSolicitud(request.getClienteId(), request.getContenedor());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear solicitud: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> updateSolicitud(@PathVariable Long id, @Valid @RequestBody Solicitud solicitud) {
        log.info("PUT /solicitudes/{}", id);
        
        try {
            Solicitud solicitudActualizada = solicitudService.update(id, solicitud);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar solicitud: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Solicitud> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        log.info("PATCH /solicitudes/{}/estado - nuevo estado: {}", id, estado);
        
        try {
            Solicitud solicitudActualizada = solicitudService.actualizarEstado(id, estado);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar estado de la solicitud: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        log.info("DELETE /solicitudes/{}", id);
        
        try {
            solicitudService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar solicitud: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasResponse> getEstadisticas() {
        log.info("GET /solicitudes/estadisticas");
        
        EstadisticasResponse estadisticas = EstadisticasResponse.builder()
                .totalBorradores(solicitudService.countByEstado("BORRADOR"))
                .totalProgramadas(solicitudService.countByEstado("PROGRAMADA"))
                .totalEnTransito(solicitudService.countByEstado("EN_TRANSITO"))
                .totalEntregadas(solicitudService.countByEstado("ENTREGADA"))
                .totalCanceladas(solicitudService.countByEstado("CANCELADA"))
                .build();
        
        return ResponseEntity.ok(estadisticas);
    }
    
    // DTOs para las respuestas
    @lombok.Data
    @lombok.Builder
    public static class SeguimientoResponse {
        private Long solicitudId;
        private String numero;
        private String estado;
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaProgramacion;
        private LocalDateTime fechaInicioTransito;
        private LocalDateTime fechaEntrega;
        private String contenedorCodigo;
        private String contenedorEstado;
        private String clienteNombre;
        private java.math.BigDecimal costoEstimado;
        private java.math.BigDecimal costoFinal;
        private Integer tiempoEstimadoHoras;
        private Integer tiempoRealHoras;
    }
    
    @lombok.Data
    @lombok.Builder
    public static class EstadisticasResponse {
        private Long totalBorradores;
        private Long totalProgramadas;
        private Long totalEnTransito;
        private Long totalEntregadas;
        private Long totalCanceladas;
    }
    
    @lombok.Data
    public static class CrearSolicitudRequest {
        @Valid
        private Long clienteId;
        @Valid
        private Contenedor contenedor;
    }
}