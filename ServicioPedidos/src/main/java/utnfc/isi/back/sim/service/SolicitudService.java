package utnfc.isi.back.sim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utnfc.isi.back.sim.domain.Solicitud;
import utnfc.isi.back.sim.domain.Contenedor;
import utnfc.isi.back.sim.domain.Cliente;
import utnfc.isi.back.sim.repository.SolicitudRepository;
import utnfc.isi.back.sim.client.LogisticaClient;
import utnfc.isi.back.sim.client.RutaRequest;
import utnfc.isi.back.sim.client.RutaResponse;
import utnfc.isi.back.sim.client.AdministracionClient;
import utnfc.isi.back.sim.client.DepositoResponse;
import utnfc.isi.back.sim.dto.AsignarRutaRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SolicitudService {
    
    private final SolicitudRepository solicitudRepository;
    private final ContenedorService contenedorService;
    private final ClienteService clienteService;
    private final LogisticaClient logisticaClient;
    private final AdministracionClient administracionClient;

    @Autowired
    public SolicitudService(SolicitudRepository solicitudRepository, ContenedorService contenedorService, 
                           ClienteService clienteService, LogisticaClient logisticaClient,
                           AdministracionClient administracionClient) {
        this.solicitudRepository = solicitudRepository;
        this.contenedorService = contenedorService;
        this.clienteService = clienteService;
        this.logisticaClient = logisticaClient;
        this.administracionClient = administracionClient;
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
    
    public Solicitud crearSolicitud(Long clienteId, Long contenedorId, String direccionDestino, 
                                   BigDecimal latitudDestino, BigDecimal longitudDestino) {
        System.out.println("=== PASO 1: Iniciando creación de solicitud ===");
        System.out.println("Cliente ID: " + clienteId + ", Contenedor ID: " + contenedorId);
        
        // Verificar que el cliente existe
        Cliente cliente = clienteService.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));
        System.out.println("Cliente encontrado: " + cliente.getNombre());
        
        // Buscar el contenedor existente por ID
        Contenedor contenedor = contenedorService.findById(contenedorId)
                .orElseThrow(() -> new IllegalArgumentException("Contenedor no encontrado con ID: " + contenedorId));
        System.out.println("Contenedor encontrado - ID Depósito: " + contenedor.getIdDeposito());
        
        // PASO 1: Crear la solicitud SIN los datos de origen primero
        Solicitud solicitud = new Solicitud();
        solicitud.setContenedor(contenedor);
        solicitud.setCliente(cliente);
        
        // Información de destino (proporcionada por el usuario)
        solicitud.setDireccionDestino(direccionDestino);
        solicitud.setLatitudDestino(latitudDestino);
        solicitud.setLongitudDestino(longitudDestino);
        
        // Dejar origen vacío por ahora
        solicitud.setDireccionOrigen(null);
        solicitud.setLatitudOrigen(null);
        solicitud.setLongitudOrigen(null);
        
        solicitud.setEstado("BORRADOR");
        solicitud.setFechaCreacion(LocalDateTime.now());
        
        // Guardar la solicitud SIN origen
        solicitud = solicitudRepository.save(solicitud);
        System.out.println("=== PASO 1 COMPLETO: Solicitud guardada sin origen, ID: " + solicitud.getId() + " ===");
        
        // PASO 2: Ahora buscar y actualizar los datos del depósito
        System.out.println("=== PASO 2: Buscando datos del depósito ===");
        try {
            DepositoResponse deposito = administracionClient.obtenerDeposito(contenedor.getIdDeposito());
            System.out.println("Depósito obtenido exitosamente: " + deposito.getDireccion());
            
            // Actualizar con los datos del origen
            solicitud.setDireccionOrigen(deposito.getDireccion());
            solicitud.setLatitudOrigen(deposito.getLatitud());
            solicitud.setLongitudOrigen(deposito.getLongitud());
            
            // Guardar nuevamente con los datos de origen
            solicitud = solicitudRepository.save(solicitud);
            System.out.println("=== PASO 2 COMPLETO: Origen actualizado ===");
            
        } catch (Exception e) {
            System.err.println("=== ERROR en PASO 2: " + e.getMessage());
            e.printStackTrace();
            // La solicitud ya existe, solo avisar que no se pudo obtener el origen
            System.out.println("Solicitud creada pero sin datos de origen debido a error de comunicación");
        }
        
        return solicitud;
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
    
    /**
     * Actualiza el costo final de una solicitud
     * Método utilizado por el servicio de logística cuando se completan todos los tramos
     */
    public Solicitud actualizarCostoFinal(Long id, java.math.BigDecimal costoFinal) {
        // Log removed for Docker compatibility
        
        return solicitudRepository.findById(id)
                .map(solicitud -> {
                    solicitud.setCostoFinal(costoFinal);
                    
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
    
    /**
     * FASE 3: Asigna una ruta elegida a la solicitud y crea la ruta efectiva
     * Cambia el estado de la solicitud de BORRADOR a PROGRAMADA
     * Crea la ruta real en ServicioLogistica con segmentos automáticos
     */
    public Solicitud asignarRuta(Long solicitudId, AsignarRutaRequest rutaRequest) {
        // Log removed for Docker compatibility
        
        return solicitudRepository.findById(solicitudId)
                .map(solicitud -> {
                    // Verificar que la solicitud esté en estado BORRADOR
                    if (!"BORRADOR".equals(solicitud.getEstado())) {
                        throw new IllegalArgumentException("Solo se pueden asignar rutas a solicitudes en estado BORRADOR");
                    }
                    
                    try {
                        // Crear la ruta efectiva en el servicio de logística
                        RutaRequest crearRutaRequest = new RutaRequest(
                            solicitudId,
                            "0,0", // Coordenadas origen - simplificado por ahora
                            "0,0", // Coordenadas destino - simplificado por ahora
                            rutaRequest.getOrigen(),
                            rutaRequest.getDestino(),
                            solicitud.getContenedor().getPeso(),
                            solicitud.getContenedor().getVolumen()
                        );
                        
                        RutaResponse rutaResponse = logisticaClient.crearRuta(crearRutaRequest);
                        
                        // Actualizar la solicitud con los datos de la ruta elegida y la ruta creada
                        solicitud.setCostoEstimado(BigDecimal.valueOf(rutaRequest.getCostoEstimado()));
                        solicitud.setTiempoEstimadoHoras((int) rutaRequest.getDuracionTotal());
                        solicitud.setEstado("PROGRAMADA");
                        solicitud.setFechaProgramacion(LocalDateTime.now());
                        
                        // Guardar y devolver la solicitud actualizada
                        return solicitudRepository.save(solicitud);
                        
                    } catch (Exception e) {
                        throw new RuntimeException("Error al crear la ruta en el servicio de logística: " + e.getMessage(), e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + solicitudId));
    }
}
