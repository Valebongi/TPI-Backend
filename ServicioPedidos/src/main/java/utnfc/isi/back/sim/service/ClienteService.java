package utnfc.isi.back.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Cliente;
import utnfc.isi.back.sim.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        // Log removed for Docker compatibility
        return clienteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> findAllActivos() {
        // Log removed for Docker compatibility
        return clienteRepository.findByActivoTrue();
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        // Log removed for Docker compatibility
        return clienteRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findByEmail(String email) {
        // Log removed for Docker compatibility
        return clienteRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findByKeycloakId(String keycloakId) {
        // Log removed for Docker compatibility
        return clienteRepository.findByKeycloakId(keycloakId);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> findByFiltro(String filtro) {
        // Log removed for Docker compatibility
        return clienteRepository.findByFiltro(filtro);
    }
    
    public Cliente save(Cliente cliente) {
        // Log removed for Docker compatibility
        
        // Validar que el email no esté duplicado
        if (cliente.getId() == null && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + cliente.getEmail());
        }
        
        return clienteRepository.save(cliente);
    }
    
    public Cliente update(Long id, Cliente clienteActualizado) {
        // Log removed for Docker compatibility
        
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
        // Log removed for Docker compatibility
        
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
        }
        
        clienteRepository.deleteById(id);
    }
    
    public void desactivar(Long id) {
        // Log removed for Docker compatibility
        
        clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setActivo(false);
                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
    }
}
