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
import utnfc.isi.back.sim.service.ContenedorService;
import utnfc.isi.back.sim.dto.AsignarRutaRequest;
import utnfc.isi.back.sim.dto.CrearSolicitudRequest;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {
    
    private final SolicitudService solicitudService;
    private final ContenedorService contenedorService;
    private final RestTemplate restTemplate;

    @Autowired
    public SolicitudController(SolicitudService solicitudService, ContenedorService contenedorService, RestTemplate restTemplate) {
        this.solicitudService = solicitudService;
        this.contenedorService = contenedorService;
        this.restTemplate = restTemplate;
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
    public ResponseEntity<Solicitud> createSolicitud(@RequestBody CrearSolicitudRequest request) {
        System.out.println("=== CONTROLADOR: Recibida petición POST ===");
        
        if (request == null) {
            System.err.println("Request es NULL");
            return ResponseEntity.badRequest().build();
        }
        
        System.out.println("Cliente ID: " + request.getClienteId());
        System.out.println("Contenedor ID: " + request.getContenedorId());
        System.out.println("Dirección destino: " + request.getDireccionDestino());
        
        try {
            Solicitud nuevaSolicitud = solicitudService.crearSolicitud(
                request.getClienteId(), 
                request.getContenedorId(),
                request.getDireccionDestino(),
                request.getLatitudDestino(),
                request.getLongitudDestino()
            );
            System.out.println("=== CONTROLADOR: Solicitud creada exitosamente con ID: " + nuevaSolicitud.getId() + " ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
        } catch (IllegalArgumentException e) {
            System.err.println("=== CONTROLADOR: Error de validación ===");
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("=== CONTROLADOR: Error interno ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ENDPOINT DE PRUEBA: Comunicación aislada con Administración
    @GetMapping("/test-comunicacion/{contenedorId}")
    public ResponseEntity<TestComunicacionResponse> testComunicacion(@PathVariable Integer contenedorId) {
        System.out.println("=== TEST COMUNICACION: Iniciando prueba para contenedor ID: " + contenedorId + " ===");
        
        try {
            // Paso 1: Buscar contenedor
            Contenedor contenedor = contenedorService.findById(contenedorId.longValue())
                    .orElse(null);
                    
            if (contenedor == null) {
                System.out.println("=== TEST: Contenedor no encontrado ===");
                return ResponseEntity.notFound().build();
            }
            
            System.out.println("=== TEST: Contenedor encontrado - Deposito ID: " + contenedor.getIdDeposito() + " ===");
            
            // Paso 2: Llamar a servicio de administración
            String url = "http://tpi-api-gateway:8080/api/admin/depositos/" + 
                        contenedor.getIdDeposito() + "/coordenadas";
            System.out.println("=== TEST: Llamando a URL: " + url + " ===");
            
            ResponseEntity<CoordenadasResponse> response = restTemplate.getForEntity(url, CoordenadasResponse.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                CoordenadasResponse coordenadas = response.getBody();
                System.out.println("=== TEST: Respuesta exitosa - " +
                                 "Dir: " + coordenadas.getDireccion() + 
                                 ", Lat: " + coordenadas.getLatitud() + 
                                 ", Lng: " + coordenadas.getLongitud() + " ===");
                
                TestComunicacionResponse testResponse = new TestComunicacionResponse(
                    contenedorId,
                    contenedor.getIdDeposito(),
                    coordenadas.getDireccion(),
                    coordenadas.getLatitud(),
                    coordenadas.getLongitud(),
                    "SUCCESS",
                    "Comunicación exitosa"
                );
                
                return ResponseEntity.ok(testResponse);
            } else {
                System.out.println("=== TEST: Respuesta vacía o error ===");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
            
        } catch (Exception e) {
            System.out.println("=== TEST: ERROR en comunicación: " + e.getMessage() + " ===");
            e.printStackTrace();
            
            TestComunicacionResponse errorResponse = new TestComunicacionResponse(
                contenedorId,
                null,
                null,
                null,
                null,
                "ERROR",
                e.getMessage()
            );
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
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
    
    /**
     * PUT /{id}/estado - Endpoint alternativo para uso interno desde otros microservicios
     * Funciona igual que PATCH pero usando PUT que es compatible con RestTemplate básico
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Solicitud> actualizarEstadoPut(@PathVariable Long id, @RequestParam String estado) {
        // Log removed for Docker compatibility
        
        try {
            Solicitud solicitudActualizada = solicitudService.actualizarEstado(id, estado);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * FASE 3: Asignar ruta elegida a una solicitud y cambiar estado a PROGRAMADA
     */
    @PostMapping("/{id}/asignar-ruta")
    public ResponseEntity<Solicitud> asignarRuta(@PathVariable Long id, @Valid @RequestBody AsignarRutaRequest rutaRequest) {
        // Log removed for Docker compatibility
        
        try {
            Solicitud solicitudActualizada = solicitudService.asignarRuta(id, rutaRequest);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    
    /**
     * PUT /solicitudes/{id}/marcar-entregada - Marcar solicitud como entregada
     * Endpoint para que el servicio de logística notifique cuando una ruta esté completa
     */
    @PutMapping("/{id}/marcar-entregada")
    public ResponseEntity<Solicitud> marcarComoEntregada(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            Solicitud solicitudActualizada = solicitudService.actualizarEstado(id, "ENTREGADA");
            return ResponseEntity.ok(solicitudActualizada);
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * PUT /solicitudes/{id}/costo-final - Actualizar costo final de la solicitud
     * Endpoint para que el servicio de logística actualice el costo final cuando se completen todos los tramos
     */
    @PutMapping("/{id}/costo-final")
    public ResponseEntity<Solicitud> actualizarCostoFinal(@PathVariable Long id, @RequestParam BigDecimal costo) {
        // Log removed for Docker compatibility
        
        try {
            Solicitud solicitudActualizada = solicitudService.actualizarCostoFinal(id, costo);
            return ResponseEntity.ok(solicitudActualizada);
        } catch (Exception e) {
            // Log removed for Docker compatibility
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    
    // DTO para test de comunicación
    public static class TestComunicacionResponse {
        private Integer contenedorId;
        private Integer depositoId;
        private String direccionDeposito;
        private java.math.BigDecimal latitudDeposito;
        private java.math.BigDecimal longitudDeposito;
        private String status;
        private String mensaje;
        
        public TestComunicacionResponse() {}
        
        public TestComunicacionResponse(Integer contenedorId, Integer depositoId, 
                                       String direccionDeposito, java.math.BigDecimal latitudDeposito, 
                                       java.math.BigDecimal longitudDeposito, String status, String mensaje) {
            this.contenedorId = contenedorId;
            this.depositoId = depositoId;
            this.direccionDeposito = direccionDeposito;
            this.latitudDeposito = latitudDeposito;
            this.longitudDeposito = longitudDeposito;
            this.status = status;
            this.mensaje = mensaje;
        }
        
        // Getters y Setters
        public Integer getContenedorId() { return contenedorId; }
        public void setContenedorId(Integer contenedorId) { this.contenedorId = contenedorId; }
        
        public Integer getDepositoId() { return depositoId; }
        public void setDepositoId(Integer depositoId) { this.depositoId = depositoId; }
        
        public String getDireccionDeposito() { return direccionDeposito; }
        public void setDireccionDeposito(String direccionDeposito) { this.direccionDeposito = direccionDeposito; }
        
        public java.math.BigDecimal getLatitudDeposito() { return latitudDeposito; }
        public void setLatitudDeposito(java.math.BigDecimal latitudDeposito) { this.latitudDeposito = latitudDeposito; }
        
        public java.math.BigDecimal getLongitudDeposito() { return longitudDeposito; }
        public void setLongitudDeposito(java.math.BigDecimal longitudDeposito) { this.longitudDeposito = longitudDeposito; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
    
    // DTO para coordenadas (debe coincidir con el del servicio de administración)
    public static class CoordenadasResponse {
        private Integer id;
        private String direccion;
        private java.math.BigDecimal latitud;
        private java.math.BigDecimal longitud;
        
        public CoordenadasResponse() {}
        
        // Getters y Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        
        public java.math.BigDecimal getLatitud() { return latitud; }
        public void setLatitud(java.math.BigDecimal latitud) { this.latitud = latitud; }
        
        public java.math.BigDecimal getLongitud() { return longitud; }
        public void setLongitud(java.math.BigDecimal longitud) { this.longitud = longitud; }
        
        @Override
        public String toString() {
            return "CoordenadasResponse{" +
                   "id=" + id +
                   ", direccion='" + direccion + '\'' +
                   ", latitud=" + latitud +
                   ", longitud=" + longitud +
                   '}';
        }
    }
}
