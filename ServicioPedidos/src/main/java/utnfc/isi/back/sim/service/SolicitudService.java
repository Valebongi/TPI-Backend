package utnfc.isi.back.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Solicitud;
import utnfc.isi.back.sim.domain.Contenedor;
import utnfc.isi.back.sim.domain.Cliente;
import utnfc.isi.back.sim.repository.SolicitudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SolicitudService {
    
    private final SolicitudRepository solicitudRepository;
    private final ContenedorService contenedorService;
    private final ClienteService clienteService;

    @Autowired
    public SolicitudService(SolicitudRepository solicitudRepository, ContenedorService contenedorService, ClienteService clienteService) {
        this.solicitudRepository = solicitudRepository;
        this.contenedorService = contenedorService;
        this.clienteService = clienteService;
    }
    
    @Transactional(readOnly = true)
    public List<Solicitud> findAll() {
        // Log removed for Docker compatibility
        return solicitudRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Solicitud> findById(Long id) {
        // Log removed for Docker compatibility
        return solicitudRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Solicitud> findByNumero(String numero) {
        // Log removed for Docker compatibility
        return solicitudRepository.findByNumero(numero);
    }
    
    @Transactional(readOnly = true)
    public List<Solicitud> findByClienteId(Long clienteId) {
        // Log removed for Docker compatibility
        return solicitudRepository.findByClienteId(clienteId);
    }
    
    @Transactional(readOnly = true)
    public List<Solicitud> findByEstado(String estado) {
        // Log removed for Docker compatibility
        return solicitudRepository.findByEstado(estado);
    }
    
    @Transactional(readOnly = true)
    public List<Solicitud> findByClienteIdAndEstado(Long clienteId, String estado) {
        // Log removed for Docker compatibility
        return solicitudRepository.findByClienteIdAndEstado(clienteId, estado);
    }
    
    @Transactional(readOnly = true)
    public List<Solicitud> findByFechaCreacion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        // Log removed for Docker compatibility
        return solicitudRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
    }
    
    @Transactional(readOnly = true)
    public Optional<Solicitud> findByContenedorId(Long contenedorId) {
        // Log removed for Docker compatibility
        return solicitudRepository.findByContenedorId(contenedorId);
    }
    
    @Transactional(readOnly = true)
    public Long countByEstado(String estado) {
        // Log removed for Docker compatibility
        return solicitudRepository.countByEstado(estado);
    }
    
    public Solicitud crearSolicitud(Long clienteId, Contenedor contenedorData) {
        // Log removed for Docker compatibility
        
        // Verificar que el cliente existe
        Cliente cliente = clienteService.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));
        
        // Asignar el cliente al contenedor
        contenedorData.setCliente(cliente);
        
        // Guardar el contenedor
        Contenedor contenedor = contenedorService.save(contenedorData);
        
        // Crear la solicitud
        Solicitud solicitud = Solicitud.builder()
                .contenedor(contenedor)
                .cliente(cliente)
                .estado("BORRADOR")
                .fechaCreacion(LocalDateTime.now())
                .build();
        
        return solicitudRepository.save(solicitud);
    }
    
    public Solicitud save(Solicitud solicitud) {
        // Log removed for Docker compatibility
        
        // Validar que el número no esté duplicado
        if (solicitud.getId() == null && solicitudRepository.existsByNumero(solicitud.getNumero())) {
            throw new IllegalArgumentException("Ya existe una solicitud con el número: " + solicitud.getNumero());
        }
        
        return solicitudRepository.save(solicitud);
    }
    
    public Solicitud actualizarEstado(Long id, String nuevoEstado) {
        // Log removed for Docker compatibility
        
        return solicitudRepository.findById(id)
                .map(solicitud -> {
                    String estadoAnterior = solicitud.getEstado();
                    solicitud.setEstado(nuevoEstado);
                    
                    // Actualizar fechas según el estado
                    switch (nuevoEstado) {
                        case "PROGRAMADA":
                            solicitud.setFechaProgramacion(LocalDateTime.now());
                            break;
                        case "EN_TRANSITO":
                            solicitud.setFechaInicioTransito(LocalDateTime.now());
                            // También actualizar el estado del contenedor
                            contenedorService.actualizarEstado(solicitud.getContenedor().getId(), "EN_TRANSITO");
                            break;
                        case "ENTREGADA":
                            solicitud.setFechaEntrega(LocalDateTime.now());
                            // También actualizar el estado del contenedor
                            contenedorService.actualizarEstado(solicitud.getContenedor().getId(), "ENTREGADO");
                            break;
                    }
                    
                    // Log removed for Docker compatibility
                    return solicitudRepository.save(solicitud);
                })
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));
    }
    
    public Solicitud update(Long id, Solicitud solicitudActualizada) {
        // Log removed for Docker compatibility
        
        return solicitudRepository.findById(id)
                .map(solicitud -> {
                    solicitud.setCostoEstimado(solicitudActualizada.getCostoEstimado());
                    solicitud.setTiempoEstimadoHoras(solicitudActualizada.getTiempoEstimadoHoras());
                    solicitud.setCostoFinal(solicitudActualizada.getCostoFinal());
                    solicitud.setTiempoRealHoras(solicitudActualizada.getTiempoRealHoras());
                    solicitud.setObservaciones(solicitudActualizada.getObservaciones());
                    solicitud.setRutaId(solicitudActualizada.getRutaId());
                    
                    return solicitudRepository.save(solicitud);
                })
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));
    }
    
    public void deleteById(Long id) {
        // Log removed for Docker compatibility
        
        if (!solicitudRepository.existsById(id)) {
            throw new IllegalArgumentException("Solicitud no encontrada con ID: " + id);
        }
        
        solicitudRepository.deleteById(id);
    }
}
