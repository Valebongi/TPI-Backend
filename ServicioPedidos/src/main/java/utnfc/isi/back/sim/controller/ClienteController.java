package utnfc.isi.back.sim.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utnfc.isi.back.sim.domain.Cliente;
import utnfc.isi.back.sim.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes(
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false, defaultValue = "false") boolean soloActivos) {
        
        // Log removed for Docker compatibility
        
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
        // Log removed for Docker compatibility
        
        return clienteService.findById(id)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> getClienteByEmail(@PathVariable String email) {
        // Log removed for Docker compatibility
        
        return clienteService.findByEmail(email)
                .map(cliente -> ResponseEntity.ok(cliente))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody Cliente cliente) {
        // Log removed for Docker compatibility
        
        try {
            Cliente nuevoCliente = clienteService.save(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        // Log removed for Docker compatibility
        
        try {
            Cliente clienteActualizado = clienteService.update(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            clienteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarCliente(@PathVariable Long id) {
        // Log removed for Docker compatibility
        
        try {
            clienteService.desactivar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // Log removed for Docker compatibility
            return ResponseEntity.notFound().build();
        }
    }
}
