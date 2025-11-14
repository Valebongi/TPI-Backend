package utnfc.isi.back.sim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Deposito;
import utnfc.isi.back.sim.service.DepositoService;

import java.util.List;

@RestController
@RequestMapping("/depositos")
public class DepositoController {
    private final DepositoService depositoService;

    @Autowired
    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }

    @GetMapping
    public ResponseEntity<List<Deposito>> all() { return ResponseEntity.ok(depositoService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Deposito> get(@PathVariable Integer id) {
        return depositoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Deposito> create(@RequestBody Deposito d) { return ResponseEntity.ok(depositoService.save(d)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        depositoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    // ENDPOINT DE PRUEBA: Solo coordenadas del depósito
    @GetMapping("/{id}/coordenadas")
    public ResponseEntity<CoordenadasResponse> getCoordenadas(@PathVariable Integer id) {
        System.out.println("=== SERVICIO ADMIN: Solicitando coordenadas del depósito ID: " + id + " ===");
        
        return depositoService.findById(id)
                .map(deposito -> {
                    CoordenadasResponse response = new CoordenadasResponse(
                        deposito.getId(),
                        deposito.getDireccion(),
                        deposito.getLatitud(),
                        deposito.getLongitud()
                    );
                    System.out.println("=== SERVICIO ADMIN: Devolviendo coordenadas - " + 
                                     "Lat: " + response.getLatitud() + ", Lng: " + response.getLongitud() + " ===");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ENDPOINT INTERNO: Para uso interno entre servicios (sin autenticación)
    @GetMapping("/{id}/interno")
    public ResponseEntity<Deposito> getInterno(@PathVariable Integer id) {
        System.out.println("=== SERVICIO ADMIN: Consulta interna de depósito ID: " + id + " ===");
        return depositoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // DTO para coordenadas
    public static class CoordenadasResponse {
        private Integer id;
        private String direccion;
        private java.math.BigDecimal latitud;
        private java.math.BigDecimal longitud;
        
        public CoordenadasResponse() {}
        
        public CoordenadasResponse(Integer id, String direccion, java.math.BigDecimal latitud, java.math.BigDecimal longitud) {
            this.id = id;
            this.direccion = direccion;
            this.latitud = latitud;
            this.longitud = longitud;
        }
        
        // Getters y Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getDireccion() { return direccion; }
        public void setDireccion(String direccion) { this.direccion = direccion; }
        
        public java.math.BigDecimal getLatitud() { return latitud; }
        public void setLatitud(java.math.BigDecimal latitud) { this.latitud = latitud; }
        
        public java.math.BigDecimal getLongitud() { return longitud; }
        public void setLongitud(java.math.BigDecimal longitud) { this.longitud = longitud; }
    }
}
