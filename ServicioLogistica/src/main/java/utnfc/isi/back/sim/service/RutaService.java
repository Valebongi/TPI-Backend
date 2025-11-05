package utnfc.isi.back.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Ruta;
import utnfc.isi.back.sim.domain.Tramo;
import utnfc.isi.back.sim.repository.RutaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de rutas
 * Contiene la lógica de negocio para operaciones con rutas
 */
@Service
@Transactional
public class RutaService {
    
    private final RutaRepository rutaRepository;
    private final TramoService tramoService;

    @Autowired
    public RutaService(RutaRepository rutaRepository, TramoService tramoService) {
        this.rutaRepository = rutaRepository;
        this.tramoService = tramoService;
    }
    
    /**
     * Obtiene todas las rutas
     */
    @Transactional(readOnly = true)
    public List<Ruta> findAll() {
        // Log removed for Docker compatibility
        return rutaRepository.findAll();
    }
    
    /**
     * Obtiene una ruta por ID
     */
    @Transactional(readOnly = true)
    public Optional<Ruta> findById(Long id) {
        // Log removed for Docker compatibility
        return rutaRepository.findById(id);
    }
    
    /**
     * Obtiene una ruta por ID de solicitud
     */
    @Transactional(readOnly = true)
    public Optional<Ruta> findBySolicitudId(Long solicitudId) {
        // Log removed for Docker compatibility
        return rutaRepository.findBySolicitudId(solicitudId);
    }
    
    /**
     * Obtiene rutas por estado
     */
    @Transactional(readOnly = true)
    public List<Ruta> findByEstado(Ruta.EstadoRuta estado) {
        // Log removed for Docker compatibility
        return rutaRepository.findByEstado(estado);
    }
    
    /**
     * Obtiene rutas activas (planificadas o en progreso)
     */
    @Transactional(readOnly = true)
    public List<Ruta> findRutasActivas() {
        // Log removed for Docker compatibility
        return rutaRepository.findRutasActivas();
    }
    
    /**
     * Crea una nueva ruta
     */
    public Ruta save(Ruta ruta) {
        // Log removed for Docker compatibility
        
        // Validar que no exista ya una ruta para esta solicitud
        if (ruta.getSolicitudId() != null && rutaRepository.findBySolicitudId(ruta.getSolicitudId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una ruta para la solicitud ID: " + ruta.getSolicitudId());
        }
        
        // Calcular cantidad de tramos si no está definida
        if (ruta.getTramos() != null) {
            ruta.setCantidadTramos(ruta.getTramos().size());
        }
        
        Ruta rutaGuardada = rutaRepository.save(ruta);
        // Log removed for Docker compatibility
        
        return rutaGuardada;
    }
    
    /**
     * Actualiza una ruta existente
     */
    public Ruta update(Long id, Ruta rutaActualizada) {
        // Log removed for Docker compatibility
        
        return rutaRepository.findById(id)
                .map(rutaExistente -> {
                    rutaExistente.setEstado(rutaActualizada.getEstado());
                    rutaExistente.setCostoTotalAproximado(rutaActualizada.getCostoTotalAproximado());
                    rutaExistente.setCostoTotalReal(rutaActualizada.getCostoTotalReal());
                    rutaExistente.setCantidadDepositos(rutaActualizada.getCantidadDepositos());
                    
                    return rutaRepository.save(rutaExistente);
                })
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con ID: " + id));
    }
    
    /**
     * Elimina una ruta
     */
    public void deleteById(Long id) {
        // Log removed for Docker compatibility
        
        if (!rutaRepository.existsById(id)) {
            throw new RuntimeException("Ruta no encontrada con ID: " + id);
        }
        
        rutaRepository.deleteById(id);
    }
    
    /**
     * Calcula el costo total de una ruta basándose en sus tramos
     */
    public void calcularCostoTotal(Long rutaId) {
        // Log removed for Docker compatibility
        
        rutaRepository.findById(rutaId).ifPresent(ruta -> {
            List<Tramo> tramos = tramoService.findByRutaId(rutaId);
            
            // Calcular costo aproximado
            double costoAproximado = tramos.stream()
                    .mapToDouble(tramo -> tramo.getCostoAproximado() != null ? tramo.getCostoAproximado() : 0.0)
                    .sum();
            ruta.setCostoTotalAproximado(costoAproximado);
            
            // Calcular costo real si todos los tramos tienen costo real
            double costoReal = tramos.stream()
                    .mapToDouble(tramo -> tramo.getCostoReal() != null ? tramo.getCostoReal() : 0.0)
                    .sum();
            
            boolean todosTramosTienenCostoReal = tramos.stream()
                    .allMatch(tramo -> tramo.getCostoReal() != null);
            
            if (todosTramosTienenCostoReal) {
                ruta.setCostoTotalReal(costoReal);
            }
            
            rutaRepository.save(ruta);
        });
    }
    
    /**
     * Actualiza el estado de una ruta
     */
    public Ruta actualizarEstado(Long id, Ruta.EstadoRuta nuevoEstado) {
        // Log removed for Docker compatibility
        
        return rutaRepository.findById(id)
                .map(ruta -> {
                    ruta.setEstado(nuevoEstado);
                    return rutaRepository.save(ruta);
                })
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con ID: " + id));
    }
}
