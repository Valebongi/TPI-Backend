package utnfc.isi.back.sim.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Cliente;
import utnfc.isi.back.sim.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes(
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false, defaultValue = "false") boolean soloActivos) {
        
        log.info("GET /clientes - filtro: {}, soloActivos: {}", filtro, soloActivos);
        
        List<Cliente> clientes;
        
        if (filtro != null && !filtro.trim().isEmpty()) {
            clientes = clienteService.findByFiltro(filtro);
        } else if (soloActivos) {
            clientes = clienteService.findAllActivos();
        } else {
            clientes = clienteService.findAll();
        }
        
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        log.info("GET /clientes/{}", id);
        
        return clienteService.findById(id)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> getClienteByEmail(@PathVariable String email) {
        log.info("GET /clientes/email/{}", email);
        
        return clienteService.findByEmail(email)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody Cliente cliente) {
        log.info("POST /clientes - {}", cliente.getEmail());
        
        try {
            Cliente nuevoCliente = clienteService.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        log.info("PUT /clientes/{}", id);
        
        try {
            Cliente clienteActualizado = clienteService.update(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        log.info("DELETE /clientes/{}", id);
        
        try {
            clienteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar cliente: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarCliente(@PathVariable Long id) {
        log.info("PATCH /clientes/{}/desactivar", id);
        
        try {
            clienteService.desactivar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al desactivar cliente: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}