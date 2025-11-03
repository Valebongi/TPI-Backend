package utnfc.isi.back.sim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Contenedor;
import utnfc.isi.back.sim.repository.ContenedorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContenedorService {
    
    private final ContenedorRepository contenedorRepository;
    
    @Transactional(readOnly = true)
    public List<Contenedor> findAll() {
        log.debug("Buscando todos los contenedores");
        return contenedorRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Contenedor> findById(Long id) {
        log.debug("Buscando contenedor por ID: {}", id);
        return contenedorRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Contenedor> findByCodigo(String codigo) {
        log.debug("Buscando contenedor por código: {}", codigo);
        return contenedorRepository.findByCodigo(codigo);
    }
    
    @Transactional(readOnly = true)
    public List<Contenedor> findByClienteId(Long clienteId) {
        log.debug("Buscando contenedores por cliente ID: {}", clienteId);
        return contenedorRepository.findByClienteId(clienteId);
    }
    
    @Transactional(readOnly = true)
    public List<Contenedor> findByEstado(String estado) {
        log.debug("Buscando contenedores por estado: {}", estado);
        return contenedorRepository.findByEstado(estado);
    }
    
    @Transactional(readOnly = true)
    public List<Contenedor> findByClienteIdAndEstado(Long clienteId, String estado) {
        log.debug("Buscando contenedores por cliente ID: {} y estado: {}", clienteId, estado);
        return contenedorRepository.findByClienteIdAndEstado(clienteId, estado);
    }
    
    @Transactional(readOnly = true)
    public List<Contenedor> findByFiltro(String filtro) {
        log.debug("Buscando contenedores por filtro: {}", filtro);
        return contenedorRepository.findByFiltro(filtro);
    }
    
    public Contenedor save(Contenedor contenedor) {
        log.debug("Guardando contenedor: {}", contenedor.getCodigo());
        
        // Validar que el código no esté duplicado
        if (contenedor.getId() == null && contenedorRepository.existsByCodigo(contenedor.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un contenedor con el código: " + contenedor.getCodigo());
        }
        
        // Generar código si no se proporciona
        if (contenedor.getCodigo() == null || contenedor.getCodigo().trim().isEmpty()) {
            contenedor.setCodigo(generateCodigoContenedor());
        }
        
        return contenedorRepository.save(contenedor);
    }
    
    public Contenedor update(Long id, Contenedor contenedorActualizado) {
        log.debug("Actualizando contenedor con ID: {}", id);
        
        return contenedorRepository.findById(id)
                .map(contenedor -> {
                    // Validar código duplicado solo si se está cambiando
                    if (!contenedor.getCodigo().equals(contenedorActualizado.getCodigo()) && 
                        contenedorRepository.existsByCodigo(contenedorActualizado.getCodigo())) {
                        throw new IllegalArgumentException("Ya existe un contenedor con el código: " + contenedorActualizado.getCodigo());
                    }
                    
                    contenedor.setCodigo(contenedorActualizado.getCodigo());
                    contenedor.setPeso(contenedorActualizado.getPeso());
                    contenedor.setVolumen(contenedorActualizado.getVolumen());
                    contenedor.setEstado(contenedorActualizado.getEstado());
                    contenedor.setDescripcion(contenedorActualizado.getDescripcion());
                    contenedor.setDireccionOrigen(contenedorActualizado.getDireccionOrigen());
                    contenedor.setLatitudOrigen(contenedorActualizado.getLatitudOrigen());
                    contenedor.setLongitudOrigen(contenedorActualizado.getLongitudOrigen());
                    contenedor.setDireccionDestino(contenedorActualizado.getDireccionDestino());
                    contenedor.setLatitudDestino(contenedorActualizado.getLatitudDestino());
                    contenedor.setLongitudDestino(contenedorActualizado.getLongitudDestino());
                    
                    return contenedorRepository.save(contenedor);
                })
                .orElseThrow(() -> new IllegalArgumentException("Contenedor no encontrado con ID: " + id));
    }
    
    public void deleteById(Long id) {
        log.debug("Eliminando contenedor con ID: {}", id);
        
        if (!contenedorRepository.existsById(id)) {
            throw new IllegalArgumentException("Contenedor no encontrado con ID: " + id);
        }
        
        contenedorRepository.deleteById(id);
    }
    
    public Contenedor actualizarEstado(Long id, String nuevoEstado) {
        log.debug("Actualizando estado del contenedor ID: {} a: {}", id, nuevoEstado);
        
        return contenedorRepository.findById(id)
                .map(contenedor -> {
                    contenedor.setEstado(nuevoEstado);
                    return contenedorRepository.save(contenedor);
                })
                .orElseThrow(() -> new IllegalArgumentException("Contenedor no encontrado con ID: " + id));
    }
    
    private String generateCodigoContenedor() {
        return "CONT-" + System.currentTimeMillis();
    }
}