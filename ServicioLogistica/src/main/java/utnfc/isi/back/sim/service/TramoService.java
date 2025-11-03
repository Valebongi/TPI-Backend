package utnfc.isi.back.sim.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Tramo;
import utnfc.isi.back.sim.repository.TramoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de tramos
 * Contiene la lógica de negocio para operaciones con tramos
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TramoService {
    
    private final TramoRepository tramoRepository;
    
    /**
     * Obtiene todos los tramos
     */
    @Transactional(readOnly = true)
    public List<Tramo> findAll() {
        log.info("Obteniendo todos los tramos");
        return tramoRepository.findAll();
    }
    
    /**
     * Obtiene un tramo por ID
     */
    @Transactional(readOnly = true)
    public Optional<Tramo> findById(Long id) {
        log.info("Buscando tramo con ID: {}", id);
        return tramoRepository.findById(id);
    }
    
    /**
     * Obtiene tramos por ID de ruta
     */
    @Transactional(readOnly = true)
    public List<Tramo> findByRutaId(Long rutaId) {
        log.info("Buscando tramos para ruta ID: {}", rutaId);
        return tramoRepository.findByRutaIdOrderById(rutaId);
    }
    
    /**
     * Obtiene tramos por estado
     */
    @Transactional(readOnly = true)
    public List<Tramo> findByEstado(Tramo.EstadoTramo estado) {
        log.info("Buscando tramos con estado: {}", estado);
        return tramoRepository.findByEstado(estado);
    }
    
    /**
     * Obtiene tramos asignados a un camión
     */
    @Transactional(readOnly = true)
    public List<Tramo> findByCamionId(Long camionId) {
        log.info("Buscando tramos para camión ID: {}", camionId);
        return tramoRepository.findByCamionId(camionId);
    }
    
    /**
     * Obtiene tramos pendientes de asignación
     */
    @Transactional(readOnly = true)
    public List<Tramo> findTramosPendientesDeAsignacion() {
        log.info("Obteniendo tramos pendientes de asignación");
        return tramoRepository.findTramosPendientesDeAsignacion();
    }
    
    /**
     * Crea un nuevo tramo
     */
    public Tramo save(Tramo tramo) {
        log.info("Creando nuevo tramo para ruta ID: {}", tramo.getRuta() != null ? tramo.getRuta().getId() : "N/A");
        
        // Validaciones básicas
        if (tramo.getOrigenCoordenadas() == null || tramo.getOrigenCoordenadas().trim().isEmpty()) {
            throw new IllegalArgumentException("Las coordenadas de origen son obligatorias");
        }
        
        if (tramo.getDestinoCoordenadas() == null || tramo.getDestinoCoordenadas().trim().isEmpty()) {
            throw new IllegalArgumentException("Las coordenadas de destino son obligatorias");
        }
        
        Tramo tramoGuardado = tramoRepository.save(tramo);
        log.info("Tramo creado con ID: {}", tramoGuardado.getId());
        
        return tramoGuardado;
    }
    
    /**
     * Actualiza un tramo existente
     */
    public Tramo update(Long id, Tramo tramoActualizado) {
        log.info("Actualizando tramo con ID: {}", id);
        
        return tramoRepository.findById(id)
                .map(tramoExistente -> {
                    tramoExistente.setOrigenCoordenadas(tramoActualizado.getOrigenCoordenadas());
                    tramoExistente.setDestinoCoordenadas(tramoActualizado.getDestinoCoordenadas());
                    tramoExistente.setOrigenDescripcion(tramoActualizado.getOrigenDescripcion());
                    tramoExistente.setDestinoDescripcion(tramoActualizado.getDestinoDescripcion());
                    tramoExistente.setTipo(tramoActualizado.getTipo());
                    tramoExistente.setEstado(tramoActualizado.getEstado());
                    tramoExistente.setCostoAproximado(tramoActualizado.getCostoAproximado());
                    tramoExistente.setCostoReal(tramoActualizado.getCostoReal());
                    tramoExistente.setDistanciaKm(tramoActualizado.getDistanciaKm());
                    tramoExistente.setTiempoEstimadoMinutos(tramoActualizado.getTiempoEstimadoMinutos());
                    tramoExistente.setTiempoRealMinutos(tramoActualizado.getTiempoRealMinutos());
                    tramoExistente.setCamionId(tramoActualizado.getCamionId());
                    
                    return tramoRepository.save(tramoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + id));
    }
    
    /**
     * Elimina un tramo
     */
    public void deleteById(Long id) {
        log.info("Eliminando tramo con ID: {}", id);
        
        if (!tramoRepository.existsById(id)) {
            throw new RuntimeException("Tramo no encontrado con ID: " + id);
        }
        
        tramoRepository.deleteById(id);
    }
    
    /**
     * Asigna un tramo a un camión
     */
    public Tramo asignarCamion(Long tramoId, Long camionId) {
        log.info("Asignando tramo ID: {} al camión ID: {}", tramoId, camionId);
        
        return tramoRepository.findById(tramoId)
                .map(tramo -> {
                    tramo.setCamionId(camionId);
                    tramo.setEstado(Tramo.EstadoTramo.ASIGNADO);
                    return tramoRepository.save(tramo);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
    }
    
    /**
     * Inicia un tramo (transportista marca inicio)
     */
    public Tramo iniciarTramo(Long tramoId) {
        log.info("Iniciando tramo ID: {}", tramoId);
        
        return tramoRepository.findById(tramoId)
                .map(tramo -> {
                    if (tramo.getEstado() != Tramo.EstadoTramo.ASIGNADO) {
                        throw new IllegalStateException("El tramo debe estar asignado para poder iniciarse");
                    }
                    
                    tramo.setEstado(Tramo.EstadoTramo.EN_PROGRESO);
                    tramo.setFechaHoraInicio(LocalDateTime.now());
                    return tramoRepository.save(tramo);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
    }
    
    /**
     * Finaliza un tramo (transportista marca fin)
     */
    public Tramo finalizarTramo(Long tramoId, Double costoReal) {
        log.info("Finalizando tramo ID: {} con costo real: {}", tramoId, costoReal);
        
        return tramoRepository.findById(tramoId)
                .map(tramo -> {
                    if (tramo.getEstado() != Tramo.EstadoTramo.EN_PROGRESO) {
                        throw new IllegalStateException("El tramo debe estar en progreso para poder finalizarse");
                    }
                    
                    LocalDateTime ahora = LocalDateTime.now();
                    tramo.setEstado(Tramo.EstadoTramo.COMPLETADO);
                    tramo.setFechaHoraFin(ahora);
                    tramo.setCostoReal(costoReal);
                    
                    // Calcular tiempo real si hay fecha de inicio
                    if (tramo.getFechaHoraInicio() != null) {
                        long minutos = java.time.Duration.between(tramo.getFechaHoraInicio(), ahora).toMinutes();
                        tramo.setTiempoRealMinutos((int) minutos);
                    }
                    
                    return tramoRepository.save(tramo);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + tramoId));
    }
    
    /**
     * Actualiza el estado de un tramo
     */
    public Tramo actualizarEstado(Long id, Tramo.EstadoTramo nuevoEstado) {
        log.info("Actualizando estado de tramo ID: {} a {}", id, nuevoEstado);
        
        return tramoRepository.findById(id)
                .map(tramo -> {
                    tramo.setEstado(nuevoEstado);
                    return tramoRepository.save(tramo);
                })
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado con ID: " + id));
    }
}