package utnfc.isi.back.sim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Cliente;
import utnfc.isi.back.sim.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        log.debug("Buscando todos los clientes");
        return clienteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> findAllActivos() {
        log.debug("Buscando todos los clientes activos");
        return clienteRepository.findByActivoTrue();
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        log.debug("Buscando cliente por ID: {}", id);
        return clienteRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findByEmail(String email) {
        log.debug("Buscando cliente por email: {}", email);
        return clienteRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findByKeycloakId(String keycloakId) {
        log.debug("Buscando cliente por Keycloak ID: {}", keycloakId);
        return clienteRepository.findByKeycloakId(keycloakId);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> findByFiltro(String filtro) {
        log.debug("Buscando clientes por filtro: {}", filtro);
        return clienteRepository.findByFiltro(filtro);
    }
    
    public Cliente save(Cliente cliente) {
        log.debug("Guardando cliente: {}", cliente.getEmail());
        
        // Validar que el email no esté duplicado
        if (cliente.getId() == null && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + cliente.getEmail());
        }
        
        return clienteRepository.save(cliente);
    }
    
    public Cliente update(Long id, Cliente clienteActualizado) {
        log.debug("Actualizando cliente con ID: {}", id);
        
        return clienteRepository.findById(id)
                .map(cliente -> {
                    // Validar email duplicado solo si se está cambiando
                    if (!cliente.getEmail().equals(clienteActualizado.getEmail()) && 
                        clienteRepository.existsByEmail(clienteActualizado.getEmail())) {
                        throw new IllegalArgumentException("Ya existe un cliente con el email: " + clienteActualizado.getEmail());
                    }
                    
                    cliente.setNombre(clienteActualizado.getNombre());
                    cliente.setApellido(clienteActualizado.getApellido());
                    cliente.setEmail(clienteActualizado.getEmail());
                    cliente.setTelefono(clienteActualizado.getTelefono());
                    cliente.setDireccion(clienteActualizado.getDireccion());
                    cliente.setKeycloakId(clienteActualizado.getKeycloakId());
                    cliente.setActivo(clienteActualizado.getActivo());
                    
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
    }
    
    public void deleteById(Long id) {
        log.debug("Eliminando cliente con ID: {}", id);
        
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
        }
        
        clienteRepository.deleteById(id);
    }
    
    public void desactivar(Long id) {
        log.debug("Desactivando cliente con ID: {}", id);
        
        clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setActivo(false);
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
    }
}