package utnfc.isi.back.sim.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SolicitudController {
    
    private final SolicitudService solicitudService;

    @Autowired
    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }
    
    @GetMapping
    public ResponseEntity<List<Solicitud>> getAllSolicitudes(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        // Log removed for Docker compatibility
        
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
        // Log removed for Docker compatibility
        
        return solicitudService.findById(id)
                .map(solicitud -> ResponseEntity.ok(solicitud))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/numero/{numero}")
    public ResponseEntity<Solicitud> getSolicitudByNumero(@PathVariable String numero) {
        // Log removed for Docker compatibility
        
        return solicitudService.findByNumero(numero)
                .map(solicitud -> ResponseEntity.ok(solicitud))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<SeguimientoResponse> getSeguimiento(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
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
        // Log removed for Docker compatibility
        
        try {
            Solicitud nuevaSolicitud = solicitudService.crearSolicitud(request.getClienteId(), request.getContenedor());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> updateSolicitud(@PathVariable Long id, @Valid @RequestBody Solicitud solicitud) {
        // Log removed for Docker compatibility
        
        try {
            Solicitud solicitudActualizada = solicitudService.update(id, solicitud);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Solicitud> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        // Log removed for Docker compatibility
        
        try {
            Solicitud solicitudActualizada = solicitudService.actualizarEstado(id, estado);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolicitud(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            solicitudService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticasResponse> getEstadisticas() {
        // Log removed for Docker compatibility
        
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

        public SeguimientoResponse() {}

        public static SeguimientoResponse builder() {
            return new SeguimientoResponse();
        }

        public SeguimientoResponse solicitudId(Long solicitudId) { this.solicitudId = solicitudId; return this; }
        public SeguimientoResponse numero(String numero) { this.numero = numero; return this; }
        public SeguimientoResponse estado(String estado) { this.estado = estado; return this; }
        public SeguimientoResponse fechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; return this; }
        public SeguimientoResponse fechaProgramacion(LocalDateTime fechaProgramacion) { this.fechaProgramacion = fechaProgramacion; return this; }
        public SeguimientoResponse fechaInicioTransito(LocalDateTime fechaInicioTransito) { this.fechaInicioTransito = fechaInicioTransito; return this; }
        public SeguimientoResponse fechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; return this; }
        public SeguimientoResponse contenedorCodigo(String contenedorCodigo) { this.contenedorCodigo = contenedorCodigo; return this; }
        public SeguimientoResponse contenedorEstado(String contenedorEstado) { this.contenedorEstado = contenedorEstado; return this; }
        public SeguimientoResponse clienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; return this; }
        public SeguimientoResponse costoEstimado(java.math.BigDecimal costoEstimado) { this.costoEstimado = costoEstimado; return this; }
        public SeguimientoResponse costoFinal(java.math.BigDecimal costoFinal) { this.costoFinal = costoFinal; return this; }
        public SeguimientoResponse tiempoEstimadoHoras(Integer tiempoEstimadoHoras) { this.tiempoEstimadoHoras = tiempoEstimadoHoras; return this; }
        public SeguimientoResponse tiempoRealHoras(Integer tiempoRealHoras) { this.tiempoRealHoras = tiempoRealHoras; return this; }

        public SeguimientoResponse build() { return this; }

        // Getters and Setters
        public Long getSolicitudId() { return solicitudId; }
        public void setSolicitudId(Long solicitudId) { this.solicitudId = solicitudId; }
        public String getNumero() { return numero; }
        public void setNumero(String numero) { this.numero = numero; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public LocalDateTime getFechaCreacion() { return fechaCreacion; }
        public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
        public LocalDateTime getFechaProgramacion() { return fechaProgramacion; }
        public void setFechaProgramacion(LocalDateTime fechaProgramacion) { this.fechaProgramacion = fechaProgramacion; }
        public LocalDateTime getFechaInicioTransito() { return fechaInicioTransito; }
        public void setFechaInicioTransito(LocalDateTime fechaInicioTransito) { this.fechaInicioTransito = fechaInicioTransito; }
        public LocalDateTime getFechaEntrega() { return fechaEntrega; }
        public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }
        public String getContenedorCodigo() { return contenedorCodigo; }
        public void setContenedorCodigo(String contenedorCodigo) { this.contenedorCodigo = contenedorCodigo; }
        public String getContenedorEstado() { return contenedorEstado; }
        public void setContenedorEstado(String contenedorEstado) { this.contenedorEstado = contenedorEstado; }
        public String getClienteNombre() { return clienteNombre; }
        public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
        public java.math.BigDecimal getCostoEstimado() { return costoEstimado; }
        public void setCostoEstimado(java.math.BigDecimal costoEstimado) { this.costoEstimado = costoEstimado; }
        public java.math.BigDecimal getCostoFinal() { return costoFinal; }
        public void setCostoFinal(java.math.BigDecimal costoFinal) { this.costoFinal = costoFinal; }
        public Integer getTiempoEstimadoHoras() { return tiempoEstimadoHoras; }
        public void setTiempoEstimadoHoras(Integer tiempoEstimadoHoras) { this.tiempoEstimadoHoras = tiempoEstimadoHoras; }
        public Integer getTiempoRealHoras() { return tiempoRealHoras; }
        public void setTiempoRealHoras(Integer tiempoRealHoras) { this.tiempoRealHoras = tiempoRealHoras; }
    }
    
    public static class EstadisticasResponse {
        private Long totalBorradores;
        private Long totalProgramadas;
        private Long totalEnTransito;
        private Long totalEntregadas;
        private Long totalCanceladas;

        public EstadisticasResponse() {}

        public static EstadisticasResponse builder() {
            return new EstadisticasResponse();
        }

        public EstadisticasResponse totalBorradores(Long totalBorradores) { this.totalBorradores = totalBorradores; return this; }
        public EstadisticasResponse totalProgramadas(Long totalProgramadas) { this.totalProgramadas = totalProgramadas; return this; }
        public EstadisticasResponse totalEnTransito(Long totalEnTransito) { this.totalEnTransito = totalEnTransito; return this; }
        public EstadisticasResponse totalEntregadas(Long totalEntregadas) { this.totalEntregadas = totalEntregadas; return this; }
        public EstadisticasResponse totalCanceladas(Long totalCanceladas) { this.totalCanceladas = totalCanceladas; return this; }

        public EstadisticasResponse build() { return this; }

        // Getters and Setters
        public Long getTotalBorradores() { return totalBorradores; }
        public void setTotalBorradores(Long totalBorradores) { this.totalBorradores = totalBorradores; }
        public Long getTotalProgramadas() { return totalProgramadas; }
        public void setTotalProgramadas(Long totalProgramadas) { this.totalProgramadas = totalProgramadas; }
        public Long getTotalEnTransito() { return totalEnTransito; }
        public void setTotalEnTransito(Long totalEnTransito) { this.totalEnTransito = totalEnTransito; }
        public Long getTotalEntregadas() { return totalEntregadas; }
        public void setTotalEntregadas(Long totalEntregadas) { this.totalEntregadas = totalEntregadas; }
        public Long getTotalCanceladas() { return totalCanceladas; }
        public void setTotalCanceladas(Long totalCanceladas) { this.totalCanceladas = totalCanceladas; }
    }
    
    public static class CrearSolicitudRequest {
        @Valid
        private Long clienteId;
        @Valid
        private Contenedor contenedor;

        public CrearSolicitudRequest() {}

        public CrearSolicitudRequest(Long clienteId, Contenedor contenedor) {
            this.clienteId = clienteId;
            this.contenedor = contenedor;
        }

        // Getters and Setters
        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
        public Contenedor getContenedor() { return contenedor; }
        public void setContenedor(Contenedor contenedor) { this.contenedor = contenedor; }
    }
}
